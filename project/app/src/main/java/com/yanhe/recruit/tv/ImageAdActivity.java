package com.yanhe.recruit.tv;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanhe.recruit.tv.utils.RotateTransformation;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 广告图片
 * @author mx
 */
public class ImageAdActivity extends Activity {
    private BGABanner imageAdBanner;
    private String extra;
    /**动画*/
    int anim = 1;
    /**旋转角度*/
    float angle;

    private String[] urls = new String[]{
            "https://www.mnyun.net/mindoc/uploads/yanheRecruitOffline/images/m_8f609891e6bcd1111c855f2953408ee3_r.png",
            "https://image.shutterstock.com/image-vector/set-web-page-design-templates-600w-1150708049.jpg",
            "https://image.shutterstock.com/image-vector/set-leaf-exotics-vintage-vector-600w-472492429.jpg",
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_ad);
        //setContentView(R.layout.activity_main);
        initViewAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        extra = getIntent().getStringExtra("data");
    }

    private void initViewAd(){
        int type = getIntent().getIntExtra("type",0);
        anim=type;
        if(anim == 1){
            /**竖屏设置*/
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        imageAdBanner = (BGABanner) findViewById(R.id.imageAd_banner);
        initImageAdBanner ();
    }

    private void initImageAdBanner (){
        List<View> views = new ArrayList<>();
        for (String url: urls) {
            views.add(getSplashView(url));
        }
        /**时长*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                finish();
            }
        }, 16000);
        //imageAdBanner.setTransitionEffect(TransitionEffect.Cube);
        /**设置去掉滑到最左或最右时的滑动效果*/
        imageAdBanner.setAutoPlayInterval(1000);
        imageAdBanner.setData(views);
        /*if(extra!=null){
            JSONObject data = JsonUtils.parse(extra, JSONObject.class);
            try {
                anim = data.getInt("anim");
                if(anim == 1){
                    *//**竖屏设置*//*
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                int sleep = data.getInt("sleep");
                int timeout = data.getInt("timeout");
                String[] imgs = (String[]) data.get("imgs");
                List<View> views = new ArrayList<>();
                for (String url: urls) {
                    views.add(getSplashView(url));
                }
                *//**时长*//*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run () {
                        finish();
                    }
                }, timeout);
                imageAdBanner.setTransitionEffect(TransitionEffect.Default);
                imageAdBanner.setAutoPlayInterval(sleep);
                imageAdBanner.setData(views);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }

    private View getSplashView(String url) {
        final ImageView view = new ImageView(this);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        if(anim == 1){
            angle=90f;
        }
        Glide.with(this)
                .load(url)
                .transform( new RotateTransformation( this, angle ))
                .into(view);
        return view;
    }
}
