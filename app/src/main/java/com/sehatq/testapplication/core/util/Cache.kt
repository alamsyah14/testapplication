package com.sehatq.testapplication.core.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sehatq.testapplication.core.network.response.Product
import com.sehatq.testapplication.core.network.response.RespondProduct
import com.sehatq.testapplication.core.network.service.HomeService
import org.json.JSONArray





object Cache {
    private const val NAME = "cache-sehatq"
    private const val KEY_PRODUCT_LIST  = "list-product"
    private const val KEY_PURCHASE_LIST = "list-purchase"

    private lateinit var sharePref: SharedPreferences
    private  val sharePrefEditor: SharedPreferences.Editor by lazy { sharePref.edit() }

    fun context(ctx: Context) {
        sharePref = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    private fun gson(): Gson {
        return Gson()
    }

    private fun toJson(obj: Any?): String {
        return gson().toJson(obj)
    }

    private fun <T> fromJson(json: String, klas: Class<T>): T? {
        return try {
            gson().fromJson(json, klas)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun removeInstance(){
        HomeService.removeInstance()
    }

    fun removeForLogout() {
        val editor = sharePrefEditor
        removeInstance()
        editor.apply {
            remove(KEY_PRODUCT_LIST)
            remove(KEY_PURCHASE_LIST)
        }
        editor.commit()
    }

    fun saveProductList(list: RespondProduct) {
        val editor = sharePrefEditor
        editor.putString(KEY_PRODUCT_LIST, toJson(list))
        editor.commit()
    }

    val getProductList: RespondProduct? get() {
        val json = sharePref.getString(KEY_PRODUCT_LIST, "")
        json?.let {
            return fromJson(it, RespondProduct::class.java)
        }?: return null
    }

    fun savePurchaseList(list: RespondProduct) {
        val editor = sharePrefEditor
        editor.putString(KEY_PURCHASE_LIST, toJson(list))
        editor.commit()
    }

    val getPurchaseList: RespondProduct? get() {
        val json = sharePref.getString(KEY_PURCHASE_LIST, "")
        json?.let {
            return fromJson(it, RespondProduct::class.java)
        }?: return null
    }


}