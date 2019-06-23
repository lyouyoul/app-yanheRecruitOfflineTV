package com.yanhe.recruit.tv.server.inf;

import com.yanhe.recruit.tv.server.type.BaseResult;

/**
 * 数据返回回调接口
 * @author yangtxiang
 * @date 2019-05-13
 */
public interface HttpResult<T extends BaseResult> {
    /**
     * 请求返回
     * @param result
     */
    void onResponse(T result);
}
