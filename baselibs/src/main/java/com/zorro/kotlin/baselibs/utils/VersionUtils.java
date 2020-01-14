package com.zorro.kotlin.baselibs.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zorro on 2019/1/11.
 * 备注：获取apk版本信息和安装apk
 */
public class VersionUtils {
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 判断是否有新版本
     *
     * @param serviceVersionName
     * @param currentVersionName
     * @return
     */
    public static boolean isUpdateApk(String serviceVersionName, String currentVersionName) {
        String[] newVersions = serviceVersionName.split("\\.");
        String[] currentVersions = currentVersionName.split("\\.");
        for (int i = 0; i < newVersions.length; i++) {
            int newVer = Integer.valueOf(newVersions[i]);
            int currentVer = Integer.valueOf(currentVersions[i]);
            if (newVer > currentVer) {
                return true;
            }
        }
        return false;
    }


    /**
     * 安装APK文件
     * 注：8.0以上需要加入 <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES"/> 权限
     */
    public static void installApk(Context mContext, File saveFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", saveFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            try {
                //把要访问的文件加777权限 exec("chmod 777 filepath"); 设为全局的权限
                Runtime.getRuntime().exec("chmod 777 " + saveFile.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            intent.setDataAndType(Uri.fromFile(saveFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}
