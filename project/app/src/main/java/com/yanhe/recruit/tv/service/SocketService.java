package com.yanhe.recruit.tv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.BusinessSignInActivity;
import com.yanhe.recruit.tv.CompanyActivity;
import com.yanhe.recruit.tv.ImageAdActivity;
import com.yanhe.recruit.tv.IntentActivity;
import com.yanhe.recruit.tv.constent.BroadcastActions;
import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.manage.DbLocalStore;
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
        DbLocalStore store = new DbLocalStore();
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
        }).on("POSITION_RECRUIT_RQ", new io.socket.emitter.Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("POSITION_RECRUIT_RQ:%s", args);
            }
        }).on("TV_CONFIG_RQ", mTVConfigRQListener)
          .on("COMPANY_SHOWING_RQ", new io.socket.emitter.Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
//                Logger.d("COMPANY_SHOWING_RQ:%s", data);
//                String serialize = JsonUtils.serialize(data);
//                Intent intent = new Intent(getContext(), CompanyActivity.class);
//                intent.putExtra("data", serialize);
//                startActivity(intent);
            }
        }).on("URL_SHOWING_RQ", new io.socket.emitter.Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("URL_SHOWING_URL:%s", args);
//                JSONObject data = (JSONObject) args[0];
//                try {
//                    String url = data.getString("url");
//                    Intent intent = new Intent(getContext(), IntentActivity.class);
//                    intent.putExtra("url",url);
//                    startActivity(intent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }).on("IMG_SHOWING_RQ", new io.socket.emitter.Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("IMG_SHOWING_RQ:%s", args);
//                JSONObject data = (JSONObject) args[0];
//                String serialize = JsonUtils.serialize(data);
//                Intent intent = new Intent(getContext(), ImageAdActivity.class);
//                intent.putExtra("data", serialize);
//                startActivity(intent);
            }
        }).on("OFFLINE_SIGNUP_RQ", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("OFFLINE_SIGNUP_RQ:%s", args);
//                Intent intent = new Intent(getContext(), BusinessSignInActivity.class);
//                startActivity(intent);
            }
        });
        Logger.d("socket init finish");
    }

    protected Emitter.Listener mConnectListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.d("socket connect: %s", args);
            Logger.d("room:%s", mRoom, " code:", mCode);
            if(mCode.equals("") || mRoom.equals("")){
                Intent intent = new Intent();
                intent.setAction(BroadcastActions.NEED_CONFIG_MSG);
                Bundle data = new Bundle();
                String socketId = mSocket.id();
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
