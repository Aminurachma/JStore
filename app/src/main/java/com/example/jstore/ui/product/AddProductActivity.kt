package com.example.jstore.ui.product

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityAddProductBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.imagePicker
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.SetOptions
import java.util.*

class AddProductActivity : BaseActivity() {
    private var _binding: ActivityAddProductBinding? = null
    private val binding get() = _binding!!

    private var mSelectedProductImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.cardImgProduct.setOnClickListener {
            imagePicker(startForImageResult)
        }
        binding.btnAddProduct.setOnClickListener {
            validateData()
        }
    }

    private val startForImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data
        when (resultCode) {
            Activity.RESULT_OK -> {
                mSelectedProductImageFileUri = data?.data
                binding.imgProduct.setImageURI(data?.data)
//                uploadProductImage()
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
        }
    }

//    private fun uploadProductImage() {
//        progress.show()
//        FirestoreClass().uploadImageProductToFirestore(mSelectedProductImageFileUri!!,
//            onSuccessListener = {
//              //  Prefs.productImage = mSelectedProductImageFileUri.toString()
//            progress.dismiss()
//                binding.ivProductImage.setImageURI(mSelectedProductImageFileUri!!)
//        }, onFailureListener = {
//            progress.dismiss()
//            showToast(getString(R.string.update_profile_failed, it.message.toString()))
//        })
//    }

    private fun validateData() {
        binding.apply {
            when {
                edtName.text.toString().trim().isEmpty() -> tilName.error = getString(
                    R.string.empty_field, getString(
                        R.string.name_product
                    ))
                edtCategory.text.toString().trim().isEmpty() -> tilCategory.error = getString(
                    R.string.empty_field, getString(
                        R.string.category_product
                    ))
                edtDescription.text.toString().trim().isEmpty() -> tilDescription.error = getString(
                    R.string.empty_field, getString(
                        R.string.desc_product
                    ))
                edtQty.text.toString().trim().isEmpty() -> tilQty.error = getString(
                    R.string.empty_field, getString(
                        R.string.stock_product
                    ))
                edtPrice.text.toString().trim().isEmpty() -> tilPrice.error = getString(
                    R.string.empty_field, getString(
                        R.string.price_product
                    ))
                mSelectedProductImageFileUri == null -> showToast(getString(R.string.empty_product_image))
                else -> firebaseAddProduct()
            }
        }
    }

     private fun firebaseAddProduct() {
        progress.show()
        val product = Product(
            title = binding.edtName.text.toString(),
            price = binding.edtPrice.text.toString(),
            description = binding.edtDescription.text.toString(),
            stockQuantity = binding.edtQty.text.toString().toInt(),
            category = binding.edtCategory.text.toString(),
            image = mSelectedProductImageFileUri!!.toString()
        )
        FirestoreClass().addProduct(product, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.add_product_success))
            pushActivity(ProductActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.add_product_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}