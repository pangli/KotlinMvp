package com.zorro.kotlin.samples.network


import com.zorro.kotlin.samples.bean.*
import com.zorro.kotlin.samples.constant.Constant
import io.reactivex.Observable
import retrofit2.http.*


interface MainApi {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("saagent/auth")
    fun login(@Field("email") email: String, @Field("password") password: String, @Field("verifyToken") verifyToken: String): Observable<HttpResult<String>>

    /**
     * 换取用户信息
     */
    @GET("saagent/{token}")
    fun getUserInfo(@Header("x-access-token") header: String, @Path("token") token: String): Observable<HttpResult<UserInfo>>

    /**
     * 保险公司列表
     */
    @GET("supplier")
    fun getSupplier(@Header("x-access-token") header: String? = Constant.token): Observable<HttpResult<MutableList<SupplierData>>>

    /**
     * 获取通用资料、产品库列表数据
     */
    @FormUrlEncoded
    @POST("database/files/list")
    fun getDatabaseFileList(@Header("x-access-token") header: String? = Constant.token, @FieldMap fieldMap: Map<String, String?>): Observable<HttpResult<DatabaseFiles>>

    /**
     * 产品列表
     */
    @GET("product/base/{supplier_id}")
    fun getProductList(@Header("x-access-token") header: String? = Constant.token, @Path("supplier_id") supplierId: String): Observable<HttpResult<List<ProductData>>>

}