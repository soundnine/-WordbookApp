package com.lsr.wordtest.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lsr.wordtest.R
import com.lsr.wordtest.RoomDB
import com.lsr.wordtest.TestPageUtil
import com.lsr.wordtest.Word
import com.lsr.wordtest.databinding.ActivityWordTestBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class WordTestActivityKor : AppCompatActivity(){
    lateinit var binding : ActivityWordTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val entireNum = binding.entireWords
        val roomObject = RoomDB.roomStatic.makeRoom(this)
        val correct = mutableListOf<Int>()

        CoroutineScope(Dispatchers.IO).launch {
            val randomWords:List<Word> = roomObject.wordDAO().getRandomRows()

            withContext(Dispatchers.Main){
                when(randomWords.size){
                    in 1..9 -> entireNum.text = "/ 0${randomWords.size}"
                    else -> entireNum.text = "/ ${randomWords.size}"
                }

                val firstWord = randomWords.get(0)
                binding.mainText.text = firstWord.kor
                binding.currentOrder.text = "01"

                bindWordsToAnswers(TestPageUtil.bindRandomList(randomWords, 0))
                val entireTime:Long = 10*1000
                val intervalTime:Long = 970
                val timer = object:CountDownTimer(entireTime, intervalTime){
                    override fun onTick(millisUntilFinished: Long) {
                        val first = Math.round(millisUntilFinished.toDouble()/ 1000)
                        var time = ""
                        when(first){
                            10L -> time = "00:10"
                            else -> time = "00:0$first"
                        }
                        binding.timer.text = time
                    }
                    override fun onFinish() {
                        //????????? recursive ??????
                        val order = binding.currentOrder.text.toString().toInt()
                        if(order < randomWords.size) { this.start()}
                        setQuizOrder(randomWords, correct)
                        if(order == randomWords.size){ this.cancel()}
                    }
                }.start()

                val btnClick = object: View.OnClickListener {
                    override fun onClick(v: View?) {
                        val order = binding.currentOrder.text.toString().toInt()
                        val engText = (v as Button).text
                        val currentOrder = binding.currentOrder.text.toString().toInt() - 1
                        val currentId = randomWords.get(currentOrder).id
                        val currentEngText = randomWords.get(currentOrder).eng

                        if(engText == currentEngText){
                            correct.add(currentId!!)
                        }

                        setQuizOrder(randomWords, correct)
                        if(order < randomWords.size) { timer.start()}
                        if(order == randomWords.size) { timer.cancel()}
                    }
                }
                binding.answer1.setOnClickListener(btnClick)
                binding.answer2.setOnClickListener(btnClick)
                binding.answer3.setOnClickListener(btnClick)
                binding.answer4.setOnClickListener(btnClick)
            }
        }
    }

    private fun bindWordsToAnswers(list:MutableList<Word>){
        binding.answer1.text = list.get(0).eng
        binding.answer2.text = list.get(1).eng
        binding.answer3.text = list.get(2).eng
        binding.answer4.text = list.get(3).eng
    }

    private fun setQuizOrder(randomWords: List<Word>, correctList:MutableList<Int>){
        val num = binding.currentOrder.text.toString().toInt()
        when(num){
            randomWords.size -> {
                var intent = Intent(this@WordTestActivityKor, TestCompleteActivity::class.java)
                intent.putExtra("randomWords", randomWords as Serializable)
                intent.putExtra("correctList", correctList as Serializable)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                return
            }
            in 1..8 -> {
                binding.currentOrder.text = "0${num+1}"
            }
            else -> binding.currentOrder.text = "${num+1}"
        }
        val targetWord = randomWords.get(num)
        binding.mainText.text = targetWord.kor
        bindWordsToAnswers(TestPageUtil.bindRandomList(randomWords, num))
    }
}