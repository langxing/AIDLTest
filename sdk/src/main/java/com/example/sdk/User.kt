package com.example.sdk

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String? = "",
    var age: Int = 0
) : Parcelable {

    // 帮助客户端读取服务端填充的数据
    fun readFromParcel(parcel: Parcel) {
        name = parcel.readString()
        age = parcel.readInt()
    }

}
