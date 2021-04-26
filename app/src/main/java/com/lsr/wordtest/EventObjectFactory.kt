package com.lsr.wordtest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EventObjectFactory(val parentContext: Context, val moveTargetActivity: Class<Any>) {

    fun makeEventObject(names: Names): View.OnClickListener? {
        var listenerObject : View.OnClickListener? = null
        when (names) {
            Names.LOOKBTN -> {
                listenerObject = object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        moveActivity(parentContext, moveTargetActivity)
                    }
                }
            }

            Names.ENGTOKORTEST, Names.KORTOENGTEST -> {
                listenerObject = object:View.OnClickListener {
                    override fun onClick(v: View?) {
                        CoroutineScope(Dispatchers.IO).launch {
                            if(getEntireWordNumInDB(parentContext) < 4){
                                withContext(Dispatchers.Main){
                                    makeConditionalSentence(parentContext)
                                }
                            } else{
                                moveActivity(parentContext, moveTargetActivity)
                            }
                        }
                    }
                }
            }
        }
        return listenerObject
    }

    private fun getEntireWordNumInDB(parentContext: Context) : Int{
        return RoomDB.roomStatic.makeRoom(parentContext).wordDAO().checkEntireRowNum()
    }

    private fun makeConditionalSentence(parentContext: Context){
        Toast.makeText(parentContext,"단어를 4개 이상 등록해주세요.", Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity(parentContext: Context, TargetActivity:Class<Any>){
        val intent = Intent(parentContext, TargetActivity)
        parentContext.startActivity(intent)
        (parentContext as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}