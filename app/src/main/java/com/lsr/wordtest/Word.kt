package com.lsr.wordtest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "wordTable")
class Word : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id:Int? = null
    @ColumnInfo
    var eng:String = ""
    @ColumnInfo
    var kor:String = ""

    constructor(eng:String, kor:String){
        this.eng = eng
        this.kor = kor
    }
}