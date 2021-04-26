package com.lsr.wordtest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase(){
    abstract fun wordDAO():WordDAO
    object roomStatic{
        private var roomInstance:RoomDB? = null
        fun makeRoom(context:Context) : RoomDB{
            if(roomInstance == null){
                roomInstance = Room.databaseBuilder(context, RoomDB::class.java, "wordTable").build()
            }
            return roomInstance as RoomDB
        }
    }
}