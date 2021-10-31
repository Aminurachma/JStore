package com.example.jstore.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivitySelectCityBinding
import com.example.jstore.ui.checkout.adapter.CityAdapter
import com.example.jstore.utils.showToast
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible
import com.example.jstore.vo.Status

class SelectCityActivity : BaseActivity() {

    private var _binding: ActivitySelectCityBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CityAdapter
    private val viewModel: CheckoutViewModel by viewModels()

    private lateinit var provinceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        setOnClickListeners()
        getAllCities()

    }

    private fun setupAdapter() {
        adapter = CityAdapter {
            setResult(RESULT_OK, Intent().putExtra(CheckoutActivity.EXTRA_CITY, it))
            finish()
        }
    }

    private fun setupUI() {
        provinceId = intent.getStringExtra(EXTRA_PROVINCE_ID) ?: ""
        binding.rvCity.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getAllCities() {
        viewModel.getCities(provinceId).observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    progress.dismiss()
                    if (it.data?.rajaOngkir?.results?.isNotEmpty() == true) {
                        binding.noData.toGone()
                        binding.rvCity.toVisible()
                        adapter.submitList(it.data.rajaOngkir.results)
                    } else {
                        binding.rvCity.toGone()
                        binding.noData.toVisible()
                    }
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

    companion object {
        const val EXTRA_PROVINCE_ID = "extra_province_id"
    }

}