package com.example.jstore.ui.lokasipengiriman

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.example.jstore.databinding.ActivityAddLokasiPengirimanBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.LokasiPengiriman
import com.example.jstore.ui.checkout.CheckoutActivity
import com.example.jstore.ui.checkout.SelectCityActivity
import com.example.jstore.ui.checkout.SelectProvinceActivity
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class AddLokasiPengirimanActivity : BaseActivity() {
    private var _binding: ActivityAddLokasiPengirimanBinding? = null
    private val binding get() = _binding!!

    private var provinceId: String? = null
    private var cityId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddLokasiPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding?.edtProvince?.setOnClickListener {
            provinceLauncher.launch(Intent(this, SelectProvinceActivity::class.java))
        }

        binding?.edtCity?.setOnClickListener {
            cityLauncher.launch(Intent(this, SelectCityActivity::class.java).apply {
                putExtra(SelectCityActivity.EXTRA_PROVINCE_ID, provinceId)
            })
        }
        binding.btnAddLokasi.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                edtName.text.toString().trim().isEmpty() -> tilName.error = getString(
                    R.string.empty_field, getString(
                        R.string.nama_lokasi
                    )
                )
                edtAlamat.text.toString().trim().isEmpty() -> tilAlamat.error =
                    getString(
                        R.string.empty_field, getString(
                            R.string.alamat_lokasi
                        )
                    )
                else -> firebaseAddLokasiPengiriman()
            }
        }
    }

    private fun firebaseAddLokasiPengiriman() {
        progress.show()
        val lokasiPengiriman = LokasiPengiriman(
            namaLokasi = binding.edtName.text.toString(),
            alamat = binding.edtAlamat.text.toString(),
            provinceId = provinceId!!,
            province = binding.edtProvince.text.toString(),
            city = binding.edtCity.text.toString(),
            cityId = cityId!!
        )
        FirestoreClass().addLokasiPengiriman(lokasiPengiriman, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_lokasi_success))
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_lokasi_failed, it.message.toString()))
        })
    }


    private var provinceLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val province =
                    data?.getParcelableExtra<GetProvinceResponse.RajaOngkir.Result>(CheckoutActivity.EXTRA_PROVINCE)
                provinceId = province?.provinceId
                binding?.edtProvince?.setText(province?.province ?: "")
                cityId = null
                binding?.edtCity?.setText("")
            }
        }

    private var cityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val city = data?.getParcelableExtra<GetCityResponse.RajaOngkir.Result>(
                    CheckoutActivity.EXTRA_CITY
                )
                cityId = city?.cityId
                binding?.edtCity?.setText(city?.cityName ?: "")
            }
        }


    companion object {
        const val EXTRA_PROVINCE = "extra_province"
        const val EXTRA_CITY = "extra_city"
        const val EXTRA_ONGKIR = "extra_ongkir"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}