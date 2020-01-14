package com.zorro.kotlin.baselibs.http.download;

import com.orhanobut.logger.Logger;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Zorro on 2019/1/11.
 * 文件下载管理
 */
public class DownLoadSubscriber<T> extends DisposableObserver<T> {
    private ProgressCallBack fileCallBack;

    public DownLoadSubscriber(ProgressCallBack fileCallBack) {
        this.fileCallBack = fileCallBack;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (fileCallBack != null) {
            fileCallBack.onStart();
            Logger.d("下载开始");
        }
    }

    @Override
    public void onComplete() {
        if (fileCallBack != null) {
            fileCallBack.onCompleted();
            Logger.d("下载结束");
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (fileCallBack != null) {
            fileCallBack.onError(e);
            Logger.d("下载失败:" + e.getMessage());
        }
    }

    @Override
    public void onNext(@NonNull T t) {
        if (fileCallBack != null) {
            fileCallBack.onSuccess(t, fileCallBack.getSaveFile());
            try {
                Logger.d("下载成功:" + fileCallBack.getSaveFile().getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}