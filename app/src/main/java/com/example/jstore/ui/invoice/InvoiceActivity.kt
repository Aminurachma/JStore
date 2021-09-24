package com.example.jstore.ui.invoice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityInvoiceBinding

class InvoiceActivity : BaseActivity() {
    private var _binding: ActivityInvoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}