package com.example.jstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.jstore.databinding.ActivityRegisterBinding
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    //ViewBinding
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    //
    private lateinit var firebaseAuth: FirebaseAuth
    private var email =""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }

        private fun validateData() {

        email = binding.edtEmail.text.toString().trim()
        password = binding.edtPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.error = "Invalid Email Format!"
        }
        else if(TextUtils.isEmpty(password)){
            binding.edtPassword.error = "Jangan kosong y"
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
                Toast.makeText(this, "Register berhasil $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, HomeCustomerActivity::class.java))
                finish()
                //
            }
            .addOnFailureListener{e ->
                //failed
                Toast.makeText(this, "Register gagal ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
