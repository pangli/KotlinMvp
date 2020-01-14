package com.zorro.kotlin.baselibs.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * A Glide {@link BitmapTransformation} to circle crop Border an image.  Behaves similar to a
 * {@link FitCenter} transform, but the resulting image is masked to a circle.
 *
 * <p> Uses a PorterDuff blend mode, see http://ssp.impulsetrain.com/porterduff.html. </p>
 */

/**
 * Created by Zorro on 2019/4/10 17:24
 * 备注：圆形加边框图片
 */
public class CircleCropBorder extends BitmapTransformation {


    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final int VERSION = 1;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.CircleCropBorder." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final float borderWidth;
    private final int borderColor;
    private Paint mBorderPaint;

    public CircleCropBorder(float borderWidth, @ColorInt int borderColor) {
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        mBorderPaint = new Paint();
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setDither(true);
    }

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int destWidth, int destHeight) {
        return circleCrop(pool, inBitmap);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CircleCropBorder;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    private Bitmap circleCrop(BitmapPool bitmapPool, Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap square = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        //画图
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        //设置 Shader
        paint.setShader(new BitmapShader(square, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float radius = size / 2f;
        //绘制一个圆
        canvas.drawCircle(radius, radius, radius, paint);
        /************************描边*********************/
        //注意：避免出现描边被屏幕边缘裁掉
        float borderRadius = radius - (borderWidth / 2);
        //画边框
        canvas.drawCircle(radius, radius, borderRadius, mBorderPaint);
        return result;
    }


}
