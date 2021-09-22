package com.example.jstore.firestore

import android.net.Uri
import android.widget.Toast
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.models.Admin
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.utils.Constants.ADMIN
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

    //    fun loginAdmin(activity: LoginAdminActivity, adminInfo: Admin) {
//        mFirestore.collection(Constants.USER)
//            .document(adminInfo.idAdmin)
//            .set(adminInfo, SetOptions.merge())
//            .addOnSuccessListener {
//                activity.adminLoginSuccess()
//            }
//            .addOnFailureListener { e ->
//                activity.hideProgressDialog()
//                Log.e(
//                    activity.javaClass.simpleName,
//                    "Error while loggining the admin",
//                    e
//                )
//
//            }
//    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }

    fun getUserDetails(onSuccessListener: (user: User) -> Unit, onFailureListener: (e: Exception) -> Unit) {
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

    private fun getCurrentAdminId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }

    fun getAdminDetails(onSuccessListener: (admin: Admin) -> Unit, onFailureListener: (e: Exception) -> Unit) {
        mFirestore.collection(ADMIN)
            .document(getCurrentAdminId())
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

    fun getProductList(onSuccessListener: (products: List<Product>) -> Unit, onFailureListener: (e: Exception) -> Unit) {
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