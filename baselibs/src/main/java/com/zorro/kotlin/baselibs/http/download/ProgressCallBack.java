package com.zorro.kotlin.baselibs.http.download;


import com.orhanobut.logger.Logger;
import com.zorro.kotlin.baselibs.bus.RxBus;
import com.zorro.kotlin.baselibs.bus.RxSubscriptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Zorro on 2019/1/11.
 * 文件下载管理
 */

public abstract class ProgressCallBack<T> {

    private String destFileDir; // 本地文件存放路径/文件夹路径
    private String destFileName; // 文件名
    private File saveFile;//保存的文件路径
    private Disposable mSubscription;


    public String getDestFileDir() {
        return destFileDir;
    }

    public String getDestFileName() {
        return destFileName;
    }

    public File getSaveFile() {
        return saveFile;
    }

    public ProgressCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        subscribeLoadProgress();
    }

    public abstract void onSuccess(T t, File saveFilePath);

    public abstract void progress(long progress, long total, String tag);

    public void onStart() {
    }

    public void onCompleted() {
    }

    public abstract void onError(Throwable e);

    public void saveFile(ResponseBody body) throws Exception {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            saveFile = new File(dir, destFileName);
            fos = new FileOutputStream(saveFile);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } finally {
            if (is != null) is.close();
            if (fos != null) fos.close();
            unsubscribe();
            Logger.d("------------finally我执行了--------");
        }
    }

    /**
     * 订阅加载的进度条
     */
    private void subscribeLoadProgress() {
        mSubscription = RxBus.getDefault().toObservable(DownLoadStateBean.class)
                .observeOn(AndroidSchedulers.mainThread()) //回调到主线程更新UI
                .subscribe(new Consumer<DownLoadStateBean>() {
                    @Override
                    public void accept(final DownLoadStateBean progressLoadBean) {
                        progress(progressLoadBean.getBytesLoaded(), progressLoadBean.getTotal(), progressLoadBean.getTag());
                    }
                });
        //将订阅者加入管理站
        RxSubscriptions.add(mSubscription);
    }

    /**
     * 取消订阅，防止内存泄漏
     */
    public void unsubscribe() {
        RxSubscriptions.remove(mSubscription);
    }
}