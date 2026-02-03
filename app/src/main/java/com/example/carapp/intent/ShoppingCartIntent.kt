package com.example.carapp.intent

import com.example.carapp.model.Goods

sealed class ShoppingCartIntent {

    data object loadData: ShoppingCartIntent()

    data class addGoods(val goods: Goods): ShoppingCartIntent()

    data class deleteGoods(val goods: Goods): ShoppingCartIntent()

    data class updateQuantity(val goodsId: Int, val quantity: Int): ShoppingCartIntent()

}