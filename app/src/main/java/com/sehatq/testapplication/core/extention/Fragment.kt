package com.sehatq.testapplication.core.extention

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

fun Fragment.withArguments(vararg arguments: Pair<String, Any?>): Fragment {
    val bundle = Bundle()
    arguments.forEach {
        when(it.second){
            is Serializable ->bundle.putSerializable(it.first, it.second as? Serializable)
            is Parcelable -> bundle.putParcelable(it.first, it.second as? Parcelable)
        }
    }
    this.arguments = bundle
    return this
}

fun <T : Any?> FragmentActivity.argument(key: String, defaultValue: T? = null) = lazy {
    check(intent.extras != null){
        return@lazy defaultValue
    }
    check(intent.extras?.containsKey(key) == true){
        return@lazy defaultValue
    }
    intent?.extras?.get(key) as? T ?: defaultValue
}

fun <T : Any?> FragmentActivity.argument(key: String) = lazy {
    checkNotNull(intent.extras){
        return@lazy null
    }
    check(intent.extras?.containsKey(key) == true){
        return@lazy null
    }
    intent?.extras?.get(key) as? T
}

fun <T : Any?> Fragment.argument(key: String, defaultValue: T? = null) = lazy { arguments?.get(key)  as? T ?: defaultValue }

fun <T : Any?> Fragment.argument(key: String) = lazy { arguments?.get(key) as? T }