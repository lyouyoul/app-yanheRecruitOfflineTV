package com.yanhe.recruit.tv.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.service.SocketBinder;
import com.yanhe.recruit.tv.utils.QRCodeUtil;


public class HomeFragment extends Fragment {
    private ImageView iv_qrcode;
    private View root;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        Logger.d("homeFragment createView");
        return root;
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        return root.findViewById(id);
    }

    protected void initView() {
        iv_qrcode = findViewById(R.id.iv_qrcode);
        initDate();
    }

    protected void initDate() {
        LocalStoreManager store = LocalStoreManager.getInstance();
        String socketId = store.read("socketId", "").toString();
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(socketId, 500, 500);
        iv_qrcode.setImageBitmap(qrCodeBitmap);
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
