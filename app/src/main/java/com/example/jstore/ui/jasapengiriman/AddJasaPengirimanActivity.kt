package com.example.jstore.ui.jasapengiriman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityAddJasaPengirimanBinding
import com.example.jstore.databinding.ActivityAddRekeningBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.JasaPengiriman
import com.example.jstore.models.Rekening
import com.example.jstore.ui.rekening.RekeningActivity
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class AddJasaPengirimanActivity : BaseActivity() {
    private var _binding: ActivityAddJasaPengirimanBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddJasaPengirimanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnAddJasa.setOnClickListener {
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
                        R.string.nama_lokasi
                    )
                )
                edtEstimasi.text.toString().trim().isEmpty() -> tilEstimasi.error =
                    getString(
                        R.string.empty_field, getString(
                            R.string.estimasi
                        )
                    )
                edtHarga.text.toString().trim().isEmpty() -> tilHarga.error = getString(
                    R.string.empty_field, getString(
                        R.string.harga_jasa
                    )
                )

                else -> firebaseAddJasaPengiriman()
            }
        }
    }

    private fun firebaseAddJasaPengiriman() {
        progress.show()
        val jasaPengiriman = JasaPengiriman(
            namaJasa = binding.edtName.text.toString(),
            estimasi = binding.edtEstimasi.text.toString(),
            harga = binding.edtHarga.text.toString()
        )
        FirestoreClass().addJasaPengiriman(jasaPengiriman, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_jasa_success))
            pushActivity(JasaPengirimanActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_jasa_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}