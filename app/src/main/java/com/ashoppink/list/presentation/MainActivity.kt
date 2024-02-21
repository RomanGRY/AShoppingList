package com.ashoppink.list.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ashoppink.list.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        createRecyclerView()
        viewModel.shopList.observe(this) {
            listAdapter.shopList = it
        }
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
                val item = listAdapter.shopList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(shopListRecycler)
    }

    private fun setUpClickListener() {
        listAdapter.onShopItemClickListener = {
            Log.e("dfghf", it.toString())
        }
    }

    private fun setUpLongClickListener() {
        listAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}