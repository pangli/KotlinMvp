package com.zorro.kotlin.baselibs.net

import com.zorro.kotlin.baselibs.bean.BaseBean
import com.zorro.kotlin.baselibs.http.HttpStatus
import com.zorro.kotlin.baselibs.http.exception.ExceptionHandle
import com.zorro.kotlin.baselibs.http.function.RetryWithDelay
import com.zorro.kotlin.baselibs.mvp.IModel
import com.zorro.kotlin.baselibs.mvp.IView
import com.zorro.kotlin.baselibs.rx.SchedulerUtils
import com.zorro.kotlin.baselibs.utils.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author Zorro
 * @date 2018/11/20
 * @desc
 */
/**
 * @author Zorro
 * @date 2019/4/30
 * @desc Rx 可添加请求失败回调
 * <p>
 *     用法1
 *  Net(mModel, mView,
 *          onSuccess = { t ->
 *              mView?.showDuration(t.data)
 *          },
 *          onError = { t ->
 *             mView?.showDuration(t.data)
 *          })
 * </p>
 * <p>
 *     用法2 onError = null or 不传递onError时 onError = null
 *  Net(mModel, mView,
 *          onSuccess = { t ->
 *              mView?.showDuration(t.data)
 *          },
 *          onError = null)
 * </p>
 *
 */
fun <T : BaseBean> Observable<T>.net(
    model: IModel?,
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
) {
    this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) {
                    view?.showLoading()
                }
                model?.addDisposable(d)
                if (!NetWorkUtil.isConnected()) {
                    view?.showError("当前网络不可用，请检查网络设置")
                    d.dispose()
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                when (t.code) {
                    HttpStatus.SUCCESS -> onSuccess.invoke(t)
                    HttpStatus.TOKEN_INVALID -> {
                        // Token 过期，重新登录
                        view?.tokenInvalid(t.msg)
                    }
                    else -> {
                        onError?.invoke(t)
                        if (!t.msg.isNullOrBlank())
                            view?.showError(t.msg)
                    }
                }
            }

            override fun onError(t: Throwable) {
                view?.hideLoading()
                view?.showError(ExceptionHandle.handleException(t))
            }
        })
}

fun <T : BaseBean> Observable<T>.net2(
    view: IView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
): Disposable {
    if (isShowLoading) {
        view?.showLoading()
    }
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            when (it.code) {
                HttpStatus.SUCCESS -> onSuccess.invoke(it)
                HttpStatus.TOKEN_INVALID -> {
                    // Token 过期，重新登录
                    view?.tokenInvalid(it.msg)
                }
                else -> {
                    onError?.invoke(it)
                    if (!it.msg.isNullOrBlank())
                        view?.showError(it.msg)
                }
            }
            if (isShowLoading) {
                view?.hideLoading()
            }
        }, {
            view?.hideLoading()
            view?.showError(ExceptionHandle.handleException(it))
        })
}
