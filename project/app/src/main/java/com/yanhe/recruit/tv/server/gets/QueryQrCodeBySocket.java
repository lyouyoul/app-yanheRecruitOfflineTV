package com.yanhe.recruit.tv.server.gets;

import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.server.inf.HttpMethod;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.BaseInput;
import com.yanhe.recruit.tv.server.type.result.QrCodeUrlResult;
import com.yanhe.recruit.tv.utils.Http;

import org.json.JSONObject;

public class QueryQrCodeBySocket implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.get(URL.getUrl(URL.QUERY_QR_CODE_BY_SOCKET), input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                QrCodeUrlResult rs = new QrCodeUrlResult();
                rs.parseJson(json);
                result.onResponse(rs);
            }
        });
    }
}
