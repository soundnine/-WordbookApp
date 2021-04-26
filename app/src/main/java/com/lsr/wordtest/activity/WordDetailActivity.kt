package com.lsr.wordtest.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.lsr.wordtest.*
import com.lsr.wordtest.databinding.ActivityWordDetailBinding
import kotlinx.coroutines.*

class WordDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityWordDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val engText = binding.engText
        val korText = binding.korText
        val submitBtn = binding.submit
        val recyclerView = binding.recycler
        val roomObject = RoomDB.roomStatic.makeRoom(this)
        val recyclerAdapter = RecyclerAdapter()

        recyclerAdapter.apply{
            setHolderRemovable(true)
            setActivityContext(this@WordDetailActivity)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val list = roomObject.wordDAO().getAll()
            recyclerAdapter.getDataList().addAll(list)
        }
        recyclerView.apply{
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(this@WordDetailActivity)
        }

        engText.addTextChangedListener(getColorAndClickableWatcher())
        korText.addTextChangedListener(getColorAndClickableWatcher())

        submitBtn.setOnClickListener {
            val engTextValue = engText.text.toString()
            val korTextValue = korText.text.toString()
            val word = Word(engTextValue, korTextValue)

            CoroutineScope(Dispatchers.IO).launch {
                roomObject.wordDAO().insert(word)
                word.id = roomObject.wordDAO().selectLatestWordId()
                recyclerAdapter.getDataList().add(0, word)
                withContext(Dispatchers.Main) {
                    engText.text.clear()
                    korText.text.clear()
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        }
        //clickListener가 true로 바꿔서 다시 false로 바꿔주기.
        submitBtn.isClickable = false
    }

    private fun setColorAndClickable(button:Button, color:Int, clickable:Boolean){
        button.backgroundTintList = ColorStateList.valueOf(color)
        button.isClickable = clickable
    }

    private fun getColorAndClickableWatcher():TextWatcher{
        return object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val submitBtn = binding.submit
                if(binding.engText.text.toString().isNotBlank() && binding.korText.text.toString().isNotBlank()){
                    val skyBlueColor = ContextCompat.getColor(this@WordDetailActivity, R.color.skyblue)
                    if(submitBtn.backgroundTintList!!.defaultColor == skyBlueColor) return
                    setColorAndClickable(submitBtn, skyBlueColor, true)
                } else{
                    if(submitBtn.backgroundTintList!!.defaultColor == Color.BLACK) return
                    setColorAndClickable(submitBtn, Color.BLACK, false)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }
}