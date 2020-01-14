package com.zorro.kotlin.baselibs.coroutines

import kotlinx.coroutines.*

/**
 * Created by Zorro on 2019/11/20.
 * 备注：协程实现定时任务
 */
object Run {

    fun onUiInterval(interval: Interval, period: Long): Job {
        return GlobalScope.launch(Dispatchers.Main) {
            var index = 0
            try {
                while (isActive) {
                    val data = withContext(Dispatchers.IO) {
                        delay(period)
                        interval.tick(index++)
                        index
                    }
                }
            } finally {
                interval.cancel()
            }
        }
    }
}