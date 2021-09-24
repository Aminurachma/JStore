package com.example.jstore.ui.metodepembayaran

import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMetodePembayaranBinding

class MetodePembayaranActivity : BaseActivity() {

    private var _binding: ActivityMetodePembayaranBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMetodePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}