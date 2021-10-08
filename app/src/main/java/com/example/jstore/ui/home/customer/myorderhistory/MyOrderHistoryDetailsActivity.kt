package com.example.jstore.ui.home.customer.myorderhistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityMyOrderHistoryDetailsBinding

class MyOrderHistoryDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityMyOrderHistoryDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMyOrderHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}