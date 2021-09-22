package com.example.jstore.ui.register

import android.os.Bundle
import android.util.Patterns
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityRegisterBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : BaseActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                edtName.text.toString().trim().isEmpty() -> tilName.error = getString(
                    R.string.empty_field, getString(
                        R.string.fullname
                    ))
                edtAddress.text.toString().trim().isEmpty() -> tilAddress.error = getString(
                    R.string.empty_field, getString(
                        R.string.address
                    ))
                edtPhoneNumber.text.toString().trim().isEmpty() -> tilPhoneNumber.error = getString(
                    R.string.empty_field, getString(R.string.phone_number))
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString().trim()).matches() -> tilEmail.error = getString(
                    R.string.invalid_email
                )
                edtPassword.text.toString().trim().isEmpty() -> tilPassword.error = getString(
                    R.string.empty_field, getString(
                        R.string.password
                    ))
                else -> firebaseRegister()
            }
        }
    }

    private fun firebaseRegister() {
        progress.show()
        firebaseAuth.createUserWithEmailAndPassword(
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString()
        ).addOnSuccessListener {
            registerToFirestore()
        }.addOnFailureListener { e ->
            progress.dismiss()
            showToast(getString(R.string.register_failed, e.message.toString()))
        }
    }

    private fun registerToFirestore() {
        val firebaseUser = firebaseAuth.currentUser
        val user = User(
            id = firebaseUser?.uid ?: "",
            fullName = binding.edtName.text.toString().trim(),
            address = binding.edtAddress.text.toString().trim(),
            mobile = binding.edtPhoneNumber.text.toString().trim().toLong(),
            email = binding.edtEmail.text.toString().trim()
        )
        FirestoreClass().registerUser(user, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.register_success))
            pushActivity(MainActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.register_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

