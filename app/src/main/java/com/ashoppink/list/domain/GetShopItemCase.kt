package com.ashoppink.list.domain

class GetShopItemCase(private val shopListRepository: ShopListRepository) {
    fun getShopItemCase(id:Int):ShopItem{
        return shopListRepository.getShopItemCase(id)
    }
}