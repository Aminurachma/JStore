package com.example.jstore.firestore

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.jstore.LoginAdminActivity
import com.example.jstore.MainActivity
import com.example.jstore.RegisterActivity
import com.example.jstore.models.Admin
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.CustomerDashboardFragment
import com.example.jstore.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    private lateinit var mProgressDialog : Dialog

    fun registerUser(activity: RegisterActivity, userInfo: User){
        mFirestore.collection(Constants.USER)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener{ e ->
                activity.progress.dismiss()
                Timber.e("Error while registering the user: ${e.message}")
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
    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getUserDetails(activity: Activity) {
        mFirestore.collection(Constants.USER)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {

                    val sharedPref = activity.getSharedPreferences(
                        Constants.MYSHOP_PREFERENCE,
                        Context.MODE_PRIVATE
                    )

                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.fullName}",
                    )
                    editor.apply()

                    when (activity) {
                        is MainActivity -> activity.userLoggedInSuccess(user)
                        //is RegisterActivity -> activity.userLoggedInSuccess(user)
                    }
                }

            }.addOnFailureListener { exception ->
                when (activity) {
                    is MainActivity -> activity.hideProgressDialog()
                    is RegisterActivity -> activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, exception.message.toString())
            }
    }

    fun getCurrentAdminId(): String{
        val currentUser  = FirebaseAuth.getInstance().currentUser

        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getAdminDetails(activity: Activity) {
        mFirestore.collection(Constants.ADMIN)
            .document(getCurrentAdminId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val admin = document.toObject(Admin::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYSHOP_PREFERENCE,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAMEADMIN,
                    "${admin.fullNameAdmin}"
                )
                editor.apply()

                when(activity) {
                    is LoginAdminActivity -> {
                        activity.adminLoggedInSuccess(admin)
                    }
                }
            }
            .addOnFailureListener{ e ->
                when(activity) {
                    is LoginAdminActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                    "Error while Login the admin",
                    e
                )

            }
    }

    fun getDashboardItemsList(fragment:CustomerDashboardFragment) {
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())

                val productList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id

                    productList.add(product)
                }
                fragment.successDashboardItemList(productList)

            }
            .addOnFailureListener { e ->
                hideProgressDialog()
                Log.e("Error :: ", e.message.toString())
            }
    }

    fun hideProgressDialog(){
        mProgressDialog.hide()
    }
}