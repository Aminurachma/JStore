package com.example.jstore.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.example.jstore.databinding.ItemCourierBinding
import com.example.jstore.databinding.ItemProvinceBinding
import com.example.jstore.models.Ongkir
import com.example.jstore.utils.formatPrice

class CourierAdapter(private val onClickListener: (Ongkir) -> Unit) : ListAdapter<Ongkir, CourierAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCourierBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemCourierBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(courier: Ongkir) {
            with(itemBinding) {
                tvCourierNameType.text = "${courier.name} - ${courier.service}"
                val estimation = if (courier.etd?.contains("hari", true) == true) {
                    "Estimasi: ${courier.etd.lowercase()}"
                } else {
                    "Estimasi: ${courier.etd} hari"
                }
                tvTimeEstimated.text = estimation
                tvCost.text = courier.cost.formatPrice()
                root.setOnClickListener { onClickListener(courier) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Ongkir>() {
            override fun areItemsTheSame(oldItem: Ongkir, newItem: Ongkir): Boolean {
                return oldItem.service == newItem.service
            }

            override fun areContentsTheSame(oldItem: Ongkir, newItem: Ongkir): Boolean {
                return oldItem == newItem
            }
        }
    }
}