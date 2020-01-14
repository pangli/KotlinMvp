package com.zorro.kotlin.baselibs.http.download;


import com.zorro.kotlin.baselibs.http.interceptor.ProgressInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Zorro on 2019/1/11.
 * 文件下载管理，封装一行代码实现下载
 */

public class DownLoadManager {
    private static DownLoadManager instance;

    private static Retrofit retrofit;

    private DownLoadManager() {
        buildNetWork();
    }

    /**
     * 单例模式
     *
     * @return DownLoadManager
     */
    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * 下载
     *
     * @param downUrl  下载地址
     * @param callBack 回调
     * @return DownLoadSubscriber 支持 DownLoadSubscriber.dispose()取消下载请求
     */
    public DownLoadSubscriber<ResponseBody> load(String downUrl, final ProgressCallBack callBack) {
        final DownLoadSubscriber<ResponseBody> downLoadSubscriber = new DownLoadSubscriber<ResponseBody>(callBack);
        retrofit.create(ApiService.class)
                .download(downUrl)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .observeOn(Schedulers.io()) //指定线程保存文件
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callBack.saveFile(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //在主线程中更新ui
                .subscribe(downLoadSubscriber);
        return downLoadSubscriber;
    }


    /**
     * @param downUrl  下载地址
     * @param headers  下载请求头信息
     * @param callBack 回调
     * @return DownLoadSubscriber 支持 DownLoadSubscriber.dispose()取消下载请求
     */
    public DownLoadSubscriber<ResponseBody> load(String downUrl, Map<String, String> headers, final ProgressCallBack callBack) {
        final DownLoadSubscriber<ResponseBody> downLoadSubscriber = new DownLoadSubscriber<ResponseBody>(callBack);
        retrofit.create(ApiService.class)
                .download(downUrl, headers)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .observeOn(Schedulers.io()) //指定线程保存文件
                .doOnNext(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        callBack.saveFile(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //在主线程中更新ui
                .subscribe(downLoadSubscriber);
        return downLoadSubscriber;
    }

    private void buildNetWork() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new ProgressInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://www.zorro.com/")
                .build();
    }

    private interface ApiService {
        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url);

        @Streaming
        @GET
        Observable<ResponseBody> download(@Url String url, @HeaderMap Map<String, String> headers);
    }
}
