package com.sehatq.testapplication.core.extention

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.view.WindowManager

fun Context.startNoDuplicateActivity(i: Intent) {
    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(i)

}

fun Activity.startNoDuplicateActivityForResult(i: Intent, requestCode: Int) {
    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivityForResult(i, requestCode)
}

fun Context.finishAllPreviousActivity(i: Intent){
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(i)
}

fun Context.isInternetAvailable() : Boolean{

    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
}


fun Activity.setTransparentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#000000")
    }
}