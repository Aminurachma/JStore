package com.example.jstore.ui.home.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
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
        binding.bottomNav.setupWithNavController(findNavController(R.id.fragmentContainerView))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}