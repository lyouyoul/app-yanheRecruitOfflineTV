package com.yanhe.recruit.tv;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanhe.recruit.tv.utils.ScreenUtils;
import com.yanhe.recruit.tv.utils.WidthHeightUtils;

/**
 * 企业签到
 * @author mx
 */
public class BusinessSignInActivity extends Activity {
    private ImageView qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_sign_in);
        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                finish();
            }
        }, 10000);
    }

    private void initView(){
        qr_code = findViewById(R.id.qr_code);
        int screenHeight = WidthHeightUtils.getScreenHeight(this);
        ScreenUtils screenUtils = new ScreenUtils(this);

        ViewGroup.LayoutParams params = qr_code.getLayoutParams();
        params.height=screenHeight/2;
        params.width=params.height;
        qr_code.setLayoutParams(params);
    }
}
