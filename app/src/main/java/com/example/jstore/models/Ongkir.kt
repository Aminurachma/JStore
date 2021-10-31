package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ongkir(
    val code: String? = null,
    val name: String? = null,
    val service: String? = null,
    val cost: Long = 0,
    val etd: String? = null
) : Parcelable