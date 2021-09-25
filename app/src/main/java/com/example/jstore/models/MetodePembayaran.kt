package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MetodePembayaran  (
val namaMetode: String = "",
val jenisMetode: String = "",
var metodeId: String = ""): Parcelable