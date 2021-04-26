package com.lsr.wordtest

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface WordDAO {
    @Query("SELECT W.id, W.eng, W.kor FROM wordTable AS W ORDER BY id DESC")
    fun getAll():List<Word>

    @Query("SELECT W.id, W.eng, W.kor FROM wordTable AS W ORDER BY RANDOM() LIMIT 20")
    fun getRandomRows():List<Word>

    @Query("SELECT COUNT(W.id) FROM wordTable AS W")
    fun checkEntireRowNum(): Int

    @Query("SELECT W.id FROM wordTable AS W WHERE W.id = (SELECT MAX(WW.ID) FROM wordTable AS WW)")
    fun selectLatestWordId():Int

    @Insert(onConflict = REPLACE)
    fun insert(word:Word)

    @Delete
    fun delete(word:Word)

    @Query("DELETE FROM wordTable WHERE id IN (:ids)")
    fun deleteByIdList(ids:MutableList<Int>)
}