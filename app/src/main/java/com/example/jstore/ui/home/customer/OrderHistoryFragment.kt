package com.example.jstore.ui.home.customer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.jstore.R
import com.example.jstore.base.BaseFragment
import com.example.jstore.databinding.FragmentOrderHistoryBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.home.customer.myorderhistory.MyOrderHistoryDetailsActivity
import com.example.jstore.ui.home.customer.myorderhistory.adapter.ViewPagerAdapter
import com.example.jstore.ui.home.customer.myorderhistory.fragment.BelumDibayarFragment
import com.example.jstore.ui.home.customer.myorderhistory.fragment.DikemasFragment
import com.example.jstore.ui.home.customer.myorderhistory.fragment.DikirimFragment
import com.example.jstore.ui.home.customer.myorderhistory.fragment.DiterimaFragment
import com.example.jstore.utils.Constants
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import timber.log.Timber

class OrderHistoryFragment : BaseFragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding
//    private lateinit var adapter: ViewPagerAdapter

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
        val adapter = fragmentManager?.let { ViewPagerAdapter(it) }
        adapter?.addFragment(BelumDibayarFragment(), Constants.BELUM_DIBAYAR)
        adapter?.addFragment(DikemasFragment(), Constants.DIKEMAS)
        adapter?.addFragment(DikirimFragment(), Constants.DIKIRIM)
        adapter?.addFragment(DiterimaFragment(), Constants.DITERIMA)
        binding?.viewPager?.adapter = adapter
        binding?.tabs?.setupWithViewPager(binding?.viewPager)

        binding?.tabs?.getTabAt(0)!!.setText(getString(R.string.belum_dibayar))
        binding?.tabs?.getTabAt(1)!!.setText(getString(R.string.dikemas))
        binding?.tabs?.getTabAt(2)!!.setText(getString(R.string.dikirim))
        binding?.tabs?.getTabAt(3)!!.setText(getString(R.string.diterima))


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