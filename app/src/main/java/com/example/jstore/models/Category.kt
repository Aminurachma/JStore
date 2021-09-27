package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category (
    val namaCategory: String = "",
    var categoryId: String = ""): Parcelable