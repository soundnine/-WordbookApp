package com.lsr.wordtest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lsr.wordtest.databinding.ItemPagerBinding

class PagerRecyclerAdapter(var pageList:ArrayList<PagerItem>) : RecyclerView.Adapter<PagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerHolder {
        val binding = ItemPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerHolder, position: Int) {
        holder.setText(pageList.get(position))
        holder.bindEventListener(pageList.get(position))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }
}