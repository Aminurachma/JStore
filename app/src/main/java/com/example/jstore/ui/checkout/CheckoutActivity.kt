package com.example.jstore.ui.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityCheckoutBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.*
import com.example.jstore.ui.category.CategoryActivity
import com.example.jstore.ui.jasapengiriman.JasaPengirimanActivity
import com.example.jstore.ui.lokasipengiriman.LokasiPengirimanActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity
import com.example.jstore.ui.product.AddProductActivity
import com.example.jstore.ui.rekening.RekeningActivity
import com.example.jstore.utils.Constants
import com.example.jstore.utils.logError

@Suppress("DEPRECATION")
class CheckoutActivity : BaseActivity() {
    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserDetails: User

    private lateinit var metodePembayaran: MetodePembayaran
    private lateinit var rekening: Rekening
    private lateinit var lokasiPengiriman: LokasiPengiriman
    private lateinit var jasaPengiriman: JasaPengiriman

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserProfile()
        setupClickListener()

        rekening = intent.getParcelableExtra(EXTRA_REKENING) ?: Rekening()
        binding.edtRekening.setText(rekening.namaBank+"-"+rekening.nomorRekening)


    }


    private fun getUserProfile() {
        progress.show()
        FirestoreClass().subscribeUserProfile(onSuccessListener = {
            progress.dismiss()
            mUserDetails = it
            binding.edtName.setText(it.firstName +" "+it.lastName)
            binding.edtAddress.setText(it.address)
            binding.edtMobile.setText(it.mobile)
        }, onFailureListener = {
            progress.dismiss()
            logError("getUserDetails: $it")
        })
    }

    private fun setupClickListener() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.edtPayment.setOnClickListener {
            paymentMethodLauncher.launch(Intent(this, MetodePembayaranActivity::class.java).apply {
                putExtra(MetodePembayaranActivity.EXTRA_TYPE, MetodePembayaranActivity.TYPE_CUSTOMER)
            })
        }
        binding.edtDeliveryLocation.setOnClickListener {
            deliveryAddressLauncher.launch(Intent(this, LokasiPengirimanActivity::class.java).apply {
                putExtra(LokasiPengirimanActivity.EXTRA_TYPE, LokasiPengirimanActivity.TYPE_CUSTOMER)
            })
        }

        binding.edtDeliveryService.setOnClickListener {
            deliveryServiceLauncher.launch(Intent(this, JasaPengirimanActivity::class.java).apply {
                putExtra(JasaPengirimanActivity.EXTRA_TYPE, JasaPengirimanActivity.TYPE_CUSTOMER)
            })
        }



//        if (binding.edtPayment.setText(metodePembayaran.jenisMetode).equals("Transfer")){
//            binding.linierrekening.isGone = false
//            binding.edtRekening.setOnClickListener {
//                bankAccountLauncher.launch(Intent(this, RekeningActivity::class.java).apply {
//                    putExtra(RekeningActivity.EXTRA_TYPE, RekeningActivity.TYPE_CUSTOMER)
//                })
//            }
//        }
    }

    var paymentMethodLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val paymentMethod = result.data?.getParcelableExtra(EXTRA_METODE_PEMBAYARAN) ?: MetodePembayaran()
            binding.edtPayment.setText(paymentMethod.jenisMetode)
            metodePembayaran = paymentMethod
        }
    }

    var deliveryAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val deliveryAddress = result.data?.getParcelableExtra(EXTRA_LOKASI) ?: LokasiPengiriman()
            binding.edtDeliveryLocation.setText(deliveryAddress.alamat)
            lokasiPengiriman = deliveryAddress
        }
    }

    var deliveryServiceLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val deliveryService = result.data?.getParcelableExtra(EXTRA_JASA) ?: JasaPengiriman()
            binding.edtDeliveryService.setText(deliveryService.namaJasa)
            jasaPengiriman = deliveryService
        }
    }

    var bankAccountLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bankAccount = result.data?.getParcelableExtra(EXTRA_REKENING) ?: Rekening()
            binding.edtRekening.setText(bankAccount.namaBank)
            rekening = bankAccount
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_METODE_PEMBAYARAN = "extra_metode_pembayaran"
        const val EXTRA_REKENING = "extra_rekening"
        const val EXTRA_LOKASI = "extra_lokasi"
        const val EXTRA_JASA = "extra_jasa"
    }
}