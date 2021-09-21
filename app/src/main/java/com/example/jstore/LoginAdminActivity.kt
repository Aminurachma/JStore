package com.example.jstore

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityLoginAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Admin
import com.example.jstore.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginAdminActivity : BaseActivity() {

    private var _binding: ActivityLoginAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private var email =""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener{
            validateData()
        }

        binding.btnLoginUser.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
        email = binding.edtEmail.text.toString().trim()
        password = binding.edtPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Invalid Email Format"
        }
        else if(TextUtils.isEmpty(password)){
            binding.edtPassword.error = "Please enter your password"
        }
        else{
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirestoreClass().getAdminDetails(this@LoginAdminActivity)

                } else {
                    hideProgressDialog()
                    Toast.makeText(
                        this@LoginAdminActivity,
                        task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener { exception ->
                hideProgressDialog()
                Toast.makeText(this@LoginAdminActivity, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun adminLoggedInSuccess(admin: Admin){
        hideProgressDialog()
        if(admin.roleAdmin == 1){
            val intent = Intent(this, HomeAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(Constants.EXTRA_ADMIN_DETAILS,admin)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this@LoginAdminActivity,R.string.bukanadmin,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}