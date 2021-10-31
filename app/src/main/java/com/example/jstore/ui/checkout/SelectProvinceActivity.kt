package com.example.jstore.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivitySelectProvinceBinding
import com.example.jstore.ui.checkout.CheckoutActivity.Companion.EXTRA_PROVINCE
import com.example.jstore.ui.checkout.adapter.ProvinceAdapter
import com.example.jstore.utils.showToast
import com.example.jstore.vo.Status

class SelectProvinceActivity : BaseActivity() {

    private var _binding: ActivitySelectProvinceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProvinceAdapter
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectProvinceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getAllProvinces()

    }

    private fun setupAdapter() {
        adapter = ProvinceAdapter {
            setResult(RESULT_OK, Intent().putExtra(EXTRA_PROVINCE, it))
            finish()
        }
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.rvProvince.adapter = adapter
    }

    private fun getAllProvinces() {
        viewModel.getProvince().observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    progress.dismiss()
                    adapter.submitList(it.data?.rajaongkir?.results)
                }
                Status.ERROR -> {
                    progress.dismiss()
                    showToast(it.message.toString())
                }
                Status.LOADING -> {
                    progress.show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}