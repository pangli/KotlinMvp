package com.zorro.kotlin.baselibs.mvp

import io.reactivex.disposables.Disposable

/**
 * @author Zorro
 * @date 2018/11/18
 * @desc IModel
 */
interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}