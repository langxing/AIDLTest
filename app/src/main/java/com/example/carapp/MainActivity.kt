package com.example.carapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.carapp.databinding.ActivityMainBinding
import com.example.sdk.CarInterface
import com.example.sdk.ICarCallback
import com.example.sdk.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var server: CarInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        binding.tvTest.setOnClickListener {
            try {
                server?.setName("haha")
            } catch (e: RemoteException) {
                Log.e("AIDL_ERROR", "调用远程方法失败", e)
            }
        }
        binding.tvOut.setOnClickListener {
            try {
                val user = User()
                server?.updateUser(user)
                Log.d("AIDL_OUT", "服务端返回值:${user.name}--->${user.age}")
            } catch (e: RemoteException) {
                Log.e("AIDL_ERROR", "调用远程方法失败", e)
            }
        }
    }

    private fun initData() {
        // 初始化server
        val connection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                server = CarInterface.Stub.asInterface(binder)
                setCallback()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                server = null
            }

        }
        val intent = Intent()
        intent.component = ComponentName("com.example.server", "com.example.server.CarService");
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun setCallback() {
        server?.setCallback(object : ICarCallback.Stub() {

            override fun onReceive(user: User?) {
                Log.d("AIDL_Receive", "服务端返回值${user?.name}--->${user?.age}")
            }

        })
    }

}