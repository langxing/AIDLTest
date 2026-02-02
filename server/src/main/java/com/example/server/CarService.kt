package com.example.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.sdk.CarInterface
import com.example.sdk.ICarCallback
import com.example.sdk.User

class CarService : Service() {

    private val binder = object : CarInterface.Stub() {

        override fun setName(name: String?) {
            Log.d("server", "客户端数据:$name")
        }

        override fun updateUser(user: User?) {
            user?.apply {
                name = "李四"
                age = 20
            }
        }

        override fun setCallback(callback: ICarCallback?) {
            callback?.onReceive(User(name = "张三", age = 18))
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

}