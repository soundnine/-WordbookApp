package com.lsr.wordtest

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lsr.wordtest.activity.WordDeleteActivity
import com.lsr.wordtest.databinding.ItemRecyclerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Holder(val binding:ItemRecyclerBinding) :RecyclerView.ViewHolder(binding.root){
    val engText = binding.engTextForRecycle
    val korText = binding.korTextForRecycle
    val holderRoot = binding.holderRoot
    val defaultColor = holderRoot.backgroundTintList

    fun setWord(word:Word){
        engText.text = "${word.eng}"
        korText.text = "${word.kor}"
    }

    fun setLongClickListenerForDelete(listener:View.OnLongClickListener){
        holderRoot.setOnLongClickListener(listener)
    }

    fun setDeleteTarget(listener:View.OnClickListener){
        holderRoot.setOnClickListener(listener)
    }

    fun setDefaultLayoutColor(){
        holderRoot.backgroundTintList = defaultColor
    }

    fun changeLayoutColor(color:Int){
        holderRoot.backgroundTintList = ColorStateList.valueOf(color)
    }

}