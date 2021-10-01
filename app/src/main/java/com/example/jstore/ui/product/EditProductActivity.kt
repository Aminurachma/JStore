package com.example.jstore.ui.product

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ActivityEditProductBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Product
import com.example.jstore.models.User
import com.example.jstore.ui.home.customer.HomeCustomerActivity
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.imagePicker
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast
import com.github.dhaval2404.imagepicker.ImagePicker
import timber.log.Timber

class EditProductActivity : BaseActivity() {
    private var _binding: ActivityEditProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product
    private var mSelectedProductImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        product = intent.getParcelableExtra(EXTRA_PRODUCT) ?: Product()
        Glide.with(this)
            .load(product.image)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .into(binding.imgProduct)
        binding.edtName.setText(product.title)
        binding.edtCategory.setText(product.category)
        binding.edtQty.setText(product.stockQuantity.toString())
        binding.edtDescription.setText(product.description)
        binding.edtPrice.setText(product.price)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.cardImgProduct.setOnClickListener {
            imagePicker(startForImageResult)
        }
        binding.btnEditProduct.setOnClickListener {
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
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
        }
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
                mSelectedProductImageFileUri == null -> showToast(getString(R.string.empty_product_image))
                else -> firebaseEditProduct()
            }
        }
    }

    private fun firebaseEditProduct() {
        progress.show()
        val product = Product(
            title = binding.edtName.text.toString(),
            price = binding.edtPrice.text.toString(),
            description = binding.edtDescription.text.toString(),
            stockQuantity = binding.edtQty.text.toString().toInt(),
            category = binding.edtCategory.text.toString(),
            image = mSelectedProductImageFileUri!!.toString(),
            productId = Prefs.productId
        )
        FirestoreClass().updateProduct(product.title,product.price,product.description,product.stockQuantity,
            product.category,product.image, onSuccessListener = {
            progress.dismiss()
            showToast(getString(R.string.update_product_success))
            pushActivity(ProductActivity::class.java)
            finish()
        }, onFailureListener = {
            progress.dismiss()
            showToast(getString(R.string.update_product_failed, it.message.toString()))
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_PRODUCT = "extra_product"
    }
}