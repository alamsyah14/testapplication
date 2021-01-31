package com.sehatq.testapplication.core.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Window
import com.sehatq.testapplication.R

object LoadingDialog {

    private val dm = DisplayMetrics()
    private var instance: Dialog? = null

    fun dismiss(){
        instance?.let {
            it.dismiss()
            instance = null
        }
    }

    fun show(ctx: Context){
        if(instance?.isShowing==true){return}

        instance = Dialog(ctx, R.style.Theme_LoadingDialog)
        instance?.apply {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.setContentView(R.layout.dlg_loading)
            (ctx as Activity).windowManager.defaultDisplay.getMetrics(dm)
            this.window?.setLayout(dm.widthPixels, dm.heightPixels)
            this.setCancelable(false)
            this.show()
        }
    }

}