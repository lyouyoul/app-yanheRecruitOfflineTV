package com.yanhe.recruit.tv.server.posts;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.server.inf.HttpMethod;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.BaseInput;
import com.yanhe.recruit.tv.server.type.data.CompanyData;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.utils.Http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 根据企业id获得招聘信息
 * @author mx
 */
public class QueryRecruitmentByRecruitIdAndCompanyId implements HttpMethod {
    @Override
    public void request(Http http, BaseInput input, final HttpResult result) {
        http.post(URL.getUrl(URL.QUERY_RECRUITMENT_BY_COMID), input.asParams(), new Http.ResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String text) {

            }

            @Override
            public void onSuccess(JSONObject json) {
                Logger.d("QueryRecruitmentByRecruitIdAndCompanyId json :%s",json);
                CompanyResult rs = new CompanyResult();
                CompanyData data = new CompanyData();
                try {
                    String context = json.getString("context");
                    Gson gson = new Gson();
                    data  = gson.fromJson(context,CompanyData.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rs.setContext(data);
                rs.parseJson(json);
                result.onResponse(rs);
            }
        });
    }
}
