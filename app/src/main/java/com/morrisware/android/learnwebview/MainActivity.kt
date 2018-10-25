package com.morrisware.android.learnwebview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.morrisware.android.learnwebview.event.RefreshItemEvent
import com.morrisware.android.learnwebview.event.RemoveItemEvent
import com.morrisware.android.learnwebview.event.ToggleViewPagerEvent
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter: MainFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.registerEvent(ToggleViewPagerEvent::class.java, Consumer {
            viewPager.isSwipeEnable = it.isZooming
            if (it.isZooming) {
                val scaleX = ObjectAnimator.ofFloat(viewPager, "scaleX", 1f, 0.6f)
                val scaleY = ObjectAnimator.ofFloat(viewPager, "scaleY", 1f, 0.6f)
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(scaleX, scaleY)
                animatorSet.duration = 300
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        RxBus.getInstance().post(RefreshItemEvent(true))
                    }
                })
                animatorSet.start()
            } else {
                val scaleX = ObjectAnimator.ofFloat(viewPager, "scaleX", 0.6f, 1f)
                val scaleY = ObjectAnimator.ofFloat(viewPager, "scaleY", 0.6f, 1f)
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(scaleX, scaleY)
                animatorSet.duration = 300
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        RxBus.getInstance().post(RefreshItemEvent(false))
                    }
                })
                animatorSet.start()
            }
        })

        lifecycle.registerEvent(RemoveItemEvent::class.java, Consumer {
            if (it.index >= 0 && it.index < mAdapter.datas.size) {
                mAdapter.datas.removeAt(viewPager.currentItem)
                mAdapter.notifyDataSetChanged()
            }
        })

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)

        val fragments = listOf(
            MainFragment(),
            MainFragment()
        )

        mAdapter = MainFragmentPagerAdapter(supportFragmentManager, fragments)
        viewPager.offscreenPageLimit = 3
        viewPager.isSwipeEnable = true
        viewPager.adapter = mAdapter
        viewPager.pageMargin = (30 * displayMetrics.density).toInt()

        btnAdd.setOnClickListener {
            val fragment = MainFragment()
            mAdapter.datas.add(fragment)
            mAdapter.notifyDataSetChanged()
            viewPager.currentItem = mAdapter.datas.indexOf(fragment)
        }
    }

}
