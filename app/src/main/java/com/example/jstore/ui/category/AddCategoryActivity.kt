package com.example.jstore.ui.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityAddCategoryBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Category
import com.example.jstore.models.MetodePembayaran
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class AddCategoryActivity : BaseActivity() {
    private var _binding: ActivityAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnAddCategory.setOnClickListener {
            validateData()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                edtName.text.toString().trim().isEmpty() -> tilName.error = getString(
                    R.string.empty_field, getString(
                        R.string.category_product
                    )
                )
                else -> firebaseAddCategory()
            }
        }
    }

    private fun firebaseAddCategory() {
        progress.show()
        val category = Category(
            namaCategory = binding.edtName.text.toString()
        )
        FirestoreClass().addCategory(category, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_category_success))
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_category_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}