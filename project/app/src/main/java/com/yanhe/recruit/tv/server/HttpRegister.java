package com.yanhe.recruit.tv.server;

import com.yanhe.recruit.tv.server.posts.QueryCompanyByComId;
import com.yanhe.recruit.tv.server.posts.QueryRecruitPositionQrcode;
import com.yanhe.recruit.tv.server.posts.QueryRecruitmentByRecruitIdAndCompanyId;

/**
 * 网络请求类注册管理器
 * @author yangtxiang
 * @date 2019-05-13
 */
public class HttpRegister {
    public static void register () {
        HttpManager.getInstance().registerPost("QueryRecruitmentByRecruitIdAndCompanyId", QueryRecruitmentByRecruitIdAndCompanyId.class);

        HttpManager.getInstance().registerPost("QueryCompanyByComId", QueryCompanyByComId.class);

        HttpManager.getInstance().registerPost("QueryRecruitPositionQrcode", QueryRecruitPositionQrcode.class);
    }
}
