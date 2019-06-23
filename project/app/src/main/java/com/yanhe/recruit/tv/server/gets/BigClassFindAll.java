package com.yanhe.recruit.tv.server.gets;

import com.mx.mnyun.constent.URL;
import com.mx.mnyun.server.inf.HttpMethod;
import com.mx.mnyun.server.inf.HttpResult;
import com.mx.mnyun.server.type.BaseInput;
import com.mx.mnyun.server.type.data.Bigclassifys;
import com.mx.mnyun.server.type.result.BigClassFindAllResult;
import com.mx.mnyun.utils.Http;

import org.json.JSONObject;

public class BigClassFindAll implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.QUERY_BIG_CLASS_ALL), input == null ? null : input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                BigClassFindAllResult res = new BigClassFindAllResult();
                res.setData(new Bigclassifys());
                res.parseJson(json);
                result.onResponse(res);
            }
        });
    }
}
