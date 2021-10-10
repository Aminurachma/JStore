package com.example.jstore.ui.orderhistory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityOrderHistoryBinding
import com.example.jstore.databinding.ActivityUpdateResiBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Order
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.imagePicker
import com.example.jstore.utils.showToast
import com.github.dhaval2404.imagepicker.ImagePicker

class UpdateResiActivity : BaseActivity() {
    private var _binding:ActivityUpdateResiBinding? = null
    private val binding get() = _binding!!
    private lateinit var order: Order
    private var orderId: String = ""
    private var imageResi: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateResiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getStringExtra("orderId") ?: ""
        getMyOrder()
        setupClickListeners()
    }

    private fun getMyOrder() {
        progress.show()
        FirestoreClass().subscribeLastCheckout(orderId, onSuccessListener = {
            progress.dismiss()
            order = it
            binding.edtOrderId.setText(it.orderId)
            binding.edtname.setText(it.firstName)
            binding.edtTotal.setText(it.subTotalAmount.toInt().formatPrice())
        }, onFailureListener = {
            showToast(it)
        })
    }

    private fun setupClickListeners() {
        binding.ivAddUpdateResi.setOnClickListener {
            imagePicker(startForImageResult)
        }
        binding.btnUpdateResi.setOnClickListener {
            validateData()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                imageResi == null -> showToast(getString(
                    R.string.empty_field, getString(
                        R.string.upload_resi)))
                edtNomorResi.text.toString().trim().isEmpty() -> tilResi.error = getString(
                    R.string.empty_field, getString(
                        R.string.no_resi
                    )
                )
                else -> firebaseUpdateResi()
            }
        }
    }

    private fun firebaseUpdateResi() {
        progress.show()
        FirestoreClass().updateResi(orderId = orderId, imageResi!!.toString(),
            onSuccessListener = { progress.dismiss()
                showToast(getString(R.string.update_resi_success))
                startActivity(Intent(this, OrderHistoryActivity::class.java))
                finish()
            }
            , onFailureListener = { progress.dismiss() })
    }

    private val startForImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    imageResi = data?.data
                    binding.ivResiImage.setImageURI(data?.data)
                }
                ImagePicker.RESULT_ERROR -> {
                    showToast(ImagePicker.getError(data))
                }
            }
        }


}