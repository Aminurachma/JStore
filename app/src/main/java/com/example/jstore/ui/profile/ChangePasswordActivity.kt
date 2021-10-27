package com.example.jstore.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityChangePasswordBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.utils.logError
import com.example.jstore.utils.showToast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : BaseActivity() {
    private var _binding: ActivityChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserDetails: User

    private lateinit var auth: FirebaseAuth
    private var oldPassFirebase: String = ""
    private var oldPass: String = ""
    private var newPass: String = ""
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        getUserProfile()
        setupClickListeners()
    }

    private fun getUserProfile() {
        progress.show()
        FirestoreClass().subscribeUserProfile(onSuccessListener = {
            progress.dismiss()
            mUserDetails = it
            email = it.email
        }, onFailureListener = {
            progress.dismiss()
            logError("getUserDetails: $it")
        })
    }


    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnUpdatePassword.setOnClickListener {
            checkConfirmPassword()
        }
    }

    private fun checkConfirmPassword() {
        if( binding.edtOldPassword.text!!.isNotEmpty() &&
            binding.edtNewPassword.text!!.isNotEmpty() &&
            binding.edtConfirmPassword.text!!.isNotEmpty()){
            if (binding.edtNewPassword.text.toString().equals(binding.edtConfirmPassword.text.toString())){
                //val user = auth.currentUser
                //firebaseChangePassword()

                val currentUser = auth.currentUser
                val  password = binding.edtOldPassword.text.toString()
                if(currentUser != null && currentUser.email != null){
                    val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
                    currentUser.reauthenticate(credential)
                        .addOnCompleteListener{
                            if (it.isSuccessful) {
//                                Toast.makeText(
//                                    this,
//                                    "Password lama benar!",
//                                    Toast.LENGTH_SHORT
//                                ).show()

                                currentUser.updatePassword(binding.edtConfirmPassword.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful){
                                            Toast.makeText(
                                                this,
                                                "Password telah diubah!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(Intent(this, HomeCustomerActivity::class.java))
                                            finish()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Password Lama Anda Salah!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            } else{
                Toast.makeText(this, "Password yang Anda isi tidak sama!", Toast.LENGTH_SHORT).show()
            }

        } else{
            Toast.makeText(this, "Tolong diisi seluruh fieldnya !", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun firebaseChangePassword() {
//        progress.show()
//        val  password = binding.edtConfirmPassword.text.toString()
//        FirestoreClass().changePassword(email ,password, onSuccessListener = {
//            progress.dismiss()
//            showToast(getString(R.string.update_password_success))
//            startActivity(
//                Intent(this, ProfileActivity::class.java)
//            )
//            finish()
//        }, onFailureListener = {
//            progress.dismiss()
//            showToast(getString(R.string.update_pass_failed, it.message.toString()))
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}