package com.yanhe.recruit.tv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yanhe.recruit.tv.R;
import com.yanhe.recruit.tv.manage.LocalStoreManager;

/**
 * @author mx
 */
public class IntentFragment extends Fragment {
    private static final String ARG_PARAM1 = "url";
    private WebView webView;
    private View root;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_intent, container, false);
        initView();
        return root;
    }
    public static IntentFragment newInstance(String url) {
        IntentFragment fragment = new IntentFragment();
        Bundle args = new Bundle();
        LocalStoreManager storeManager = LocalStoreManager.getInstance();
        storeManager.write("urlIntent",url);
        args.putString(ARG_PARAM1, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_PARAM1);
        }
    }

    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        return root.findViewById(id);
    }

    void initView(){
        LocalStoreManager storeManager = LocalStoreManager.getInstance();
        if(url==null){
            url = storeManager.read("urlIntent","").toString();
        }
        webView = findViewById(R.id.webView1);
        // 设置可以执行js
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置web视图客户端
        webView.setWebViewClient(new MyWebViewClient());

        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮

        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);

        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加

        //这行很关键
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(this.url);
        webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });
    }
    /**web视图客户端*/
    public class MyWebViewClient extends WebViewClient {
        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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
