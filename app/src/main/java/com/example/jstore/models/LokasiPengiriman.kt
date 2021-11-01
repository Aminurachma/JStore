package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LokasiPengiriman(
    val namaLokasi: String = "",
    val alamat: String = "",
    val province: String = "",
    val city: String = "",
    val provinceId: String = "",
    val cityId: String = "",
    var lokasiPengirimanId: String = ""): Parcelable