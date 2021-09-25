package com.example.jstore.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.databinding.ItemCartBinding
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.Cart
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.showToast

class CartAdapter(
    private val onIncrement: (product: Product) -> Unit,
    private val onDecrement: (product: Product) -> Unit,
    private val onRemoved: (product: Product) -> Unit
) : ListAdapter<Product, CartAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemCartBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: Product) {
            with(itemBinding) {
                tvProductName.text = product.title
                tvProductPrice.text = product.price.toInt().formatPrice()
                tvQuantity.text = product.quantity.toString()

                Glide.with(root.context)
                    .load(product.image)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgProduct)

                btnDecrement.setOnClickListener {
                    if (tvQuantity.text.toString().toInt() > 1) {
                        onDecrement(product)
                    } else {
                        onRemoved(product)
                    }
                }

                btnIncrement.setOnClickListener {
                    if (tvQuantity.text.toString().toInt() < product.stockQuantity) {
                        onIncrement(product)
                    } else {
                        root.context.showToast(root.context.getString(R.string.not_enough_stock))
                    }
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}