package com.example.jstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.jstore.ui.login.customer.MainActivity

class SplashscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        Handler(Looper.getMainLooper()).postDelayed({
            //getCurrentLocation()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}