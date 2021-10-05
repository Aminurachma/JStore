package com.example.jstore.ui.product

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityProductBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import timber.log.Timber

class ProductActivity : BaseActivity() {
    private var _binding: ActivityProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getProductList()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupUI() {
        binding.rvProduct.adapter = adapter

        binding.searchView.searchBar.doOnTextChanged { text, _, _, _ ->
            adapter.filter.filter(text)
        }
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(onClickListener = { product ->
            startActivity(Intent(this, EditProductActivity::class.java).apply {
                putExtra(EditProductActivity.EXTRA_PRODUCT, product)
                Prefs.productId = product.productId
                Timber.e("Prefs ${Prefs.productId}")
            })
        })
    }


    private fun getProductList() {
        progress.show()
        FirestoreClass().getProductList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvProduct.toVisible()
                adapter.submitList(it)
                adapter.setProductList(it)
            } else {
                binding.rvProduct.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}