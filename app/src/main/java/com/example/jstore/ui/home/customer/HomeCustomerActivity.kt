package com.example.jstore.ui.home.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.jstore.R
import com.example.jstore.databinding.ActivityHomeCustomerBinding
import com.google.firebase.auth.FirebaseAuth

class HomeCustomerActivity : AppCompatActivity() {

    private var _binding: ActivityHomeCustomerBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()


        firebaseAuth = FirebaseAuth.getInstance()

    }

    private fun setupUI() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}