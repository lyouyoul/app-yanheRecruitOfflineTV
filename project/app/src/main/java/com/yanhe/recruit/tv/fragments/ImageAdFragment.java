package com.yanhe.recruit.tv.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.MainActivity;
import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.constent.BroadcastActions;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.server.type.data.ImageUrlData;
import com.yanhe.recruit.tv.utils.RotateTransformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.TransitionEffect;

/**
 * 广告图片
 * @author mx
 */
public class ImageAdFragment extends Fragment {
    private static final String ARG_PARAM1 = "anim";
    private static final String ARG_PARAM2 = "sleep";
    private static final String ARG_PARAM3 = "timeout";
    private static final String ARG_PARAM4 = "imgs";
    private BGABanner imageAdBanner;
    private View root;
    /**动画*/
    /**旋转角度*/
    float angle;
    private String anim = "";
    private String sleep = "";
    private String timeout = "";
    private String imgs = "";
    private ImageUrlData[] imageUrlDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_image_ad, container, false);
        initViewAd();
        return root;
    }

    public static ImageAdFragment newInstance(String anim,String sleep,String timeout,String imgs) {
        ImageAdFragment imageAdFragment = new ImageAdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, anim);
        args.putString(ARG_PARAM2, sleep);
        args.putString(ARG_PARAM3, timeout);
        args.putString(ARG_PARAM4, imgs);
        imageAdFragment.setArguments(args);
        return imageAdFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            anim = getArguments().getString(ARG_PARAM1);
            sleep = getArguments().getString(ARG_PARAM2);
            timeout = getArguments().getString(ARG_PARAM3);
            imgs = getArguments().getString(ARG_PARAM4);
        }
    }
    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        return root.findViewById(id);
    }

    private void initViewAd(){
        if(anim.equals("1")){
            /**竖屏设置*/
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }
        imageAdBanner = (BGABanner) findViewById(R.id.imageAd_banner);
        //间隔
        imageAdBanner.setAutoPlayInterval(Integer.valueOf(sleep));
        initImageAdBanner();
    }

    private void initImageAdBanner (){
        Gson gson = new Gson();
        List<String> list = new ArrayList<>();
        imageUrlDatas = gson.fromJson(imgs,ImageUrlData[].class);
        for (ImageUrlData imageUrlData : imageUrlDatas) {
            list.add(imageUrlData.getUrl());
        }
        imageAdBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                if(anim.equals("1")){
                    angle=90f;
                }
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                Glide.with(getContext())
                        .load(model)
                        .centerCrop()
                        .transform( new RotateTransformation( getContext(), angle ))
                        .dontAnimate()
                        .into(itemView);
            }
        });
        imageAdBanner.setData(list,null);
        /**时长*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run () {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //getActivity().getSupportFragmentManager().popBackStack();
                LocalStoreManager store = LocalStoreManager.getInstance();
                String previousPage = store.read("previousPage", "").toString();
                Logger.d("========>previousPage: %s",previousPage);
                //previousPage = BroadcastActions.URL_SHOWING_RQ_MSG;
                switch (previousPage){
                   case BroadcastActions.NEED_CONFIG_MSG:
                        getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_client, new ConfigFragment())
                            .addToBackStack(null)
                            .commit();
                       break;
                    case BroadcastActions.POSITION_RECRUIT_RQ_MSG:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_client, new PositionRecruitRQFragment())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case BroadcastActions.OFFLINE_SIGNUP_RQ_MSG:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_client, new OfflineSignupRQFragment())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case BroadcastActions.COMPANY_SHOWING_RQ_MSG:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_client, new ConfigFragment())
                                .addToBackStack(null)
                                .commit();
                        break;
                    case BroadcastActions.URL_SHOWING_RQ_MSG:
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_client, new IntentFragment())
                                .addToBackStack(null)
                                .commit();
                        break;
                   default:
                       getActivity().getSupportFragmentManager()
                               .beginTransaction()
                               .replace(R.id.fl_client, new ConfigFragment())
                               .addToBackStack(null)
                               .commit();
                       break;
                }
                }
        }, Integer.valueOf(timeout));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
