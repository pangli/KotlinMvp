package com.zorro.kotlin.samples.ui.base

import android.graphics.Color
import android.support.annotation.Size
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.squareup.moshi.Moshi
import com.zorro.kotlin.baselibs.base.BaseMvpActivity
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView
import com.zorro.kotlin.baselibs.utils.AppManagerDelegate
import com.zorro.kotlin.baselibs.utils.Preference
import com.zorro.kotlin.samples.R
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.utils.ActivityHelper
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Zorro on 2019/1/2.
 * 备注：沉浸式初始化
 */
abstract class ZorroBaseMvpActivity<in V : IView, P : IPresenter<V>> : BaseMvpActivity<V, P>(),
    EasyPermissions.PermissionCallbacks {

    /**
     * check login
     */
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    /**
     * token
     */
    protected var token: String by Preference(Constant.TOKEN_KEY, "")
    /**
     * 用户信息json
     */
    protected var userInfoJson: String by Preference(Constant.USER_INFO_KEY, "")
    /**
     * 用户信息对象
     */
    protected var userInfo: UserInfo? = null

    protected var versionPreference: String by Preference(Constant.VERSION_KEY, "")

    override fun initCommonConfig() {
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
        refreshUserInfo()
    }

    protected fun initImmersionBar() {
        ImmersionBar.with(this).init()
    }

    open fun isImmersionBarEnabled(): Boolean {
        return true
    }


    /**
     * 设置StatusBar为黑色并实现沉浸式状态栏
     */
    fun setBlackStatusBar(toolbar: View) {
        ImmersionBar.with(this)
            .titleBar(toolbar)
            .statusBarDarkFont(true, 0.2f)
            .init()
    }

    /**
     * 设置StatusBar为黑色
     */
    fun setBlackStatusBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true, 0.2f)
            .init()
    }

    /**
     *设置StatusBar为白色
     */
    fun setWhiteStatusBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(false)
            .init()
    }

    /**
     * 解决白色状态栏问题和设置导航栏为白色
     */
    fun whiteImmersionBar(toolbar: View) {
        ImmersionBar.with(this)
            .titleBar(toolbar)
            .navigationBarColorInt(Color.WHITE)
            .statusBarDarkFont(true, 0.2f)
            .navigationBarDarkIcon(true, 0.2f)
            .init()
    }


    /**
     * 权限检测
     *
     * @param perms
     * @return
     */
    fun hasPermissions(@Size(min = 1) vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(this, *perms)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 申请成功时调用
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    /**
     * 申请失败时调用
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun tokenInvalid(errorMsg: String) {
        showMsg("登录已失效，请您重新登录")
        Constant.clearUserPreference()
        AppManagerDelegate.getInstance().finishAllActivity()
        ActivityHelper.jumpToLoginActivity(this)
    }

    /**
     * 刷新用户基础信息
     */
    fun refreshUserInfo() {
        if (!TextUtils.isEmpty(userInfoJson)) {
            userInfo = Moshi.Builder().build().adapter(UserInfo::class.java).fromJson(userInfoJson)
        }
    }

    /**
     * 添加空view
     */
    protected fun getEmptyView(recyclerView: RecyclerView): View {
        return LayoutInflater.from(this).inflate(R.layout.empty_data_layout, recyclerView, false)
    }
}