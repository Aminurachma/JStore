package com.example.jstore.ui.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.example.jstore.databinding.ActivityCheckoutBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.*
import com.example.jstore.ui.invoice.InvoiceActivity
import com.example.jstore.ui.jasapengiriman.JasaPengirimanActivity
import com.example.jstore.ui.lokasipengiriman.LokasiPengirimanActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity
import com.example.jstore.ui.rekening.RekeningActivity
import com.example.jstore.utils.*
import com.example.jstore.utils.Constants.BELUM_DIBAYAR
import timber.log.Timber

class CheckoutActivity : BaseActivity() {

    private var _binding: ActivityCheckoutBinding? = null
    private val binding get() = _binding
    private val viewModel: CheckoutViewModel by viewModels()

    private lateinit var mUserDetails: User

    private var subTotal: Int = 0
    private var ongkir: Int = 0
    private var totalPrice: Int = 0

    private var atasNamaRekening: String = ""
    private var nomorRekening: String = ""

    private lateinit var cart: Cart

    private lateinit var metodePembayaran: MetodePembayaran
    private lateinit var rekening: Rekening
    private lateinit var lokasiPengiriman: LokasiPengiriman

    private lateinit var jasaPengiriman: JasaPengiriman

    private var provinceId: String? = null
    private var cityId: String? = null
    private var origin: String = "" // default Malang

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        getAdminProfile()
        getUserProfile()
        setupClickListener()
        getMyCart()
        calculateTotal()

    }

