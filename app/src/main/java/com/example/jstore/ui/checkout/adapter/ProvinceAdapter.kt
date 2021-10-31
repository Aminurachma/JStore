package com.example.jstore.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jstore.data.source.remote.response.GetProvinceResponse
import com.example.jstore.databinding.ItemProvinceBinding

class ProvinceAdapter(private val onClickListener: (GetProvinceResponse.RajaOngkir.Result) -> Unit) : ListAdapter<GetProvinceResponse.RajaOngkir.Result, ProvinceAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProvinceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemProvinceBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(province: GetProvinceResponse.RajaOngkir.Result) {
            with(itemBinding) {
                tvProvinceName.text = province.province
                root.setOnClickListener { onClickListener(province) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetProvinceResponse.RajaOngkir.Result>() {
            override fun areItemsTheSame(oldItem: GetProvinceResponse.RajaOngkir.Result, newItem: GetProvinceResponse.RajaOngkir.Result): Boolean {
                return oldItem.provinceId == newItem.provinceId
            }

            override fun areContentsTheSame(oldItem: GetProvinceResponse.RajaOngkir.Result, newItem: GetProvinceResponse.RajaOngkir.Result): Boolean {
                return oldItem == newItem
            }
        }
    }
}