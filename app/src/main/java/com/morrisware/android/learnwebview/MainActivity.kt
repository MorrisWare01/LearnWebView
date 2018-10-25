package com.morrisware.android.learnwebview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.morrisware.android.learnwebview.event.ToggleViewPager
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter: MainFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.registerEvent(ToggleViewPager::class.java, Consumer {
            viewPager.isSwipeEnable = it.isSwipe
            btnAdd.visibility = if (it.isSwipe) View.VISIBLE else View.GONE
            if (it.isSwipe) {

            }
        })

        val fragments = listOf(MainFragment())
        mAdapter = MainFragmentPagerAdapter(supportFragmentManager, fragments)
        viewPager.offscreenPageLimit = 3
        viewPager.isSwipeEnable = false
        viewPager.adapter = mAdapter

        btnAdd.setOnClickListener {
            val fragment = MainFragment()
            mAdapter.datas.add(fragment)
            mAdapter.notifyDataSetChanged()
            viewPager.currentItem = mAdapter.datas.indexOf(fragment)
        }
    }

}
