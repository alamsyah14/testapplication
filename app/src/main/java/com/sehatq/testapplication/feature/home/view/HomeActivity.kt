package com.sehatq.testapplication.feature.home.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.platform.BaseActivity
import com.sehatq.testapplication.core.util.Constants
import com.sehatq.testapplication.core.widget.NonSwipeAbleViewPager
import com.sehatq.testapplication.databinding.ActivityHomeBinding
import com.sehatq.testapplication.feature.history.view.HistoryActivity
import com.sehatq.testapplication.feature.webview.view.WebViewFragment
import io.reactivex.disposables.CompositeDisposable

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    var mBackPressed: Long = 0
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit  var viewPager: NonSwipeAbleViewPager

    private val homeAdapter by lazy { MainPagerAdapter(supportFragmentManager) }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setPage(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_feed -> {
                setPage(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cart -> {
                setPage(2)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                openPagesPurchase()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_logout -> {
                signoutDialog()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun setCurrentPage(menu: Int){
        when (menu){
            0 -> binding.navigation.selectedItemId = R.id.navigation_home
            1 -> binding.navigation.selectedItemId = R.id.navigation_feed
            2 -> binding.navigation.selectedItemId = R.id.navigation_cart
            3 -> binding.navigation.selectedItemId = R.id.navigation_profile
        }
        setPage(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        subscribeUI()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
        else {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                if(viewPager.currentItem==0) {
                    Toast.makeText(this, "Tap again to close apps", Toast.LENGTH_LONG).show()
                }else{
                    setCurrentPage(0)
                }
            }
            mBackPressed = System.currentTimeMillis()
        }
    }

    private fun setPage(page: Int){
        viewPager.setCurrentItem(page, false)
    }

    inner class MainPagerAdapter(manager: FragmentManager): FragmentStatePagerAdapter(manager) {
        val homeFragment: HomeFragment = HomeFragment.newInstance()
        val feedFragment: WebViewFragment = WebViewFragment.newInstance(getString(R.string.menu_feed),"https://www.sehatq.com/artikel")
        val cartFragment: WebViewFragment = WebViewFragment.newInstance(getString(R.string.menu_cart),"https://toko.sehatq.com/")
        val profileFragment: WebViewFragment = WebViewFragment.newInstance(getString(R.string.menu_profile),"https://account.sehatq.com/login")

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when(position){
                0       -> homeFragment
                1       -> feedFragment
                else    -> cartFragment
                //else    -> profileFragment
            }
        }
    }

    private fun initBinding() {
        bindView(R.layout.activity_home)
        viewPager = binding.viewPager
        viewPager.apply {
            setPageTransformer(false) { view, position ->
                if (position <= -1.0F || position >= 1.0F) {
                    view.translationX = view.width * position
                    view.alpha = 0.0F
                } else if (position == 0.0F) {
                    view.translationX = view.width * position
                    view.alpha = 1.0F
                } else {
                    view.translationX = view.width * -position
                    view.alpha = 1.0F - Math.abs(position)
                }
            }
            adapter = homeAdapter
            currentItem = intent.getIntExtra("page", 0)
            offscreenPageLimit = 4
        }

        binding.navigation.apply {
            itemIconTintList = null
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

        setCurrentPage(intent.getIntExtra("page", 0))
    }

    private fun subscribeUI() {}

    override fun onDestroy() {
        super.onDestroy()

        if (!compositeDisposable.isDisposed){
            compositeDisposable.clear()
        }
    }

    private fun openPagesPurchase() {
        val intent = Intent(this@HomeActivity,HistoryActivity::class.java)
        startActivity(intent)
    }

}