//    private fun getAdminProfile() {
//        progress.show()
//        FirestoreClass().subscribeAdminProfile(onSuccessListener = {
//            progress.dismiss()
//            origin = it.cityId
//        }, onFailureListener = {
//            progress.dismiss()
//            logError("getAdminDetails: $it")
//        })
//    }

    private fun getMyCart() {
        FirestoreClass().subscribeToCart(onSuccessListener = {
            cart = it
        }, onFailureListener = {
            showToast(it)
        })
    }

    private fun calculateSubTotal() {

        subTotal += totalPrice + ongkir
        binding?.edtSubtotal?.text = subTotal.formatPrice()
    }

    private fun calculateTotal() {
        totalPrice = intent.getIntExtra("totalPrice", 0)
        binding?.edtTotal?.text = totalPrice.formatPrice()

    }


    @SuppressLint("SetTextI18n")
    private fun getUserProfile() {
        progress.show()
        FirestoreClass().subscribeUserProfile(onSuccessListener = {
            progress.dismiss()
            mUserDetails = it
            binding?.edtName?.setText("${it.firstName} ${it.lastName}")
            binding?.edtAddress?.setText(it.address)
            binding?.edtMobile?.setText(it.mobile)
        }, onFailureListener = {
            progress.dismiss()
            logError("getUserDetails: $it")
        })
    }

    private fun setupClickListener() {
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.edtPayment?.setOnClickListener {
            paymentMethodLauncher.launch(Intent(this, MetodePembayaranActivity::class.java).apply {
                putExtra(
                    MetodePembayaranActivity.EXTRA_TYPE,
                    MetodePembayaranActivity.TYPE_CUSTOMER
                )
            })

        }
        binding?.edtDeliveryLocation?.setOnClickListener {
            deliveryAddressLauncher.launch(
                Intent(
                    this,
                    LokasiPengirimanActivity::class.java
                ).apply {
                    putExtra(
                        LokasiPengirimanActivity.EXTRA_TYPE,
                        LokasiPengirimanActivity.TYPE_CUSTOMER
                    )
                })
        }

        binding?.edtDeliveryService?.setOnClickListener {
            if (!cityId.isNullOrEmpty()) {
                deliveryServiceLauncher.launch(
                    Intent(
                        this,
                        SelectCourierActivity::class.java
                    ).apply {
                        putExtra(SelectCourierActivity.EXTRA_ORIGIN, cityId)
                        putExtra(SelectCourierActivity.EXTRA_DESTINATION, origin)
                    })
            }
        }
        binding?.edtRekening?.setOnClickListener {
            bankAccountLauncher.launch(Intent(this, RekeningActivity::class.java).apply {
                putExtra(RekeningActivity.EXTRA_TYPE, RekeningActivity.TYPE_CUSTOMER)
            })
        }

        binding?.btnCheckout?.setOnClickListener {
            validateData()
        }

        binding?.edtProvince?.setOnClickListener {
            provinceLauncher.launch(Intent(this, SelectProvinceActivity::class.java))
        }

        binding?.edtCity?.setOnClickListener {
            cityLauncher.launch(Intent(this, SelectCityActivity::class.java).apply {
                putExtra(SelectCityActivity.EXTRA_PROVINCE_ID, provinceId)
            })
        }

    }

    private fun validateData() {
        binding?.apply {
            when {
                provinceId.isNullOrEmpty() -> tilProvince.error =
                    getString(R.string.select_x_first, getString(R.string.province))
                cityId.isNullOrEmpty() -> tilCity.error =
                    getString(R.string.select_x_first, getString(R.string.province))
                edtPayment.text.toString().trim().isEmpty() -> tilPayment.error =
                    getString(R.string.empty_field, getString(R.string.payment))
                edtDeliveryService.text.toString().trim().isEmpty() -> tilDelivery.error =
                    getString(
                        R.string.empty_field, getString(
                            R.string.choose_delivery_service
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
            jasaPengiriman = binding?.edtDeliveryService?.text.toString(),
            metodePembayaran = binding?.edtPayment?.text.toString(),
            namaRekening = binding?.edtRekening?.text.toString(),
            atasNamaRekening = atasNamaRekening,
            nomorRekening = nomorRekening,
            subTotalAmount = subTotal.toString(),
            shippingCharge = ongkir.toString(),
            alamatPengiriman = binding?.edtDeliveryLocation?.text.toString(),
            totalAmount = totalPrice.toString(),
            statusPembayaran = BELUM_DIBAYAR,
            statusPesanan = BELUM_DIBAYAR,
            mobile = binding?.edtMobile?.text.toString(),
            orderDateTime = System.currentTimeMillis()
        )
        FirestoreClass().placeOrder(cart.cartId, order, onSuccessListener = { orderId ->
            progress.dismiss()
            showToast(getString(R.string.checkout_success))
            cart.products.forEach { product ->
                FirestoreClass().updateProductStock(
                    productId = product.productId,
                    stock = product.stockQuantity - product.quantity,
                    onSuccessListener = {
                        startActivity(Intent(this, InvoiceActivity::class.java).apply {
                            putExtra("orderId", orderId)
                        })
                        finish()
                    },
                    onFailureListener = {
                        showToast(it.message.toString())
                    }
                )
            }
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
        if (binding?.edtPayment?.text.toString() != "Transfer") {
            binding?.linierrekening?.toGone()
        } else {
            binding?.linierrekening?.toVisible()
        }
    }

    private var paymentMethodLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val paymentMethod =
                    result.data?.getParcelableExtra(EXTRA_METODE_PEMBAYARAN) ?: MetodePembayaran()
                binding?.edtPayment?.setText(paymentMethod.jenisMetode)
                metodePembayaran = paymentMethod
            }
            checkPaymentMethod()
        }

    var deliveryAddressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val deliveryAddress =
                    result.data?.getParcelableExtra(EXTRA_LOKASI) ?: LokasiPengiriman()
                binding?.edtDeliveryLocation?.setText(deliveryAddress.namaLokasi)
                lokasiPengiriman = deliveryAddress
                origin = deliveryAddress.cityId
            }
        }

    private var deliveryServiceLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val courier = result.data?.getParcelableExtra(EXTRA_ONGKIR) ?: Ongkir()
                binding?.edtDeliveryService?.setText("${courier.name} - ${courier.service}")
                binding?.edtOngkir?.text = courier.cost.formatPrice()
                ongkir = courier.cost.toInt()
                calculateSubTotal()
            }
        }

    private var bankAccountLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bankAccount = result.data?.getParcelableExtra(EXTRA_REKENING) ?: Rekening()
                binding?.edtRekening?.setText(bankAccount.namaBank)
                atasNamaRekening = bankAccount.atasNama
                nomorRekening = bankAccount.nomorRekening
                rekening = bankAccount
            }
        }

    private var provinceLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val province =
                    data?.getParcelableExtra<GetProvinceResponse.RajaOngkir.Result>(EXTRA_PROVINCE)
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
                val city = data?.getParcelableExtra<GetCityResponse.RajaOngkir.Result>(EXTRA_CITY)
                cityId = city?.cityId
                binding?.edtCity?.setText(city?.cityName ?: "")
                binding?.edtDeliveryService?.setText("")
                binding?.edtOngkir?.text = ""
                ongkir = 0
                calculateSubTotal()
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

        const val EXTRA_PROVINCE = "extra_province"
        const val EXTRA_CITY = "extra_city"
        const val EXTRA_ONGKIR = "extra_ongkir"
    }
}