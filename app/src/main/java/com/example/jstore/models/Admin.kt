package com.example.jstore.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Admin(
    val idAdmin: String = "",
    val fullNameAdmin: String = "",
    val mobileAdmin: String = "",
    val emailAdmin: String = "",
    val imageAdmin: String = "",
    val genderAdmin: String = "",
    val passwordAdmin: String = "",
    val addressAdmin: String = "",
    val provinceAdmin: String = "",
    val cityAdmin: String = ""
) : Parcelable