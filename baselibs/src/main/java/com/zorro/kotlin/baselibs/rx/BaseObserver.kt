package com.zorro.kotlin.baselibs.rx

import com.zorro.kotlin.baselibs.bean.BaseBean
import com.zorro.kotlin.baselibs.http.HttpStatus
import com.zorro.kotlin.baselibs.http.exception.ExceptionHandle
import com.zorro.kotlin.baselibs.mvp.IView
import com.zorro.kotlin.baselibs.utils.NetWorkUtil
import io.reactivex.observers.ResourceObserver

/**
 * Created by Zorro on 2018/6/11.
 */
abstract class BaseObserver<T : BaseBean> : ResourceObserver<T> {

    private var mView: IView? = null
    private var mErrorMsg = ""
    private var bShowLoading = true

    constructor(view: IView) {
        this.mView = view
    }

    constructor(view: IView, bShowLoading: Boolean) {
        this.mView = view
        this.bShowLoading = bShowLoading
    }

    /**
     * 成功的回调
     */
    protected abstract fun onSuccess(t: T)

    /**
     * 错误的回调
     */
    protected fun onError(t: T) {}

    override fun onStart() {
        super.onStart()
        if (bShowLoading) mView?.showLoading()
        if (!NetWorkUtil.isConnected()) {
            mView?.showError("当前网络不可用，请检查网络设置")
            onComplete()
        }
    }

    override fun onNext(t: T) {
        mView?.hideLoading()
        when {
            t.code == HttpStatus.SUCCESS -> onSuccess(t)
            t.code == HttpStatus.TOKEN_INVALID -> {
                // TODO Token 过期，重新登录
                mView?.tokenInvalid(t.msg)
            }
            else -> {
                onError(t)
                mView?.showError(t.msg)
            }
        }
    }

    override fun onError(e: Throwable) {
        mView?.hideLoading()
        if (mView == null) {
            throw RuntimeException("mView can not be null")
        }
        if (mErrorMsg.isEmpty()) {
            mErrorMsg = ExceptionHandle.handleException(e)
        }
        mView?.showError(mErrorMsg)
    }

    override fun onComplete() {
        mView?.hideLoading()
    }
}

