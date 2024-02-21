package com.ashoppink.list.domain

class EditShopItemCase(private val shopListRepository: ShopListRepository) {
    fun editShopItemCase(shopItem: ShopItem){
        shopListRepository.editShopItemCase(shopItem)
    }
}