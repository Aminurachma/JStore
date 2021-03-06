package com.example.jstore.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.jstore.R
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivitySplashscreenBinding
import com.example.jstore.ui.home.admin.HomeAdminActivity
import com.example.jstore.ui.login.customer.MainActivity

class SplashscreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashscreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (Prefs.adminId.isEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, HomeAdminActivity::class.java))
            }
            finish()
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}