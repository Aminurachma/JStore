package com.example.jstore.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.base.BaseActivity
import com.example.jstore.databinding.ActivityMyCartBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Cart
import com.example.jstore.models.Product
import com.example.jstore.ui.checkout.CheckoutActivity
import com.example.jstore.utils.Constants.QUANTITY
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.logDebug
import com.example.jstore.utils.logError
import com.example.jstore.utils.showToast

class MyCartActivity : BaseActivity() {

    private var _binding: ActivityMyCartBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: CartAdapter

    private lateinit var cart: Cart
    private var totalPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupAdapter()
        setupUI()
        setupClickListeners()
        getMyCart()

    }

    private fun setupUI() {
        binding?.rvCart?.adapter = adapter
    }

    private fun setupClickListeners() {
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btnCheckout?.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("totalPrice", totalPrice)
            startActivity(intent)
        }
    }

    private fun setupAdapter() {
        adapter = CartAdapter(onIncrement = {
            updateMyCart(it.apply { quantity++ })
        }, onDecrement = {
            updateMyCart(it.apply { quantity-- })
        }, onRemoved = {
            FirestoreClass().removeProductFromCart(product = it, onSuccessListener = {
                showToast(getString(R.string.success_remove_from_cart, it.title))
            }, onFailureListener = { e ->
                showToast(e.message.toString())
            })
        })
    }

    private fun updateMyCart(product: Product) {
        progress.show()
        FirestoreClass().updateMyCart(cart.cartId, cart.products.toMutableList(), product,
            onSuccessListener = { progress.dismiss() }, onFailureListener = { progress.dismiss() })
    }

    private fun getMyCart() {
        FirestoreClass().subscribeToCart(onSuccessListener = {
            cart = it
            adapter.submitList(it.products)
            adapter.notifyItemRangeChanged(0, it.products.size)
            binding?.tvTotal?.text = calculateTotalPrice(it.products).formatPrice()
            totalPrice = calculateTotalPrice(it.products)
        }, onFailureListener = {
            showToast(it)
        })
    }

    private fun calculateTotalPrice(products: List<Product>): Int {
        var price = 0
        products.forEach {
            price += it.price.toInt() * it.quantity
        }
        return price
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}