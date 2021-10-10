package com.example.jstore.ui.home.customer.myorderhistory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.databinding.ActivityMyOrderHistoryDetailsBinding
import com.example.jstore.models.Order
import com.example.jstore.utils.Constants
import com.example.jstore.utils.Constants.DIKIRIM
import com.example.jstore.utils.toGone

class MyOrderHistoryDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityMyOrderHistoryDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DetailMyOrderHistoryAdapter()

    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMyOrderHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        getOrderDetail()
    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter
    }

    private fun getOrderDetail() {
        order = intent.getParcelableExtra(EXTRA_ORDER) ?: Order()
        showOrderDetail()
    }

    @SuppressLint("SetTextI18n")
    private fun showOrderDetail() {
        binding.apply {
            tvOrderId.text = "Order:#${order.orderId}"
            tvJasaPengiriman.text = order.jasaPengiriman
            tvDeliveryStatus.text = order.statusPesanan
            tvCustomerName.text = order.firstName
            tvCustomerAddress.text = order.address
            tvPaymentMethod.text = order.metodePembayaran
            tvPaymentStatus.text = order.statusPembayaran
            btnAcceptPackage.isEnabled = order.statusPesanan == DIKIRIM
        }
        adapter.submitList(order.products)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_ORDER = "extra_order"
    }

}