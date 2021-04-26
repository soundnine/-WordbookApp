package com.lsr.wordtest

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lsr.wordtest.activity.WordDeleteActivity
import com.lsr.wordtest.activity.WordDetailActivity
import com.lsr.wordtest.databinding.ItemRecyclerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecyclerAdapter(private var isHolderRemovable:Boolean = false,
                      private var isSelectableForDelete:Boolean = false,
                      private var isBeingTested:Boolean = false,
                      private val dataList: MutableList<Word> = mutableListOf(),
                      private val correctList: MutableList<Int> = mutableListOf(),
                      private val deleteList: MutableList<Int> = mutableListOf()): RecyclerView.Adapter<Holder>() {
//    private val dataList = mutableListOf<Word>()
//    private val correctList = mutableListOf<Int>()
//    private val deleteList = mutableListOf<Int>()
    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val pickedWord:Word = dataList.get(position)
        val pinkRedColor = ContextCompat.getColor(parentContext, R.color.pinkred)
        val skyBlue = ContextCompat.getColor(parentContext, R.color.skyblue)
        holder.setWord(pickedWord)

        if(!correctList.contains(pickedWord.id) && isBeingTested){
            holder.changeLayoutColor(pinkRedColor)
        }
        if(correctList.contains(pickedWord.id) && isBeingTested){
            holder.changeLayoutColor(skyBlue)
        }
        
        //deleteActivity 넘어가기
        if(isHolderRemovable){
            holder.setLongClickListenerForDelete {
                holder.changeLayoutColor(pinkRedColor)

                CoroutineScope(Dispatchers.Main).launch {
                    delay(500L)
                    val intent = Intent(parentContext, WordDeleteActivity::class.java)
                    intent.putExtra("id", pickedWord.id)
                    intent.putExtra("scrollPosition", position)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    parentContext.startActivity(intent)
                    holder.setDefaultLayoutColor()
                }
                true
            }
        }

        //deleteActivity Hover
        if(deleteList.size != 0 && deleteList.get(0) == pickedWord.id){
            holder.changeLayoutColor(pinkRedColor)
        }

        if(isSelectableForDelete){
            holder.setDeleteTarget{
                val pinkRedStateList = ColorStateList.valueOf(pinkRedColor)
                if(it.backgroundTintList != pinkRedStateList){
                    it.backgroundTintList = pinkRedStateList
                    deleteList.add(dataList.get(position).id!!)
                } else{
                    it.backgroundTintList = null
                    deleteList.remove(dataList.get(position).id!!)
                }

                val parentDeleteBtn = (parentContext as WordDeleteActivity).binding.deleteBtn
                    parentDeleteBtn.isClickable = deleteList.size != 0

            }
        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setActivityContext(context: Context){
        parentContext = context
    }

    fun setHolderRemovable(b:Boolean){
        isHolderRemovable = b
    }

    fun setSelectableForDelete(b:Boolean){
        isSelectableForDelete = b
    }

    fun setIsBeingTested(b:Boolean){
        isBeingTested = b
    }

    fun getDataList(): MutableList<Word> {
        return dataList
    }

    fun getDeleteList(): MutableList<Int> {
        return deleteList
    }

    fun getCorrectList(): MutableList<Int> {
        return correctList
    }
}