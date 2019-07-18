/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yanhe.recruit.tv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.constent.URL;
import com.yanhe.recruit.tv.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * @author mx
 */
public class MainActivity extends Activity  {
    private Socket mSocket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //garbageCompany
        //2.建立socket.io服务器的连接
        try {
            //basicArrearsOfWagesFor3Months
            //1.初始化socket.io，设置链接
            String wsurl = URL.SOCKET_HOST + "/api/socket?channel=4&room=08&code=019";
            IO.Options opts = new IO.Options();
            opts.host = "192.168.0.101";
            opts.port = 3000;
            opts.path = "/api/socket";
            opts.query = "channel=4&room=08&code=019";
            opts.reconnection = true;
            opts.forceNew = true;
            opts.multiplex = true;
            opts.transports = new String[]{"polling", "websocket"};
            mSocket = IO.socket(wsurl);
            //Turning to look at the boss's mood, the salary is basically c
            //使用 onNewMessage 来监听服务器发来的 "new message" 事件
            messageListener();
            mSocket.connect();
            if (mSocket.connected()) {
                Logger.d("socket connect success");
            }
        } catch (Exception e) {
            Logger.d("socket init fail:%s", e.getMessage());
        }
        Logger.d("socket create finish");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
        Logger.d("socket close====>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSocket.connect();
        if (mSocket.connected()) {
            Logger.d("socket connect success");
        }
        Logger.d("msocket open=====>");
    }

    private void messageListener(){
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("socket connect");
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("socket error");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("socket connect error:%s", args);
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("socket reconnect:%s", args);
            }
        }).on(Socket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("socket reconnect error:%s", args);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("socket disconnect");
            }
        }).on("status", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Logger.d("status: %s", JsonUtils.serialize(args[0]));
            }
        }).on("COMPANY_SHOWING_RQ", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Logger.d("COMPANY_SHOWING_RQ:", data);
                String serialize = JsonUtils.serialize(data);
                Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
                intent.putExtra("data", serialize);
                startActivity(intent);
            }
        }).on("URL_SHOWING_RQ", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String url = data.getString("url");
                    Intent intent = new Intent(MainActivity.this, IntentActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).on("IMG_SHOWING_RQ", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String serialize = JsonUtils.serialize(data);
                Intent intent = new Intent(MainActivity.this, ImageAdActivity.class);
                intent.putExtra("data", serialize);
                startActivity(intent);
            }
        }).on("OFFLINE_SIGNUP_RQ", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Intent intent = new Intent(MainActivity.this, BusinessSignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**释放socket*/
        mSocket.disconnect();
    }
}
