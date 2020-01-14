package com.zorro.kotlin.samples.app

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.zorro.kotlin.baselibs.base.BaseApplication
import com.zorro.kotlin.samples.BuildConfig
import com.zorro.kotlin.samples.R

/**
 * @author Zorro
 * @date 2018/11/21
 * @desc
 */
class App : BaseApplication() {
    companion object {
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setEnableHeaderTranslationContent(false)
                MaterialHeader(context).setColorSchemeResources(R.color.colorAccent)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(context)
            }
        }
    }

    override fun initAppConfigure() {
        instance = this
        initLogger()
    }

    /**
     * log初始化
     */
    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
            .methodCount(1)         // (Optional) How many method line to show. Default 2
            .tag("Wealth")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

}