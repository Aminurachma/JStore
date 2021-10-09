package com.example.jstore.ui.invoice

import android.content.Intent
import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityInvoiceBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Order
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.showToast

class InvoiceActivity : BaseActivity() {
    private var _binding: ActivityInvoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var order: Order
    private var orderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ini yang bener, harusnya ambil id nya dulu baru getMylastCheckout, tadi kamu getMyLastCheckout dulu
        orderId = intent.getStringExtra("orderId") ?: ""
        getMyLastCheckout()
        setupClickListeners()
    }

    private fun getMyLastCheckout() {
        progress.show()
        FirestoreClass().subscribeLastCheckout(orderId, onSuccessListener = {
            progress.dismiss()
            order = it
            binding.total.text = it.subTotalAmount.toInt().formatPrice()
            binding.tvFullname.text = it.firstName
            binding.tvAddress.text = it.address
            binding.tvPayment.text = it.metodePembayaran+
                    " "+it.namaRekening +" "+it.nomorRekening+" "+it.atasNamaRekening+" "
        }, onFailureListener = {
            showToast(it)
        })
    }

    private fun setupClickListeners() {
        binding.btnBayar.setOnClickListener {
            progress.show()
            FirestoreClass().updatePaymentStatus(orderId = orderId,
                onSuccessListener = { progress.dismiss()
                    showToast(getString(R.string.payment_success))
                    startActivity(Intent(this, HomeCustomerActivity::class.java))
                    finish()
                }
                , onFailureListener = { progress.dismiss() })

        }
    }
}