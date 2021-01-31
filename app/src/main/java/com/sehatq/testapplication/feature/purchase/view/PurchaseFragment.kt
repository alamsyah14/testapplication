package com.sehatq.testapplication.feature.purchase.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.gone
import com.sehatq.testapplication.core.extention.visible
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.util.Constants
import com.sehatq.testapplication.core.widget.LoadingDialog
import com.sehatq.testapplication.databinding.FragmentPurchaseBinding
import com.sehatq.testapplication.feature.home.view.BaseHomeFragment
import com.sehatq.testapplication.feature.product.view.ProductDetailActivity
import com.sehatq.testapplication.feature.purchase.viewmodel.PurchaseViewModel
import com.sehatq.testapplication.feature.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.activity_history.*

class PurchaseFragment: BaseHomeFragment() {

    companion object {
        fun newInstance(): PurchaseFragment {
            return PurchaseFragment()
        }
    }

    lateinit var binding: FragmentPurchaseBinding

    private val searchAdapter by lazy {
        SearchAdapter { product ->
            goToDetailProductPages(product)
        }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PurchaseViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase, container, false)
        initBinding()
        subscribeUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initialLoadData()
    }

    private fun initBinding() {
        binding.apply {
            backButton.setOnClickListener {
                //todo - must back to home
            }
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }

    }

    private fun subscribeUI() {

        viewModel.getLoading().observe(this, Observer { isLoading ->
            if (isLoading) LoadingDialog.show(activity) else LoadingDialog.dismiss()
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
        val intent = Intent(activity, ProductDetailActivity::class.java)
        intent.putExtra(Constants.ARGUMENT_DATA, product)
        activity.startActivity(intent)
    }
}