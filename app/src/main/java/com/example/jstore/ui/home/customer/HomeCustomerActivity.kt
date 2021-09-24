package com.example.jstore.ui.home.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.jstore.R
import com.example.jstore.databinding.ActivityHomeCustomerBinding

class HomeCustomerActivity : AppCompatActivity() {

    private var _binding: ActivityHomeCustomerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()


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