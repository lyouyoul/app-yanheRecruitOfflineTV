package com.yanhe.recruit.tv;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanhe.recruit.tv.model.CompanyShowItem;


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

    public CompanyBannerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.activity_main_item, this, true);
        tv_num = (TextView) findViewById(R.id.tv_num);
        company_name = (TextView) findViewById(R.id.company_name);
        tv_job = (TextView) findViewById(R.id.tv_job);
        tv_numberOfPeople = (TextView) findViewById(R.id.tv_numberOfPeople);
        tv_treatment = (TextView) findViewById(R.id.tv_treatment);
        tv_claim = (TextView) findViewById(R.id.tv_claim);
    }

    public void bindData(CompanyShowItem item) {
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

}
