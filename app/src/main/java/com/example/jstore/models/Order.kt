package com.example.jstore.models

import android.os.Parcelable
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
    val statusPembayaran: Boolean = false,
    val statusPesanan: String = "",
    val nomorResi: String = "",
    val orderDateTime : Long = 0L,
    var orderId : String = ""
    ) : Parcelable