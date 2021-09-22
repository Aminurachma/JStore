package com.example.jstore.ui.login.admin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import com.example.jstore.ui.home.admin.HomeAdminActivity
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityLoginAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.google.firebase.auth.FirebaseAuth

class LoginAdminActivity : BaseActivity() {

    private var _binding: ActivityLoginAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            validateData()
        }

        binding.btnLoginUser.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches() -> tilEmail.error = getString(R.string.invalid_email)
                edtPassword.text.toString().isEmpty() -> tilPassword.error = getString(R.string.empty_field, getString(R.string.password))
                else -> proceedLogin(edtEmail.text.toString(), edtPassword.text.toString())
            }
        }
    }

    private fun proceedLogin(email: String, password: String) {
        progress.show()
        FirestoreClass().loginAdmin(
            email = email,
            password = password,
            onLoginSuccess = { admin ->
                progress.dismiss()
                Prefs.adminId = admin.idAdmin
                Prefs.adminFullName = admin.fullNameAdmin
                showToast(getString(R.string.login_success))
                startActivity(Intent(this, HomeAdminActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            },
            onLoginFailed = {
                progress.dismiss()
                showToast(it)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}