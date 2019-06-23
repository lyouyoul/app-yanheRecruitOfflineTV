package com.yanhe.recruit.tv.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 获得适配屏幕的宽高工具类
 */
public class WidthHeightUtils {

    public static int getScreenWidth(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
