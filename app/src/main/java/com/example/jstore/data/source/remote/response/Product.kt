package com.example.jstore.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String? = null,
    val name: String? = null,
    val category: Category? = null,
    val image: String? = null,
    val price: String? = null,
): Parcelable