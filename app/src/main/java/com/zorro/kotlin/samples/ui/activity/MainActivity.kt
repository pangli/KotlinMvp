package com.zorro.kotlin.samples.ui.activity

import android.Manifest
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View


import com.zorro.kotlin.baselibs.base.BackHandlerHelper
import com.zorro.kotlin.baselibs.base.BaseApplication
import com.zorro.kotlin.baselibs.net.showToast
import com.zorro.kotlin.baselibs.webview.X5WebViewUtils
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.mvp.contract.MainContract
import com.zorro.kotlin.samples.mvp.presenter.MainPresenter
import com.zorro.kotlin.samples.network.modelToJsonString
import com.zorro.kotlin.samples.ui.base.ZorroBaseMvpActivity
import com.zorro.kotlin.samples.ui.database.fragment.DatabaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.menu_badge.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : ZorroBaseMvpActivity<MainContract.View, MainContract.Presenter>(),
    MainContract.View {


    private var mExitTime: Long = 0L
    private val bottomIndex: String = "bottom_index"
    private val fragmentHome = 0x01
    private val fragmentDatabase = 0x02
    private val fragmentMine = 0x03

    private var mIndex = fragmentHome


    private var homeFragment: DatabaseFragment? = null
    private var databaseFragment: DatabaseFragment? = null
    private var mineFragment: DatabaseFragment? = null


    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    override fun attachLayoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt(bottomIndex)
        }
        super.onCreate(savedInstanceState)
        Constant.token = token
        mPresenter?.getUserInfo()
        requestPermission()
    }

    override fun initView() {
        super.initView()
        bottom_navigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }
        initBottomNavigation()
    }

    override fun bindListener() {

    }

    override fun initData() {
        super.initData()
    }

    override fun start() {

    }

    /**
     * 配置按钮
     */
    private fun initBottomNavigation() {
        //BottomNavigationView 选中和未选择字体的样式
        bottom_navigation.itemTextAppearanceActive = R.style.bottom_selected_text
        bottom_navigation.itemTextAppearanceInactive = R.style.bottom_normal_text
        //addMessageRedDot()
        bottom_navigation.selectedItemId = bottom_navigation.menu.getItem(0).itemId
    }

    /**
     * 我的Menu添加消息红点
     */
    private fun addMessageRedDot() {
        val menus = bottom_navigation.menu
        val menuSize = menus.size()
        for (i in 0 until menuSize) {
            when (menus.getItem(i).itemId) {
                R.id.action_mine -> {
                    //获取整个的NavigationView
                    val menuView = bottom_navigation.getChildAt(0) as BottomNavigationMenuView
                    //这里就是获取所添加的每一个Tab(或者叫menu)，
                    val tab = menuView.getChildAt(i)
                    val itemView = tab as BottomNavigationItemView
                    //加载我们的角标View，新创建的一个布局
                    val badge =
                        LayoutInflater.from(this).inflate(R.layout.menu_badge, menuView, false)
                    //添加到Tab上
                    itemView.addView(badge)
                    iv_dot.visibility = View.GONE
                }
            }
        }

    }


    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            return@OnNavigationItemSelectedListener when (item.itemId) {
                R.id.action_home -> {
                    //setWhiteStatusBar()
                    setBlackStatusBar()
                    showFragment(fragmentHome)
                    true
                }
                R.id.action_database -> {
                    setBlackStatusBar()
                    showFragment(fragmentDatabase)
                    true
                }
                R.id.action_mine -> {
                    setBlackStatusBar()
                    showFragment(fragmentMine)
                    true
                }
                else -> {
                    false
                }

            }
        }

    /**
     * 展示Fragment
     * @param index
     */
    private fun showFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index
        when (index) {
            fragmentHome // 首页
            -> {
                if (homeFragment == null) {
                    homeFragment = DatabaseFragment.newInstance()
                    transaction.add(
                        R.id.container,
                        homeFragment!!,
                        Constant.TAG_FRAGMENT_HOME
                    )
                } else {
                    transaction.show(homeFragment!!)
                }
            }
            fragmentDatabase // 资料库
            -> {
                if (databaseFragment == null) {
                    databaseFragment = DatabaseFragment.newInstance()
                    transaction.add(
                        R.id.container,
                        databaseFragment!!,
                        Constant.TAG_FRAGMENT_DATABASE
                    )
                } else {
                    transaction.show(databaseFragment!!)
                }
            }
            fragmentMine // 我的
            -> {
                if (mineFragment == null) {
                    mineFragment = DatabaseFragment.newInstance()
                    transaction.add(
                        R.id.container,
                        mineFragment!!,
                        Constant.TAG_FRAGMENT_MINE
                    )
                } else {
                    transaction.show(mineFragment!!)
                }
            }
        }
        transaction.commit()
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        homeFragment?.let { transaction.hide(it) }
        databaseFragment?.let { transaction.hide(it) }
        mineFragment?.let { transaction.hide(it) }
    }


    override fun onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                super.onBackPressed()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
        }
    }

    override fun onDestroy() {
        homeFragment = null
        databaseFragment = null
        mineFragment = null
        super.onDestroy()
    }

    /**
     * 申请权限
     */
    @AfterPermissionGranted(101)
    private fun requestPermission() {
        val perms = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (hasPermissions(*perms)) {//检查是否获取该权限
            //获取权限成功
            X5WebViewUtils.initX5Environment(BaseApplication.instance)
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "读写文件权限未开启,请先在设置中打开存储权限", 101, *perms)
        }
    }

    override fun httpResultUserInfo(userInfo: UserInfo) {
        this.userInfo = userInfo
        userInfoJson = modelToJsonString(UserInfo::class.java, userInfo)
    }
}
