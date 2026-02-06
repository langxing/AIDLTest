package com.example.carapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.intent.ShoppingCartIntent
import com.example.carapp.model.Goods
import com.example.carapp.repository.ShoppingCartRepository
import com.example.carapp.state.ShoppingCartState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingCartViewModel : ViewModel() {
    private val repository = ShoppingCartRepository()
    private val _state = MutableStateFlow(ShoppingCartState())
    val state = _state.asStateFlow()

    // 执行意图
    fun todoIntent(intent: ShoppingCartIntent) {
        when (intent) {
            is ShoppingCartIntent.loadData -> loadData()
            is ShoppingCartIntent.addGoods -> addGoods(intent)
            is ShoppingCartIntent.deleteGoods -> deleteGoods(intent)
            is ShoppingCartIntent.updateQuantity -> updateQuantity(intent)
        }
    }

    private fun updateQuantity(intent: ShoppingCartIntent.updateQuantity) {
        _state.update {
            val selectList = it.selectGoods
            val selectGoods = selectList.map { goods ->
                if (goods.id == intent.goodsId) {
                    goods.copy(quantity = goods.quantity + intent.quantity)
                } else goods
            }.filter { goods ->
                goods.quantity > 0
            }
            val totalPrice = updateTotalPrice(selectGoods)
            val goodsList = it.dataList.map {  goods ->
                if (goods.id == intent.goodsId) {
                    goods.copy(quantity = goods.quantity + intent.quantity)
                } else goods
            }
            val dataList = updateStock(goodsList, intent.goodsId, - intent.quantity)
            it.copy(totalPrice = totalPrice, selectGoods = selectGoods, dataList = dataList)
        }
    }

    private fun updateStock(dataList: List<Goods>, goodsId: Int, stockChange: Int): List<Goods> {
        // 模拟刷新库存
        val goodsList = dataList.map { item ->
            if (item.id == goodsId) {
                // coerceAtLeast(0) 限制最小值为0
                item.copy(stock = (item.stock + stockChange).coerceAtLeast(0))
            } else item
        }
        return goodsList
    }

    private fun updateTotalPrice(selectList: List<Goods>): Double {
        var totalPrice = 0.0
        selectList.forEach { goods ->
            totalPrice += goods.price * goods.quantity
        }
        return totalPrice
    }

    private fun deleteGoods(intent: ShoppingCartIntent.deleteGoods) {
        _state.update {
            val goods = intent.goods
            val selectList = it.selectGoods.filterNot { item ->
                item.id == goods.id
            }
            val totalPrice = updateTotalPrice(selectList)
            val goodsList = it.dataList.map { item ->
                if (item.id == goods.id) {
                    item.copy(quantity = item.quantity - goods.quantity)
                } else item
            }
            val dataList = updateStock(goodsList, intent.goods.id, + goods.quantity)
            it.copy(totalPrice = totalPrice, selectGoods = selectList, dataList = dataList)
        }
    }

    private fun addGoods(intent: ShoppingCartIntent.addGoods) {
        _state.update {
            val list = it.selectGoods.toMutableList()
            val goods = intent.goods.copy(quantity = 1)
            list.add(goods)
            val goodsList = it.dataList.map { item ->
                if (item.id == intent.goods.id) {
                    item.copy(quantity = 1)
                } else item
            }
            val dataList = updateStock(goodsList, intent.goods.id, - 1)
            val totalPrice = updateTotalPrice(list)
            it.copy(totalPrice = totalPrice, selectGoods = list, dataList = dataList)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val dataList = withContext(Dispatchers.IO) {
                repository.loadData()
            }
            _state.update {
                it.copy(isLoading = false, dataList = dataList)
            }
        }
    }

}