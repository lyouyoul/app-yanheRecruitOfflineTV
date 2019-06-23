package com.yanhe.recruit.tv.utils;

import android.content.Context;

public class ScreenUtils {
    Context context;
    public ScreenUtils(Context context) {
        super();
        this.context = context;
    }
    public int dp2px(float dpValue) {
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
    public int px2dp(float pxValue) {
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
    public int sp2px(float spValue) {
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }
    public int px2sp(float pxValue) {
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }
}
