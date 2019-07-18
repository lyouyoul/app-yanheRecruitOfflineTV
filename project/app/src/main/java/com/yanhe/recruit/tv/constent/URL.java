package com.yanhe.recruit.tv.constent;

public class URL {
    //socket.io 地址
    public static final String SOCKET_HOST = "http://192.168.0.101:3000";


    public static final String API_URL = "http://192.168.0.101:3000/api/";

    // 服务器端app版本文件存放路径
    public static final String APP_VER_URL = "";

    public static final String QUERY_RECRUITMENT_BY_COM_ID = "getRecruitCompany";

    public static String getUrl(String relativeUrl) {
        return API_URL + relativeUrl;
    }
}
