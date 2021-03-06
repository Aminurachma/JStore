package com.example.jstore.ui.home.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jstore.R
import com.example.jstore.base.BaseFragment
import com.example.jstore.databinding.FragmentOrderHistoryBinding
import com.example.jstore.ui.home.customer.myorderhistory.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OrderHistoryFragment : BaseFragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderHistoryBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        setupAdapter()
//        setupUI()
//        getMyOrderList()
        setUpTabs()

    }

    private fun setUpTabs() {
        binding?.apply {
            viewPager.adapter = ViewPagerAdapter(this@OrderHistoryFragment)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = getString(R.string.belum_dibayar)
                    1 -> tab.text = getString(R.string.dikemas)
                    2 -> tab.text = getString(R.string.dikirim)
                    3 -> tab.text = getString(R.string.diterima)
                }
            }.attach()
        }
    }

//    private fun setupUI() {
//        binding?.rvMyOrderHistory?.adapter = adapter
//    }

//    private fun getMyOrderList() {
//        progress.show()
//        FirestoreClass().subscribeUserOrderHistory(
//            onSuccessListener = {
//                progress.dismiss()
//                if (it.isNotEmpty()) {
//                    binding?.noData?.toGone()
//                    binding?.rvMyOrderHistory?.toVisible()
//                    adapter.submitList(it)
//                } else {
//                    binding?.rvMyOrderHistory?.toGone()
//                    binding?.noData?.toVisible()
//                }
//            },
//            onFailureListener = {
//                progress.dismiss()
//                showToast(it)
//            }
//        )
//    }
//
//    private fun setupAdapter() {
//        adapter = MyOrderHistoryAdapter {
//            startActivity(Intent(requireContext(), MyOrderHistoryDetailsActivity::class.java).apply {
//                putExtra(MyOrderHistoryDetailsActivity.EXTRA_ORDER, it)
//            })
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
       const val TAG = "OrderHistoryFragment"
    }
}