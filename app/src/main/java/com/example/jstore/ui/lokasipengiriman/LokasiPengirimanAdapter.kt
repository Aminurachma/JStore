package com.example.jstore.ui.lokasipengiriman

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ItemJasaPengirimanBinding
import com.example.jstore.databinding.ItemLokasiPengirimanBinding
import com.example.jstore.databinding.ItemNomorRekeningBinding
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.JasaPengiriman
import com.example.jstore.models.LokasiPengiriman
import com.example.jstore.models.Product
import com.example.jstore.models.Rekening
import com.example.jstore.utils.formatPrice

class LokasiPengirimanAdapter(private val onClickListener: (lokasiPengiriman: LokasiPengiriman) -> Unit,
) : ListAdapter<LokasiPengiriman, LokasiPengirimanAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLokasiPengirimanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lokasiPengiriman = getItem(position)
        return holder.bind(lokasiPengiriman)
    }

    inner class ViewHolder(private val itemBinding: ItemLokasiPengirimanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(lokasiPengiriman: LokasiPengiriman) {
            with(itemBinding) {
                tvLokasiPengiriman.text = lokasiPengiriman.namaLokasi
                tvAlamatPengiriman.text = lokasiPengiriman.alamat
//                Glide.with(root.context)
//                    .load(rekening.image)
//                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
//                    .into(imgProduct)

                root.setOnClickListener { onClickListener(lokasiPengiriman) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LokasiPengiriman>() {
            override fun areItemsTheSame(oldItem: LokasiPengiriman, newItem: LokasiPengiriman): Boolean {
                return oldItem.lokasiId == newItem.lokasiId
            }

            override fun areContentsTheSame(oldItem: LokasiPengiriman, newItem: LokasiPengiriman): Boolean {
                return oldItem == newItem
            }
        }
    }
}