package com.yanhe.recruit.tv.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanhe.recruit.tv.App;
import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.data.CompanyData;
import com.yanhe.recruit.tv.server.type.input.RecruitmentInput;
import com.yanhe.recruit.tv.server.type.result.CompanyResult;
import com.yanhe.recruit.tv.utils.JsonUtils;

public class OfflineSignupRQFragment extends Fragment {
    private static final String ARG_PARAM1 = "recruitId";
    private static final String ARG_PARAM2 = "companyId";
    private String recruitId;
    private String companyId;
    private CompanyData companyData;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_offline_signup_rq, container, false);
        initView();
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recruitId = getArguments().getString(ARG_PARAM1);
            companyId = getArguments().getString(ARG_PARAM2);
        }
    }

    public static OfflineSignupRQFragment newInstance(String recruitId,String companyId) {
        OfflineSignupRQFragment fragment = new OfflineSignupRQFragment();
        LocalStoreManager store = LocalStoreManager.getInstance();
        store.write("recruitIdOF",recruitId);
        store.write("companyIdOF",companyId);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, recruitId);
        args.putString(ARG_PARAM2, companyId);
        fragment.setArguments(args);
        return fragment;
    }
    private void initView() {
        LocalStoreManager store = LocalStoreManager.getInstance();
        if(recruitId==null || companyId==null ){
            recruitId = store.read("recruitIdOF","").toString();
            companyId = store.read("companyIdOF","").toString();
        }
        RecruitmentInput recruitmentInput = new RecruitmentInput(companyId,recruitId);
        App.httpPost("QueryRecruitmentByRecruitIdAndCompanyId",recruitmentInput, new HttpResult<CompanyResult>() {
            @Override
            public void onResponse(CompanyResult result) {
                companyData = result.getContext();
                LocalStoreManager store = LocalStoreManager.getInstance();
                if(companyData!=null){
                    store.write("companyData", JsonUtils.serialize(companyData));
                }
                if(!"".equals(store.read("companyData",""))){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_client, new CompanyFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
}
