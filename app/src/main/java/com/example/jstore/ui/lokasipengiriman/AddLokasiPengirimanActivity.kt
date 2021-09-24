package com.example.jstore.ui.lokasipengiriman

import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityAddLokasiPengirimanBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.LokasiPengiriman
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class AddLokasiPengirimanActivity : BaseActivity() {
    private var _binding: ActivityAddLokasiPengirimanBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddLokasiPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListener()
    }

    private fun setupClickListener() {
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
            alamat = binding.edtAlamat.text.toString()
        )
        FirestoreClass().addLokasiPengiriman(lokasiPengiriman, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_lokasi_success))
            pushActivity(LokasiPengirimanActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_lokasi_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}