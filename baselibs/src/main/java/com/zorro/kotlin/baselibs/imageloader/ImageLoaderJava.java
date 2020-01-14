package com.zorro.kotlin.baselibs.imageloader;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.concurrent.ExecutionException;


/**
 * @Author Zorro
 * @Date 2019/2/19
 * @Description 图片加载类
 */
public class ImageLoaderJava {
    /**
     * @param context   Any context, will not be retained.
     * @param imageView
     * @param url       图片地址
     * @Description 图片加载
     * @see #(android.app.Activity)
     * @see #(android.app.Fragment)
     * @see #(android.support.v4.app.Fragment)
     * @see #(android.support.v4.app.FragmentActivity)
     */
    public static void displayImage(Context context, ImageView imageView, String url) {
        GlideApp.with(context)
                .load(url)//图片地址
                .into(imageView);
    }

    public static void displayImage(Context context, ImageView imageView, String url, int defaultResourceId) {
        GlideApp.with(context)
                .load(url)//图片地址
                .placeholder(defaultResourceId)
                .error(defaultResourceId)
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 图片加载---居中裁剪并加圆角
     */
    public static void displayImageCenterCrop(Context context, ImageView imageView, int radius, String url) {
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(), new
                        RoundedCorners(radius))))//圆角
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param radius
     * @param url
     * @param errorResourceId 错误图片
     * @Description 图片加载---居中裁剪并加圆角
     */
    public static void displayImageCenterCrop(Context context, ImageView imageView, int radius, String url, int
            errorResourceId) {
        GlideApp.with(context)
                .load(url)
                .placeholder(errorResourceId)
                .error(errorResourceId)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(), new
                        RoundedCorners(radius))))//圆角
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 图片加载---圆形用户图像
     */
    public static void displayUserIcon(Context context, ImageView imageView, String url) {
        GlideApp.with(context)
                .load(url)
                .circleCrop()//圆形
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param resourceId
     * @Description 图片加载---圆形用户图像
     */
    public static void displayUserIcon(Context context, ImageView imageView, int resourceId) {
        GlideApp.with(context)
                .load(resourceId)
                .circleCrop()//圆形
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 图片加载---加圆角图像
     */
    public static void displayRoundCornerImage(Context context, ImageView imageView, int radius, String url) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))//圆角
                .into(imageView);

    }

    /**
     * @param context
     * @param imageView
     * @param file
     * @Description 图片加载---File
     */
    public static void displayImage(Context context, ImageView imageView, File file) {
        GlideApp.with(context)
                .load(file)
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param resourceId
     * @Description 图片加载---resourceId
     */
    public static void displayImage(Context context, ImageView imageView, int resourceId) {
        GlideApp.with(context)
                .load(resourceId)
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param uri
     * @Description 图片加载---Uri
     */
    public static void displayImage(Context context, ImageView imageView, Uri uri) {
        GlideApp.with(context)
                .load(uri)
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @param errorResourceId 加载和加载错误占位图
     * @Description 图片加载---Uri
     */
    public static void displayUserIcon(Context context, ImageView imageView, String url, int errorResourceId) {
        GlideApp.with(context)
                .load(url)
                .placeholder(errorResourceId)
                .error(errorResourceId)
                .circleCrop()//圆形
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param resourceId
     * @Description 加载gif图片
     */
    public static void displayGif(Context context, ImageView imageView, int resourceId) {
        GlideApp.with(context)
                .asGif()
                .load(resourceId)
                .into(imageView);
    }

    /**
     * 此处只作为Glide返回本地文件File事例
     * 拓展用法
     *
     * @param context
     * @param imgUrl
     */
    private static void getImagerFile(final Context context, final String imgUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> futureTarget =
                        GlideApp.with(context)
                                .asFile()
                                .load(imgUrl)
                                .submit();
                try {
                    File file = futureTarget.get();//得到图片下载后的file，然后可以自己去拿着它做别的事情了
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Glide.with(context).clear(futureTarget);
            }
        }).start();

    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 获取视频第一帧
     */
    public static void displayVideoFrame(Context context, ImageView imageView, String url) {
        GlideApp.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop())
                .load(url)
                .into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param uri
     * @Description 获取视频第一帧
     */
    public static void displayVideoFrame(Context context, ImageView imageView, Uri uri) {
        GlideApp.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop())
                .load(uri)
                .into(imageView);
    }

    /**
     * 构建图片请求添加header的 GlideUrl
     *
     * @param url
     * @param auth
     * @param sign
     * @return
     */
    public static GlideUrl buildGlideUrl(String url, String auth, String sign) {
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("auth", auth)
                .addHeader("sign", sign)
                .build());
//               //异步添加头部
//               .addHeader("sign", new LazyHeaderFactory() {
//                   @Override
//                   public String buildHeader() {
//                       String expensiveAuthHeader = computeExpensiveAuthHeader();
//                       return expensiveAuthHeader;
//                   }
//               })

    }

    /**
     * 圆形图片加载并添加header
     *
     * @param context
     * @param imageView
     * @param glideUrl
     * @param errorResourceId
     */
    public static void displayUserIcon(Context context, ImageView imageView, GlideUrl glideUrl, int errorResourceId) {
        GlideApp.with(context)
                .load(glideUrl)
                .placeholder(errorResourceId)
                .error(errorResourceId)
                .circleCrop()//圆形
                .into(imageView);
    }

    /**
     * 居中裁剪加圆角图片加载并添加header
     *
     * @param context
     * @param imageView
     * @param glideUrl
     * @param radius
     */
    public static void displayImage(Context context, ImageView imageView, GlideUrl glideUrl, int radius) {
        GlideApp.with(context)
                .load(glideUrl)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(), new
                        RoundedCorners(radius))))//圆角
                .into(imageView);
    }

    /**
     * 居中裁剪图片加载并添加header
     *
     * @param context
     * @param imageView
     * @param glideUrl
     */
    public static void displayImage(Context context, ImageView imageView, GlideUrl glideUrl) {
        GlideApp.with(context)
                .load(glideUrl)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop())))//居中裁剪
                .into(imageView);
    }

    /**
     * 圆形加边框图片加载并添加header
     *
     * @param context
     * @param imageView
     * @param glideUrl
     * @param borderWidth
     * @param borderColor
     * @param errorResourceId
     */
    public static void displayUserIcon(Context context, ImageView imageView, GlideUrl glideUrl, float borderWidth, @ColorInt int borderColor, int errorResourceId) {
        GlideApp.with(context)
                .load(glideUrl)
                .placeholder(errorResourceId)
                .error(errorResourceId)
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new
                        CircleCropBorder(borderWidth, borderColor))))
                .into(imageView);
    }
}
