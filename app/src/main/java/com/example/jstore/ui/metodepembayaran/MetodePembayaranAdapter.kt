package com.example.jstore.ui.metodepembayaran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.*
import com.example.jstore.models.*
import com.example.jstore.utils.formatPrice

class MetodePembayaranAdapter(private val onClickListener: (metodePembayaran: MetodePembayaran) -> Unit,
) : ListAdapter<MetodePembayaran, MetodePembayaranAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMetodePembayaranBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metodePembayaran = getItem(position)
        return holder.bind(metodePembayaran)
    }

    inner class ViewHolder(private val itemBinding: ItemMetodePembayaranBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(metodePembayaran: MetodePembayaran) {
            with(itemBinding) {
                tvNamaPembayaran.text = metodePembayaran.namaMetode
                tvJenisPembayaran.text = metodePembayaran.jenisMetode
//                Glide.with(root.context)
//                    .load(rekening.image)
//                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
//                    .into(imgProduct)

                root.setOnClickListener { onClickListener(metodePembayaran) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MetodePembayaran>() {
            override fun areItemsTheSame(oldItem: MetodePembayaran, newItem: MetodePembayaran): Boolean {
                return oldItem.metodeId == newItem.metodeId
            }

            override fun areContentsTheSame(oldItem: MetodePembayaran, newItem: MetodePembayaran): Boolean {
                return oldItem == newItem
            }
        }
    }
}