package com.ashoppink.list.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun getShopList(): LiveData<List<ShopItem>>

    fun getShopItemCase(id:Int):ShopItem

    fun editShopItemCase(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)

    fun addShopItem(shopItem:ShopItem)
}