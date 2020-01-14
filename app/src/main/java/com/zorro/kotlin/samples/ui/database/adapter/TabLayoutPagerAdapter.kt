package com.zorro.kotlin.samples.ui.database.adapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 * @author Zorro
 * @date 2019/12/19
 * @desc TabLayoutPagerAdapter
 */
class TabLayoutPagerAdapter(
    fm: FragmentManager,
    private val titles: MutableList<String>,
    private val fragmentList: MutableList<Fragment>
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragmentList[position]
    override fun getPageTitle(position: Int): CharSequence? = titles[position]
    override fun getCount(): Int = titles.size
}