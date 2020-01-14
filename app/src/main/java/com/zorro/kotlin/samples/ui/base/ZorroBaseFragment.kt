package com.zorro.kotlin.samples.ui.base
import android.support.annotation.Size

import com.orhanobut.logger.Logger
import com.zorro.kotlin.baselibs.base.BaseFragment
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by Zorro on 2019/1/2.
 * 备注：权限初始化
 */
abstract class ZorroBaseFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    /**
     * 权限检测
     *
     * @param perms
     * @return
     */
    fun hasPermissions(@Size(min = 1) vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(context!!, *perms)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

    override fun onResume() {
        super.onResume()
        Logger.d("onPageStart---" + this.javaClass.simpleName)
        //MobclickAgent.onPageStart(this.javaClass.simpleName) //统计页面(this.javaClass.simpleName为页面名称，可自定义)
    }

    override fun onPause() {
        super.onPause()
        Logger.d("onPageEnd---" + this.javaClass.simpleName)
        //MobclickAgent.onPageEnd(this.javaClass.simpleName) //统计页面(this.javaClass.simpleName为页面名称，可自定义)
    }
}