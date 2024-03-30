package com.ashoppink.list.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ashoppink.list.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(),ShopItemFragment.OnEditingFinishListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: ShopListAdapter
    private var shopItemContainer:FragmentContainerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer=findViewById(R.id.shop_item_container)
        if (isOnePainMode()){

        }else{
            
        }
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val add_shop_item_btn:FloatingActionButton=findViewById(R.id.add_shop_item_btn)
        add_shop_item_btn.setOnClickListener{
            if (isOnePainMode()){
                startActivity(ShopItemActivity.newIntentAddItem(this@MainActivity))
            }else{
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }

        }
        createRecyclerView()
        viewModel.shopList.observe(this) {
            listAdapter.submitList(it)
        }
    }

    private fun isOnePainMode():Boolean{
        return shopItemContainer==null
    }
    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container,fragment)
            .addToBackStack(null).commit()
    }
    override fun onEditingFinish(){
        Toast.makeText(this@MainActivity,"Success",Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun createRecyclerView() {
        val shop_list_recycler = findViewById<RecyclerView>(R.id.shop_list_recycler)
        listAdapter = ShopListAdapter()
        with(shop_list_recycler) {
            adapter = listAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLED_XML,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLED_XML,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setUpLongClickListener()
        setUpClickListener()
        setUpSwipeListener(shop_list_recycler)
    }

    private fun setUpSwipeListener(shopListRecycler: RecyclerView) {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = listAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(shopListRecycler)
    }

    private fun setUpClickListener() {
        listAdapter.onShopItemClickListener = {
            if (isOnePainMode()){
                startActivity(ShopItemActivity.newIntentEditItem(this@MainActivity,it.id))
            }else{
             launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setUpLongClickListener() {
        listAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}