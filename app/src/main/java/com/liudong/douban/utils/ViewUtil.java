package com.liudong.douban.utils;

import android.content.res.Resources;

/**
 * Created by liudong on 2016/11/24.
 * 单位转换
 */

public class ViewUtil {

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static float dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
