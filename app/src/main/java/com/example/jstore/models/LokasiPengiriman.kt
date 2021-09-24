package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LokasiPengiriman(
    val namaLokasi: String = "",
    val alamat: String = "",
    var lokasiId: String = ""): Parcelable