package com.example.jstore.ui.home.customer.myorderhistory.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jstore.base.BaseFragment
import com.example.jstore.databinding.FragmentDikirimBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.home.customer.MyOrderHistoryAdapter
import com.example.jstore.ui.home.customer.myorderhistory.MyOrderHistoryDetailsActivity
import com.example.jstore.utils.Constants
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class DikirimFragment : BaseFragment() {
    private var _binding: FragmentDikirimBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: MyOrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDikirimBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupUI()
        getMyOrderList()
    }

    private fun setupAdapter() {
        adapter = MyOrderHistoryAdapter {
            startActivity(Intent(requireContext(), MyOrderHistoryDetailsActivity::class.java).apply {
                putExtra(MyOrderHistoryDetailsActivity.EXTRA_ORDER, it)
            })
        }
    }

    private fun setupUI() {
        binding?.rvMyOrderHistory?.adapter = adapter
    }

    private fun getMyOrderList() {
        progress.show()
        FirestoreClass().subscribeUserOrderHistory(
            Constants.DIKIRIM,
            onSuccessListener = {
                progress.dismiss()
                if (it.isNotEmpty()) {
                    binding?.noData?.toGone()
                    binding?.rvMyOrderHistory?.toVisible()
                    adapter.submitList(it)
                } else {
                    binding?.rvMyOrderHistory?.toGone()
                    binding?.noData?.toVisible()
                }
            },
            onFailureListener = {
                progress.dismiss()
                showToast(it)
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}