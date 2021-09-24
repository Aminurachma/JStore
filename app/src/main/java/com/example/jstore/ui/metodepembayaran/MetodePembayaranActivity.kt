package com.example.jstore.ui.metodepembayaran

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityMetodePembayaranBinding

class MetodePembayaranActivity : AppCompatActivity() {

    private var _binding: ActivityMetodePembayaranBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMetodePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}