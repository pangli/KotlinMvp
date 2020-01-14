package com.zorro.kotlin.samples.mvp.model


import com.zorro.kotlin.baselibs.mvp.BaseModel
import com.zorro.kotlin.samples.bean.HttpResult
import com.zorro.kotlin.samples.bean.UserInfo
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.mvp.contract.MainContract
import com.zorro.kotlin.samples.network.MainRetrofit
import io.reactivex.Observable

/**
 * @author Zorro
 * @date 2020/1/2
 * @desc 主界面
 */
class MainModel : BaseModel(), MainContract.Model {
    override fun getUserInfo(): Observable<HttpResult<UserInfo>> {
        return MainRetrofit.service.getUserInfo(Constant.token, Constant.token)
    }
}