package com.example.jstore.ui.invoice

import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityInvoiceBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Order
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.showToast

class InvoiceActivity : BaseActivity() {
    private var _binding: ActivityInvoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var order: Order


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyLastCheckout()
    }

    private fun getMyLastCheckout() {
            FirestoreClass().subscribeLastCheckout(onSuccessListener = {
                order = it
                binding.total.setText(order.subTotalAmount.toInt().formatPrice())
                binding.tvFullname.setText(order.firstName)
                binding.tvAddress.setText(order.address)
                binding.tvPayment.setText(order.metodePembayaran+" "+order.namaRekening +"-"+order.nomorRekening+"a/n ("+order.atasNamaRekening+")")
            }, onFailureListener = {
                showToast(it)
            })
        }
}