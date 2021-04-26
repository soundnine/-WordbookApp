package com.lsr.wordtest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lsr.wordtest.databinding.ItemPagerBinding

class PagerHolder(val binding:ItemPagerBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setText(pagerItem:PagerItem){
        binding.wordBtn.text = pagerItem.text
    }
    fun bindEventListener(pagerItem: PagerItem){
        binding.wordBtn.setOnClickListener(pagerItem.clickEventListenerForIntent)
    }
}