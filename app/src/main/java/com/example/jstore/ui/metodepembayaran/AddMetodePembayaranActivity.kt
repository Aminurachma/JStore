package com.example.jstore.ui.metodepembayaran

import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityAddMetodePembayaranBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.MetodePembayaran
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class AddMetodePembayaranActivity : BaseActivity() {

    private var _binding: ActivityAddMetodePembayaranBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddMetodePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnAddMetodePembayaran.setOnClickListener {
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
                        R.string.nama_metode_pembayaran
                    )
                )
                edtJenisMetodePembayaran.text.toString().trim().isEmpty() -> tilJenisMetodePembayaran.error =
                    getString(
                        R.string.empty_field, getString(
                            R.string.jenis_metode_pembayaran
                        )
                    )
                else -> firebaseAddMetodePembayaran()
            }
        }
    }

    private fun firebaseAddMetodePembayaran() {
        progress.show()
        val metodePembayaran = MetodePembayaran(
            namaMetode = binding.edtName.text.toString(),
            jenisMetode = binding.edtJenisMetodePembayaran.text.toString()
        )
        FirestoreClass().addMetodePembayaran(metodePembayaran, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_metode_success))
            pushActivity(MetodePembayaranActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_metode_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}