package com.example.jstore.ui.home.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jstore.base.BaseFragment
import com.example.jstore.databinding.FragmentOrderHistoryBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import timber.log.Timber

class OrderHistoryFragment : BaseFragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MyOrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupUI()
        getMyOrderList()

    }

    private fun setupUI() {
        binding.rvMyOrderHistory.adapter = adapter
    }

    private fun getMyOrderList() {
        progress.show()
        FirestoreClass().subscribeUserOrderHistory(
            onSuccessListener = {
                progress.dismiss()
                if (it.isNotEmpty()) {
                    binding.noData.toGone()
                    binding.rvMyOrderHistory.toVisible()
                    adapter.submitList(it)
                } else {
                    binding.rvMyOrderHistory.toGone()
                    binding.noData.toVisible()
                }
            },
            onFailureListener = {
                progress.dismiss()
                showToast(it)
            }
        )
    }

    private fun setupAdapter() {
        adapter = MyOrderHistoryAdapter {  }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
       const val TAG = "OrderHistoryFragment"
    }
}