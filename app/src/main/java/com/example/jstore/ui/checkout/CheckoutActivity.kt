package com.example.jstore.ui.checkout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}