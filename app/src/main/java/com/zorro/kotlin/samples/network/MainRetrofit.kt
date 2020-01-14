package com.zorro.kotlin.samples.network

import com.zorro.kotlin.baselibs.http.RetrofitFactory
import com.zorro.kotlin.samples.constant.Constant

/**
 * @author Zorro
 * @date 2018/11/21
 * @desc
 */
object MainRetrofit : RetrofitFactory<MainApi>() {

    override fun baseUrl(): String = Constant.BASE_URL

    override fun getService(): Class<MainApi> = MainApi::class.java

}