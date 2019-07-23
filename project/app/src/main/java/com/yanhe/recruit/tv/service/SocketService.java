package com.yanhe.recruit.tv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.constent.BroadcastActions;
import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {
    SocketBinderImpl binder;
    Socket mSocket;
    String mRoom;
    String mCode;
    {
        LocalStoreManager store = LocalStoreManager.getInstance();
        /*store.write("room", "08");
        store.write("position", "019");*/
        mRoom = store.read("room", "").toString();
        mCode = store.read("position", "").toString();
        try {
            String wsurl = URL.SOCKET_HOST+ "?channel=4&room="+ mRoom+"&code="+ mCode;
            Logger.d("=========>wsurl:%s", wsurl);
            IO.Options opts = new IO.Options();
            opts.path = "/api/socket";
            opts.transports = new String[]{"polling", "websocket"};
            opts.timeout = 1000;
            mSocket = IO.socket(wsurl, opts);
        } catch (Exception e) {
            Logger.d("socket init fail:%s", e.getMessage());
        }
    }

    protected void initSocketEvents() {
        if (mSocket == null) {
            return;
        }
        mSocket
        .on(Socket.EVENT_CONNECT, mConnectListener)
        .on(Socket.EVENT_ERROR, new io.socket.emitter.Emitter.Listener() {
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
        }).on("POSITION_RECRUIT_RQ", positionRecruitRQListener)
          .on("TV_CONFIG_RQ", mTVConfigRQListener)
          .on("COMPANY_SHOWING_RQ", companyShowingRQMsg)
          .on("URL_SHOWING_RQ", urlShowingRQMsg).
           on("IMG_SHOWING_RQ", imgShowingRQMsg)
          .on("OFFLINE_SIGNUP_RQ", offlineSignupRQMsg);
        Logger.d("socket init finish");
    }

    protected Emitter.Listener mConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.d("socket connect: %s", args);
            Logger.d("room:%s", mRoom, " code:", mCode);
            String socketId = mSocket.id();
            LocalStoreManager store = LocalStoreManager.getInstance();
            store.write("socketId", socketId);
            if(mCode.equals("") || mRoom.equals("")){
                Intent intent = new Intent();
                intent.setAction(BroadcastActions.NEED_CONFIG_MSG);
                Bundle data = new Bundle();
                data.putString("socketId", socketId);
                intent.putExtras(data);
                sendBroadcast(intent);
            }
        }
    };

    protected Emitter.Listener mTVConfigRQListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Logger.d("TV_CONFIG_RQ:%s", data);
            String roomCode = "";
            String code = "";
            try {
                roomCode = data.has("roomCode") ? data.getString("roomCode") : "";
                code = data.has("code") ? data.getString("code") : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(BroadcastActions.TV_CONFIG_MSG);
            intent.putExtra("roomCode", roomCode);
            intent.putExtra("code", code);
            sendBroadcast(intent);
        }
    };
    /**
     * 展位招聘会启动消息
     */
    protected Emitter.Listener positionRecruitRQListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Logger.d("POSITION_RECRUIT_RQ:%s", args);
            String companyId = "";
            String recruitId = "";
            try {
                companyId = data.has("companyId") ? data.getString("companyId") : "";
                recruitId = data.has("recruitId") ? data.getString("recruitId") : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(BroadcastActions.POSITION_RECRUIT_RQ_MSG);
            intent.putExtra("companyId", companyId);
            intent.putExtra("recruitId", recruitId);
            sendBroadcast(intent);
        }
    };
    /**
     * 签到
     */
    protected Emitter.Listener offlineSignupRQMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Logger.d("OFFLINE_SIGNUP_RQ:%s", data);
            String companyId = "";
            String recruitId = "";
            try {
                companyId = data.has("companyId") ? data.getString("companyId") : "";
                recruitId = data.has("recruitId") ? data.getString("recruitId") : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(BroadcastActions.OFFLINE_SIGNUP_RQ_MSG);
            intent.putExtra("companyId", companyId);
            intent.putExtra("recruitId", recruitId);
            sendBroadcast(intent);
        }
    };
    /**
     * 企业展示消息
     */
    protected Emitter.Listener companyShowingRQMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Logger.d("COMPANY_SHOWING_RQ:%s", data);
            String companyId = "";
            String recruitId = "";
            String context="";
            try {
                companyId = data.has("companyId") ? data.getString("companyId") : "";
                recruitId = data.has("recruitId") ? data.getString("recruitId") : "";
                context = data.has("data") ? data.getString("data") : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(BroadcastActions.COMPANY_SHOWING_RQ_MSG);
            intent.putExtra("companyId", companyId);
            intent.putExtra("recruitId", recruitId);
            intent.putExtra("context", context);
            sendBroadcast(intent);
        }
    };
    /**
     * url消息
     */
    protected Emitter.Listener urlShowingRQMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Logger.d("URL_SHOWING_RQ:%s", data);
            String url = "";
            try {
                url = data.has("url") ? data.getString("url") : "";

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(BroadcastActions.URL_SHOWING_RQ_MSG);
            intent.putExtra("url",url);
            sendBroadcast(intent);
        }
    };
    /**
     * 图片消息
     */
    protected Emitter.Listener imgShowingRQMsg = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Logger.d("IMG_SHOWING_RQ:%s", data);
            String anim = "";
            String sleep = "";
            String timeout = "";
            String imgs = "";
            try {
                anim = data.has("anim") ? data.getString("anim") : "";
                sleep = data.has("sleep") ? data.getString("sleep") : "";
                timeout = data.has("timeOut") ? data.getString("timeOut") : "";
                imgs = data.has("imgs") ? data.getString("imgs") : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.setAction(BroadcastActions.IMG_SHOWING_RQ_MSG);
            intent.putExtra("anim",anim);
            intent.putExtra("sleep",sleep);
            intent.putExtra("timeout",timeout);
            intent.putExtra("imgs",imgs);
            sendBroadcast(intent);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        initSocketEvents();
        if (mSocket != null) {
            binder = new SocketBinderImpl(this, mSocket);
            mSocket.connect();
        }
    }

    @Override
    public void onDestroy() {
        if (mSocket != null) {
            mSocket.disconnect();
        }
        binder = null;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void reconnect(String room, String position) {
        mRoom = room;
        mCode = position;
        if (mSocket != null) {
            mSocket.disconnect();
        }
        try {
            String wsurl = URL.SOCKET_HOST+ "?channel=4&room="+ mRoom+"&code="+ mCode;
            Logger.d("=========>wsurl:%s", wsurl);
            IO.Options opts = new IO.Options();
            opts.path = "/api/socket";
            opts.transports = new String[]{"polling", "websocket"};
            opts.timeout = 1000;
            mSocket = IO.socket(wsurl, opts);
            mSocket.connect();
        } catch (Exception e) {
            Logger.d("socket init fail:%s", e.getMessage());
        }
    }
}
