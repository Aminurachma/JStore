package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JasaPengiriman(
    val namaJasa: String = "",
    val estimasi: String = "",
    val harga: String = "",
    val image: String = "",
    var jasaPengirimanId: String = ""
):Parcelable