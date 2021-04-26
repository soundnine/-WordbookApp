package com.lsr.wordtest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lsr.wordtest.RecyclerAdapter
import com.lsr.wordtest.RoomDB
import com.lsr.wordtest.databinding.ActivityWordDeleteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordDeleteActivity : AppCompatActivity() {
    lateinit var binding:ActivityWordDeleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clickedRowNum = intent.getIntExtra("id", 0)
        val scrollPosition = intent.getIntExtra("scrollPosition", 0)
        val recyclerView = binding.recyclerForDelete
        val roomObject = RoomDB.roomStatic.makeRoom(this)

        val recyclerAdapter = RecyclerAdapter()
        recyclerAdapter.apply{
            setActivityContext(this@WordDeleteActivity)
            setSelectableForDelete(true)
        }

        val cancelBtn = binding.cancelBtn
        val deleteBtn = binding.deleteBtn

        CoroutineScope(Dispatchers.IO).launch {
            val list = roomObject.wordDAO().getAll()
            recyclerAdapter.getDataList().addAll(list)
            recyclerAdapter.getDeleteList().add(clickedRowNum)
        }
        recyclerView.apply{
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(this@WordDeleteActivity)
            scrollToPosition(scrollPosition)
        }
        cancelBtn.setOnClickListener {
            finish()
        }
        deleteBtn.setOnClickListener {
            val deleteList = recyclerAdapter.getDeleteList()
            CoroutineScope(Dispatchers.IO).launch {
                roomObject.wordDAO().deleteByIdList(deleteList)
            }
            finish()
            val intent = Intent(this, WordDetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
    }

}