package com.ashoppink.list.domain

data class ShopItem(
    val name:String,
    val count:Int,
    val enabled:Boolean,
    var id:Int=UNDEFINED_ITEM
){
    companion object{
        const val UNDEFINED_ITEM=-1
    }
}
