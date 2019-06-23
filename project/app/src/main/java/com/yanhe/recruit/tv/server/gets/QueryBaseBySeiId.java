package com.yanhe.recruit.tv.server.gets;

import com.mx.mnyun.constent.URL;
import com.mx.mnyun.server.inf.HttpMethod;
import com.mx.mnyun.server.inf.HttpResult;
import com.mx.mnyun.server.type.BaseInput;
import com.mx.mnyun.server.type.data.Bases;
import com.mx.mnyun.server.type.result.MyShowBaseResults;
import com.mx.mnyun.utils.Http;

import org.json.JSONObject;

/**
 * @author zhl
 */
public class QueryBaseBySeiId implements HttpMethod {
    @Override
    public void request (Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.QUERY_ALL_BASE_BY_SELID_URL), input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess (int statusCode, String text) {

            }

            @Override
            public void onSuccess (JSONObject json) {
                MyShowBaseResults rs   = new MyShowBaseResults();
                Bases             data = new Bases();
                rs.setData(data);
                rs.parseJson(json);
                result.onResponse(rs);
            }
        });
    }
}
