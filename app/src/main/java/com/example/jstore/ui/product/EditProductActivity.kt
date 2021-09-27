package com.example.jstore.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ActivityEditProductBinding
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice

class EditProductActivity : AppCompatActivity() {
    private var _binding: ActivityEditProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product

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
//        binding.nameLabel.setText(product.title)
        binding.edtName.setText(product.title)
        binding.edtCategory.setText(product.category)
        binding.edtQty.setText(product.stockQuantity.toString())
        binding.edtDescription.setText(product.description)
        binding.edtPrice.setText(product.price.toInt().formatPrice())
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_PRODUCT = "extra_product"
    }
}