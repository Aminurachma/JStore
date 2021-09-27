package com.example.jstore.ui.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityProductBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.profile.ProfileAdminActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

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
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(onClickListener = { product ->
            startActivity(Intent(this, EditProductActivity::class.java).apply {
                putExtra(EditProductActivity.EXTRA_PRODUCT, product)
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