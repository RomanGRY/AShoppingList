package com.ashoppink.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashoppink.list.data.ShopListRepoImpl
import com.ashoppink.list.domain.AddShopItemCase
import com.ashoppink.list.domain.EditShopItemCase
import com.ashoppink.list.domain.GetShopItemCase
import com.ashoppink.list.domain.ShopItem

class ShopItemViewModel:ViewModel() {
    private val repository=ShopListRepoImpl

    private val addShopItemUseCase=AddShopItemCase(repository)
    private val getShopItemUseCase=GetShopItemCase(repository)
    private val editShopItemUseCase=EditShopItemCase(repository)

    private val _errorInputName=MutableLiveData<Boolean>()
    private val _errorInputCount=MutableLiveData<Boolean>()

    private val _shopItem=MutableLiveData<ShopItem>()

    private val _canCloseScreen=MutableLiveData<Unit>()

    val canCloseScreen:LiveData<Unit>
        get() = _canCloseScreen

    val shopItem:LiveData<ShopItem>
        get() = _shopItem

    val errorInputName:LiveData<Boolean>
        get() = _errorInputName

    val errorInputCount:LiveData<Boolean>
        get() = _errorInputCount

    fun resetErrorNameInput(){
        _errorInputName.value=false
    }
    fun resetErrorCountInput(){
        _errorInputCount.value=false
    }

    private fun finishWork(){
        _canCloseScreen.value=Unit
    }

    fun getShopItem(idItem:Int){
        val item=getShopItemUseCase.getShopItemCase(idItem)
        _shopItem.value=item
    }
    fun addShopItem(inputName:String?,inputCount:String?){
        val name=parseName(inputName)
        val count=parseCount(inputCount)
        val fieldsValid=validateInput(name,count)
        if (fieldsValid) {
            val shopItem=ShopItem(name,count,true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }
    }
    fun editShopItem(inputName:String?,inputCount:String?){
        val name=parseName(inputName)
        val count=parseCount(inputCount)
        val fieldsValid=validateInput(name,count)
        if (fieldsValid) {
            shopItem.value?.let {
                val editedShopItem= it.copy(name=name,count=count)
                editShopItemUseCase.editShopItemCase(editedShopItem)
                finishWork()
            }
        }
    }
    private fun parseName(strName:String?):String{
        return strName?.trim() ?:""
    }
    private fun parseCount(strCount:String?):Int{
        return try {
            strCount?.trim()?.toInt() ?:0
        }catch (e:Exception){
            0
        }
    }
    private fun validateInput(name:String,count:Int):Boolean{
        var res=true
        if (name.isBlank()){
            _errorInputName.value=true
            res=false
        }
        if (count<=0){
            _errorInputCount.value=true
            res=false
        }
        return res
    }
}