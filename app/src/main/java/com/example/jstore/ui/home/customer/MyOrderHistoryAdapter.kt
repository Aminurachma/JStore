package com.example.jstore.ui.home.customer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.data.source.local.Prefs
import com.example.jstore.databinding.ItemMyorderHistoryBinding
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.Order
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice
import com.example.jstore.utils.toGone
import com.example.jstore.utils.toVisible

class MyOrderHistoryAdapter(private val onClickListener: (order: Order) -> Unit,
) : ListAdapter<Order, MyOrderHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyorderHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemMyorderHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            with(itemBinding) {
                tvOrderId.text = "Order#${order.orderId}"
                tvStatus.text = order.statusPesanan
                tvProductName.text = order.products.first().title
                tvProductPrice.text = order.products.first().price.toInt().formatPrice()
                tvQuantity.text = "${order.products.first().quantity} pcs"
                tvTotalProduct.text = root.context.getString(R.string.total_products_count, order.products.size)
                tvTotalPesanan.text = "Total Pesanan: ${order.subTotalAmount.toInt().formatPrice()}"
                btnAcceptPackage.isVisible = order.statusPesanan == "Dikirim"
                Glide.with(root.context)
                    .load(order.products.first().image)
                    .placeholder(R.drawable.product_placeholder)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgProduct)

                root.setOnClickListener { onClickListener(order) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem.orderId == newItem.orderId
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }
        }
    }
}