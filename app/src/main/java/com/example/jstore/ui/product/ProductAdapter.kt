package com.example.jstore.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class ProductAdapter( private val onClickListener: (product: Product) -> Unit,
                      private val activity : ProductActivity,
) : ListAdapter<Product, ProductAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductBinding.inflate(
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

    inner class ViewHolder(private val itemBinding: ItemProductBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: Product) {
            with(itemBinding) {
                tvProductName.text = product.title
                tvProductPrice.text = product.price.toInt().formatPrice()
                Glide.with(root.context)
                    .load(product.image)
                    .placeholder(R.drawable.product_placeholder)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgProduct)
                if (Prefs.adminId.isNotEmpty()) {
                    ibDeleteProduct.toVisible()
                    ibDeleteProduct.setOnClickListener{
                        activity.deleteProduct(product.productId)
                    }
                } else
                {
                    ibDeleteProduct.toGone()
                }

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