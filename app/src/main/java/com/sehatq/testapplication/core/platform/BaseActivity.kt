package com.sehatq.testapplication.core.platform

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

}