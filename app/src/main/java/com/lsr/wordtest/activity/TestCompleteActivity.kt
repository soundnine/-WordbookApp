package com.lsr.wordtest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.lsr.wordtest.RecyclerAdapter
import com.lsr.wordtest.Word
import com.lsr.wordtest.databinding.ActivityTestCompleteBinding

class TestCompleteActivity : AppCompatActivity() {
    lateinit var binding:ActivityTestCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        val entireWordList:List<Word> = intent.getSerializableExtra("randomWords") as List<Word>
        val correctList:MutableList<Int> = intent.getSerializableExtra("correctList") as MutableList<Int>
        binding.entireNum.text = "/${zeroMaker(entireWordList as MutableList<Any>)}"
        binding.correctNum.text = zeroMaker(correctList as MutableList<Any>)

        val recyclerAdapter = RecyclerAdapter()
        recyclerAdapter.apply{
            getDataList().addAll(entireWordList)
            getCorrectList().addAll(correctList)
            setActivityContext(this@TestCompleteActivity)
            setIsBeingTested(true)
        }
        recyclerView.apply{
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(this@TestCompleteActivity)
        }
    }

    fun zeroMaker(list: MutableList<Any>):String{
        var value = ""

        if(list.size in 0..9){
            value = "0${list.size}"
        } else{
            value = list.size.toString()
        }
        return value
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}