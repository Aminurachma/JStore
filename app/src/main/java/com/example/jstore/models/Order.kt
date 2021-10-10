package com.example.jstore.models

import android.os.Parcelable
import com.example.jstore.utils.Constants
import com.example.jstore.utils.Constants.BELUM_DIBAYAR
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order (
    val userId : String = "",
    val firstName: String = "",
    val products : List<Product> = emptyList(),
    val address: String = "",
    val jasaPengiriman: String = "",
    val metodePembayaran: String= "",
    val namaRekening: String = "",
    val nomorRekening: String = "",
    val atasNamaRekening: String = "",
    val alamatPengiriman: String = "",
    val subTotalAmount : String = "",
    val shippingCharge : String = "",
    val totalAmount : String = "",
    val statusPembayaran: String = BELUM_DIBAYAR,
    val statusPesanan :String = BELUM_DIBAYAR,
    val nomorResi: String = "",
    val resiImage: String = "",
    val mobile: String = "",
    val imageBuktiBayar: String = "",
    val orderDateTime : Long = 0L,
    var orderId : String = ""
    ) : Parcelable