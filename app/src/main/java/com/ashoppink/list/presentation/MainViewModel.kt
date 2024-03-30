package com.ashoppink.list.presentation

import androidx.lifecycle.ViewModel
import com.ashoppink.list.data.ShopListRepoImpl
import com.ashoppink.list.domain.DeleteShopItemCase
import com.ashoppink.list.domain.EditShopItemCase
import com.ashoppink.list.domain.GetShopListCase
import com.ashoppink.list.domain.ShopItem

class MainViewModel :ViewModel(){

    private val repository=ShopListRepoImpl

    private val getShopListCase=GetShopListCase(repository)
    private val editShopItemCase=EditShopItemCase(repository)
    private val deleteShopItemCase=DeleteShopItemCase(repository)

    val shopList = getShopListCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemCase.deleteShopItem(shopItem)
    }
    fun changeEnableState(shopItem: ShopItem){
        val newItem=shopItem.copy(enabled = !shopItem.enabled)
        editShopItemCase.editShopItemCase(newItem)
    }
}