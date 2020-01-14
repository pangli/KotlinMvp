package com.zorro.kotlin.baselibs.http.interceptor;

import com.orhanobut.logger.Logger;
import com.zorro.kotlin.baselibs.http.download.ProgressResponseBody;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Zorro on 2019/1/11.
 * 备注：下载进度拦截器
 */

public class ProgressInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);
        String requestUrl = request.url().toString();
        Logger.d("下载标记:" + requestUrl);
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), requestUrl))
                .build();
    }
}
