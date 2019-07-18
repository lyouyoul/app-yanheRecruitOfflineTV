package com.yanhe.recruit.tv.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.utils.QRCodeUtil;


public class ConfigFragment extends Fragment {
    private static final String ARG_PARAM1 = "qrcode";
    private String mQrcode;
    private View root;
    private ImageView ivQrCode;

    public ConfigFragment() {
        // Required empty public constructor
    }

    public static ConfigFragment newInstance(String qrcode) {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, qrcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQrcode = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_config, container, false);
        initView();
        return root;
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        return root.findViewById(id);
    }

    protected void initView() {
        ivQrCode = findViewById(R.id.iv_qrcode);
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(mQrcode, 500, 500);
        ivQrCode.setImageBitmap(qrCodeBitmap);
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
