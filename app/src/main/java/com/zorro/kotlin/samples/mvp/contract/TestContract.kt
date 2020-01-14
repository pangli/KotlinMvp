package com.zorro.kotlin.samples.mvp.contract

import com.zorro.kotlin.baselibs.mvp.IModel
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView

/**
 * @author Zorro
 * @date 2018/12/1
 * @desc
 */
interface TestContract {

    interface View : IView {

    }

    interface Presenter : IPresenter<View> {

    }

    interface Model : IModel {

    }

}