package com.zorro.kotlin.baselibs.imageloader


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.zorro.kotlin.baselibs.R
import java.io.File
import java.util.concurrent.ExecutionException


/**
 * @Author Zorro
 * @Date 2019/1/3
 * @Description 图片加载类
 */
object ImageLoaderKotlin {
    /**
     * @param context   Any context, will not be retained.
     * @param imageView
     * @param url       图片地址
     * @Description 图片加载
     * @see .
     * @see .
     * @see .
     * @see .
     */
    fun displayImage(context: Context?, imageView: ImageView, url: String?) {
        GlideApp.with(context!!)
            .load(url)//图片地址
            .into(imageView)
    }

    fun displayImage(
        context: Context?,
        imageView: ImageView,
        url: String?,
        defaultResourceId: Int
    ) {
        GlideApp.with(context!!)
            .load(url)//图片地址
            .placeholder(defaultResourceId)
            .error(defaultResourceId)
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 图片加载---居中裁剪并加圆角
     */
    fun displayImageCenterCrop(context: Context?, imageView: ImageView, radius: Int, url: String?) {
        GlideApp.with(context!!)
            .load(url)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(radius)
                    )
                )
            )//圆角
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param radius
     * @param url
     * @param errorResourceId 错误图片
     * @Description 图片加载---居中裁剪并加圆角
     */
    fun displayImageCenterCrop(
        context: Context?,
        imageView: ImageView,
        radius: Int,
        url: String?,
        errorResourceId: Int
    ) {
        GlideApp.with(context!!)
            .load(url)
            .placeholder(errorResourceId)
            .error(errorResourceId)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(radius)
                    )
                )
            )//圆角
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param radius
     * @param resourceId
     * @param errorResourceId 错误图片
     * @Description 图片加载---居中裁剪并加圆角
     */
    fun displayImageCenterCrop(
        context: Context?,
        imageView: ImageView,
        radius: Int,
        resourceId: Int,
        errorResourceId: Int
    ) {
        GlideApp.with(context!!)
            .load(resourceId)
            .placeholder(errorResourceId)
            .error(errorResourceId)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(radius)
                    )
                )
            )//圆角
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 图片加载---圆形用户图像
     */
    fun displayUserIcon(context: Context?, imageView: ImageView, url: String?) {
        GlideApp.with(context!!)
            .load(url)
            .circleCrop()//圆形
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param resourceId
     * @Description 图片加载---圆形用户图像
     */
    fun displayUserIcon(context: Context?, imageView: ImageView, resourceId: Int) {
        GlideApp.with(context!!)
            .load(resourceId)
            .circleCrop()//圆形
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 图片加载---加圆角图像
     */
    fun displayRoundCornerImage(
        context: Context?,
        imageView: ImageView,
        radius: Int,
        url: String?
    ) {
        GlideApp.with(context!!)
            .load(url)
            .centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radius)))//圆角
            .into(imageView)

    }

    /**
     * @param context
     * @param imageView
     * @param file
     * @Description 图片加载---File
     */
    fun displayImage(context: Context?, imageView: ImageView, file: File) {
        GlideApp.with(context!!)
            .load(file)
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param resourceId
     * @Description 图片加载---resourceId
     */
    fun displayImage(context: Context?, imageView: ImageView, resourceId: Int) {
        GlideApp.with(context!!)
            .load(resourceId)
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param uri
     * @Description 图片加载---Uri
     */
    fun displayImage(context: Context?, imageView: ImageView, uri: Uri) {
        GlideApp.with(context!!)
            .load(uri)
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @param errorResourceId 加载和加载错误占位图
     * @Description 图片加载---Uri
     */
    fun displayUserIcon(
        context: Context?,
        imageView: ImageView,
        url: String,
        errorResourceId: Int
    ) {
        GlideApp.with(context!!)
            .load(url)
            .placeholder(errorResourceId)
            .error(errorResourceId)
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation<Bitmap>(
                        CircleCropBorder(5f, ContextCompat.getColor(context, R.color.white))
                    )
                )
            )
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param resourceId
     * @Description 加载gif图片
     */
    fun displayGif(context: Context?, imageView: ImageView, resourceId: Int) {
        GlideApp.with(context!!)
            .asGif()
            .load(resourceId)
            .into(imageView)
    }

    /**
     * 此处只作为Glide返回本地文件File事例
     * 拓展用法
     *
     * @param context
     * @param imgUrl
     */
    private fun getImagerFile(context: Context?, imgUrl: String?) {
        Thread(Runnable {
            val futureTarget = GlideApp.with(context!!)
                .asFile()
                .load(imgUrl)
                .submit()
            try {
                val file = futureTarget.get()//得到图片下载后的file，然后可以自己去拿着它做别的事情了
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            Glide.with(context).clear(futureTarget)
        }).start()

    }

    /**
     * @param context
     * @param imageView
     * @param url
     * @Description 获取视频第一帧
     */
    fun displayVideoFrame(context: Context?, imageView: ImageView, url: String?) {
        GlideApp.with(context!!)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .centerCrop()
            )
            .load(url)
            .into(imageView)
    }

    /**
     * @param context
     * @param imageView
     * @param uri
     * @Description 获取视频第一帧
     */
    fun displayVideoFrame(context: Context?, imageView: ImageView, uri: Uri) {
        GlideApp.with(context!!)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(1000000)
                    .centerCrop()
            )
            .load(uri)
            .into(imageView)
    }

    /**
     * 根据图片地址获取bitmap
     */
    fun getBitmap(context: Context, width: Int, height: Int, url: String): Bitmap {
        return GlideApp.with(context).asBitmap().load(url).centerCrop().submit(width, height).get()
    }
    //    /**
    //     * 获取Glide造成的缓存大小
    //     *
    //     * @return CacheSize
    //     */
    //    public static String getCacheSize(Context context) {
    //        try {
    //            return DataCleanManager.getFormatSizeMB(DataCleanManager.getFolderSize(new File(context.getCacheDir() +
    //                    "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return "";
    //    }


}
