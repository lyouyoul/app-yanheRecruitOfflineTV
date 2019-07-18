package com.yanhe.recruit.tv;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import com.yanhe.recruit.tv.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import java.util.logging.LogRecord;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * @author yangtxiang
 * @date 2019-05-07
 */
public class App extends Application {
    private final static int TV_CONFIG_MSG = 1;
    private final static String CLASS_NAME = App.class.getName();
    private static Context mContext;
    private static RemoteAppVersion remoteVersion = new RemoteAppVersion();
    public static int AppRunCount = 0;
    private String mCode;
    private String mRoom;
    private static Socket mSocket;
    /**
     * http网络请求
     */
    private static Http http;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TV_CONFIG_MSG:
                    Bundle data = msg.getData();
                    Intent intent = new Intent(App.this, QrCodeActivity.class);
                    intent.putExtra("id", data.getString("socketId"));
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                    default:
            }
        }
    };

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
        initSocket();
    }

    private void initSocket() {
        try {
            LocalStoreManager localStoreManager = LocalStoreManager.getInstance();
            mCode = (String) localStoreManager.read("code","");
            mRoom = (String) localStoreManager.read("room", "");
            //basicArrearsOfWagesFor3Months
            //1.初始化socket.io，设置链接
            String wsurl = URL.SOCKET_HOST+ "?channel=4&room="+ mRoom+"&code="+ mCode;
            Logger.d("=========>wsurl:%s", wsurl);
            IO.Options opts = new IO.Options();
            opts.path = "/api/socket";
            opts.transports = new String[]{"polling", "websocket"};
            opts.timeout = 1000;
            mSocket = IO.socket(wsurl, opts);
            mSocket.on(Socket.EVENT_CONNECT, new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("socket connect: %s", args);
                    if(mCode.equals("") || mRoom.equals("")){
                        String id = mSocket.id();
                        Logger.d("mSocket.id()=====> %s", id);
                        Message msg = Message.obtain();
                        msg.what = TV_CONFIG_MSG;
                        Bundle data = new Bundle();
                        data.putString("socketId", id);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                }
            }).on(Socket.EVENT_ERROR, new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("socket error:%s", args);
                }
            }).on(Socket.EVENT_CONNECT_ERROR, new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("socket connect error:%s", args);
                }
            }).on(Socket.EVENT_RECONNECT, new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("socket reconnect:%s", args);
                }
            }).on(Socket.EVENT_RECONNECT_ERROR, new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("socket reconnect error:%s", args);
                }
            }).on(Socket.EVENT_DISCONNECT, new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("socket disconnect:%s", args);
                }
            }).on("status", new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("status: %s", JsonUtils.serialize(args[0]));
                }
            }).on("POSITION_RECRUIT_RQ", new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("POSITION_RECRUIT_RQ:%s", args);
                }
            }).on("TV_CONFIG_RQ", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    LocalStoreManager store = LocalStoreManager.getInstance();
                    Logger.d("TV_CONFIG_RQ:", args);
                }
            }).on("COMPANY_SHOWING_RQ", new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject data = (JSONObject) args[0];
                    Logger.d("COMPANY_SHOWING_RQ:%s", data);
                    String serialize = JsonUtils.serialize(data);
                    Intent intent = new Intent(getContext(), CompanyActivity.class);
                    intent.putExtra("data", serialize);
                    startActivity(intent);
                }
            }).on("URL_SHOWING_RQ", new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("URL_SHOWING_URL:%s", args);
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String url = data.getString("url");
                        Intent intent = new Intent(getContext(), IntentActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).on("IMG_SHOWING_RQ", new io.socket.emitter.Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("IMG_SHOWING_RQ:%s", args);
                    JSONObject data = (JSONObject) args[0];
                    String serialize = JsonUtils.serialize(data);
                    Intent intent = new Intent(getContext(), ImageAdActivity.class);
                    intent.putExtra("data", serialize);
                    startActivity(intent);
                }
            }).on("OFFLINE_SIGNUP_RQ", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Logger.d("OFFLINE_SIGNUP_RQ:%s", args);
                    Intent intent = new Intent(getContext(), BusinessSignInActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Logger.d("socket init fail:%s", e.getMessage());
        }
    }

    @Override
    public void onTerminate() {
        if (mSocket != null) {
            mSocket.disconnect();
        }
        super.onTerminate();
    }

    public static Socket getSocket() {
        return mSocket;
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
