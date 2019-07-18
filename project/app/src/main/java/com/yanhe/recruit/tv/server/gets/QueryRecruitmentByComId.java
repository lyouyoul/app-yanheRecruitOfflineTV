package com.yanhe.recruit.tv.server.gets;

import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.server.inf.HttpMethod;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.BaseInput;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.utils.Http;

import org.json.JSONObject;

/**
 * 根据企业id获得招聘信息
 * @author mx
 */
public class QueryRecruitmentByComId implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.QUERY_RECRUITMENT_BY_COM_ID), input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                CompanyResult rs = new CompanyResult();
                rs.parseJson(json);
                result.onResponse(rs);
            }
        });
    }
}
