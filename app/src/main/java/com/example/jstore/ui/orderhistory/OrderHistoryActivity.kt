package com.example.jstore.ui.orderhistory

import android.content.Intent
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityOrderHistoryBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import timber.log.Timber

class OrderHistoryActivity : BaseActivity() {
    private var _binding: ActivityOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getOrderHistoryList()
        setupClickListeners()


    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAdapter() {
        adapter = OrderHistoryAdapter(onClickListener = { orderHistory ->
            startActivity(Intent(this, OrderHistoryDetailsActivity::class.java).apply {
                putExtra(OrderHistoryDetailsActivity.EXTRA_ORDER, orderHistory)
                Prefs.orderId = orderHistory.orderId
            })
        })
    }
    private fun setupUI() {
        binding.rvOrderHistory.adapter = adapter
    }


    private fun getOrderHistoryList() {
        progress.show()
        FirestoreClass().getOrderHistoryList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvOrderHistory.toVisible()
                adapter.submitList(it)
//                adapter.setProductList(it.)
            } else {
                binding.rvOrderHistory.toGone()
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