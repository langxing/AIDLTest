package com.example.sdk;

import com.example.sdk.User;

interface ICarCallback {
   // in (默认)：数据从调用者（Caller）流向接收者（Callee）, 因为server是调用者
   void onReceive(in User user);

}