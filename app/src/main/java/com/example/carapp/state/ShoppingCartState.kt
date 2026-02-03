package com.example.carapp.state

import com.example.carapp.model.Goods

data class ShoppingCartState(
    val totalPrice: Double = 0.0,
    val isLoading: Boolean = false,
    val dataList: List<Goods> = emptyList(),
    val selectGoods: List<Goods> = emptyList()
)
