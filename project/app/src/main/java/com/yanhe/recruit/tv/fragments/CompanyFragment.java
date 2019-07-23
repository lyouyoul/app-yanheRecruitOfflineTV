package com.yanhe.recruit.tv.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.App;
import com.yanhe.recruit.tv.CompanyBannerView;
import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.data.CompanyData;
import com.yanhe.recruit.tv.server.type.data.RecruitmentData;
import com.yanhe.recruit.tv.server.type.input.RecruitmentInput;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.utils.JsonUtils;
import com.yanhe.recruit.tv.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 企业招聘
 * @author mx
 */
public class CompanyFragment extends Fragment {
    private static final String ARG_PARAM1 = "recruitId";
    private static final String ARG_PARAM2 = "companyId";
    private static final String ARG_PARAM3 = "context";
    private String recruitId;
    private String companyId;
    private String context;
    private View root;
    /**公司名称*/
    private TextView company_name;
    /**联系人*/
    private TextView tv_contact;
    /**联系电话*/
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_adress;
    /**公司信息*/
    private CompanyData companyData;
    /**招聘信息*/
    private List<RecruitmentData> recruitmentDatas;

    private BGABanner companyBanner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_company, container, false);
        initView();
        return root;
    }
    public static CompanyFragment newInstance(String recruitId,String companyId,String context) {
        CompanyFragment fragment = new CompanyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, recruitId);
        args.putString(ARG_PARAM2, companyId);
        args.putString(ARG_PARAM3, context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recruitId = getArguments().getString(ARG_PARAM1);
            companyId = getArguments().getString(ARG_PARAM2);
            context = getArguments().getString(ARG_PARAM3);
        }
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        return root.findViewById(id);
    }

    private void initView(){
        company_name = findViewById(R.id.company_name);
        tv_contact = findViewById(R.id.tv_contact);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        tv_adress = findViewById(R.id.tv_adress);
        companyBanner = (BGABanner) findViewById(R.id.company_banner);
        companyBanner.setAutoPlayInterval(20000);
        initData();
        initBannerView();
    }

    void initBannerView(){
        List<View> views = new ArrayList<>();
        if (recruitmentDatas != null){
            for (RecruitmentData recruitmentData : recruitmentDatas) {
                CompanyBannerView companyBannerView = new CompanyBannerView(getContext());
                companyBannerView.bindData(recruitmentData);
                views.add(companyBannerView);
            }
        }
        companyBanner.setData(views);
    }

    void initData(){
        LocalStoreManager store = LocalStoreManager.getInstance();
        Gson gson = new Gson();
        if(StringUtils.isEmpty(context)){
            companyData = gson.fromJson(store.read("companyData", null).toString(),CompanyData.class);
            if(companyData!=null){
                recruitmentDatas =Arrays.asList(this.companyData.getStations()) ;
                company_name.setText(companyData.getName());
                tv_contact.setText(companyData.getContent());
                tv_email.setText(companyData.getEmail());
                tv_adress.setText(companyData.getAddress());
            }else{
                QueryById();
            }
        }else{
            JSONObject data = gson.fromJson(context, JSONObject.class);
            try {
                companyData = gson.fromJson(data.get("company").toString(),CompanyData.class) ;
                RecruitmentData[] stations = gson.fromJson(data.get("stations").toString(), RecruitmentData[].class);
                recruitmentDatas = Arrays.asList(stations);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            company_name.setText(companyData.getName());
            tv_contact.setText(companyData.getContent());
            tv_email.setText(companyData.getEmail());
            tv_adress.setText(companyData.getAddress());
        }
    }

    private void QueryById () {
        final LocalStoreManager store = LocalStoreManager.getInstance();
        Logger.d("new RecruitmentInput(recruitId,companyId) %s,%s",recruitId,companyId);
        RecruitmentInput recruitmentInput = new RecruitmentInput(companyId,recruitId);
        App.httpPost("QueryRecruitmentByRecruitIdAndCompanyId",recruitmentInput, new HttpResult<CompanyResult>() {
            @Override
            public void onResponse(CompanyResult result) {
                companyData = result.getContext();
                if(companyData!=null){
                    Logger.d("========>companyData %s",companyData);
                    recruitmentDatas = Arrays.asList(companyData.getStations());
                    store.write("companyData", JsonUtils.serialize(companyData));
                    company_name.setText(companyData.getName());
                    tv_contact.setText(companyData.getContent());
                    tv_email.setText(companyData.getEmail());
                    tv_adress.setText(companyData.getAddress());
                }
            }
        });
    }
}
