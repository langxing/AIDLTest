package com.example.carapp.intent

import com.example.carapp.model.Goods

sealed class ShoppingCartIntent {

    data object LoadData: ShoppingCartIntent()

    data class AddGoods(val goods: Goods): ShoppingCartIntent()

    data class DeleteGoods(val goods: Goods): ShoppingCartIntent()

    data class UpdateQuantity(val goodsId: Int, val quantity: Int): ShoppingCartIntent()

}