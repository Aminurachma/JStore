package com.example.jstore.ui.checkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityCheckoutBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.*
import com.example.jstore.ui.invoice.InvoiceActivity
import com.example.jstore.ui.jasapengiriman.JasaPengirimanActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity
import com.example.jstore.ui.rekening.RekeningActivity
import com.example.jstore.utils.*
import com.example.jstore.utils.Constants.BELUM_DIBAYAR

@Suppress("DEPRECATION")
class CheckoutActivity : BaseActivity() {
    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserDetails: User

    private var subTotal: Int = 0
    private var ongkir: Int = 0
    private var totalPrice: Int = 0

    private var atasNamaRekening: String = ""
    private var nomorRekening: String = ""

    private lateinit var cart: Cart

    private lateinit var metodePembayaran: MetodePembayaran
    private lateinit var rekening: Rekening
//    private lateinit var lokasiPengiriman: LokasiPengiriman
    private lateinit var jasaPengiriman: JasaPengiriman

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserProfile()
        setupClickListener()
        getMyCart()
        calculateTotal()

    }

    private fun getMyCart() {
        FirestoreClass().subscribeToCart(onSuccessListener = {
            cart = it
        }, onFailureListener = {
            showToast(it)
        })
    }

    private fun calculateSubTotal() {

        subTotal += totalPrice + ongkir
        binding.edtSubtotal.text = subTotal.formatPrice()
    }

    private fun calculateTotal() {
        totalPrice = intent.getIntExtra("totalPrice",0)
        binding.edtTotal.text = totalPrice.formatPrice()

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
//        binding.edtDeliveryLocation.setOnClickListener {
//            deliveryAddressLauncher.launch(Intent(this, LokasiPengirimanActivity::class.java).apply {
//                putExtra(LokasiPengirimanActivity.EXTRA_TYPE, LokasiPengirimanActivity.TYPE_CUSTOMER)
//            })
//        }

        binding.edtDeliveryService.setOnClickListener {
            deliveryServiceLauncher.launch(Intent(this, JasaPengirimanActivity::class.java).apply {
                putExtra(JasaPengirimanActivity.EXTRA_TYPE, JasaPengirimanActivity.TYPE_CUSTOMER)
            })
        }
        binding.edtRekening.setOnClickListener {
            bankAccountLauncher.launch(Intent(this, RekeningActivity::class.java).apply {
                putExtra(RekeningActivity.EXTRA_TYPE, RekeningActivity.TYPE_CUSTOMER)
            })
        }

        binding.btnCheckout.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        binding.apply {
            when {
                edtDeliveryService.text.toString().trim().isEmpty() -> tilDelivery.error = getString(
                    R.string.empty_field, getString(
                        R.string.choose_delivery_service
                    )
                )
                edtPayment.text.toString().trim().isEmpty() -> tilPayment.error = getString(
                    R.string.empty_field, getString(
                        R.string.payment
                    )
                )
                else -> firebaseCheckout()
            }
        }
    }

    private fun firebaseCheckout() {
        progress.show()
        val order = Order(
            userId = mUserDetails.id,
            firstName = mUserDetails.firstName,
            products = cart.products.toMutableList(),
            address = mUserDetails.address,
            jasaPengiriman = binding.edtDeliveryService.text.toString(),
            metodePembayaran = binding.edtPayment.text.toString(),
            namaRekening = binding.edtRekening.text.toString(),
            atasNamaRekening = atasNamaRekening,
            nomorRekening = nomorRekening,
            subTotalAmount = subTotal.toString(),
            shippingCharge = ongkir.toString(),
            totalAmount = totalPrice.toString(),
            statusPembayaran = BELUM_DIBAYAR,
            statusPesanan = BELUM_DIBAYAR,
            mobile = binding.edtMobile.text.toString(),
            orderDateTime = System.currentTimeMillis()
        )
        FirestoreClass().placeOrder(cart.cartId,order, onSuccessListener = { orderId ->
            progress.dismiss()
            showToast(getString(R.string.checkout_success))
            val intent = Intent(this, InvoiceActivity::class.java)
            intent.putExtra("orderId", orderId)
            startActivity(intent)

            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.checkout_failed, it.message.toString()))
        })
    }

//    private fun updateMyCart() {
//        progress.show()
//        FirestoreClass().updateCheckoutMyCart(cart.cartId,
//            onSuccessListener = { progress.dismiss() }, onFailureListener = { progress.dismiss() })
//    }


    private fun checkPaymentMethod() {
        if(!binding.edtPayment.text.toString().equals("Transfer")){
            binding.linierrekening.toGone()
        }else{
            binding.linierrekening.toVisible()
        }
    }

    var paymentMethodLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val paymentMethod = result.data?.getParcelableExtra(EXTRA_METODE_PEMBAYARAN) ?: MetodePembayaran()
            binding.edtPayment.setText(paymentMethod.jenisMetode)
            metodePembayaran = paymentMethod
        }
        checkPaymentMethod()
    }

//    var deliveryAddressLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val deliveryAddress = result.data?.getParcelableExtra(EXTRA_LOKASI) ?: LokasiPengiriman()
//            binding.edtDeliveryLocation.setText(deliveryAddress.alamat)
//            lokasiPengiriman = deliveryAddress
//        }
//    }

    var deliveryServiceLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val deliveryService = result.data?.getParcelableExtra(EXTRA_JASA) ?: JasaPengiriman()
            binding.edtDeliveryService.setText(deliveryService.namaJasa)
            binding.edtOngkir.setText(deliveryService.harga.toInt().formatPrice())
            ongkir  = deliveryService.harga.toInt()
            jasaPengiriman = deliveryService
            calculateSubTotal()
        }
    }

    var bankAccountLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bankAccount = result.data?.getParcelableExtra(EXTRA_REKENING) ?: Rekening()
            binding.edtRekening.setText(bankAccount.namaBank)
            atasNamaRekening = bankAccount.atasNama
            nomorRekening = bankAccount.nomorRekening
            rekening = bankAccount
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_TOTAL_PRICE = "extra_total_price"
        const val EXTRA_METODE_PEMBAYARAN = "extra_metode_pembayaran"
        const val EXTRA_REKENING = "extra_rekening"
        const val EXTRA_LOKASI = "extra_lokasi"
        const val EXTRA_JASA = "extra_jasa"
    }
}