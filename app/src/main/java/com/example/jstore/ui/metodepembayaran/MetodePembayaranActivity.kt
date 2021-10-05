package com.example.jstore.ui.metodepembayaran

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMetodePembayaranBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.checkout.CheckoutActivity.Companion.EXTRA_METODE_PEMBAYARAN
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class MetodePembayaranActivity : BaseActivity() {

    private var _binding: ActivityMetodePembayaranBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MetodePembayaranAdapter

    private var isAdmin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMetodePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getMetodePembayaranList()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnAddMetodePembayaran.setOnClickListener {
            startActivity(Intent(this,AddMetodePembayaranActivity::class.java))
        }
    }

    private fun getMetodePembayaranList() {
        progress.show()
        FirestoreClass().getMetodePembayaranList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvMetodePembayaran.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvMetodePembayaran.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {
        binding.rvMetodePembayaran.adapter = adapter
        val type = intent.getStringExtra(EXTRA_TYPE)
        isAdmin = type.isNullOrEmpty()
        binding.btnAddMetodePembayaran.isGone = !isAdmin
    }

    private fun setupAdapter() {
        adapter = MetodePembayaranAdapter(onClickListener = { paymentMethod ->
            val intent = Intent()
            intent.putExtra(EXTRA_METODE_PEMBAYARAN, paymentMethod)
            setResult(RESULT_OK, intent)
            finish()
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_CUSTOMER = "type_customer"
    }

}