package com.example.jstore.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.databinding.ActivityMyCartBinding

class MyCartActivity : AppCompatActivity() {

    private var _binding: ActivityMyCartBinding? = null
    private val  binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}