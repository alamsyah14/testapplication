package com.sehatq.testapplication.core.network.response

import android.os.Parcelable
import com.sehatq.testapplication.R
import com.sehatq.testapplication.core.extention.toBoolean
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data (
    val data: RespondHome
): Parcelable

@Parcelize
data class RespondHome (
    val category: List<Category>?,
    val productPromo: List<Product>?
): Parcelable

@Parcelize
data class Category (
    val id: Int?,
    val name: String?,
    val imageUrl:String?
): Parcelable

@Parcelize
data class Product (
    val id: Int?,
    val title: String,
    val description: String?,
    val imageUrl:String?,
    val price:String?,
    var loved:Int = 0
): Parcelable {

    val isLoved : Boolean get() {
        return loved.toBoolean()
    }

    fun getWishIcon() : Int {
        return when (loved) {
            1 -> R.drawable.ic_wish_list_on
            else -> R.drawable.ic_wish_list_off
        }
    }
}

@Parcelize
data class RespondProduct(val product: List<Product>) : Parcelable