package com.zorro.kotlin.baselibs.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import com.orhanobut.logger.Logger
import com.zorro.kotlin.baselibs.utils.AppManagerDelegate
import kotlin.properties.Delegates

/**
 * @author Zorro
 * @date 2018/11/18
 * @desc BaseApp
 */
abstract class BaseApplication : Application() {


    companion object {
        var instance: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        initAppConfigure()
    }

    abstract fun initAppConfigure()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            Logger.d("onCreated: " + activity.componentName.className)
            AppManagerDelegate.getInstance().addActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            Logger.d("onStart: " + activity.componentName.className)
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            Logger.d("onDestroy: " + activity.componentName.className)
            AppManagerDelegate.getInstance().finishActivity(activity)
        }
    }
}