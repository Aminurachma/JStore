package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val stockQuantity: Int = 0,
    val category: String = "",
    val image: String = "",
    var productId: String = ""
) : Parcelable