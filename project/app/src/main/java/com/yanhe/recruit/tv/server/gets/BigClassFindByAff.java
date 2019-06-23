package com.yanhe.recruit.tv.server.gets;

import com.mx.mnyun.constent.URL;
import com.mx.mnyun.server.inf.HttpMethod;
import com.mx.mnyun.server.inf.HttpResult;
import com.mx.mnyun.server.type.BaseInput;
import com.mx.mnyun.server.type.data.Bigclassifys;
import com.mx.mnyun.server.type.result.BigClassFindByAffResult;
import com.mx.mnyun.utils.Http;

import org.json.JSONObject;

public class BigClassFindByAff implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.QUERY_BIG_CLASS_BY_AFF), input == null ? null : input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                BigClassFindByAffResult res = new BigClassFindByAffResult();
                res.setData(new Bigclassifys());
                res.parseJson(json);
                result.onResponse(res);
            }
        });
    }
}
