package com.yanhe.recruit.tv.server;

import com.yanhe.recruit.tv.server.gets.QueryRecruitmentByComId;

/**
 * 网络请求类注册管理器
 * @author yangtxiang
 * @date 2019-05-13
 */
public class HttpRegister {
    public static void register () {
        HttpManager.getInstance().registerGet("QueryRecruitmentByComId", QueryRecruitmentByComId.class);
    }
}
