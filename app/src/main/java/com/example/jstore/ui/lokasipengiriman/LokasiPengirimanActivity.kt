package com.example.jstore.ui.lokasipengiriman

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityLokasiPengirimanBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.checkout.CheckoutActivity.Companion.EXTRA_LOKASI
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class LokasiPengirimanActivity : BaseActivity() {

    private var _binding: ActivityLokasiPengirimanBinding? = null
    private val binding get() = _binding!!
    private var isAdmin = true

    private lateinit var adapter: LokasiPengirimanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLokasiPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getLokasiPengirimanList()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnAddLokasi.setOnClickListener {
            startActivity(Intent(this, AddLokasiPengirimanActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getLokasiPengirimanList() {
        progress.show()
        FirestoreClass().getLokasiPengirimanList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvLokasiPengiriman.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvLokasiPengiriman.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {
        binding.rvLokasiPengiriman.adapter = adapter
        val type = intent.getStringExtra(EXTRA_TYPE)
        isAdmin = type.isNullOrEmpty()
        binding.btnAddLokasi.isGone = !isAdmin
    }

    private fun setupAdapter() {
        adapter = LokasiPengirimanAdapter(onClickListener = { lokasiPengiriman ->
            val intent = Intent()
            intent.putExtra(EXTRA_LOKASI, lokasiPengiriman)
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