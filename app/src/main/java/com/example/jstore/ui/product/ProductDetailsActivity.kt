package com.example.jstore.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ActivityProductDetailsBinding
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice

class ProductDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityProductDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailsBinding.inflate(layoutInflater)
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
        binding.tvProductName.text = product.title
        binding.tvProductPrice.text = product.price.toInt().formatPrice()
        binding.tvStock.text = product.stockQuantity.toString()
        binding.tvCategory.text = product.category
        binding.tvDetailProduct.text = product.description
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnDecrement.setOnClickListener {
            if (binding.tvQuantity.text.toString().toInt() > 1) {
                binding.tvQuantity.text = (binding.tvQuantity.text.toString().toInt() - 1).toString()
            }
        }

        binding.btnIncrement.setOnClickListener {
            binding.tvQuantity.text = (binding.tvQuantity.text.toString().toInt() + 1).toString()
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