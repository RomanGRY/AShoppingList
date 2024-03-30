package com.ashoppink.list.domain

class AddShopItemCase(private val shopListRepository: ShopListRepository) {
    fun addShopItem(shopItem:ShopItem){
        shopListRepository.addShopItem(shopItem)
    }
}