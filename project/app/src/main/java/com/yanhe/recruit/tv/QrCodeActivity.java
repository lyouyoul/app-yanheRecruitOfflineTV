package com.yanhe.recruit.tv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanhe.recruit.tv.utils.QRCodeUtil;
import com.yanhe.recruit.tv.utils.ScreenUtils;
import com.yanhe.recruit.tv.utils.WidthHeightUtils;

/**
 * @author mx
 */
public class QrCodeActivity extends Activity {
    private ImageView tv_qrcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
    }


    private void initView(){
        String extra = getIntent().getStringExtra("id");
        if(extra.isEmpty()){
           return;
        }
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(extra, 500, 500);
        tv_qrcode = findViewById(R.id.tv_qrcode);
        tv_qrcode.setImageBitmap(qrCodeBitmap);
        int screenHeight = WidthHeightUtils.getScreenHeight(this);
        ScreenUtils screenUtils = new ScreenUtils(this);

        ViewGroup.LayoutParams params = tv_qrcode.getLayoutParams();
        params.height=screenHeight/3;
        params.width=params.height;
        tv_qrcode.setLayoutParams(params);
    }
}
