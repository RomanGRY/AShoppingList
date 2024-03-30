package com.ashoppink.list.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ashoppink.list.R
import com.ashoppink.list.domain.ShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(),ShopItemFragment.OnEditingFinishListener {

//    private lateinit var til_name:TextInputLayout
//    private lateinit var til_count:TextInputLayout
//    private lateinit var et_name:EditText
//    private lateinit var et_count:EditText
//    private lateinit var save_btn:Button
//
//    private lateinit var viewModel: ShopItemViewModel
    private var screenMode=MODE_UNKNOWN
    private var shopItemId=ShopItem.UNDEFINED_ITEM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState==null){
            launchRightMode()
        }

    }

    private fun launchRightMode() {
        val fragment=when(screenMode){
            MODE_ADD->ShopItemFragment.newInstanceAddItem()
            MODE_EDIT->ShopItemFragment.newInstanceEditItem(shopItemId)
            else->throw RuntimeException("Unknown Mode $screenMode")
        }
    supportFragmentManager.beginTransaction()
        .replace(R.id.shop_item_container,fragment).commit()
    }

    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param Screen Mode Is Absent")
        }
        val mode=intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("Unknown Mode $mode")
        }
        screenMode=mode
        if (screenMode == MODE_EDIT){
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param Shop Item Is Absent")
            }
            shopItemId=intent.getIntExtra(EXTRA_SHOP_ITEM_ID,ShopItem.UNDEFINED_ITEM)
        }
    }

    companion object{
        private const val EXTRA_SCREEN_MODE="extra_mode"
        private const val EXTRA_SHOP_ITEM_ID="extra_shop_item_id"
        private const val MODE_EDIT="mode_edit"
        private const val MODE_ADD="mode_add"
        private const val MODE_UNKNOWN=""

        fun newIntentAddItem(context: Context):Intent{
            val intent=Intent(context,ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE,MODE_ADD)
            return intent
        }
        fun newIntentEditItem(context: Context,idItem:Int):Intent{
            val intent=Intent(context,ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE,MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID,idItem)
            return intent
        }
    }

    override fun onEditingFinish() {
        finish()
    }
}