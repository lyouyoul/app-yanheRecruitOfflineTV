package com.yanhe.recruit.tv.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 拨号
 */
public class CallUtils {
    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    public static void call(String phone, Context context) {
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        context.startActivity(intent);
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    public static void call1(String phone, Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
