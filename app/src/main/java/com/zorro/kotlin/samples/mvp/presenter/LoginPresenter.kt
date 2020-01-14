package com.zorro.kotlin.samples.mvp.presenter

import com.zorro.kotlin.baselibs.mvp.BasePresenter
import com.zorro.kotlin.baselibs.net.net
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.mvp.contract.LoginContract
import com.zorro.kotlin.samples.mvp.model.LoginModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * @author Zorro
 * @date 2019/12/23
 * @desc 登录
 */
class LoginPresenter : BasePresenter<LoginContract.Model, LoginContract.View>(),
    LoginContract.Presenter {


    override fun createModel(): LoginContract.Model? = LoginModel()
    override fun login(email: String, password: String, verifyToken: String) {
        mModel?.login(email, password, verifyToken)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.filter {
                if (it.code == 200) {
                    return@filter true
                } else {
                    mView?.showError(it.msg)
                    return@filter false
                }
            }?.observeOn(Schedulers.io())?.flatMap {
                Constant.token = it.result
                mModel?.getUserInfo(it.result)
            }?.net(mModel, mView, onSuccess = {
                mView?.setLoginResult(it.result)
            })
    }


}