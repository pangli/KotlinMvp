package com.zorro.kotlin.samples.mvp.contract


import com.zorro.kotlin.baselibs.mvp.IModel
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView
import com.zorro.kotlin.samples.bean.HttpResult
import com.zorro.kotlin.samples.bean.UserInfo
import io.reactivex.Observable


/**
 * @author Zorro
 * @date 2019/12/23
 * @desc 登录
 */
interface LoginContract {

    interface View : IView {
        /**
         * 登录
         */
        fun setLoginResult(userInfo: UserInfo)
    }

    interface Presenter : IPresenter<View> {
        fun login(email: String, password: String, verifyToken: String)
    }

    interface Model : IModel {
        fun login(
            email: String,
            password: String,
            verifyToken: String
        ): Observable<HttpResult<String>>

        fun getUserInfo(token: String): Observable<HttpResult<UserInfo>>
    }

}