package com.example.jstore.ui.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityCategoryBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.ui.metodepembayaran.AddMetodePembayaranActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranAdapter
import com.example.jstore.ui.product.AddProductActivity
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class CategoryActivity : BaseActivity() {
    private var _binding: ActivityCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getCategoryList()
        setupClickListener()

    }

    private fun setupClickListener() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.btnAddCategory.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }
    }

    private fun getCategoryList() {
        progress.show()
        FirestoreClass().getCategoryList(onSuccessListener = {
            progress.dismiss()
            if (it.isNotEmpty()) {
                binding.rvCategory.toVisible()
                adapter.submitList(it)
            } else {
                binding.rvCategory.toGone()
            }
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {
        binding.rvCategory.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = CategoryAdapter(onClickListener = { category ->
            startActivity(Intent(this, AddProductActivity::class.java).apply {
                putExtra(AddProductActivity.EXTRA_CATEGORY, category)
            })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}