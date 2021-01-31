package com.sehatq.testapplication.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.testapplication.core.extention.loadImageUrl
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseAdapter
import com.sehatq.testapplication.databinding.ListViewProductBinding
import kotlinx.android.synthetic.main.list_view_product.view.*

class ProductAdapter (val onClick: (Product) -> Unit) : BaseAdapter<ProductAdapter.ViewHolder, Product>() {

    override fun loadData(models: List<Product>){
        this.models = models.reversed()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListViewProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = models[position]
        holder.apply {
            bind(product)
            itemView.productImage.setOnClickListener { onClick(product) }
            itemView.wishImageView.setOnClickListener {
                product.loved = if (product.isLoved)  0 else 1
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(
        private val binding: ListViewProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                model = item
                item.imageUrl?.let {
                    productImage.loadImageUrl(context = itemView.context, imageUrlPath = it)
                }
                wishImageView.setBackgroundResource(item.getWishIcon())
                executePendingBindings()
            }
        }
    }



}