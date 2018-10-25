package com.morrisware.android.learnwebview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by MorrisWare on 2018/10/25.
 * Email: MorrisWare01@gmail.com
 */
class MainFragmentPagerAdapter(
    fragmentManager: FragmentManager,
    fragments: List<MainFragment>
) : FragmentStatePagerAdapter(fragmentManager) {

    val datas = ArrayList<MainFragment>()

    init {
        datas.addAll(fragments)
    }

    override fun getItem(position: Int): Fragment {
        return datas[position]
    }

    override fun getCount(): Int {
        return datas.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}