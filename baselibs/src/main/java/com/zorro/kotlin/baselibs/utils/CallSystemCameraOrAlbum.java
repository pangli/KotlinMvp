package com.zorro.kotlin.baselibs.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 注册manifest
 * <provider
 * android:name="android.support.v4.content.FileProvider"
 * android:authorities="${applicationId}.provider"
 * android:exported="false"
 * android:grantUriPermissions="true">
 * <meta-data
 * android:name="android.support.FILE_PROVIDER_PATHS"
 * android:resource="@xml/file_paths"/>
 * </provider>
 * <p>
 * 调用（需要先申请权限）
 * String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
 * String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
 *
 * @Override public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
 * super.onPermissionsGranted(requestCode, perms);
 * switch (requestCode) {
 * case 101:
 * cameraPath = CallSystemCameraOrAlbum.OpenCamera(this);
 * break;
 * case 102:
 * CallSystemCameraOrAlbum.OpenAlbum(this);
 * break;
 * default:
 * break;
 * }
 * }
 * 返回接收
 * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 * if (resultCode == RESULT_OK) {
 * switch (requestCode) {
 * case CallSystemCameraOrAlbum.CAMERA_REQUEST_CODE:
 * cropResultPath = CallSystemCameraOrAlbum.getCameraUri(this, cameraPath);
 * break;
 * case CallSystemCameraOrAlbum.GALLERY_REQUEST_CODE:
 * cropResultPath = CallSystemCameraOrAlbum.getAlbumUri(this, data);
 * break;
 * case CallSystemCameraOrAlbum.CROP_REQUEST_CODE:
 * ImageLoaderUtils.displayUserIcon(mContext, ivIcon, cropResultPath);
 * break;
 * default:
 * break;
 * }
 * }
 * super.onActivityResult(requestCode, resultCode, data);
 * }
 * Created by Zorro on 2019/1/15 11:50
 * 备注： 调用系统相册或相机拍照裁剪
 */
public class CallSystemCameraOrAlbum {
    public final static int CAMERA_REQUEST_CODE = 1;
    public final static int GALLERY_REQUEST_CODE = 2;
    public final static int CROP_REQUEST_CODE = 3;
    private static String path = "";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String EDITOR_PHOTO_NAME = "Club";

    /**
     * 调用系统相册
     *
     * @param activityCompat
     */
    public static void OpenAlbum(AppCompatActivity activityCompat) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activityCompat.startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
    }

    /**
     * 调用系统相机
     *
     * @param activityCompat
     * @return
     */
    public static File OpenCamera(AppCompatActivity activityCompat) {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        File cameraFile = createImageFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //判读版本是否在7.0以上
            takeIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(activityCompat, activityCompat.getPackageName() + ".provider",
                    cameraFile);
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, apkUri);
        } else {
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
        }
        activityCompat.startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
        return cameraFile;
    }

    /**
     * 拍照裁剪返回图片地址
     *
     * @param activityCompat
     * @param cameraFile
     * @return
     */
    public static String getCameraUri(AppCompatActivity activityCompat, File cameraFile, Boolean needCrop) {
        //用相机返回的照片去调用剪裁也需要对Uri进行处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(activityCompat, activityCompat.getPackageName() +
                    ".provider", cameraFile);
            return cropPhoto(activityCompat, contentUri,needCrop);
        } else {
            return cropPhoto(activityCompat, Uri.fromFile(cameraFile),needCrop);
        }
    }

    /**
     * 相册裁剪返回地址
     *
     * @param activityCompat
     * @param data
     * @return
     */
    public static String getAlbumUri(AppCompatActivity activityCompat, Intent data, Boolean needCrop) {
        Uri contentUri = data.getData();
        return cropPhoto(activityCompat, contentUri,needCrop);
    }

    /**
     * 调用系统裁剪方法
     *
     * @param activityCompat
     * @param uri
     * @return
     */
    private static String cropPhoto(AppCompatActivity activityCompat, Uri uri, Boolean needCrop) {
        String lastPictureName = savePhotoJpgPath();
        if (needCrop) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            //7.0以上权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(lastPictureName)));//定义输出的File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            activityCompat.startActivityForResult(intent, CROP_REQUEST_CODE);
        }
        return lastPictureName;
    }


    private static String initPath() {
        if (path.equals("")) {
            path = parentPath.getAbsolutePath() + File.separator + EDITOR_PHOTO_NAME;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        return path;
    }

    private static String savePhotoJpgPath() {
        return initPath() + File.separator + "avatar_" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * 注册manifest
     * <provider
     * android:name="android.support.v4.content.FileProvider"
     * android:authorities="${applicationId}.provider"
     * android:exported="false"
     * android:grantUriPermissions="true">
     * <meta-data
     * android:name="android.support.FILE_PROVIDER_PATHS"
     * android:resource="@xml/file_paths"/>
     * </provider>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * file_paths.xml
     * <paths>
     * <external-path
     * name="camera_photos"
     * path=""/>
     * </paths>
     * 注意file地址和file_paths中的external-path的对应关系
     *
     * @return
     */
    private static File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // Avoid joining path components manually
        File tempFile = new File(storageDir, imageFileName);
        // Handle the situation that user's external storage is not ready
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }
}
