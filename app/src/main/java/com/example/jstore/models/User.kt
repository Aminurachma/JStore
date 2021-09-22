package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val fullName: String = "",
    val address: String = "",
    val mobile: Long = 0,
    val email: String = "",
    val image: String = "",
    val gender: String = "",
    val profileCompleted: Int = 0
) : Parcelable