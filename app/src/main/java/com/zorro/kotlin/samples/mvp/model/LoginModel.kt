package com.zorro.kotlin.samples.mvp.model


import com.zorro.kotlin.baselibs.mvp.BaseModel
import com.zorro.kotlin.samples.bean.HttpResult
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.mvp.contract.LoginContract
import com.zorro.kotlin.samples.network.MainRetrofit
import io.reactivex.Observable

/**
 * @author Zorro
 * @date 2019/12/23
 * @desc 登录
 */
class LoginModel : BaseModel(), LoginContract.Model {
    override fun login(
        email: String,
        password: String,
        verifyToken: String
    ): Observable<HttpResult<String>> {
        return MainRetrofit.service.login(email, password, verifyToken)
    }

    override fun getUserInfo(token: String): Observable<HttpResult<UserInfo>> {
        return MainRetrofit.service.getUserInfo(token, token)
    }

}