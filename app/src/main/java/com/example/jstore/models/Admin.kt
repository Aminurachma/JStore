package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Admin  (
    val idAdmin: String = "",
    val fullNameAdmin: String = "",
    val mobileAdmin: Long = 0,
    val emailAdmin: String = "",
    val roleAdmin: Int = 1 ,
    val imageAdmin: String = "",
    val genderAdmin: String = ""):Parcelable