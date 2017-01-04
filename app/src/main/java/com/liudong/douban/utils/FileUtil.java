package com.liudong.douban.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by liudong on 2017/1/2.
 * 文件管理工具类
 */

public class FileUtil {

    /**
     * 判断SD卡可用
     */
    private static boolean hasSDCardMounted() {
        String state = Environment.getExternalStorageState();
        return state != null && state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 头像裁剪后存放路径
     */
    public static Uri getSavePictureUri(Context context) {
        if (hasSDCardMounted()) {
            File outDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdir();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            return Uri.fromFile(outFile);
        } else {
            Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 拍照取头像，文件存放路径
     */
    public static Uri getCameraPictureUri(Context context) {
        if (hasSDCardMounted()) {
            File cacheDir = context.getExternalCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            File file = new File(cacheDir, "temp.jpg");
            return Uri.fromFile(file);
        } else {
            Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
