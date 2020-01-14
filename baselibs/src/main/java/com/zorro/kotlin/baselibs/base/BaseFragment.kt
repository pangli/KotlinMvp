package com.zorro.kotlin.baselibs.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zorro.kotlin.baselibs.widget.LoadingDialog
import org.greenrobot.eventbus.EventBus

/**
 * @author Zorro
 * @date 2018/11/19
 * @desc BaseFragment
 */
abstract class BaseFragment : Fragment() {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun attachLayoutRes(): Int

    /**
     * 初始化公共数据
     */
    abstract fun initCommonConfig()

    /**
     * 初始化数据
     */
    open fun initData() {}

    /**
     * 初始化 View
     */
    abstract fun initView(view: View)

    /**
     * 绑定监听 View
     */
    abstract fun bindListener(view: View)


    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    /**
     * 加载动画
     */
    protected var loading: LoadingDialog? = null

    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * 获取权限处理类
     */
    protected val rxPermissions: RxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(attachLayoutRes(), null)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus()) EventBus.getDefault().register(this)
        if (loading == null) {
            loading = LoadingDialog.initDialog(context!!, true)
        }
        isViewPrepare = true
        hasLoadData = false
        initCommonConfig()
        initView(view)
        bindListener(view)
        initData()
        lazyLoadDataIfPrepared()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    override fun onDestroyView() {
        loading = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) EventBus.getDefault().unregister(this)
    }
}