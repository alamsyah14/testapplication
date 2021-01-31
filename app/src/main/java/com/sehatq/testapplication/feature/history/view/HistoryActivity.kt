package com.sehatq.testapplication.feature.history.view

import android.content.Intent
import android.os.Bundle
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
import com.sehatq.testapplication.databinding.FragmentPurchaseBinding
import com.sehatq.testapplication.feature.history.viewmodel.HistoryViewModel
import com.sehatq.testapplication.feature.product.view.ProductDetailActivity
import com.sehatq.testapplication.feature.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity: BaseActivity<FragmentPurchaseBinding>() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(HistoryViewModel::class.java) }

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
        bindView(R.layout.fragment_purchase)
        binding.titleTextView.text = getString(R.string.title_history)
        binding.backButton.setOnClickListener { finish() }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = searchAdapter
        }

        viewModel.initialLoadData()
    }

    private fun subscribeUI() {
        viewModel.getLoading().observe(this, Observer { isLoading ->
            if (isLoading) LoadingDialog.show(this@HistoryActivity) else LoadingDialog.dismiss()
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
        val intent = Intent(this@HistoryActivity, ProductDetailActivity::class.java)
        intent.putExtra(Constants.ARGUMENT_DATA, product)
        startActivity(intent)
    }
}