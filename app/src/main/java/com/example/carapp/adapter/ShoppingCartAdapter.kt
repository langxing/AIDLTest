package com.example.carapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.carapp.R
import com.example.carapp.databinding.LayoutItemShoppingCartBinding
import com.example.carapp.model.Goods

class ShoppingCartAdapter : ListAdapter<Goods, ShoppingCartAdapter.ShoppingCartHolder>(object : DiffUtil.ItemCallback<Goods>() {
    override fun areItemsTheSame(oldItem: Goods, newItem: Goods): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Goods, newItem: Goods): Boolean {
        return oldItem.id == newItem.id
    }

}) {

    var onMinus: ((goods: Goods) -> Unit)? = null
    var onAdd: ((goods: Goods) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartHolder {
        val contentView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_shopping_cart, parent, false)
        val binding = LayoutItemShoppingCartBinding.bind(contentView)
        return ShoppingCartHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShoppingCartHolder, position: Int) {
        val goods = getItem(position)
        holder.binding.apply {
            tvName.text = goods.name
            tvStock.text = "库存：${goods.stock}"
            tvPrice.text = "价格：￥${goods.price}"
        }
    }

    inner class ShoppingCartHolder(val binding: LayoutItemShoppingCartBinding) : ViewHolder(binding.root) {

        init {
            binding.tvMinus.setOnClickListener {
                onMinus?.invoke(getItem(absoluteAdapterPosition))
            }
            binding.tvAdd.setOnClickListener {
                onAdd?.invoke(getItem(absoluteAdapterPosition))
            }
        }

    }

}