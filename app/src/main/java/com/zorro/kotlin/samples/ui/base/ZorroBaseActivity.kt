package com.zorro.kotlin.samples.ui.base

import android.graphics.Color
import android.support.annotation.Size
import android.view.View

import com.gyf.immersionbar.ImmersionBar
import com.zorro.kotlin.baselibs.base.BaseActivity
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Zorro on 2019/1/2.
 * 备注：沉浸式初始化
 */
abstract class ZorroBaseActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    override fun initCommonConfig() {
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
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
}
