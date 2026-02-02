package com.example.sdk;

import com.example.sdk.ICarCallback;
import com.example.sdk.User;

interface CarInterface {
   // oneway异步执行，不阻塞 UI，只能用于in和没有返回值得情况
   oneway void setName(String name);

   // 接收方填充数据给调用方
   void updateUser(out User user);

   // 接收方操作回调给调用方
   void setCallback(in ICarCallback callback);

}