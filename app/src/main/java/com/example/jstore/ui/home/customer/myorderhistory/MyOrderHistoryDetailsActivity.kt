package com.example.jstore.ui.home.customer.myorderhistory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMyOrderHistoryDetailsBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Order
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.Constants
import com.example.jstore.utils.Constants.DIKIRIM
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone

class MyOrderHistoryDetailsActivity : BaseActivity() {

    private var _binding: ActivityMyOrderHistoryDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = DetailMyOrderHistoryAdapter()

    private lateinit var order: Order
    private var orderId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMyOrderHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        getOrderDetail()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
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
            orderId = order.orderId
            tvJasaPengiriman.text = order.jasaPengiriman
            tvDeliveryStatus.text = order.statusPesanan
            tvCustomerName.text = order.firstName
            tvCustomerAddress.text = order.address
            tvCustomerMobile.text = order.mobile
            tvPaymentMethod.text = order.metodePembayaran
            tvPaymentStatus.text = order.statusPembayaran
            btnAcceptPackage.isEnabled = order.statusPesanan == DIKIRIM

            binding.btnAcceptPackage.setOnClickListener {
                firebaseUpdateStatusTerimaPesanan()
            }
        }
        adapter.submitList(order.products)
    }


    private fun firebaseUpdateStatusTerimaPesanan() {
        progress.show()
        FirestoreClass().updatePaymentStatusUser(orderId = orderId,
            onSuccessListener = { progress.dismiss()
                showToast(getString(R.string.updatepayment_success))
                startActivity(Intent(this, HomeCustomerActivity::class.java))
                finish()
            }
            , onFailureListener = { progress.dismiss() })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_ORDER = "extra_order"
    }

}