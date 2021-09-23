package com.example.jstore.ui.home.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityHomeAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Admin
import com.example.jstore.ui.customer.CustomerActivity
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.ui.product.ProductActivity
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.logError
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class HomeAdminActivity : BaseActivity() {

    private var _binding: ActivityHomeAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var admin: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAdminDetail()
        setupUI()
        setupClickListeners()
        getAdminProfile()

    }

    private fun getAdminProfile() {
        progress.show()
        FirestoreClass().subscribeAdminProfile(onSuccessListener = {
            progress.dismiss()
            admin = it
            binding.tvWelcomingText.text = getString(R.string.hello_user, it.fullNameAdmin)
            Timber.e("it.fullname ${it.fullNameAdmin}" )
        }, onFailureListener = {
            progress.dismiss()
            logoutUser()
            logError("getAdminDetails: $it")
        })
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,MainActivity::class.java))
        Prefs.clear()
        finish()
    }

    private fun setupClickListeners() {
        binding.btnCustomers.setOnClickListener {
            startActivity(Intent(this,CustomerActivity::class.java))
        }
        binding.btnProducts.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }
    }

    private fun getAdminDetail() {
        progress.show()
        FirestoreClass().getAdminDetails(onSuccessListener = {
            progress.dismiss()
            admin = it
        }, onFailureListener = {
            progress.dismiss()
            showToast(it.message.toString())
        })
    }

    private fun setupUI() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}