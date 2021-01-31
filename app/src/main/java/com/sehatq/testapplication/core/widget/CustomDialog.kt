package com.sehatq.testapplication.core.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.sehatq.testapplication.R

data class DialogPopUp(
    val image: Int,
    val title: String,
    val body: String = "",
    val button: String = "BACK"
)

object CustomDialog {

    private val dm = DisplayMetrics()
    private var dlg: Dialog? = null

    fun dismiss() {
        dlg?.dismiss()
        dlg = null
    }

    private fun createDialog(ctx: Context, layoutID: Int, isCancelAble: Boolean = false){
        dlg = Dialog(ctx, R.style.DialogAnimation)

        dlg?.apply {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            (ctx as Activity).windowManager.defaultDisplay.getMetrics(dm)
            window?.setLayout(dm.widthPixels, dm.heightPixels)
            window?.setGravity(Gravity.TOP)
            setContentView(layoutID)
            setCancelable(isCancelAble)
            setCanceledOnTouchOutside(isCancelAble)
        }
    }

    private fun createDialog(ctx: Context, view: View, isCancelAble: Boolean = false){
        dlg = Dialog(ctx, R.style.DialogAnimation)

        dlg?.apply {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.windowAnimations = R.style.DialogAnimation
            (ctx as Activity).windowManager.defaultDisplay.getMetrics(dm)
            window?.setLayout(dm.widthPixels, dm.heightPixels)
            window?.setGravity(Gravity.TOP)
            setContentView(view)
            setCancelable(isCancelAble)
            setCanceledOnTouchOutside(isCancelAble)
        }
    }

    fun showPopUpWithNegativeButton(ctx: Context, image: Int, title: String, body: String = "",info: String = "",
                                    btnPositive: String = "Ok",
                                    btnPositiveListener: View.OnClickListener = View.OnClickListener { dlg?.dismiss() },
                                    btnNegative: String = "Cancelled",
                                    btnNegativeListener: View.OnClickListener = View.OnClickListener { dlg?.dismiss() },
                                    cancelAble: Boolean = true
    ){
        if (dlg?.isShowing == true){
            return
        }

        createDialog(ctx, R.layout.dlg_pop_up_negative_button, cancelAble)

        dlg?.apply {
            findViewById<ImageView>(R.id.dlgIcon).setImageResource(image)
            findViewById<TextView>(R.id.dlgTitle).text = title
            findViewById<TextView>(R.id.dlgSubTitle).apply {
                if (body.isEmpty()) visibility = View.GONE
                else {
                    visibility = View.VISIBLE
                    text = body
                }
            }
            findViewById<TextView>(R.id.dlgBtn).text = btnPositive
            findViewById<AppCompatButton>(R.id.dlgBtn).setOnClickListener(btnPositiveListener)
            findViewById<TextView>(R.id.dlgNegativeBtn).text = btnNegative
            findViewById<AppCompatButton>(R.id.dlgNegativeBtn).setOnClickListener(btnNegativeListener)
        }
        dlg?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dlg?.show()
    }
}