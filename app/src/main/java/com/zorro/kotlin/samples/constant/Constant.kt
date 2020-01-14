package com.zorro.kotlin.samples.constant

import com.zorro.kotlin.baselibs.utils.Preference
import com.zorro.kotlin.samples.BuildConfig


/**
 * @author Zorro
 * @date 2018/11/21
 * @desc
 */

object Constant {

    const val BASE_URL = BuildConfig.BASE_URL
    //每页显示条数
    const val PAGE_SIZE = 20
    const val INSURANCE_PAGE_SIZE = 10
    //主域名
    //人机验证vid
    const val VID = BuildConfig.VID
    //WebSocket地址
    const val WS_URL = BuildConfig.WS_URL
    //接口Header
    var token: String = ""

    //接口签名
    const val SIGN_KEY = BuildConfig.SIGN_KEY
    //Home tab fragment tag
    const val TAG_FRAGMENT_HOME = "FRAGMENT_HOME"
    const val TAG_FRAGMENT_DATABASE = "FRAGMENT_DATABASE"
    const val TAG_FRAGMENT_MINE = "FRAGMENT_MINE"


    //SharedPreferences key
    const val GUIDE_VERSION_NAME = BuildConfig.VERSION_NAME //用来存储对应的版本引导页是否展示过
    const val LOGIN_KEY = "login"
    const val TOKEN_KEY = "token"
    const val USER_INFO_KEY = "userBaseInfo"

    const val AGREEMENT = "agreenment"
    const val HISTORY_SEARCH = "history_search"
    const val VERSION_KEY = "version"

    //计划书业务
    const val PLAN_BASE_URL = BuildConfig.PLAN_BASE_URL

    const val PLAN_SIGN_KEY = BuildConfig.PLAN_SIGN_KEY

    //清除部分用户SharedPreferences数据
    fun clearUserPreference() {
//        Preference.clearPreference(GUIDE_VERSION_NAME)
        Preference.clearPreference(LOGIN_KEY)
        Preference.clearPreference(TOKEN_KEY)
        Preference.clearPreference(USER_INFO_KEY)
    }
}
