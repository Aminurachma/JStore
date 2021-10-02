package com.example.jstore.ui.customer

import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityCustomerBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class CustomerActivity : BaseActivity() {

    private var _binding: ActivityCustomerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        setupClickListeners()
        getCustomerList()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupUI() {
        binding.rvCustomer.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = CustomerAdapter(User())
    }

    private fun getCustomerList() {
        progress.show()
        FirestoreClass().getCustomerList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvCustomer.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvCustomer.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}