package com.ashoppink.list.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ashoppink.list.R
import com.ashoppink.list.domain.ShopItem

class ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    companion object {
        const val ENABLED_XML = 1
        const val DISABLED_XML = 2
        const val MAX_POOL_SIZE = 15
    }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val idType = when (viewType) {
            DISABLED_XML -> R.layout.item_shop_disabled
            ENABLED_XML -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view Type : $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(idType, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            ENABLED_XML
        } else (DISABLED_XML)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        viewHolder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        viewHolder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        viewHolder.tvName.text = shopItem.name
        viewHolder.tvCount.text = shopItem.count.toString()
    }
}