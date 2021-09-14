package com.example.jstore.data.source.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String = "",
    val name: String? = null,
    val products: List<Product> = emptyList()
): Parcelable