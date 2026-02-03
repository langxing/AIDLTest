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
            else -> {}
        }
    }

    private fun updateStock(goods: Goods, stockChange: Int): List<Goods> {
        // 模拟刷新库存
        val dataList = _state.value.dataList.map { item ->
            if (item.id == goods.id) {
                // coerceAtLeast(0) 限制最小值为0
                item.copy(quantity = (item.quantity + stockChange).coerceAtLeast(0))
            } else item
        }
        return dataList
    }

    private fun deleteGoods(intent: ShoppingCartIntent.deleteGoods) {
        _state.update {
            val goods = intent.goods
            val selectList = it.selectGoods.filterNot { item ->
                item.id == goods.id
            }
            val dataList = updateStock(intent.goods, + goods.quantity)
            it.copy(selectGoods = selectList, dataList = dataList)
        }
    }

    private fun addGoods(intent: ShoppingCartIntent.addGoods) {
        _state.update {
            val goods = intent.goods
            val list = it.selectGoods.toMutableList()
            list.add(goods)
            val dataList = updateStock(intent.goods, - goods.quantity)
            it.copy(selectGoods = list, dataList = dataList)
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