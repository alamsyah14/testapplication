package com.sehatq.testapplication.feature.search.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.gone
import com.sehatq.testapplication.core.extention.visible
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseActivity
import com.sehatq.testapplication.core.util.Constants
import com.sehatq.testapplication.core.widget.LoadingDialog
import com.sehatq.testapplication.databinding.ActivityHistoryBinding
import com.sehatq.testapplication.feature.product.view.ProductDetailActivity
import com.sehatq.testapplication.feature.search.adapter.SearchAdapter
import com.sehatq.testapplication.feature.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_history.*

class SearchActivity : BaseActivity<ActivityHistoryBinding>() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(SearchViewModel::class.java) }

    private val searchAdapter by lazy {
        SearchAdapter { product ->
            goToDetailProductPages(product)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        bindView(R.layout.activity_history)
        binding.apply {
            backButton.setOnClickListener { finish() }
            titleTextView.text = getString(R.string.hint_search)
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.reLoadProduct()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchTextField.set(newText)
                    return false
                }
            })

            findViewById<ImageView>(R.id.search_close_btn).setOnClickListener {
                viewModel.searchTextField.set("")
                viewModel.reLoadProduct()
                binding.searchView.apply {
                    setQuery("", false)
                    isIconified = true
                }
            }
        }

        handleEmptyState(true)
    }

    private fun subscribeUI() {
        viewModel.getLoading().observe(this, Observer { isLoading ->
            if (isLoading) LoadingDialog.show(this@SearchActivity) else LoadingDialog.dismiss()
        })

        viewModel.product.observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                handleEmptyState(false)
                searchAdapter.loadData(list)
            }
            else handleEmptyState(true)
        })

    }

    private fun handleEmptyState(isEmpty: Boolean) {
        emptyStateLayout.apply {
            if (isEmpty) visible() else gone()
        }
        productRecyclerView.apply {
            if (isEmpty) gone() else visible()
        }
    }


    private fun goToDetailProductPages(product: Product) {
        val intent = Intent(this@SearchActivity, ProductDetailActivity::class.java)
        intent.putExtra(Constants.ARGUMENT_DATA, product)
        startActivity(intent)
    }
}