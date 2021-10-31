package com.example.jstore.ui.orderhistory

import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityOrderHistoryBinding
import com.example.jstore.ui.home.customer.myorderhistory.adapter.ViewPagerAdapter
import com.example.jstore.ui.orderhistory.adapter.ViewPagerAdminAdapter
import com.example.jstore.ui.orderhistory.fragment.MenungguKonfirmasiAdminFragment
import com.example.jstore.ui.orderhistory.fragment.PerluDikirimAdminFragment
import com.example.jstore.ui.orderhistory.fragment.SedangDikirimAdminFragment
import com.example.jstore.ui.orderhistory.fragment.SelesaiAdminFragment
import com.example.jstore.utils.Constants
import com.google.android.material.tabs.TabLayoutMediator

class OrderHistoryActivity : BaseActivity() {
    private var _binding: ActivityOrderHistoryBinding? = null
    private val binding get() = _binding!!
//    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTabs()
//        setupAdapter()
//        setupUI()
////        getOrderHistoryList()
//        setupClickListeners()


    }

    private fun setUpTabs() {
        binding.apply {
            viewPager.adapter =
                ViewPagerAdminAdapter(this@OrderHistoryActivity)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.menunggu_kofirmasi)
                    1 -> tab.text = getString(R.string.perlu_dikirim)
                    2 -> tab.text = getString(R.string.sedang_dikirim)
                    3 -> tab.text = getString(R.string.pesanan_selesai)
                }
            }.attach()
        }
    }
//
//    private fun setupClickListeners() {
//        binding.btnBack.setOnClickListener {
//            onBackPressed()
//        }
//    }

//    private fun setupAdapter() {
//        adapter = OrderHistoryAdapter(onClickListener = { orderHistory ->
//            startActivity(Intent(this, OrderHistoryDetailsActivity::class.java).apply {
//                putExtra(OrderHistoryDetailsActivity.EXTRA_ORDER, orderHistory)
//                Prefs.orderId = orderHistory.orderId
//            })
//        })
//    }
//    private fun setupUI() {
//        binding.rvOrderHistory.adapter = adapter
//    }
//
//
//    private fun getOrderHistoryList() {
//        progress.show()
//        FirestoreClass().getOrderHistoryList(onSuccessListener = {
//            progress.dismiss()
//            if (it.isNotEmpty()) {
//                binding.rvOrderHistory.toVisible()
//                adapter.submitList(it)
////                adapter.setProductList(it.)
//            } else {
//                binding.rvOrderHistory.toGone()
//            }
//        }, onFailureListener = {
//            progress.dismiss()
//            showToast(it.message.toString())
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}