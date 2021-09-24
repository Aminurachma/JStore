package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val cartId: String = "",
    val userId: String = "",
    val products: List<Product> = emptyList()
) : Parcelable