package com.yanhe.recruit.tv;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanhe.recruit.tv.model.CompanyShowItem;

import java.util.ArrayList;
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

    private BGABanner companyBanner;
    private CompanyShowItem[] companyShowItems = new CompanyShowItem[]{
            new CompanyShowItem("沿河雷氏照明有限公司","导购",10,"8000-10000元","全职"),
            new CompanyShowItem("沿河雷氏照明有限公司","导购",10,"8000-10000元","全职"),
            new CompanyShowItem("沿河雷氏照明有限公司","导购",10,"8000-10000元","全职"),
            new CompanyShowItem("沿河雷氏照明有限公司","导购",10,"8000-10000元","全职"),
    } ;
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
        initCompanyBanner();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                finish();
            }
        }, 10000);
    }

    private void initCompanyBanner () {
        List<View> views = new ArrayList<>();
        List<CompanyShowItem> group = Arrays.asList(companyShowItems);
        int i=0;
        for (CompanyShowItem companyShowItem : group) {
            companyShowItem.setIndex(i+1);
            CompanyBannerView companyBannerView = new CompanyBannerView(this);
            companyBannerView.bindData(companyShowItem);
            views.add(companyBannerView);
            i++;
        }
        companyBanner.setData(views);
    }
}
