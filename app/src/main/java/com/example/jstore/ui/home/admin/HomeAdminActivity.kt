package com.example.jstore.ui.home.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityHomeAdminBinding
import com.example.jstore.models.Admin

class HomeAdminActivity : AppCompatActivity() {

    private var _binding: ActivityHomeAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var admin: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

    }

    private fun setupUI() {
        admin = intent.getParcelableExtra(EXTRA_ADMIN_DETAIL) ?: Admin()
        binding.tvName.text = admin.fullNameAdmin
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_ADMIN_DETAIL = "extra_admin_detail"
    }

}