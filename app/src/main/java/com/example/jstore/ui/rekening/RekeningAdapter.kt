package com.example.jstore.ui.rekening

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.example.jstore.databinding.ItemNomorRekeningBinding
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.Product
import com.example.jstore.models.Rekening
import com.example.jstore.utils.formatPrice

class RekeningAdapter(private val onClickListener: (rekening: Rekening) -> Unit,
) : ListAdapter<Rekening, RekeningAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNomorRekeningBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rekening = getItem(position)
        return holder.bind(rekening)
    }

    inner class ViewHolder(private val itemBinding: ItemNomorRekeningBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(rekening: Rekening) {
            with(itemBinding) {
                tvNamaRekening.text = rekening.namaBank
                tvAtasNama.text = rekening.atasNama
                tvNomorRekening.text = rekening.nomorRekening
//                Glide.with(root.context)
//                    .load(rekening.image)
//                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
//                    .into(imgProduct)

                root.setOnClickListener { onClickListener(rekening) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Rekening>() {
            override fun areItemsTheSame(oldItem: Rekening, newItem: Rekening): Boolean {
                return oldItem.rekeningId == newItem.rekeningId
            }

            override fun areContentsTheSame(oldItem: Rekening, newItem: Rekening): Boolean {
                return oldItem == newItem
            }
        }
    }
}