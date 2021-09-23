package com.example.jstore.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jstore.R
import com.example.jstore.databinding.ActivityMyCartBinding
import com.example.jstore.firestore.FirestoreClass
import com.example.jstore.models.Cart
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.logDebug
import com.example.jstore.utils.logError
import com.example.jstore.utils.showToast

class MyCartActivity : AppCompatActivity() {

    private var _binding: ActivityMyCartBinding? = null
    private val  binding get() = _binding!!
    private lateinit var adapter: CartAdapter

    private lateinit var cart: Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupUI()
        getMyCart()

    }

    private fun setupUI() {
        binding.rvCart.adapter = adapter
    }

    private fun setupAdapter() {
        adapter = CartAdapter(onAddQuantity = {
            updateTotalPrice()
        }, onSubstractQuantity = {
            updateTotalPrice()
        })
    }

    private fun getMyCart() {
        FirestoreClass().subscribeToCart(onSuccessListener = {
            cart = it
            adapter.submitList(it.products)
            updateTotalPrice()
        }, onFailureListener = {
            showToast(it)
        })
    }

    private fun updateTotalPrice() {
        binding.tvTotal.text = calculateTotalPrice().formatPrice()
    }

    private fun calculateTotalPrice(): Int {
        var price = 0
        adapter.currentList.forEach {
            price += (it.price.toInt() * it.quantity)
        }
        return price
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}