package com.yanhe.recruit.tv.base;

import com.yanhe.recruit.tv.R;

/**
 * 信息配置
 */

public class BaseConfig {
    /**
     * app主颜色
     */
    private int appColor = R.color.base;
    /**
     * 加载框的gif图
     */
    private int loadingView = 0;
    /**
     * 网络请求成功返回的code码
     */
    private int code = 1;
    /**
     * APP图标
     */
    private int appLogo = R.drawable.ic_logo;
    /**
     * 图片加载中和加载失败显示的图
     */
    private int failPicture = R.drawable.ic_logo;

    public int getLoadingView() {
        return loadingView;
    }

    public BaseConfig setLoadingView(int loadingView) {
        this.loadingView = loadingView;
        return this;
    }

    public int getAppColor() {
        return appColor;
    }

    public BaseConfig setAppColor(int appColor) {
        this.appColor = appColor;
        return this;
    }

    public int getAppLogo() {
        return appLogo;
    }

    public BaseConfig setAppLogo(int appLogo) {
        this.appLogo = appLogo;
        return this;
    }

    public int getFailPicture() {
        return failPicture;
    }

    public BaseConfig setFailPicture(int failPicture) {
        this.failPicture = failPicture;
        return this;
    }

    public int getCode() {
        return code;
    }

    public BaseConfig setCode(int code) {
        this.code = code;
        return this;
    }
}
