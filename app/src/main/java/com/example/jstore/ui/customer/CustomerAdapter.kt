package com.example.jstore.ui.customer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.R
import com.example.jstore.databinding.ItemCustomerBinding
import com.example.jstore.models.User

class CustomerAdapter(customer: User) : ListAdapter<User, CustomerAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCustomerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = getItem(position)
        return holder.bind(customer)
    }

    inner class ViewHolder(private val itemBinding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(customer: User) {
            with(itemBinding) {
                tvCustomerName.text = customer.firstName +" "+ customer.lastName
                tvCustomerAddress.text = customer.address
                Glide.with(root.context)
                    .load(customer.image)
                    .placeholder(R.drawable.user_pisc)
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(imgCustomerAvatar)

//                root.setOnClickListener { onClickListener(customer) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}