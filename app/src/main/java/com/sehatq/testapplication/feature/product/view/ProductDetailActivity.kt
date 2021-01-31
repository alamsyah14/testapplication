package com.sehatq.testapplication.feature.product.view

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.loadImageUrl
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.platform.BaseActivity
import com.sehatq.testapplication.core.util.Constants
import com.sehatq.testapplication.core.widget.LoadingDialog
import com.sehatq.testapplication.databinding.ActivityProductDetailBinding
import com.sehatq.testapplication.feature.product.viewmodel.ProductDetailViewModel

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {

    private var data: Product? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ProductDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initSubscriber()
    }

    private fun initBinding() {
        data = intent.getParcelableExtra(Constants.ARGUMENT_DATA) ?: null
        bindView(R.layout.activity_product_detail)
        binding.apply {
            data?.imageUrl?.let {
                productImage.loadImageUrl(this@ProductDetailActivity,it)
            }
            wishImageView.setBackgroundResource(data?.getWishIcon() ?: R.drawable.ic_wish_list_off)
            productName.text = data?.title ?: ""
            productDescription.text = data?.description ?: ""
            productPrice.text = data?.price ?: ""
            buyButton.setOnClickListener {
                hadnleAddProductToListPurchase()
            }
            backImagesView.setOnClickListener { finish() }
            shareImagesView.setOnClickListener { handleShare() }
        }
    }

    private fun handleShare() {
        val product = data
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, product?.description)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun hadnleAddProductToListPurchase() {
        data?.let {
            viewModel.addPurchaseProduct(it)
        }
    }

    private fun initSubscriber() {
        viewModel.getLoading().observe(this, Observer { isLoading ->
            if (isLoading) LoadingDialog.show(this) else LoadingDialog.dismiss()
        })

        viewModel.successAdd.observe(this, Observer {
            showToast("Success, add item to cart")
        })
    }

}