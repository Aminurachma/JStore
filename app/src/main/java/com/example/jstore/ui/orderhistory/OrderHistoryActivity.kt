package com.example.jstore.ui.orderhistory

import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityOrderHistoryBinding
import com.example.jstore.ui.orderhistory.adapter.ViewPagerAdminAdapter
import com.example.jstore.ui.orderhistory.fragment.MenungguKonfirmasiAdminFragment
import com.example.jstore.ui.orderhistory.fragment.PerluDikirimAdminFragment
import com.example.jstore.ui.orderhistory.fragment.SedangDikirimAdminFragment
import com.example.jstore.ui.orderhistory.fragment.SelesaiAdminFragment
import com.example.jstore.utils.Constants

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

        val adapter = ViewPagerAdminAdapter(supportFragmentManager)
        adapter.addFragment(MenungguKonfirmasiAdminFragment(), Constants.MENUNGGU_KONFIRMASI_ADMIN)
        adapter.addFragment(PerluDikirimAdminFragment(), Constants.DIKEMAS)
        adapter.addFragment(SedangDikirimAdminFragment(), Constants.DIKIRIM)
        adapter.addFragment(SelesaiAdminFragment(), Constants.DITERIMA)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.tabs.getTabAt(0)!!.text = getString(R.string.menunggu_kofirmasi)
        binding.tabs.getTabAt(1)!!.text = getString(R.string.perlu_dikirim)
        binding.tabs.getTabAt(2)!!.text = getString(R.string.sedang_dikirim)
        binding.tabs.getTabAt(3)!!.text = getString(R.string.pesanan_selesai)
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