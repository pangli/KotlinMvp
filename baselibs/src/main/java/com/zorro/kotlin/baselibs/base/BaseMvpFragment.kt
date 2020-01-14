package com.zorro.kotlin.baselibs.base

import android.view.View
import com.zorro.kotlin.baselibs.net.showToast
import com.zorro.kotlin.baselibs.mvp.IPresenter
import com.zorro.kotlin.baselibs.mvp.IView

/**
 * @author Zorro
 * @date 2018/11/19
 * @desc BaseMvpFragment
 */
abstract class BaseMvpFragment<in V : IView, P : IPresenter<V>> : BaseFragment(), IView {

    /**
     * Presenter
     */
    protected var mPresenter: P? = null

    protected abstract fun createPresenter(): P

    override fun initView(view: View) {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroyView() {
        super.onDestroyView()
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