package com.zorro.kotlin.samples.mvp.contract


import com.zorro.kotlin.baselibs.mvp.IModel
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView
import com.zorro.kotlin.samples.bean.HttpResult
import com.zorro.kotlin.samples.bean.UserInfo
import io.reactivex.Observable


/**
 * @author Zorro
 * @date 2020/1/2
 * @desc 主界面
 */
interface MainContract {
    interface View : IView {
        fun httpResultUserInfo(userInfo: UserInfo)
    }

    interface Presenter : IPresenter<View> {
        fun getUserInfo()
    }

    interface Model : IModel {
        fun getUserInfo(): Observable<HttpResult<UserInfo>>
    }

}