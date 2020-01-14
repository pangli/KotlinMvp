package com.zorro.kotlin.baselibs.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Zorro on 2019/10/25.
 * 备注：保存图片
 */
object TakePhotoUtils {


    /**
     * 保存图片
     */
    fun saveImage(bitmap: Bitmap, context: Context, isSaveAlbum: Boolean = false): String {
        val destFileDir = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().absolutePath + File.separatorChar + "ClubFile"
        } else context.cacheDir?.path + File.separatorChar + "ClubFile"
        val dir = File(destFileDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val destFileName = "webView_capture.jpg"
        val savePath = destFileDir + File.separatorChar + destFileName
        val fos = FileOutputStream(savePath)
        //压缩bitmap到输出流中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos)
        fos.close()
        //保存到相册
        if (isSaveAlbum) {
            saveImageToAlbum(bitmap, context)
        }
        bitmap.recycle()
        return savePath
    }

    /**
     *  保存图片到系统相册
     */
    fun saveImageToAlbum(bmp: Bitmap, context: Context) {
        val appDir = File(Environment.getExternalStorageDirectory(), "/DCIM/camera/")
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        //val fileName = System.currentTimeMillis().toString() + ".jpg"
        val fileName = "webView_screen_${System.currentTimeMillis()}.jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // 其次把文件插入到系统图库,会导致出现两张图片
        try {
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath, fileName, null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // 最后通知图库更新
//        context.sendBroadcast(
//            Intent(
//                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                Uri.parse("file://$fileName")
//            )
//        )
        // Toast.makeText(context, "已成功保存到相册", Toast.LENGTH_LONG).show()
    }

}