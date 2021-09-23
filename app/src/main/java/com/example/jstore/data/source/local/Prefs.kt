package com.example.jstore.data.source.local

import com.chibatching.kotpref.KotprefModel

object Prefs : KotprefModel() {
    var userId by stringPref()
    var userFullName by stringPref()

    var adminId by stringPref()
    var adminFullName by stringPref()

    var activeCartId by stringPref()
}