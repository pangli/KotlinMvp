package com.zorro.kotlin.samples.mvp.presenter

import com.zorro.kotlin.baselibs.mvp.BasePresenter
import com.zorro.kotlin.baselibs.net.net
import com.zorro.kotlin.samples.mvp.contract.MainContract
import com.zorro.kotlin.samples.mvp.model.MainModel


/**
 * @author Zorro
 * @date 2020/1/2
 * @desc 主界面
 */
class MainPresenter :
    BasePresenter<MainContract.Model, MainContract.View>(),
    MainContract.Presenter {

    override fun createModel(): MainContract.Model? = MainModel()

    override fun getUserInfo() {
        mModel?.getUserInfo()?.net(mModel, mView, onSuccess = {
            mView?.httpResultUserInfo(it.result)
        })
    }

}