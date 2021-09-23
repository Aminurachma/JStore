package com.example.jstore.firestore

import android.net.Uri
import android.widget.Toast
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.models.Admin
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.utils.Constants.ADMIN
import com.example.jstore.utils.Constants.EMAIL_ADMIN
import com.example.jstore.utils.Constants.PASSWORD_ADMIN
import com.example.jstore.utils.Constants.PRODUCTS
import com.example.jstore.utils.Constants.USERS
import com.example.jstore.utils.logDebug
import com.example.jstore.utils.logError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(
        userInfo: User,
        onSuccessListener: () -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    fun updateProfileUser(user: User,
                          onSuccessListener: () -> Unit,
                          onFailureListener: (e: Exception) -> Unit){
        mFirestore.collection(USERS)
            .document(Prefs.userId)
            .set(user)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }

    }

    fun uploadImageToFirestore(fileUri: Uri, onSuccessListener: (imageUrl: String) -> Unit, onFailureListener: (e: Exception) -> Unit) {
        val fileName = UUID.randomUUID().toString() +".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")
        refStorage.putFile(fileUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    updateProfileImageUrl(imageUrl = imageUrl, onSuccessListener = {
                        onSuccessListener(imageUrl)
                    }, onFailureListener = {
                        onFailureListener(it)
                    })
                }
            }
            .addOnFailureListener {
                onFailureListener(it)
            }
    }

    fun updateProfileImageUrl(imageUrl: String, onSuccessListener: () -> Unit, onFailureListener: (e: Exception) -> Unit) {
        mFirestore.collection(USERS)
            .document(Prefs.userId)
            .update("image", imageUrl)
            .addOnSuccessListener {
                onSuccessListener()
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
            }
    }

    fun loginAdmin(email: String, password: String, onLoginSuccess: (admin: Admin) -> Unit, onLoginFailed: (e: String) -> Unit) {
        mFirestore.collection(ADMIN)
            .whereEqualTo(EMAIL_ADMIN, email)
            .whereEqualTo(PASSWORD_ADMIN, password)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result
                    if (result?.isEmpty == true) {
                        onLoginFailed("Email atau password salah!")
                    } else {
                        onLoginSuccess(result?.first()?.toObject(Admin::class.java) ?: Admin())
                    }
                } else {
                    onLoginFailed(it.exception?.message.toString())
                    logError(it.exception?.message.toString())
                }
            }
            .addOnFailureListener {
                onLoginFailed(it.message.toString())
                logError(it.message.toString())
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }

    fun getUserDetails(
        onSuccessListener: (user: User) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    Prefs.userId = user.id
                    Prefs.userFullName = user.fullName
                    onSuccessListener(user)
                }

            }.addOnFailureListener { exception ->
                onFailureListener(exception)
            }
    }

    fun getAdminDetails(
        onSuccessListener: (admin: Admin) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(ADMIN)
            .document(Prefs.adminId)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getAdminDetails: $document")
                val admin = document.toObject(Admin::class.java)!!
                Prefs.adminId = admin.idAdmin
                Prefs.adminFullName = admin.fullNameAdmin
                onSuccessListener(admin)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getAdminDetails: ${e.message}")
            }
    }

    fun getProductList(
        onSuccessListener: (products: List<Product>) -> Unit,
        onFailureListener: (e: Exception) -> Unit
    ) {
        mFirestore.collection(PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                logDebug("getDashboardItemsList: ${document.documents}")

                val productList = document.documents.map {
                    it.toObject(Product::class.java) ?: Product()
                }
                onSuccessListener(productList)
            }
            .addOnFailureListener { e ->
                onFailureListener(e)
                logError("getDashboardItemsList: ${e.message}")
            }
    }

}