package com.example.jstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityRegisterBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : BaseActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private var email =""
    private var password = ""
    private var address = ""
    private var phone = ""
    private var fullName = ""

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

        email = binding.edtEmail.text.toString().trim()
        password = binding.edtPassword.text.toString().trim()
        address = binding.edtAddress.text.toString().trim()
        fullName = binding.edtName.text.toString().trim()
        phone = binding.edtPhoneNumber.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Invalid Email Format !"
        }
        else if(TextUtils.isEmpty(password)){
            binding.edtPassword.error = "Please enter your password !"
        }
        else if(TextUtils.isEmpty(address)){
            binding.edtPassword.error = "Please enter your address !"
        }
        else if(TextUtils.isEmpty(fullName)){
            binding.edtPassword.error = "Please enter your full name !"
        }
        else if(TextUtils.isEmpty(phone)){
            binding.edtPassword.error = "Please enter your phone number !"
        }

        else{
            firebaseRegister()
        }
    }

    private fun firebaseRegister() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                //             Toast.makeText(this, "Register berhasil $email", Toast.LENGTH_SHORT).show()

                val user = User(
                    firebaseUser.uid,
                    binding.edtName.text.toString().trim{ it <= ' '},
                    binding.edtAddress.text.toString().trim{ it <= ' '},
                    binding.edtPhoneNumber.text.toString().toLong(),
                    binding.edtEmail.text.toString().trim{ it <= ' '}
                )

                FirestoreClass().registUser(this@RegisterActivity, user)

//                startActivity(Intent(this, HomeCustomerActivity::class.java))
//                finish()
            }
            .addOnFailureListener{e ->
                hideProgressDialog()
                //failed register
                Toast.makeText(this, "Register gagal ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun userRegisterSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }
}

