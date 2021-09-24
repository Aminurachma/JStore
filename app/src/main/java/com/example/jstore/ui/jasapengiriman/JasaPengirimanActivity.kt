package com.example.jstore.ui.jasapengiriman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityJasaPengirimanBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.lokasipengiriman.AddLokasiPengirimanActivity
import com.example.jstore.ui.product.ProductDetailsActivity
import com.example.jstore.ui.rekening.RekeningAdapter
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class JasaPengirimanActivity : BaseActivity() {

    private var _binding: ActivityJasaPengirimanBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: JasaPengirimanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityJasaPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        setupClickListener()
        getJasaPengirimanList()
    }

    private fun setupClickListener() {
        binding.btnAddJasa.setOnClickListener {
            startActivity(Intent(this, AddJasaPengirimanActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getJasaPengirimanList() {
        progress.show()
        FirestoreClass().getJasaPengirimanList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvJasaPengiriman.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvJasaPengiriman.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {
        binding.rvJasaPengiriman.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = JasaPengirimanAdapter(onClickListener = { jasaPengiriman ->
            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ProductDetailsActivity.EXTRA_PRODUCT, jasaPengiriman)
            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}