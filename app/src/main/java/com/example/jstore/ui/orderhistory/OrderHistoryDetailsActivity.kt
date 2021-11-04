package com.example.jstore.ui.orderhistory

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityOrderHistoryDetailsBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Order
import com.example.jstore.ui.home.admin.HomeAdminActivity
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.ui.invoice.InvoiceActivity
import com.example.jstore.utils.Constants.BELUM_DIBAYAR
import com.example.jstore.utils.Constants.DIKEMAS
import com.example.jstore.utils.Constants.DIKIRIM
import com.example.jstore.utils.Constants.DITERIMA
import com.example.jstore.utils.Constants.SUDAH_DIBAYAR
import com.example.jstore.utils.logDebug
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toVisible

class OrderHistoryDetailsActivity : BaseActivity() {
    private var _binding: ActivityOrderHistoryDetailsBinding?  = null
    private val binding get() = _binding!!
    private lateinit var order: Order
    private lateinit var adapter: OrderHistoryDetailsAdapter
    private var orderIdSelected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
//        getOrderHistoryProductList()
        setupClickListeners()

    }

    private fun setupAdapter() {
        adapter = OrderHistoryDetailsAdapter(onClickListener = { orderHistory ->
//            startActivity(Intent(this, OrderHistoryDetailsActivity::class.java).apply {
//                putExtra(OrderHistoryDetailsActivity.EXTRA_ORDER, orderHistory)
//                Prefs.orderId = orderHistory.orderId
//            })
        })
    }

    private fun getOrderHistoryProductList() {
        FirestoreClass().subscribeToOrder(onSuccessListener = {
            order = it
            logDebug("checkProducts: ${it.products}")
            adapter.submitList(it.products)
        }, onFailureListener = {
            progress.dismiss()
            showToast(it)
        })
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnEdtStatus.setOnClickListener {
            checkStatusPemesanan()
        }
    }

    private fun checkStatusPemesanan() {
        when(order.statusPesanan) {
            BELUM_DIBAYAR -> {
                firebaseUpdatePayment()
            }
            DIKEMAS -> {
                firebaseUpdateResi()
            }
            DIKIRIM -> {
                binding.btnEdtStatus.isEnabled = false
            }
            DITERIMA -> {
                binding.btnEdtStatus.isEnabled= false
            }
        }
    }

    private fun firebaseUpdateResi() {
        val intent = Intent(this, UpdateResiActivity::class.java)
        intent.putExtra("orderId", orderIdSelected)
        startActivity(intent)
    }

    private fun firebaseUpdatePayment() {
        progress.show()
        FirestoreClass().updatePaymentStatusAdmin(orderId = orderIdSelected,
            onSuccessListener = { progress.dismiss()
                showToast(getString(R.string.updatepayment_success))
                finish()
            }
            , onFailureListener = { progress.dismiss() })
    }

    private fun setupUI() {
        order = intent.getParcelableExtra(EXTRA_ORDER) ?: Order() // ini apa? kalo ngambil dari activity sebelumnya ngapain subsribe ke order lagi?
        Glide.with(this)
            .load(order.imageBuktiBayar)
            .placeholder(R.drawable.product_placeholder)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .into(binding.imgBuktiBayar)
        orderIdSelected = order.orderId
        binding.tvOrderId.text = "Order:#${order.orderId}"
        binding.tvCustomerAddress.text = order.address
        binding.tvCustomerName.text = order.firstName
        binding.tvCustomerMobile.text = order.mobile
        binding.tvJasaPengiriman.text = order.jasaPengiriman
        binding.tvPaymentMethod.text = order.metodePembayaran
        binding.tvPaymentStatus.text = order.statusPembayaran
        binding.tvPesananStatus.text = order.statusPesanan
        //pake when yang kalo banyak kondisinya

        when(order.statusPesanan) {
            BELUM_DIBAYAR -> {
                binding.btnEdtStatus.text = getString(R.string.edit_status)
            }
            DIKEMAS -> {
                binding.btnEdtStatus.text = getString(R.string.kirim_pesanan)
            }
            DIKIRIM -> {
                binding.btnEdtStatus.text = getString(R.string.sedang_dikirim)
                binding.btnEdtStatus.isEnabled = false
            }
            DITERIMA -> {
                binding.btnEdtStatus.text = getString(R.string.pesanan_selesai)
                binding.btnEdtStatus.isEnabled= false
            }
        }
        adapter.submitList(order.products)
        binding.rvProduct.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_ORDER = "extra_order"
    }
}