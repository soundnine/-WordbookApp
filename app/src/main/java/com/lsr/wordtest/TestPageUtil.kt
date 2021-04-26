package com.lsr.wordtest

import android.util.Log
import android.view.View

class TestPageUtil() {
    companion object {
        fun getRandomThreeWords(list:List<Word>, index: Int):MutableList<Word>{
            val filteredList = mutableListOf<Word>()
            filteredList.addAll(list.minus(list.get(index)).shuffled().take(3))
            return filteredList
        }

        fun makeRandomList(currentWord:Word, randomWords:MutableList<Word>):MutableList<Word>{
            randomWords.add(currentWord)
            randomWords.shuffled()
            return randomWords
        }

        fun bindRandomList(randomWords:List<Word>, index:Int):MutableList<Word>{
            val firstWord = randomWords.get(index)
            val randomList = makeRandomList(firstWord, getRandomThreeWords(randomWords, index))
            return randomList
        }
    }
}