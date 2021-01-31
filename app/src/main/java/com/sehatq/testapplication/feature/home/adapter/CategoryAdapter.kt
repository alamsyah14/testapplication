package com.sehatq.testapplication.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sehatq.testapplication.core.extention.loadImageUrl
import com.sehatq.testapplication.core.network.response.Category
import com.sehatq.testapplication.core.platform.BaseAdapter
import com.sehatq.testapplication.databinding.ListViewCategoryBinding

class CategoryAdapter(val onClick: (Category) -> Unit): BaseAdapter<CategoryAdapter.ViewHolder, Category>() {

    override fun loadData(models: List<Category>){
        this.models = models.reversed()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListViewCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = models[position]
        holder.apply {
            bind(category)
            itemView.setOnClickListener { onClick(category) }
        }
    }

    class ViewHolder(
        private val binding: ListViewCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.apply {
                model = item
                item.imageUrl?.let {
                    categoryImages.loadImageUrl(context = itemView.context, imageUrlPath = it)
                }
                executePendingBindings()
            }
        }
    }

}