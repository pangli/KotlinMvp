package com.zorro.kotlin.baselibs.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zorro.kotlin.baselibs.utils.CommonUtil
import com.zorro.kotlin.baselibs.utils.KeyBoardUtil
import com.zorro.kotlin.baselibs.utils.StatusBarUtil
import com.zorro.kotlin.baselibs.widget.LoadingDialog
import org.greenrobot.eventbus.EventBus

/**
 * @author Zorro
 * @date 2018/11/19
 * @desc BaseActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 布局文件id
     */
    @LayoutRes
    protected abstract fun attachLayoutRes(): Int

    /**
     * 初始化公共数据
     */
    abstract fun initCommonConfig()

    /**
     * 初始化数据
     */
    open fun initData() {}

    /**
     * 初始化 View
     */
    abstract fun initView()

    /**
     * 绑定监听 View
     */
    abstract fun bindListener()

    /**
     * 开始请求
     */
    abstract fun start()

    /**
     * 加载动画
     */
    protected var loading: LoadingDialog? = null

    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * 获取权限处理类
     */
    protected val rxPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    /**
     * 设置状态栏的背景颜色
     */
    fun setStatusBarColor(@ColorInt color: Int) {
        StatusBarUtil.setColor(this, color, 0)
    }

    /**
     * 设置状态栏图标的颜色
     *
     * @param dark true: 黑色  false: 白色
     */
    fun setStatusBarIcon(dark: Boolean) {
        if (dark) {
            StatusBarUtil.setLightMode(this)
        } else {
            StatusBarUtil.setDarkMode(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 强制竖屏
        setContentView(attachLayoutRes())
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        if (loading == null) {
            loading = LoadingDialog.initDialog(this, true)
        }
        initCommonConfig()
        initView()
        bindListener()
        initData()
        start()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        loading = null
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        CommonUtil.fixInputMethodManagerLeak(this)
    }
}