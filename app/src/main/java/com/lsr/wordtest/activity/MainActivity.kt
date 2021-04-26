package com.lsr.wordtest.activity

import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.lsr.wordtest.*
import com.lsr.wordtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val pagerItemList:ArrayList<PagerItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lookBtnListener = EventObjectFactory(this, WordDetailActivity::class.java as Class<Any>).makeEventObject(Names.LOOKBTN)
        val engToKorTestListener = EventObjectFactory(this, WordTestActivity::class.java as Class<Any>).makeEventObject(Names.ENGTOKORTEST)
        val korToEngTestListener = EventObjectFactory(this, WordTestActivityKor::class.java as Class<Any>).makeEventObject(Names.KORTOENGTEST)

        pagerItemList.add(makePagerItem(R.string.word_look, lookBtnListener!!))
        pagerItemList.add(makePagerItem(R.string.word_test_eng_to_kor, engToKorTestListener!!))
        pagerItemList.add(makePagerItem(R.string.word_test_kor_to_eng, korToEngTestListener!!))

        val pagerRecyclerAdapter = PagerRecyclerAdapter(pagerItemList)
        val viewPager = binding.viewPager
        viewPager.apply{
            adapter = pagerRecyclerAdapter
            orientation = ViewPager2.ORIENTATION_VERTICAL
            offscreenPageLimit = 3
            val pageMarginY = resources.getDimensionPixelOffset(R.dimen.pager_margin_y)
            val pagerHeight = resources.getDimensionPixelOffset(R.dimen.pager_y)
            val screenHeight = resources.displayMetrics.heightPixels
            val offsetY = screenHeight - pageMarginY - pagerHeight

           setPageTransformer { page, position ->
               page.translationY = position * -offsetY
           }
        }
    }

    fun makePagerItem(contentText:Int, itemEventListener: View.OnClickListener) : PagerItem{
        return PagerItem(getString(contentText), itemEventListener)
    }
}