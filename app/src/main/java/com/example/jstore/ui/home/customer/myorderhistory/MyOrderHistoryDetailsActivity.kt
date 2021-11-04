package com.example.jstore.ui.home.customer.myorderhistory

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMyOrderHistoryDetailsBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Order
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.ui.invoice.InvoiceActivity
import com.example.jstore.utils.Constants
import com.example.jstore.utils.Constants.BELUM_DIBAYAR
import com.example.jstore.utils.Constants.DIKIRIM
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import timber.log.Timber

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
            if (tvPaymentStatus.text == BELUM_DIBAYAR) {
                btnPaymentPackage.toVisible()
                btnPaymentPackage.isEnabled
                Timber.e(" orderid "+ orderId)
                btnPaymentPackage.setOnClickListener {
                    val intent = Intent(this@MyOrderHistoryDetailsActivity, InvoiceActivity::class.java)
                    intent.putExtra("orderId", orderId)
                    startActivity(intent)
                    finish()
                }
            }
            btnAcceptPackage.isEnabled = order.statusPesanan == DIKIRIM
            if (order.statusPesanan == DIKIRIM){
                resi.toVisible()
                tvNoResi.text = "Nomor Resi :" + order.nomorResi
                Glide.with(root.context)
                    .load(order.resiImage)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgResi)
            }

            binding.btnAcceptPackage.setOnClickListener {
                firebaseUpdateStatusTerimaPesanan()
            }
        }
        adapter.submitList(order.products)
    }


    private fun firebaseUpdateStatusTerimaPesanan() {
        progress.show()
        FirestoreClass().updatePaymentStatusUser(orderId = orderId,
            onSuccessListener = {
                progress.dismiss()
                showToast(getString(R.string.updatepayment_success))
                startActivity(Intent(this, HomeCustomerActivity::class.java))
                finish()
            }, onFailureListener = { progress.dismiss() })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_ORDER = "extra_order"
    }

}