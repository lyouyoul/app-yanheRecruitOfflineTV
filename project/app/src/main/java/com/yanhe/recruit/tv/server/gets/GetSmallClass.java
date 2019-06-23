package com.yanhe.recruit.tv.server.gets;

import com.mx.mnyun.constent.URL;
import com.mx.mnyun.server.inf.HttpMethod;
import com.mx.mnyun.server.inf.HttpResult;
import com.mx.mnyun.server.type.BaseInput;
import com.mx.mnyun.server.type.data.ClassifyKinds;
import com.mx.mnyun.server.type.result.GetSmallClassResult;
import com.mx.mnyun.utils.Http;

import org.json.JSONObject;

public class GetSmallClass implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.GET_SMALL_CLASS_BY_PARENT), input == null ? null : input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                GetSmallClassResult res = new GetSmallClassResult();
                res.setData(new ClassifyKinds());
                res.parseJson(json);
                result.onResponse(res);
            }
        });
    }
}
