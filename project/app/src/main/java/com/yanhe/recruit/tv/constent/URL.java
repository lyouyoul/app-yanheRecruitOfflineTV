package com.yanhe.recruit.tv.constent;

public class URL {
    public static final String API_URL = "";

    // 服务器端app版本文件存放路径
    public static final String APP_VER_URL = "";

    public static String getUrl(String relativeUrl) {
        return API_URL + relativeUrl;
    }
}
