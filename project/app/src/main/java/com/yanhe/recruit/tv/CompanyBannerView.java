package com.yanhe.recruit.tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanhe.recruit.tv.model.CompanyShowItem;
import com.yanhe.recruit.tv.server.type.data.RecruitmentData;
import com.yanhe.recruit.tv.utils.StringUtils;


/**
 * @author mx
 */
public class CompanyBannerView extends LinearLayout {
    private TextView tv_num;
    private TextView company_name;
    private TextView tv_job;
    private TextView tv_numberOfPeople;
    private TextView tv_treatment;
    private TextView tv_claim;
    private TextView tv_workHours;
    private TextView tv_workAddr;

    public CompanyBannerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.activity_company_item, this, true);
        tv_num = (TextView) findViewById(R.id.tv_num);
        company_name = (TextView) findViewById(R.id.company_name);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_numberOfPeople = (TextView) findViewById(R.id.tv_numberOfPeople);
        tv_treatment = (TextView) findViewById(R.id.tv_treatment);
        tv_claim = (TextView) findViewById(R.id.tv_claim);
        tv_workHours =(TextView) findViewById(R.id.tv_workHours);
        tv_workAddr = (TextView) findViewById(R.id.tv_workAddr);

    }

    /**测试*/
    public void bindDataTest(CompanyShowItem item) {
        if (item == null) {
            setVisibility(GONE);
            return;
        }
        tv_num.setText(String.valueOf(item.getIndex()));
        company_name.setText(item.getComName());
        tv_job.setText(item.getJob());
        tv_numberOfPeople.setText(String.valueOf(item.getNumberOfPeople()));
        tv_treatment.setText(item.getTreatment());
        tv_claim.setText(item.getClaim());
    }

    public void bindData(RecruitmentData item) {
        if (item == null) {
            setVisibility(GONE);
            return;
        }
        tv_num.setText(String.valueOf(item.getIndex()));
        company_name.setText(item.getCompanyName());
        tv_job.setText(item.getName());
        tv_numberOfPeople.setText(String.valueOf(item.getNumber()));
        tv_workHours.setText(item.getWorkHours());
        tv_workAddr.setText(item.getWorkAddr());
        tv_treatment.setText(item.getSalary());
        String sex = item.getSex();
        String age = item.getAge();
        /**学历*/
        String education=item.getEducation();
        String claim="性别:"+sex+"、"+age+"、"+education+"、"+item.getSpeciality();
        if(StringUtils.isEmpty(item.getSpeciality())){
            claim+="、"+item.getSpeciality();
        }
        if(StringUtils.isEmpty(item.getCondition())){
            claim+="、"+item.getCondition();
        }
        tv_claim.setText(claim);
    }

}
