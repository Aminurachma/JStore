package com.example.jstore.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityProfileAdminBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Admin
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.ui.setting.SettingActivity
import com.example.jstore.utils.imagePicker
import com.example.jstore.utils.logError
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth

class ProfileAdminActivity : BaseActivity() {

    private var _binding: ActivityProfileAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var admin: Admin
    private var mSelectedProfileImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAdminProfile()
        setupClickListeners()
    }

    private fun getAdminProfile() {
        progress.show()
        FirestoreClass().getAdminDetails(onSuccessListener = {
            progress.dismiss()
            admin = it
            Glide.with(this)
                .load(it.imageAdmin ?: R.drawable.user_pisc)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(binding.imgAvatar)
            binding.edtName.setText(it.fullNameAdmin)
            binding.edtEmail.setText(it.emailAdmin)
            binding.edtPhoneNumber.setText(it.mobileAdmin)
        }, onFailureListener = {
            progress.dismiss()
            logoutUser()
            logError("getAdminDetails: ${it.message}")
        })
    }

    private fun setupClickListeners() {

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnUpdateProfile.setOnClickListener {
            validateData()
        }

        binding.imgAvatar.setOnClickListener{
            imagePicker(startForImageResult)
        }
    }

    private val startForImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                mSelectedProfileImageFileUri = data?.data
                uploadProfileImage()
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
        }
    }

    private fun uploadProfileImage() {
        progress.show()
        FirestoreClass().uploadImageAdminToFirestore(mSelectedProfileImageFileUri!!, onSuccessListener = {
            progress.dismiss()
            binding.imgAvatar.setImageURI(mSelectedProfileImageFileUri)
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.update_profile_failed, it.message.toString()))
        })
    }


    private fun validateData() {
        binding.apply {
            when {
                edtName.text.toString().trim().isEmpty() -> tilName.error = getString(
                    R.string.empty_field, getString(
                        R.string.fullname
                    ))
                edtPhoneNumber.text.toString().trim().isEmpty() -> tilPhoneNumber.error = getString(
                    R.string.empty_field, getString(
                        R.string.phone_number
                    ))
                !Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString().trim()).matches() -> tilEmail.error = getString(
                    R.string.invalid_email
                )
                else -> firebaseUpdateProfile()
            }
        }
    }

    private fun firebaseUpdateProfile() {
        progress.show()
//        val admin = Admin(
//            idAdmin = Prefs.adminId,
//            fullNameAdmin = binding.edtName.text.toString(),
//            mobileAdmin = binding.edtPhoneNumber.text.toString(),
//            emailAdmin = binding.edtEmail.text.toString(),
//            imageAdmin = admin.imageAdmin
//        )
        val  fullNameAdmin = binding.edtName.text.toString()
        val  emailAdmin = binding.edtEmail.text.toString()
        val  mobileAdmin = binding.edtPhoneNumber.text.toString()
        val imageAdmin = admin.imageAdmin
        FirestoreClass().updateProfileAdmin(fullNameAdmin,emailAdmin, mobileAdmin, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.update_profile_success))
            startActivity(
                Intent(this, SettingActivity::class.java))
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.update_profile_failed, it.message.toString()))
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