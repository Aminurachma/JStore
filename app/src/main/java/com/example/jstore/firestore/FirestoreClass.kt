package com.example.jstore.firestore

import com.example.jstore.RegisterActivity
import com.example.jstore.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        mFirestore.collection("users")
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
}