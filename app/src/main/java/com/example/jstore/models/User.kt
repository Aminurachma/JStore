package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
//    val fullName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val mobile: String = "",
    val email: String = "",
    val image: String? = null,
    val gender: String = "",
    val profileCompleted: Int = 0
) : Parcelable