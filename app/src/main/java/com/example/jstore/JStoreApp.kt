package com.example.jstore

import android.app.Application
import com.chibatching.kotpref.Kotpref
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class JStoreApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Kotpref.init(this)
    }

}