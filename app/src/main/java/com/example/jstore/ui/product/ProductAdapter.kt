package com.example.jstore.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jstore.R
import com.example.jstore.databinding.ItemProductBinding
import com.example.jstore.models.Product
import com.example.jstore.utils.GlideLoader

class ProductAdapter(
    private val context: Context,
    private val list : ArrayList<Product>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var onClickListener : OnClickListener? = null

    class ViewHolder(val binding : ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false)
        return ViewHolder(ItemProductBinding.bind(view))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image,holder.binding.imgProduct)
        holder.binding.tvProductName.text = model.title
        holder.binding.tvProductPrice.text = "Rs. ${model.price}"

        holder.itemView.setOnClickListener{
            if (onClickListener != null){
                onClickListener!!.onClick(position,model)
            }
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClickListener{
        fun onClick(position: Int,product: Product)

    }
//    private val onClickListener: () -> Unit,
//) : ListAdapter<Product, ProductAdapter.ViewHolder>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            ItemProductBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val product = getItem(position)
//        return holder.bind(product)
//    }
//
//    inner class ViewHolder(private val itemBinding: ItemProductBinding) :
//        RecyclerView.ViewHolder(itemBinding.root) {
//        fun bind(product: Product) {
//            with(itemBinding) {
//                tvProductName.text = product.name
//                tvProductPrice.text = product.price
//                Glide.with(root.context)
//                    .load(product.image)
//                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
//                    .into(imgProduct)
//
//                root.setOnClickListener { onClickListener() }
//            }
//        }
//    }
//
//    companion object {
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
//            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }


}