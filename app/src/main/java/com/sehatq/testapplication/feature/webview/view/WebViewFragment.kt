package com.sehatq.testapplication.feature.webview.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.argument
import com.sehatq.testapplication.core.util.Constants
import com.sehatq.testapplication.databinding.FragmentWebviewBinding
import com.sehatq.testapplication.feature.home.view.BaseHomeFragment

class WebViewFragment: BaseHomeFragment() {

    companion object {
        fun newInstance(tittle:String,url:String): WebViewFragment {
            val fragment = WebViewFragment()
            fragment.arguments = Bundle().apply {
                putString(Constants.ARGUMENT_ACTION, tittle)
                putString(Constants.ARGUMENT_URL, url)
            }
            return fragment
        }
    }

    lateinit var binding: FragmentWebviewBinding
    private val action by argument<String>(Constants.ARGUMENT_ACTION)
    private val url by argument<String>(Constants.ARGUMENT_URL)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUI()
    }

    private fun initBinding() {
        binding?.title.text = action

        binding?.webView.clearCache(true)
        binding?.webView?.clearHistory()
        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.settings?.domStorageEnabled = true
        binding?.webView?.loadUrl(url)
        binding?.webView?.webViewClient = object : WebViewClient() {
            @SuppressLint("NewApi")
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return interceptContentUrl(view,request?.url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return interceptContentUrl(view, Uri.parse(url))
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding?.contentLoadingProgressBar?.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.contentLoadingProgressBar?.hide()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if(error?.errorCode == ERROR_HOST_LOOKUP || error?.errorCode == ERROR_BAD_URL
                    || error?.errorCode == ERROR_TIMEOUT || error?.errorCode == ERROR_CONNECT
                    || error?.errorCode == ERROR_FAILED_SSL_HANDSHAKE
                )
                {
                    displayErrorDefault(view)
                }
                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun subscribeUI() {}

    private fun interceptContentUrl(view: WebView?,contentUrl: Uri?): Boolean {
        view?.loadUrl(contentUrl.toString())
        return true
    }

    private fun displayErrorDefault(view: WebView?) {
        view?.loadUrl("file:///android_asset/raw_default_webpage.html")
    }

    @SuppressLint("NewApi")
    fun clearAllDataStorage() {
        WebStorage.getInstance().deleteAllData()
        binding?.webView?.clearCache(true)
        binding?.webView?.clearFormData()
        binding?.webView?.clearHistory()
        binding?.webView?.clearSslPreferences()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    override fun onDestroy() {
        clearAllDataStorage()
        super.onDestroy()
    }
}