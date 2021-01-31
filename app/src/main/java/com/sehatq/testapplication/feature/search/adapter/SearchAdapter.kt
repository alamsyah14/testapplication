package com.sehatq.testapplication.feature.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.testapplication.core.extention.loadImageUrl
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseAdapter
import com.sehatq.testapplication.databinding.ListViewSearchBinding

class SearchAdapter(val onClick: (Product) -> Unit) : BaseAdapter<SearchAdapter.ViewHolder, Product>() {

    override fun loadData(models: List<Product>){
        this.models = models.reversed()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListViewSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = models[position]
        holder.apply {
            bind(product)
            itemView.setOnClickListener { onClick(product) }
        }
    }

    class ViewHolder(
        private val binding: ListViewSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
                model = item

                item.imageUrl?.let {
                    productImage.loadImageUrl(context = itemView.context, imageUrlPath = it)
                }

                executePendingBindings()
            }
        }
    }
}