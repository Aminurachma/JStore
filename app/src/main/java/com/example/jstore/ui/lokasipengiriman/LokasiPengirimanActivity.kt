package com.example.jstore.ui.lokasipengiriman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityLokasiPengirimanBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.jasapengiriman.JasaPengirimanAdapter
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class LokasiPengirimanActivity : BaseActivity() {

    private var _binding: ActivityLokasiPengirimanBinding? = null
    private val binding get() = _binding!!

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
            startActivity(Intent(this, SettingActivity::class.java))
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
    }

    private fun setupAdapter() {
        adapter = LokasiPengirimanAdapter(onClickListener = { lokasiPengiriman ->
//            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
//                putExtra(ProductDetailsActivity.EXTRA_PRODUCT, lokasiPengiriman)
//            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}