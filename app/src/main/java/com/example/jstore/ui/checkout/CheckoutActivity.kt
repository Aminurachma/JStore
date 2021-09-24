package com.example.jstore.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityCheckoutBinding
import com.example.jstore.ui.lokasipengiriman.LokasiPengirimanActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity

class CheckoutActivity : AppCompatActivity() {
    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.edtPayment.setOnClickListener {
            startActivity(Intent(this, MetodePembayaranActivity::class.java))
        }
        binding.edtDeliveryLocation.setOnClickListener {
            startActivity(Intent(this, LokasiPengirimanActivity::class.java))
        }
        binding.edtDelivery.setOnClickListener {
            startActivity(Intent(this, MetodePembayaranActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}