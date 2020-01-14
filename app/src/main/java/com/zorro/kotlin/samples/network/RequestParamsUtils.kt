package com.zorro.kotlin.samples.network

import com.bumptech.glide.load.model.GlideUrl
import com.orhanobut.logger.Logger
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.zorro.kotlin.baselibs.imageloader.ImageLoaderJava
import com.zorro.kotlin.baselibs.utils.AppUtils
import com.zorro.kotlin.baselibs.utils.Preference
import com.zorro.kotlin.samples.constant.Constant
import com.zorro.kotlin.samples.constant.Constant.GUIDE_VERSION_NAME
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.lang.reflect.ParameterizedType
import java.net.URLEncoder
import java.text.MessageFormat
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by Zorro on 2019/2/13.
 * 备注：请求参数json 转 RequestBody
 */
fun buildRequestBody(json: JSONObject?): RequestBody {
    return RequestBody.create(
        MediaType.parse("application/json; charset=utf-8"),
        json?.let { json.toString() }
            ?: JSONObject().toString())
}

/**
 * 文件上传MultipartBody.Part生成
 */
fun buildMultipartBody(filePath: String): MultipartBody.Part {
    val file = File(filePath)
    val body = RequestBody.create(MediaType.parse("multipart/form-data"), file)
    return MultipartBody.Part.createFormData("file", file.name, body)
}

/**
 * Get请求Header生成
 */
fun buildGetHeaderMap(
    params: TreeMap<String, Any?>?,
    domain: Domain? = Domain.DEFAULT
): Map<String, String> {
    val headers = HashMap<String, String>()
    headers["auth"] = getToken()
    headers["sign"] = buildGetParamsSign(params, domain)
    headers["source"] = "android"
    return headers
}

/**
 * Get请求Header生成
 */
fun buildVersionConfigHeaderMap(
    params: TreeMap<String, Any?>?,
    domain: Domain? = Domain.DEFAULT
): Map<String, String> {
    val headers = HashMap<String, String>()
    headers["vercon"] = GUIDE_VERSION_NAME
    headers["sign"] = buildGetParamsSign(params, domain)
    headers["source"] = "android"
    return headers
}

fun getToken(): String {
    val auth: String by Preference(Constant.TOKEN_KEY, "")
    return auth
}

/**
 * post请求Header生成
 */
fun buildPostHeaderMap(params: JSONObject?, domain: Domain? = Domain.DEFAULT): Map<String, String> {
    val headers = HashMap<String, String>()
    headers["auth"] = getToken()
    headers["sign"] = buildPostParamsSign(params, domain)
    headers["source"] = "android"
    return headers
}


/**
 * Get请求Sign生成
 */
fun buildGetParamsSign(params: TreeMap<String, Any?>?, domain: Domain? = Domain.DEFAULT): String {
    val currentTimeMillis = System.currentTimeMillis() / 1000
    val buffer = StringBuilder()
    if (params == null) {
        buffer.append("")
    } else {
        val it = params.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            buffer.append(entry.key)
            buffer.append("=")
            buffer.append(entry.value)
            if (it.hasNext()) {
                buffer.append("&")
            }
        }
    }
    when (domain) {
        Domain.DEFAULT -> buffer.append(Constant.SIGN_KEY)
        Domain.CIRCLE -> buffer.append(Constant.SIGN_KEY)
//        Domain.AUTH -> buffer.append(Constant.AUTH_SIGN_KEY)
        Domain.PLAN -> buffer.append(Constant.PLAN_SIGN_KEY)
    }
    buffer.append(currentTimeMillis)
    Logger.d(
        "Sign-----" + buffer.toString() + "------------" + currentTimeMillis.toString() + "-" + AppUtils.hexDigest(
            buffer.toString().toByteArray()
        )
    )
    return currentTimeMillis.toString() + "-" + AppUtils.hexDigest(buffer.toString().toByteArray())
}

/**
 * Post请求Sign生成
 */
fun buildPostParamsSign(params: JSONObject?, domain: Domain? = Domain.DEFAULT): String {
    val currentTimeMillis = System.currentTimeMillis() / 1000
    val buffer = StringBuilder()
    if (params == null) {
        buffer.append("{}")
    } else {
        buffer.append(params.toString())
    }
    when (domain) {
        Domain.DEFAULT -> buffer.append(Constant.SIGN_KEY)
        Domain.CIRCLE -> buffer.append(Constant.SIGN_KEY)
//        Domain.AUTH -> buffer.append(Constant.AUTH_SIGN_KEY)
        Domain.PLAN -> buffer.append(Constant.PLAN_SIGN_KEY)
    }
    buffer.append(currentTimeMillis)
    Logger.d(
        "Sign-----" + buffer.toString() + "------------" + currentTimeMillis.toString() + "-" + AppUtils.hexDigest(
            buffer.toString().toByteArray()
        )
    )
    return currentTimeMillis.toString() + "-" + AppUtils.hexDigest(buffer.toString().toByteArray())
}

/**
 * Get请求用户图像,Header生成并构建GlideUrl
 */
fun buildImageGlideUrl(host: String, url: String?, domain: Domain? = Domain.DEFAULT): GlideUrl {
    val map = TreeMap<String, Any?>()
    map["filePath"] = url
    return ImageLoaderJava.buildGlideUrl(
        MessageFormat.format(host, URLEncoder.encode(url)),
        getToken(),
        buildGetParamsSign(map, domain)
    )
}

/**
 * 对象bean转JSONObject
 */
fun <T> modelToJSONObject(type: Class<T>, value: T?): JSONObject {
    return JSONObject(Moshi.Builder().build().adapter(type).toJson(value))
}

/**
 * 对象bean转jsonString
 */
fun <T> modelToJsonString(type: Class<T>, value: T?): String {
    return Moshi.Builder().build().adapter(type).toJson(value)
}

/**
 * JSONObjectString转对象bean
 */
fun <T> jsonObjectToModel(type: Class<T>, jsonString: String): T? {
    return Moshi.Builder().build().adapter(type).fromJson(jsonString)
}

/**
 * JSONOArrayString转对象List bean
 */
fun <T> jsonArrayToListModel(type: Class<T>, jsonString: String): List<T>? {
    val moshi: Moshi = Moshi.Builder().build()
    val listOfCardsType: ParameterizedType = Types.newParameterizedType(List::class.java, type)
    val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(listOfCardsType)
    return jsonAdapter.fromJson(jsonString)
}

/**
 * JSONObject转map
 */
fun <T> jsonToMap(type: Class<T>, jsonString: String): Map<String, T>? {
    val moshi: Moshi = Moshi.Builder().build()
    val listOfCardsType: ParameterizedType = Types.newParameterizedType(
        Map::class.java,
        String::class.java, type
    )
    val jsonAdapter: JsonAdapter<Map<String, T>> = moshi.adapter(listOfCardsType)
    return jsonAdapter.fromJson(jsonString)
}

/**
 * List转json
 */
fun <T> listToJson(list: List<T>, type: Class<T>): String {
    val moshi = Moshi.Builder().build()
    val listOfCardsType: ParameterizedType = Types.newParameterizedType(List::class.java, type)
    val jsonAdapter: JsonAdapter<List<T>> = moshi.adapter(listOfCardsType)
    return jsonAdapter.toJson(list)
}

