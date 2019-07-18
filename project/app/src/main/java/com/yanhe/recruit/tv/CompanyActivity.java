package com.yanhe.recruit.tv;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.data.CompanyData;
import com.yanhe.recruit.tv.server.type.data.RecruitmentData;
import com.yanhe.recruit.tv.server.type.input.RecruitmentInput;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.utils.JsonUtils;
import com.yanhe.recruit.tv.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 企业招聘
 * @author mx
 */
public class CompanyActivity extends Activity {
    /**联系人*/
    private TextView tv_contact;
    /**联系电话*/
    private TextView tv_phone;
    private TextView tv_email;
    /**公司信息*/
    private CompanyData companyData;
    /**招聘信息*/
    private List<RecruitmentData> recruitmentData;

    private BGABanner companyBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        initView();
    }

    private void initView(){
        tv_contact = findViewById(R.id.tv_contact);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        companyBanner = (BGABanner) findViewById(R.id.company_banner);
        companyBanner.setAutoPlayInterval(2000);
        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                finish();
            }
        }, 10000);
    }
    void initDataTest(){
        String companyId = "2";
        String recruitId = "1";
        QueryById(new RecruitmentInput(companyId,recruitId));
        if(companyData!=null){
            recruitmentData = companyData.getStations();
        }
    }

    void initData(){
        Gson gson = new Gson();
        String extra = getIntent().getStringExtra("data");
        if(StringUtils.isEmpty(extra)){
            JSONObject data = JsonUtils.parse(extra, JSONObject.class);
            try {
                String data1 = (String)data.get("data");
                if(StringUtils.isEmpty(data1)){
                    //TODO 调接口
                    String companyId = (String)data.get("companyId");
                    String recruitId = (String)data.get("recruitId");
                    QueryById(new RecruitmentInput(companyId,recruitId));
                    if(companyData!=null){
                        recruitmentData = companyData.getStations();
                    }
                }else{
                    JSONObject json = gson.fromJson(data1, JSONObject.class);
                    companyData =(CompanyData)json.get("company");
                    RecruitmentData[] stations = (RecruitmentData[]) json.get("stations");
                    recruitmentData = Arrays.asList(stations);
                }
                tv_contact.setText(companyData.getContactPer());
                tv_phone.setText(companyData.getTelPhone());
                tv_phone.setText(companyData.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void QueryById (RecruitmentInput input) {
        App.httpGet("QueryRecruitmentByComId",input, new HttpResult<CompanyResult>() {
            @Override
            public void onResponse(CompanyResult result) {
                companyData = result.getData();
            }
        });
    }
}
