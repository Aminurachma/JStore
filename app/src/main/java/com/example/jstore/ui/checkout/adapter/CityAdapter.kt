package com.example.jstore.ui.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jstore.data.source.remote.response.GetCityResponse
import com.example.jstore.databinding.ItemCityBinding

class CityAdapter(private val onClickListener: (GetCityResponse.RajaOngkir.Result) -> Unit) : ListAdapter<GetCityResponse.RajaOngkir.Result, CityAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: ItemCityBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(city: GetCityResponse.RajaOngkir.Result) {
            with(itemBinding) {
                tvCityName.text = city.cityName
                root.setOnClickListener { onClickListener(city) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetCityResponse.RajaOngkir.Result>() {
            override fun areItemsTheSame(oldItem: GetCityResponse.RajaOngkir.Result, newItem: GetCityResponse.RajaOngkir.Result): Boolean {
                return oldItem.cityId == newItem.cityId
            }

            override fun areContentsTheSame(oldItem: GetCityResponse.RajaOngkir.Result, newItem: GetCityResponse.RajaOngkir.Result): Boolean {
                return oldItem == newItem
            }
        }
    }
}