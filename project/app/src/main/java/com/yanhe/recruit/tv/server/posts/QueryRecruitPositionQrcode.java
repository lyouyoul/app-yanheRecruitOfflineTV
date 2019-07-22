package com.yanhe.recruit.tv.server.posts;

import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.server.inf.HttpMethod;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.BaseInput;
import com.yanhe.recruit.tv.server.type.data.CompanyData;
import com.yanhe.recruit.tv.server.type.data.QueryQrcodeData;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.server.type.result.QueryQrcodeResult;
import com.yanhe.recruit.tv.utils.Http;

import org.json.JSONObject;

public class QueryRecruitPositionQrcode implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.post(URL.getUrl(URL.QUERY_RECRUIT_POSITION_QRCODE), input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                QueryQrcodeResult rs = new QueryQrcodeResult();
                QueryQrcodeData data = new QueryQrcodeData();
                rs.setContext(data);
                rs.parseJson(json);
                result.onResponse(rs);
            }
        });
    }
}
