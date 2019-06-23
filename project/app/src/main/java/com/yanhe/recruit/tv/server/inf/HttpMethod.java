package com.yanhe.recruit.tv.server.inf;

import com.yanhe.recruit.tv.server.type.BaseInput;
import com.yanhe.recruit.tv.utils.Http;

/**
 * 发送请求方法
 * @author yangtxiang
 */
public interface HttpMethod {
    /**
     * 发送请求
     * @param http
     * @param input
     * @param result
     */
    void request(Http http, BaseInput input, HttpResult result);
}
