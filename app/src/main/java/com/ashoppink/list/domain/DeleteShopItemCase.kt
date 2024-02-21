package com.ashoppink.list.domain

class DeleteShopItemCase(private val shopListRepository: ShopListRepository) {
    fun deleteShopItem(shopItem: ShopItem){
        shopListRepository.deleteShopItem(shopItem)
    }
}