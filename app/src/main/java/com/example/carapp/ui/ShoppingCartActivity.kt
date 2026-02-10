package com.example.carapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carapp.adapter.ShoppingCartAdapter
import com.example.carapp.databinding.ActivityShoppingCartBinding
import com.example.carapp.intent.ShoppingCartIntent
import com.example.carapp.viewmodel.ShoppingCartViewModel
import kotlinx.coroutines.launch

class ShoppingCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingCartBinding
    private val viewModel by viewModels<ShoppingCartViewModel>()
    private val adapter by lazy {
        ShoppingCartAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        viewModel.todoIntent(ShoppingCartIntent.LoadData)
        lifecycleScope.launch {
            // 只有在 Lifecycle 处于 STARTED 状态（用户可见）时才收集数据
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.isLoading) {
                        // todo showLoading
                    } else {
                        // todo hideLoading
                    }
                    binding.tvTotalNum.text = "共${state.selectGoods.size}件商品"
                    binding.tvTotalPrice.text = "商品总价￥${state.totalPrice}"
                    adapter.submitList(state.dataList)
                }
            }
        }
        adapter.apply {
            onMinus = { goods ->
                if (goods.quantity == 1) {
                    viewModel.todoIntent(ShoppingCartIntent.DeleteGoods(goods))
                } else if (goods.quantity > 0) {
                    viewModel.todoIntent(ShoppingCartIntent.UpdateQuantity(goods.id, - 1))
                }
            }
            onAdd = { goods ->
                if (goods.quantity == 0) {
                    viewModel.todoIntent(ShoppingCartIntent.AddGoods(goods))
                } else if (goods.stock > 0) {
                    viewModel.todoIntent(ShoppingCartIntent.UpdateQuantity(goods.id, + 1))
                }
            }
        }
    }

}