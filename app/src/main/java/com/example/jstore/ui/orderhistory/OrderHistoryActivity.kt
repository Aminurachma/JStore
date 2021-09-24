package com.example.jstore.ui.orderhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : BaseActivity() {
    private var _binding: ActivityOrderHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}