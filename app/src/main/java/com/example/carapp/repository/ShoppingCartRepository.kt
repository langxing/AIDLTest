package com.example.carapp.repository

import com.example.carapp.model.Goods
import kotlinx.coroutines.delay
import kotlin.random.Random

class ShoppingCartRepository {

    suspend fun loadData(): List<Goods> {
        val list = mutableListOf<Goods>()
        for (i in 1 .. 100) {
            list.add(Goods(i, Random.nextInt(10) + i, name = "商品$i", price = Random.nextDouble(100.0)))
            // 模拟延时
            delay(10)
        }
        return list
    }

}