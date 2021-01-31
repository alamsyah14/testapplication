package com.sehatq.testapplication.core.platform

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.finishAllPreviousActivity
import com.sehatq.testapplication.core.util.Cache
import com.sehatq.testapplication.core.widget.CustomDialog
import com.sehatq.testapplication.feature.entrance.SplashActivity

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: B
    private var toast: Toast? = null

    protected fun bindView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected fun bindViewFullscreen(layoutId: Int) {
        // set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        binding = DataBindingUtil.setContentView(this, layoutId)
    }

    protected fun hideKeyboard() {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    fun showToast(msg: String) {
        toast?.cancel()
        toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
        toast?.show()
    }

    fun showPopUpMessage(msg: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.dlg_toast_top_success, null)

        val text = layout.findViewById(R.id.toast_message) as TextView
        text.text = msg

        val toast = Toast(applicationContext)

        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    fun signoutDialog(){
        CustomDialog.showPopUpWithNegativeButton(this,
            image = R.mipmap.ic_launcher_round,
            title = getString(R.string.menu_sign_out),
            body = getString(R.string.title_sign_out),
            btnPositive = getString(R.string.label_ok),
            btnPositiveListener = View.OnClickListener {
                CustomDialog?.dismiss()
                removeForLogout()
            },
            btnNegative = getString(R.string.label_cancel),
            btnNegativeListener = View.OnClickListener {
                CustomDialog?.dismiss()
            }
        )
    }

    private fun removeForLogout() {
        Cache.removeForLogout().also {
            val i = Intent(this, SplashActivity::class.java)
            finishAllPreviousActivity(i)
        }
    }

}