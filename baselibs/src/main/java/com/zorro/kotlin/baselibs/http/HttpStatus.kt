package com.zorro.kotlin.baselibs.http

/**
 * Created by Zorro on 2018/4/21.
 */
object HttpStatus {
    /**
     * 响应成功
     */
    const val SUCCESS = 200

    /**
     * Token 过期
     */
    const val TOKEN_INVALID = 401

    /**
     * 未知错误
     */
    const val UNKNOWN_ERROR = 1002

    /**
     * 服务器内部错误
     */
    const val SERVER_ERROR = 1003

    /**
     * 网络连接超时
     */
    const val NETWORK_ERROR = 1004

    /**
     * API解析异常（或者第三方数据结构更改）等其他异常
     */
    const val API_ERROR = 1005
}