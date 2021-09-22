package com.example.jstore

import android.app.Application
import com.chibatching.kotpref.Kotpref
import timber.log.Timber

class JStoreApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Kotpref.init(this)
    }

}