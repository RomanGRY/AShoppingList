package com.ashoppink.list.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashoppink.list.R

class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.name_tv)
    val tvCount = view.findViewById<TextView>(R.id.count_tv)
}