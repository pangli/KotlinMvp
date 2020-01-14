package com.zorro.kotlin.samples.ui.database.fragment

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.TextView

import com.gyf.immersionbar.ImmersionBar
import com.zorro.kotlin.baselibs.base.BackHandlerHelper
import com.zorro.kotlin.baselibs.base.FragmentBackHandler
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.mvp.contract.TestContract
import com.zorro.kotlin.samples.mvp.presenter.TestPresenter
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpFragment
import com.zorro.kotlin.samples.ui.database.adapter.TabLayoutPagerAdapter
import com.zorro.kotlin.samples.utils.ActivityHelper
import kotlinx.android.synthetic.main.include_toolbar_layout.iv_right
import kotlinx.android.synthetic.main.include_toolbar_layout.toolbar
import kotlinx.android.synthetic.main.include_toolbar_tab_layout.*
import kotlinx.android.synthetic.main.toolbar_tab_layout.*


/**
 * @author Zorro
 * @date 2019/12/19
 * @desc 资料库
 */
class DatabaseFragment : ZorroBaseMvpFragment<TestContract.View, TestContract.Presenter>(),
    TestContract.View, FragmentBackHandler {
    private val mTitle = mutableListOf("通用资料", "产品资料")
    private lateinit var fragmentList: MutableList<Fragment>
    private var onTabSelectedListener: MyOnTabSelectedListener? = null

    companion object {
        fun newInstance(): DatabaseFragment = DatabaseFragment()
    }

    override fun createPresenter(): TestContract.Presenter = TestPresenter()

    override fun attachLayoutRes(): Int = R.layout.toolbar_tab_layout
    override fun initView(view: View) {
        super.initView(view)
        ImmersionBar.setTitleBar(this, toolbar)
        iv_right.visibility = View.VISIBLE
        initTab()
    }


    override fun bindListener(view: View) {
        onTabSelectedListener = MyOnTabSelectedListener()
        tab_layout.addOnTabSelectedListener(onTabSelectedListener!!)
        iv_right.setOnClickListener(onClickListener)
    }

    override fun lazyLoad() {
    }

    /**
     * tab_layout tab 初始化
     */
    private fun initTab() {
        fragmentList =
            mutableListOf(
                CommonInformationFragment.newInstance(),
                ProductInformationFragment.newInstance()
            )
        vp_fragment.adapter = TabLayoutPagerAdapter(childFragmentManager, mTitle, fragmentList)
        vp_fragment.offscreenPageLimit = mTitle.size
        tab_layout.setupWithViewPager(vp_fragment)
        for (i in 0 until tab_layout.tabCount) {
            val tab = tab_layout.getTabAt(i)//获得每一个tab
            if (tab != null) {
                tab.setCustomView(R.layout.item_tab_view)//给每一个tab设置view
                val item = tab.customView as TextView
                item.textSize = if (i == 0) 18f else 15f
                item.isSelected = i == 0
                item.text = mTitle[i]//设置tab上的文字
            }
        }
        vp_fragment.addOnPageChangeListener(pageListener)
    }

    /**
     * ViewPager 页面改变监听
     */
    private val pageListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            if (position == 0) {
                (fragmentList[1] as ProductInformationFragment).onBackPressed()
            } else if (position == 1) {
                (fragmentList[0] as CommonInformationFragment).onBackPressed()
            }
        }

    }

    /**
     * TabLayout OnTabSelectedListener
     */
    private inner class MyOnTabSelectedListener : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            val item = tab.customView as TextView
            item.textSize = 18f
            item.isSelected = true
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            val item = tab.customView as TextView
            item.textSize = 15f
            item.isSelected = false
        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    }

    /**
     * view 点击事件
     */
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_right -> {
                ActivityHelper.jumpToWebSocketActivity(context!!)
            }
        }
    }

    override fun onDestroyView() {
        if (onTabSelectedListener != null)
            tab_layout.removeOnTabSelectedListener(onTabSelectedListener!!)
        vp_fragment.removeOnPageChangeListener(pageListener)
        super.onDestroyView()
    }

    /**
     * Fragment返回键
     */
    override fun onBackPressed(): Boolean = BackHandlerHelper.handleBackPress(this)

}