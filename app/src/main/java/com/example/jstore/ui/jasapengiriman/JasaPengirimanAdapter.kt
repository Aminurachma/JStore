package com.example.jstore.ui.jasapengiriman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ItemJasaPengirimanBinding
import com.example.jstore.databinding.ItemNomorRekeningBinding
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.JasaPengiriman
import com.example.jstore.models.Product
import com.example.jstore.models.Rekening
import com.example.jstore.utils.formatPrice

class JasaPengirimanAdapter(private val onClickListener: (jasaPengiriman: JasaPengiriman) -> Unit,
) : ListAdapter<JasaPengiriman, JasaPengirimanAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemJasaPengirimanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jasaPengiriman = getItem(position)
        return holder.bind(jasaPengiriman)
    }

    inner class ViewHolder(private val itemBinding: ItemJasaPengirimanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(jasaPengiriman: JasaPengiriman) {
            with(itemBinding) {
                tvJasaPengirimanNama.text = jasaPengiriman.namaJasa
                tvJasaPengirimanEstimasi.text = jasaPengiriman.estimasi
                tvJasaPengirimanHarga.text = jasaPengiriman.harga.toInt().formatPrice()
//                Glide.with(root.context)
//                    .load(rekening.image)
//                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
//                    .into(imgProduct)

                root.setOnClickListener { onClickListener(jasaPengiriman) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JasaPengiriman>() {
            override fun areItemsTheSame(oldItem: JasaPengiriman, newItem: JasaPengiriman): Boolean {
                return oldItem.jasaPengirimanId == newItem.jasaPengirimanId
            }

            override fun areContentsTheSame(oldItem: JasaPengiriman, newItem: JasaPengiriman): Boolean {
                return oldItem == newItem
            }
        }
    }
}