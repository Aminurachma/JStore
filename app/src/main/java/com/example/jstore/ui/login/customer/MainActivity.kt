package com.example.jstore.ui.login.customer

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMainBinding
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.ui.login.admin.LoginAdminActivity
import com.example.jstore.ui.register.RegisterActivity
import com.example.jstore.utils.logError
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLoginAdmin.setOnClickListener{
            startActivity(Intent(this, LoginAdminActivity::class.java))
        }

        binding.btnLogin.setOnClickListener{
            validateData()
        }

        binding.btnSignUp.setOnClickListener {
            pushActivity(RegisterActivity::class.java)
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString())
                    .matches() -> tilEmail.error = getString(R.string.invalid_email)
                edtPassword.text.toString().isEmpty() -> tilPassword.error =
                    getString(R.string.empty_field, getString(R.string.password))
                else -> firebaseLogin(edtEmail.text.toString(), edtPassword.text.toString())
            }
        }
    }

    private fun firebaseLogin(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login sukses
                showToast(getString(R.string.login_success))
                startActivity(Intent(this, HomeCustomerActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e ->
                logError("loginUser: ${e.message}")
                showToast(getString(R.string.login_failed))
            }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user sudah login
            pushActivity(HomeCustomerActivity::class.java)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}