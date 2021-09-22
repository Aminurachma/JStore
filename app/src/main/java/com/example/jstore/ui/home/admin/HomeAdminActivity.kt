package com.example.jstore.ui.home.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityHomeAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Admin
import com.example.jstore.utils.showToast

class HomeAdminActivity : BaseActivity() {

    private var _binding: ActivityHomeAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var admin: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAdminDetail()
        setupUI()

    }

    private fun getAdminDetail() {
        progress.show()
        FirestoreClass().getAdminDetails(onSuccessListener = {
            progress.dismiss()
            admin = it
            binding.tvName.text = admin.fullNameAdmin
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}