package com.zorro.kotlin.baselibs.coroutines

/**
 * Created by Zorro on 2019/11/20.
 * 备注：
 */
interface Interval {
    /**
     * @param index in [0,times) or (0 until times)
     */
    fun tick(index: Int)

    /**
     * on Job invoke like "cancel" methods
     */
    fun cancel()
}