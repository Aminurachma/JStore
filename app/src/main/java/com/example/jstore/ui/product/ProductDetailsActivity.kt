package com.example.jstore.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.databinding.ActivityProductDetailsBinding

class ProductDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityProductDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupClickListeners()

    }

    private fun setupUI() {

    }

    private fun setupClickListeners() {
        binding.btnDecrement.setOnClickListener {
            if (binding.tvQuantity.text.toString().toInt() > 1) {
                binding.tvQuantity.text = (binding.tvQuantity.text.toString().toInt() - 1).toString()
            }
        }

        binding.btnIncrement.setOnClickListener {
            binding.tvQuantity.text = (binding.tvQuantity.text.toString().toInt() + 1).toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}