package com.sehatq.testapplication.feature.home.view

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
import com.sehatq.testapplication.core.network.response.Category
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.util.Constants
import com.sehatq.testapplication.core.widget.LoadingDialog
import com.sehatq.testapplication.databinding.FragmentHomeBinding
import com.sehatq.testapplication.feature.home.adapter.CategoryAdapter
import com.sehatq.testapplication.feature.home.adapter.ProductAdapter
import com.sehatq.testapplication.feature.home.viewmodel.HomeViewModel
import com.sehatq.testapplication.feature.product.view.ProductDetailActivity
import com.sehatq.testapplication.feature.search.view.SearchActivity

class HomeFragment : BaseHomeFragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var binding: FragmentHomeBinding
    private val adapterCategory by lazy {
        CategoryAdapter { category ->
            handleChangeDataByCategory(category)
        }
    }

    private val adapterProduct by lazy {
        ProductAdapter { product ->
            goToDetailProductPages(product)
        }
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
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
            searchView.clearFocus()
            searchCardView.setOnClickListener {
                goToSearchPages()
            }
        }

        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCategory
        }

        binding.productRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = adapterProduct
        }
    }

    private fun subscribeUI() {
        viewModel.getLoading().observe(this, Observer { isLoading ->
            if (isLoading) LoadingDialog.show(activity) else LoadingDialog.dismiss()
        })

        viewModel.listCategory.observe(this, Observer { list ->
            adapterCategory.loadData(list)
        })

        viewModel.listProduct.observe(this, Observer { list ->
            adapterProduct.loadData(list)
        })

        viewModel.errorResponse.observe(this, Observer {
            activity.showToast(it.message.toString())
        })
    }

    private fun handleChangeDataByCategory(category: Category) {}

    private fun goToDetailProductPages(product: Product) {
        val intent = Intent(activity, ProductDetailActivity::class.java)
        intent.putExtra(Constants.ARGUMENT_DATA, product)
        activity.startActivity(intent)
    }

    private fun goToSearchPages(){
        val intent = Intent(activity, SearchActivity::class.java)
        activity.startActivity(intent)
    }
}