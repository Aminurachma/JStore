package com.example.jstore.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityProductDetailsBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Product
import com.example.jstore.ui.cart.MyCartActivity
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.logDebug
import com.example.jstore.utils.pushActivity
import com.example.jstore.utils.showToast

class ProductDetailsActivity : BaseActivity() {

    private var _binding: ActivityProductDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var productId: String
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupClickListeners()

    }

    private fun setupUI() {
        productId = intent.getStringExtra(EXTRA_PRODUCT_ID) ?: ""
        showDetailProduct(productId)
    }

    private fun showDetailProduct(productId: String) {
        progress.show()
        FirestoreClass().subscribeDetailProduct(productId, onSuccessListener = {
            progress.dismiss()
            product = it
            Glide.with(this)
                .load(product.image)
                .placeholder(R.drawable.product_placeholder)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(binding.imgProduct)
            binding.tvProductName.text = product.title
            binding.tvProductPrice.text = product.price.toInt().formatPrice()
            binding.tvStock.text = product.stockQuantity.toString()
            binding.tvCategory.text = product.category
            binding.tvDetailProduct.text = product.description
        }, onFailureListener = {
            progress.dismiss()
            showToast(it)
        })
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnCart.setOnClickListener {
            pushActivity(MyCartActivity::class.java)
        }

        binding.btnAddToCart.setOnClickListener {
            if (binding.tvQuantity.text.toString().toInt() <= product.stockQuantity) {
                FirestoreClass().addProductToCart(
                    product = Product(
                        title = product.title,
                        price = product.price,
                        description = product.description,
                        stockQuantity = product.stockQuantity,
                        category = product.category,
                        image = product.image,
                        productId = product.productId,
                        quantity = binding.tvQuantity.text.toString().toInt()
                    ),
                    onSuccessListener = {
                        showToast(getString(R.string.added_to_cart, product.title))
                    },
                    onFailureListener = {
                        showToast(it.message.toString())
                    }
                )
            } else {
                showToast(getString(R.string.not_enough_stock))
            }
        }

        binding.btnDecrement.setOnClickListener {
            if (binding.tvQuantity.text.toString().toInt() > 1) {
                binding.tvQuantity.setText((binding.tvQuantity.text.toString().toInt() - 1).toString())
            }
        }

        binding.btnIncrement.setOnClickListener {
            if (binding.tvQuantity.text.toString().toInt() < product.stockQuantity) {
                binding.tvQuantity.setText((binding.tvQuantity.text.toString().toInt() + 1).toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }

}