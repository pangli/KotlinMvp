package com.zorro.kotlin.baselibs.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by Zorro on 2019/8/19 16:18
 * 备注： <读取Assets目录下的Json文件的工具类>
 */
public class GetAssetsJsonFileUtils {


    public String getAssetsFileToJson(Context context, String fileName) {
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader bf = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            is = assetManager.open(fileName);
            reader = new InputStreamReader(is);
            bf = new BufferedReader(reader);
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null) bf.close();
                if (reader != null) reader.close();
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}

