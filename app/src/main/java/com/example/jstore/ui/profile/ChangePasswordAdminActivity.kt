package com.example.jstore.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityChangePasswordAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Admin
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.logError
import com.example.jstore.utils.showToast

class ChangePasswordAdminActivity : BaseActivity() {

    private var _binding: ActivityChangePasswordAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var admin: Admin

    private var oldPassFirebase: String = ""
    private var oldPass: String = ""
    private var newPass: String = ""
    private var confirmNewPass: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangePasswordAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        getAdminProfile()
    }

    private fun getAdminProfile() {
        progress.show()
        FirestoreClass().getAdminDetails(onSuccessListener = {
            progress.dismiss()
            admin = it
            oldPassFirebase = it.passwordAdmin
        }, onFailureListener = {
            progress.dismiss()
            logError("getAdminDetails: ${it.message}")
        })
    }


    private fun checkConfirmPassword() {
        newPass = binding.edtNewPassword.text.toString()
        confirmNewPass = binding.edtConfirmPassword.text.toString()
        oldPass = binding.edtOldPassword.text.toString()

        if(!oldPassFirebase.equals(oldPass)){
            binding.tilOldPassword.isErrorEnabled = true
            binding.tilOldPassword.error = getString(R.string.old_password_not_match)
        }
        else {
            if (!newPass.equals(confirmNewPass)) {
                binding.tilConfirmPassword.isErrorEnabled = true
                binding.tilConfirmPassword.error =
                    getString(R.string.confirm_new_password_not_match)
            } else {
                binding.tilOldPassword.error = null
                binding.tilOldPassword.isErrorEnabled = false
                binding.tilConfirmPassword.isErrorEnabled = false
                binding.tilConfirmPassword.error = null
                validateData()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnUpdatePassword.setOnClickListener {
            checkConfirmPassword()
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                edtOldPassword.text.toString().trim().isEmpty() -> tilOldPassword.error = getString(
                    R.string.empty_field, getString(
                        R.string.oldPassword
                    ))
                edtNewPassword.text.toString().trim().isEmpty() -> tilNewPassword.error = getString(
                    R.string.empty_field, getString(
                        R.string.newPassword
                    ))
                edtConfirmPassword.text.toString().trim().isEmpty() -> tilConfirmPassword.error = getString(
                    R.string.empty_field, getString(
                        R.string.confirmnewPassword
                    ))

                else -> firebaseChangePassword()
            }
        }
    }

    private fun firebaseChangePassword() {
        progress.show()
        val  passwordAdmin = binding.edtConfirmPassword.text.toString()
        FirestoreClass().changePasswordAdmin(passwordAdmin, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.update_password_success))
            startActivity(
                Intent(this, SettingActivity::class.java)
            )
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.update_pass_failed, it.message.toString()))
        })
    }
}