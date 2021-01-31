package com.sehatq.testapplication.feature.entrance

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.platform.BaseActivity
import com.sehatq.testapplication.databinding.ActivitySplashBinding
import com.sehatq.testapplication.feature.aut.view.LoginActivity
import io.reactivex.disposables.CompositeDisposable

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(R.layout.activity_splash)
        binding.apply {
            descTextView.text = getString(R.string.app_name)
        }
        val handler = Handler()
        handler.postDelayed( Runnable {
            val intent = Intent(this,
                LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000L)
    }
}