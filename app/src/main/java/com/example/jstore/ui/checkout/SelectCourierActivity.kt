package com.example.jstore.ui.checkout

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivitySelectCourierBinding
import com.example.jstore.models.Ongkir
import com.example.jstore.ui.checkout.adapter.CourierAdapter
import com.example.jstore.utils.showToast
import com.example.jstore.vo.Status

class SelectCourierActivity : BaseActivity() {

    private var _binding: ActivitySelectCourierBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CourierAdapter
    private val viewModel: CheckoutViewModel by viewModels()

    private lateinit var originId: String
    private lateinit var destinationId: String
    private val courierList = mutableListOf<Ongkir>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySelectCourierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getCourier("jne")
        getCourier("pos")
        getCourier("tiki")

    }

    private fun setupAdapter() {
        adapter = CourierAdapter {
            setResult(RESULT_OK, Intent().putExtra(CheckoutActivity.EXTRA_ONGKIR, it))
            finish()
        }
    }

    private fun getCourier(courier: String) {
        viewModel.getCost(origin = originId, destination = destinationId, courier = courier).observe(this) {
            when(it.status) {
                Status.LOADING -> {
                    progress.show()
                }
                Status.ERROR -> {
                    progress.dismiss()
                    showToast(it.message.toString())
                }
                Status.SUCCESS -> {
                    if (courier == "tiki") progress.dismiss()
                    val data = it.data?.rajaOngkir?.results?.firstOrNull()
                    courierList.addAll(data?.costs?.map { detail ->
                        Ongkir(
                            code = data.code,
                            name = data.name,
                            service = detail.service,
                            cost = detail.cost.firstOrNull()?.value ?: 0,
                            etd = detail.cost.firstOrNull()?.etd
                        )
                    } ?: emptyList())
                    adapter.notifyItemRangeInserted(adapter.itemCount -1, data?.costs?.size ?: adapter.itemCount -1)
                }
            }
        }
    }

    private fun setupUI() {
        originId = intent.getStringExtra(EXTRA_ORIGIN) ?: ""
        destinationId = intent.getStringExtra(EXTRA_DESTINATION) ?: ""
        adapter.submitList(courierList)
        binding.rvCourier.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_ORIGIN = "extra_origin"
        const val EXTRA_DESTINATION = "extra_destination"
    }

}