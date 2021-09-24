package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rekening (
    val namaBank: String = "",
    val atasNama: String = "",
    val nomorRekening: String = "",
    val image: String = "",
    var rekeningId: String = ""
): Parcelable