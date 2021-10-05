package com.example.jstore.ui.rekening

import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityAddRekeningBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Rekening
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class AddRekeningActivity : BaseActivity() {

    private var _binding: ActivityAddRekeningBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddRekeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnAddRekening.setOnClickListener {
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
                        R.string.nama_rekening_bank
                    ))
                edtNomorRekening.text.toString().trim().isEmpty() -> tilNomorRekening.error = getString(
                    R.string.empty_field, getString(
                        R.string.nomor_rekening
                    ))
                edtPemilikRekening.text.toString().trim().isEmpty() -> tilPemilik.error = getString(
                    R.string.empty_field, getString(
                        R.string.pemilik_nomor_rekening
                    ))

                else -> firebaseAddRekening()
            }
        }
    }

    private fun firebaseAddRekening() {
        progress.show()
        val rekening = Rekening(
            namaBank = binding.edtName.text.toString(),
            nomorRekening = binding.edtNomorRekening.text.toString(),
            atasNama = binding.edtPemilikRekening.text.toString()
        )
        FirestoreClass().addRekening(rekening, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_rekening_success))
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_rekening_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}