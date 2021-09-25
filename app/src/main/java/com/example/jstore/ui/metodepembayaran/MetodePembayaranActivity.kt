package com.example.jstore.ui.metodepembayaran

import android.content.Intent
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMetodePembayaranBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.lokasipengiriman.LokasiPengirimanAdapter
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class MetodePembayaranActivity : BaseActivity() {

    private var _binding: ActivityMetodePembayaranBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MetodePembayaranAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMetodePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getLokasiPengirimanList()
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
    private fun getLokasiPengirimanList() {
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
    }

    private fun setupAdapter() {
        adapter = MetodePembayaranAdapter(onClickListener = { metodePembayaran ->
//            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
//                putExtra(ProductDetailsActivity.EXTRA_PRODUCT, metodePembayaran)
//            })
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}