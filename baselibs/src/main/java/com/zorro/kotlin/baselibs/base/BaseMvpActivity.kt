package com.zorro.kotlin.baselibs.base

import com.zorro.kotlin.baselibs.net.showToast
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView

/**
 * @author Zorro
 * @date 2018/11/19
 * @desc BaseMvpActivity
 */
abstract class BaseMvpActivity<in V : IView, P : IPresenter<V>> : BaseActivity(), IView {

    /**
     * Presenter
     */
    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun initView() {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        this.mPresenter = null
    }

    override fun showLoading() {
        if (loading != null && !loading!!.isShowing) {
            loading?.show()
        }
    }

    override fun hideLoading() {
        if (loading != null && loading!!.isShowing) {
            loading?.dismiss()
        }
    }

    override fun showError(errorMsg: String) {
        showToast(errorMsg)
    }

    override fun showDefaultMsg(msg: String) {
        showToast(msg)
    }

    override fun showMsg(msg: String) {
        showToast(msg)
    }

}