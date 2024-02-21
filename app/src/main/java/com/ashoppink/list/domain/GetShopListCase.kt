package com.ashoppink.list.domain

import androidx.lifecycle.LiveData

class GetShopListCase(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> {
        return shopListRepository.getShopList()
    }
}