package com.example.jstore.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.data.source.remote.response.Product
import com.example.jstore.databinding.ItemProductBinding

class ProductAdapter(
    private val onClickListener: () -> Unit,
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
                tvProductName.text = product.name
                tvProductPrice.text = product.price
                Glide.with(root.context)
                    .load(product.image)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgProduct)

                root.setOnClickListener { onClickListener() }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}