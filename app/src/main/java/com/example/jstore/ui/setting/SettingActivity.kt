package com.example.jstore.ui.setting

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivitySettingBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Admin
import com.example.jstore.ui.category.CategoryActivity
import com.example.jstore.ui.jasapengiriman.JasaPengirimanActivity
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.ui.lokasipengiriman.LokasiPengirimanActivity
import com.example.jstore.ui.metodepembayaran.MetodePembayaranActivity
import com.example.jstore.ui.profile.ProfileAdminActivity
import com.example.jstore.ui.rekening.RekeningActivity
import com.example.jstore.utils.logError
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class SettingActivity : BaseActivity() {

    private var _binding: ActivitySettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var admin: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAdminProfile()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnUpdateProfile.setOnClickListener {
            startActivity(Intent(this, ProfileAdminActivity::class.java))
        }
        binding.btnJasaPengiriman.setOnClickListener {
            startActivity(Intent(this, JasaPengirimanActivity::class.java))
        }
        binding.btnLokasiPengiriman.setOnClickListener {
            startActivity(Intent(this, LokasiPengirimanActivity::class.java))
        }
        binding.btnMetodePembayaran.setOnClickListener {
            startActivity(Intent(this, MetodePembayaranActivity::class.java))
        }
        binding.btnNomorRekening.setOnClickListener {
            startActivity(Intent(this, RekeningActivity::class.java))
        }
        binding.btnCategory.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun getAdminProfile() {
        progress.show()
        FirestoreClass().subscribeAdminProfile(onSuccessListener = {
            progress.dismiss()
            admin = it
            binding.tvAdminName.text = it.fullNameAdmin
            binding.tvAdminEmail.text = it.emailAdmin
            Glide.with(this)
                .load(it.imageAdmin)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(binding.imgAdminAvatar)
            Timber.e("nama admin = ${it.fullNameAdmin}" )
        }, onFailureListener = {
            progress.dismiss()
            logoutUser()
            logError("getAdminDetails: $it")
        })
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
        Prefs.clear()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}