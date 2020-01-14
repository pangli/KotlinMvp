package com.zorro.kotlin.samples.bean

import com.squareup.moshi.Json
import com.zorro.kotlin.baselibs.bean.BaseBean

/**
 * @author Zorro
 * @date 2018/11/21
 * @desc
 */

data class HttpResult<T>(
        @Json(name = "result") val result: T
) : BaseBean()

