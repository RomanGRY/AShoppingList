package com.ashoppink.list.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ashoppink.list.R
import com.ashoppink.list.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object {
        const val ENABLED_XML = 1
        const val DISABLED_XML = 2
        const val MAX_POOL_SIZE=15
    }
    var count=0
    var onShopItemLongClickListener:((ShopItem)->Unit)?=null
    var onShopItemClickListener:((ShopItem)->Unit)?=null

    var shopList = listOf<ShopItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            val callback=ShopListDiffCallback(shopList,value)
            val result=DiffUtil.calculateDiff(callback)
            result.dispatchUpdatesTo(this)
            field = value
        }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.name_tv)
        val tvCount = view.findViewById<TextView>(R.id.count_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.e("CREATED","CREATED VIEW HOLDER ${++count}")
        val idType = when (viewType) {
            DISABLED_XML -> R.layout.item_shop_disabled
            ENABLED_XML -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view Type : $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(idType, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            ENABLED_XML
        } else (DISABLED_XML)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        Log.e("CREATED","BIND VIEW HOLDER ${++count}")
        val shopItem = shopList[position]
        viewHolder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        viewHolder.view.setOnClickListener{
            onShopItemClickListener?.invoke(shopItem)
        }
        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()
    }

    override fun onViewRecycled(holder: ShopItemViewHolder) {
        super.onViewRecycled(holder)
        holder.tvName.text = ""
        holder.tvCount.text = ""
        holder.tvName.setTextColor(
            ContextCompat.getColor(
                holder.view.context,
                android.R.color.white
            )
        )
    }
}