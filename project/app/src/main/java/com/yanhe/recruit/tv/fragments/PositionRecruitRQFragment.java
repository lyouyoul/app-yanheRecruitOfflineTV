package com.yanhe.recruit.tv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.App;
import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.data.CompanyData;
import com.yanhe.recruit.tv.server.type.data.QueryQrcodeData;
import com.yanhe.recruit.tv.server.type.input.QueryCompanyByIdInput;
import com.yanhe.recruit.tv.server.type.input.QueryRecruitPositionQrcodeInput;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.server.type.result.QueryQrcodeResult;
import com.yanhe.recruit.tv.utils.JsonUtils;
import com.yanhe.recruit.tv.utils.StringUtils;

public class PositionRecruitRQFragment extends Fragment {
    private static final String ARG_PARAM1 = "recruitId";
    private static final String ARG_PARAM2 = "companyId";
    private View root;
    private String recruitId;
    private String companyId;
    private TextView tv_noinfo; // companyId不存在
    private LinearLayout tv_info; // companyId存在
    private TextView company_name;
    private ImageView iv_faceImg;
    private ImageView sign_in_qr_code;
    private TextView tv_contact;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_adress;
    private CompanyData companyData;


    public static PositionRecruitRQFragment newInstance(String recruitId,String companyId) {
        PositionRecruitRQFragment fragment = new PositionRecruitRQFragment();
        Bundle args = new Bundle();
        LocalStoreManager store = LocalStoreManager.getInstance();
        store.write("recruitIdPR",recruitId);
        store.write("companyIdPR",companyId);
        args.putString(ARG_PARAM1, recruitId);
        args.putString(ARG_PARAM2, companyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_position_recruit_rq, container, false);
        initView();
        return root;
    }
    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        return root.findViewById(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recruitId = getArguments().getString(ARG_PARAM1);
            companyId = getArguments().getString(ARG_PARAM2);
        }
    }

    void initView(){
        LocalStoreManager store = LocalStoreManager.getInstance();
        if(companyId==null){
            companyId = store.read("companyIdPR","").toString();
        }
        if(recruitId==null){
            recruitId = store.read("recruitIdPR","").toString();
        }
        tv_noinfo = findViewById(R.id.tv_noinfo);
        tv_info = findViewById(R.id.tv_info);
        company_name = findViewById(R.id.company_name);
        iv_faceImg = findViewById(R.id.iv_faceImg);
        sign_in_qr_code = findViewById(R.id.sign_in_qr_code);
        tv_contact = findViewById(R.id.tv_contact);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        tv_adress = findViewById(R.id.tv_adress);
        Logger.d("====================>companyId %s",companyId);
        if(StringUtils.isEmpty(companyId)){
            tv_noinfo.setVisibility(View.VISIBLE);
            tv_info.setVisibility(View.GONE);
        }else{
            tv_info.setVisibility(View.VISIBLE);
            tv_noinfo.setVisibility(View.GONE);
        }

        initDate();
    }

    private void initDate() {
        queryByComId();
    }

    private void queryByComId(){
        QueryCompanyByIdInput input = new QueryCompanyByIdInput(companyId);
        App.httpPost("QueryCompanyByComId", input, new HttpResult<CompanyResult>() {
            @Override
            public void onResponse(CompanyResult result) {
                companyData = result.getContext();
                if(companyData!=null){
                    LocalStoreManager store = LocalStoreManager.getInstance();
                    store.write("companyData", JsonUtils.serialize(companyData));
                    if(companyData.getSignUp()){
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fl_client, new CompanyFragment())
                                .addToBackStack(null)
                                .commit();
                    }else{
                        company_name.setText(companyData.getName());
                        Glide.with(getContext()).load(companyData.getFaceImg()).into(iv_faceImg);
                        tv_contact.setText(companyData.getContactPer());
                        tv_phone.setText(companyData.getTelPhone());
                        tv_phone.setText(companyData.getEmail());
                        tv_adress.setText(companyData.getAddress());
                        QueryQrcode();
                    }


                }
            }
        });
    }

    private void QueryQrcode(){
         LocalStoreManager storeManager = LocalStoreManager.getInstance();
        String room = storeManager.read("room", "").toString();
        String position = storeManager.read("position", "").toString();
        QueryRecruitPositionQrcodeInput input = new QueryRecruitPositionQrcodeInput(recruitId,room,position);
        App.httpPost("QueryRecruitPositionQrcode", input, new HttpResult<QueryQrcodeResult>() {
            @Override
            public void onResponse(QueryQrcodeResult result) {
                QueryQrcodeData queryQrcodeData = result.getContext();
                String qrcode = queryQrcodeData.getQrcode();
                String[] split = qrcode.split(",");
                Glide.with(getActivity()).load(Base64.decode(split[1], Base64.DEFAULT)).into(sign_in_qr_code);
            }
        });
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
