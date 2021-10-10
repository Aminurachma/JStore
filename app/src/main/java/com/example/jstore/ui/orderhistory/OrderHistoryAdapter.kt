package com.example.jstore.ui.orderhistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ItemOrderHistoryBinding
import com.example.jstore.models.Order
import com.example.jstore.models.Product
import com.example.jstore.utils.formatPrice

class OrderHistoryAdapter(private val onClickListener: (order:Order) -> Unit,
) : ListAdapter<Order, OrderHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        return holder.bind(order)
    }

    inner class ViewHolder(private val itemBinding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(order: Order) {
            with(itemBinding) {
                tvCustomerName.text = order.firstName
                tvStatus.text = order.statusPembayaran
                order.products.forEach{ product ->
                    tvProductName.text = product.title
                    tvProductPrice.text = product.price.toInt().formatPrice()
                    tvQuantity.text = product.quantity.toString()+ " pcs"
                    Glide.with(root.context)
                        .load(product.image)
                        .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                        .into(imgProduct)

                }.toString()
                tvTotalProduct.text = order.products.count().toString()+ " produk"
                tvOrderId.text = order.orderId
                tvTotalPayment.text= "Total Pembayaran "+ order.totalAmount.toInt().formatPrice()

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