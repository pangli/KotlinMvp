package com.zorro.kotlin.samples.ui.base

import android.support.annotation.Size
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View

import com.orhanobut.logger.Logger
import com.squareup.moshi.Moshi
import com.zorro.kotlin.baselibs.base.BaseMvpFragment
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
 * 备注：权限初始化
 */
abstract class ZorroBaseMvpFragment<in V : IView, P : IPresenter<V>> : BaseMvpFragment<V, P>(),
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


    override fun initCommonConfig() {
        refreshUserInfo()
    }

    /**
     * 权限检测
     *
     * @param perms
     * @return
     */
    fun hasPermissions(@Size(min = 1) vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(context!!, *perms)
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
        ActivityHelper.jumpToLoginActivity(activity!!)
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
        return LayoutInflater.from(context).inflate(R.layout.empty_data_layout, recyclerView, false)
    }

    override fun onResume() {
        super.onResume()
        Logger.d("onPageStart---" + this.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        Logger.d("onPageEnd---" + this.javaClass.simpleName)
    }

    override fun onStop() {
        Logger.d("onStop---" + this.javaClass.simpleName)
        super.onStop()
    }

    override fun onDestroyView() {
        Logger.d("onDestroyView---" + this.javaClass.simpleName)
        super.onDestroyView()
    }

    override fun onDestroy() {
        Logger.d("onDestroy---" + this.javaClass.simpleName)
        super.onDestroy()
    }
}