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
import com.example.jstore.databinding.ActivityProfileBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.ui.login.customer.MainActivity
import com.example.jstore.utils.imagePicker
import com.example.jstore.utils.logError
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : BaseActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserDetails: User

    private var mSelectedProfileImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserProfile()
        setupClickListeners()

    }

    private fun getUserProfile() {
        progress.show()
        FirestoreClass().subscribeUserProfile(onSuccessListener = {
            progress.dismiss()
            mUserDetails = it
            Glide.with(this)
                .load(it.image ?: R.drawable.user_pisc)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(binding.imgAvatar)
            binding.edtName.setText(it.fullName)
            binding.edtEmail.setText(it.email)
            binding.edtAddress.setText(it.address)
            binding.edtPhoneNumber.setText(it.mobile)
        }, onFailureListener = {
            progress.dismiss()
            logoutUser()
            logError("getUserDetails: $it")
        })
    }

    private fun setupClickListeners() {
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

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
        FirestoreClass().uploadImageToFirestore(mSelectedProfileImageFileUri!!, onSuccessListener = {
            progress.dismiss()
//            binding.imgAvatar.setImageURI(mSelectedProfileImageFileUri)
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
                edtAddress.text.toString().trim().isEmpty() -> tilAddress.error = getString(
                    R.string.empty_field, getString(
                        R.string.address
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
        val user = User(
            id = mUserDetails.id,
            fullName = binding.edtName.text.toString(),
            address = binding.edtAddress.text.toString(),
            mobile = binding.edtPhoneNumber.text.toString(),
            email = binding.edtEmail.text.toString(),
            image = mUserDetails.image
        )
        FirestoreClass().updateProfileUser(user, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.update_profile_success))
            pushActivity(HomeCustomerActivity::class.java)
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