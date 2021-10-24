package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ongkir(
    val code: String? = null,
    val name: String? = null,
    val costs: List<Cost> = emptyList()
) : Parcelable {

    @Parcelize
    data class Cost(
        val service: String? = null,
        val description: String? = null,
        val cost: Cost? = null
    ) : Parcelable {
        @Parcelize
        data class Cost(
            val value: Int? = null,
            val etd: String? = null,
            val note: String? = null
        ) : Parcelable
    }
}