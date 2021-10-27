package com.example.jstore.ui.orderhistory.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jstore.base.BaseFragment
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.FragmentSelesaiAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.orderhistory.OrderHistoryAdapter
import com.example.jstore.ui.orderhistory.OrderHistoryDetailsActivity
import com.example.jstore.utils.Constants
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class SelesaiAdminFragment : BaseFragment() {
    private var _binding: FragmentSelesaiAdminBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelesaiAdminBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupUI()
        getOrderHistoryList()
    }

    private fun setupAdapter() {
        adapter = OrderHistoryAdapter(onClickListener = { orderHistory ->
            startActivity(Intent(requireContext(), OrderHistoryDetailsActivity::class.java).apply {
                putExtra(OrderHistoryDetailsActivity.EXTRA_ORDER, orderHistory)
                Prefs.orderId = orderHistory.orderId
            })
        })
    }
    private fun setupUI() {
        binding?.rvOrderHistory?.adapter = adapter
    }


    private fun getOrderHistoryList() {
        progress.show()
        FirestoreClass().getOrderHistoryList(Constants.DITERIMA, onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding   ?.rvOrderHistory?.toVisible()
                adapter.submitList(it)
//                adapter.setProductList(it.)
            } else {
                binding?.rvOrderHistory?.toGone()
                binding?.noData?.toVisible()
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