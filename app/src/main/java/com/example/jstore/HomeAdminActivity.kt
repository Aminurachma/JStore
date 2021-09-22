package com.example.jstore

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityHomeAdminBinding
import com.example.jstore.models.Admin
import com.example.jstore.utils.Constants

class HomeAdminActivity : AppCompatActivity() {
    private var _binding: ActivityHomeAdminBinding? = null
    private val binding get() = _binding!!


    private lateinit var mAdminDetails: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(Constants.MYSHOP_PREFERENCE, Context.MODE_PRIVATE)
        val userName = sharedPref.getString(Constants.LOGGED_IN_USERNAMEADMIN,"Hello")

        binding.namaTv.text = userName.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}