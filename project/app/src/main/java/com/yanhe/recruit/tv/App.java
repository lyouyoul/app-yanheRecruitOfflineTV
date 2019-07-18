package com.yanhe.recruit.tv;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.yanhe.recruit.tv.base.BaseAndroid;
import com.yanhe.recruit.tv.base.BaseConfig;
import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.manage.*;
import com.yanhe.recruit.tv.model.RemoteAppVersion;
import com.yanhe.recruit.tv.server.HttpManager;
import com.yanhe.recruit.tv.server.HttpRegister;
import com.yanhe.recruit.tv.server.inf.HttpMethod;
import com.yanhe.recruit.tv.server.inf.HttpResult;
import com.yanhe.recruit.tv.server.type.BaseInput;
import com.yanhe.recruit.tv.server.type.BaseResult;
import com.yanhe.recruit.tv.utils.Http;
import com.yanhe.recruit.tv.utils.Http.ResponseHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;


/**
 * @author yangtxiang
 * @date 2019-05-07
 */
public class App extends Application {
    private final static String CLASS_NAME = App.class.getName();
    private static Context mContext;
    private static RemoteAppVersion remoteVersion = new RemoteAppVersion();
    public static int AppRunCount = 0;
    /**
     * http网络请求
     */
    private static Http http;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("recruit-tv", "============ app create");
        mContext = getApplicationContext();
        initLogger();
        // 注册本地存储
        LocalStoreManager.getInstance().registerStore(new DbLocalStore());
        // 实例网络请求库
        http = new Http(mContext);
        // 注册网络请求类
        HttpRegister.register();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        BaseConfig config = new BaseConfig()
                .setAppColor(R.color.colorPrimary)
                .setAppLogo(R.drawable.ic_logo)
                .setFailPicture(R.drawable.ic_logo);
        BaseAndroid.init(config);
        loadRemoteVersionInfo();
        LocalStoreManager store = LocalStoreManager.getInstance();
        AppRunCount = (int)store.read("AppRunCount", 0);
        store.write("AppRunCount", AppRunCount + 1);
        Logger.d("app run count:%s", AppRunCount);
    }


    public static void loadRemoteVersionInfo() {
//        http.get(URL.APP_VER_URL, null, new ResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, String text) {
//
//            }
//            @Override
//            public void onSuccess(JSONObject json) {
//                Logger.d("remote app ver:%s", json);
//                try {
//                    remoteVersion.setVersionCode(json.getInt("versionCode"));
//                    remoteVersion.setVersion(json.getString("version"));
//                    remoteVersion.setAppId(json.getString("appId"));
//                    remoteVersion.setAppUrl(json.getString("url"));
//                    remoteVersion.setSummery(json.getString("summery"));
//                }catch (JSONException e) {
//                    Logger.d("get version json error: %s", e.getMessage());
//                }
//            }
//        });
    }

    public static RemoteAppVersion getRemoteVersion() {
        return remoteVersion;
    }

    private void initLogger() {
        LogcatLogStrategy strategy = new LogcatLogStrategy();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                // (Optional) Whether to show thread info or not. Default true
                .showThreadInfo(false)
                // (Optional) How many method line to show. Default 2
                .methodCount(0)
                // (Optional) Hides internal method calls up to offset. Default 5
                .methodOffset(7)
                // (Optional) Changes the log strategy to print out. Default LogCat
                .logStrategy(strategy)
                // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .tag("RECRUIT-TV")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 发起网络请求
     * @param methodType  请求方法类型
     * @param methodName  请求方法名称
     * @param input       输入参数
     * @param result      回调接口
     */
    public static void fetch (HttpManager.HttpRequestMethodType methodType, final String methodName, BaseInput input, final HttpResult result) {
        HttpMethod method = HttpManager.getInstance().getMethod(methodType, methodName);
        if (method == null) {
            Logger.d("网络请求类%s未注册或未定义无参数的构造方法.");
            return;
        }
        //UIManager.showLoading("数据请求中...");
        method.request(http, input, new HttpResult() {
            @Override
            public void onResponse(BaseResult res) {
                //UIManager.hiddenLoading();
                if (res.getStatus() != 200) {
                    Logger.d("网络请求[%s]错误:%s", methodName, res.getMsg());
                    UIManager.showToast(res.getMsg());
                } else {
                    if (result != null) {
                        result.onResponse(res);
                    }
                }
            }
        });
    }

    /**
     * 发送get请求
     * @param methodName    方法名称
     * @param input         输入参数
     * @param result        回调接口
     */
    public static void httpGet(String methodName, BaseInput input, HttpResult result) {
        fetch(HttpManager.HttpRequestMethodType.GET, methodName, input, result);
    }

    /**
     * post请求
     * @param methodName 方法名称
     * @param input      输入参数
     * @param result     回调接口
     *
     */
    public static void httpPost(String methodName, BaseInput input, HttpResult result) {
        fetch(HttpManager.HttpRequestMethodType.POST, methodName, input, result);
    }

    /**
     * delete请求
     * @param methodName 方法名称
     * @param input      输入参数
     * @param result     回调接口
     */
    public static void httpDelete(String methodName, BaseInput input, HttpResult result) {
        fetch(HttpManager.HttpRequestMethodType.DELETE, methodName, input, result);
    }

    /**
     * put请求
     * @param methodName  方法名称
     * @param input       输入参数
     * @param result      回调接口
     */
    public static void httpPut(String methodName, BaseInput input, HttpResult result) {
        fetch(HttpManager.HttpRequestMethodType.PUT, methodName, input, result);
    }

}
