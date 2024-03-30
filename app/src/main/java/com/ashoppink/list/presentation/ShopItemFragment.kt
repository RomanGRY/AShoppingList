package com.ashoppink.list.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ashoppink.list.R
import com.ashoppink.list.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import kotlin.RuntimeException

class ShopItemFragment:Fragment() {

    private lateinit var til_name: TextInputLayout
    private lateinit var til_count:TextInputLayout
    private lateinit var et_name:EditText
    private lateinit var et_count: EditText
    private lateinit var save_btn: Button
    private lateinit var onEditingFinishListener:OnEditingFinishListener

    private lateinit var viewModel: ShopItemViewModel
    private var screenMode:String= MODE_UNKNOWN
    private var shopItemId:Int= ShopItem.UNDEFINED_ITEM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishListener){
            onEditingFinishListener=context
        }else{
            throw RuntimeException("Activity must implement OnEditingFinishListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_shop_item,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        launchrightMode()
        observeViewModel()
    }

    interface OnEditingFinishListener{
        fun onEditingFinish()
    }

        private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner){
            val message = if (it){
                getString(R.string.count_error)
            }else{
                null
            }
            til_count.error=message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner){
            val message = if (it){
                getString(R.string.name)
            }else{
                null
            }
            til_name.error=message
        }
        viewModel.canCloseScreen.observe(viewLifecycleOwner){
            onEditingFinishListener.onEditingFinish()
        }
    }

    private fun launchrightMode() {
        when(screenMode){
            MODE_ADD->launchAddMode()
            MODE_EDIT->launchEditMode()
        }
    }

    private fun addTextChangeListeners() {
        et_name.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorNameInput()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        et_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorCountInput()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun launchAddMode() {
        save_btn.setOnClickListener{
            viewModel.addShopItem(et_name.text?.toString(),et_count.text?.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner){
            et_name.setText(it.name)
            et_count.setText(it.count.toString())
        }
        save_btn.setOnClickListener{
            viewModel.editShopItem(et_name.text?.toString(),et_count.text?.toString())
        }
    }

    private fun parseParams(){
        val arggs=requireArguments()
        if (!arggs.containsKey(SCREEN_MODE)){
            throw RuntimeException("Param Screen Mode Is Absent")
        }
        val mode=arggs.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("Unknown Mode $mode")
        }
        screenMode=mode
        if (screenMode == MODE_EDIT){
            if (!arggs.containsKey(SHOP_ITEM_ID)){
                throw RuntimeException("Param Shop Item Is Absent")
            }
            shopItemId=arggs.getInt(SHOP_ITEM_ID,ShopItem.UNDEFINED_ITEM)
        }
    }
    private fun initViews(view: View){
        til_name=view.findViewById(R.id.til_name)
        til_count=view.findViewById(R.id.til_count)
        et_name=view.findViewById(R.id.et_name)
        et_count=view.findViewById(R.id.et_count)
        save_btn=view.findViewById(R.id.save_btn)
    }
    companion object{
        private const val SCREEN_MODE="extra_mode"
        private const val SHOP_ITEM_ID="extra_shop_item_id"
        private const val MODE_EDIT="mode_edit"
        private const val MODE_ADD="mode_add"
        private const val MODE_UNKNOWN=""

        fun newInstanceAddItem():ShopItemFragment{
            return ShopItemFragment().apply {
                arguments=Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
        fun newInstanceEditItem(shopItemId: Int):ShopItemFragment{
            return ShopItemFragment().apply {
                arguments=Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}