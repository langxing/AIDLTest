package com.example.carapp.model

data class Goods(
    val id: Int = 0,
    // 库存
    val stock: Int = 1,
    // 数量
    val quantity: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
)
