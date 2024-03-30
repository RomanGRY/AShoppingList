package com.ashoppink.list.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ashoppink.list.domain.ShopItem
import com.ashoppink.list.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepoImpl : ShopListRepository {

    private val shopList= sortedSetOf<ShopItem>({ p0, p1->p0.id.compareTo(p1.id)})
    private val mutableLiveData=MutableLiveData<List<ShopItem>>()
    private var autoIncrementId=0

    init {
        for (i in 0..100){
            val item=ShopItem(name = "Name $i", count = i, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return mutableLiveData
    }


    override fun getShopItemCase(id: Int): ShopItem {
        return shopList.find { it.id==id } ?: throw RuntimeException("Element with $id was not fount")
    }

    override fun editShopItemCase(shopItem: ShopItem) {
        val oldShopItem= getShopItemCase(shopItem.id)
        shopList.remove(oldShopItem)
        addShopItem(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id==ShopItem.UNDEFINED_ITEM){
            shopItem.id=autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }
    private fun updateList(){
        mutableLiveData.value= shopList.toList()
    }
}