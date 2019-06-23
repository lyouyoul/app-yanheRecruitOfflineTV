package com.yanhe.recruit.tv.server.gets;

import com.mx.mnyun.constent.URL;
import com.mx.mnyun.server.inf.HttpMethod;
import com.mx.mnyun.server.inf.HttpResult;
import com.mx.mnyun.server.type.BaseInput;
import com.mx.mnyun.server.type.data.CompanyDatas;
import com.mx.mnyun.server.type.result.CompanyResults;
import com.mx.mnyun.utils.Http;

import org.json.JSONObject;

/**
 * @author mx
 */
public class QueryCompanyBySeiId implements HttpMethod {
    @Override
    public void request (Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.QUERY_ALL_COMPANY_BY_SELID_URL),input.asParams(),
        new Http.ResponseHandler() {
            @Override
            public void onSuccess (int statusCode, String text) {

            }

            @Override
            public void onSuccess (JSONObject json) {
                CompanyResults rs   = new CompanyResults();
                CompanyDatas   data = new CompanyDatas();
                rs.setData(data);
                rs.parseJson(json);
                result.onResponse(rs);
            }
        });
    }
}
