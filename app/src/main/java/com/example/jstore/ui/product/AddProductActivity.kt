package com.example.jstore.ui.product

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityAddProductBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.imagePicker
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import java.util.*

class AddProductActivity : BaseActivity() {
    private var _binding: ActivityAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var mProductDetails: Product

    private var mSelectedProductImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.ivAddUpdateProduct.setOnClickListener {
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
                uploadProductImage()
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
        }
    }

    private fun uploadProductImage() {
        progress.show()
        FirestoreClass().uploadImageProductToFirestore(mSelectedProductImageFileUri!!, onSuccessListener = {
            progress.dismiss()
            binding.ivProductImage.setImageURI(mSelectedProductImageFileUri)
            firebaseAddProduct()
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
                else -> firebaseAddProduct()
            }
        }
    }

     fun firebaseAddProduct() {
        progress.show()
        val product = Product(
            title = binding.edtName.text.toString(),
            price = binding.edtPrice.text.toString(),
            description = binding.edtDescription.text.toString(),
            stockQuantity = binding.edtQty.text.toString().toInt(),
            category = binding.edtCategory.text.toString(),
            image = mProductDetails.image
        )
        FirestoreClass().addProduct(product, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.update_profile_success))
            pushActivity(ProductActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.update_profile_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}