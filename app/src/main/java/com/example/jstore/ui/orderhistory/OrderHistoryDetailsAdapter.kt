package com.example.jstore.ui.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ItemOrderHistoryBinding
import com.example.jstore.databinding.ItemOrderHistoryDetailsBinding
import com.example.jstore.models.Order
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice

class OrderHistoryDetailsAdapter(
    private val onClickListener: (product: Product) -> Unit,
) : ListAdapter<Product, OrderHistoryDetailsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderHistoryDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        return holder.bind(product)
    }

    inner class ViewHolder(private val itemBinding: ItemOrderHistoryDetailsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: Product) {
            with(itemBinding) {
                tvProductName.text = product.title
                tvProductPrice.text = product.price.toInt().formatPrice()
                tvQuantity.text = "${product.quantity} pcs"
                Glide.with(root.context)
                    .load(product.image)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgProduct)
                root.setOnClickListener { onClickListener(product) }
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