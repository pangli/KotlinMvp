package com.zorro.kotlin.baselibs.rx

import com.zorro.kotlin.baselibs.rx.scheduler.IoMainScheduler

/**
 * Created by Zorro on 2018/4/21.
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> = IoMainScheduler()

}