package com.example.jstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMainBinding
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private var email =""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.btnLoginAdmin.setOnClickListener{
            startActivity(Intent(this, LoginAdminActivity::class.java))
        }

        binding.btnLogin.setOnClickListener{
            validateData()
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
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
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login sukses
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Login berhasil $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeCustomerActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, "Login gagal ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user sudah login
            startActivity(Intent(this, HomeCustomerActivity::class.java))
            finish()
        }
    }

    fun userLoggedInSuccess(user : User){
        hideProgressDialog()
        if(user.profileCompleted == 0){
            val intent = Intent(this@MainActivity, HomeCustomerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else{
            val intent = Intent(this@MainActivity, HomeCustomerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}