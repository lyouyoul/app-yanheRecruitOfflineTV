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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanhe.recruit.tv.model.CompanyShowItem;
import com.yanhe.recruit.tv.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * @author mx
 */
public class MainActivity extends Activity  {
    private Socket mSocket;

    private Button b1,b2,b3,b4,b5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //😂
        //2.建立socket.io服务器的连接
        mSocket.connect();
        messageListener();
        initView();
    }

    {
        try {
            //1.初始化socket.io，设置链接
             mSocket = IO.socket("http://chat.socket.io");
            //使用 onNewMessage 来监听服务器发来的 "new message" 事件
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    void initView(){
        b1 = findViewById(R.id.d1);
        b2 = findViewById(R.id.d2);
        b3 = findViewById(R.id.d3);
        b4 = findViewById(R.id.d4);
        b5 = findViewById(R.id.d5);

        b1.setOnClickListener(mOnClickListener);
        b2.setOnClickListener(mOnClickListener);
        b3.setOnClickListener(mOnClickListener);
        b4.setOnClickListener(mOnClickListener);
        b5.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              switch (v.getId()){
                  case R.id.d1:
                      Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
                      startActivity(intent);
                      break;
                  case R.id.d2:
                      Intent intent2 = new Intent(MainActivity.this, IntentActivity.class);
                      startActivity(intent2);
                      break;
                  case R.id.d3:
                      Intent intent3 = new Intent(MainActivity.this, ImageAdActivity.class);
                      intent3.putExtra("type",1);
                      startActivity(intent3);
                      break;
                  case R.id.d5:
                      Intent intent5 = new Intent(MainActivity.this, ImageAdActivity.class);
                      intent5.putExtra("type",0);
                      startActivity(intent5);
                      break;
                  case R.id.d4:
                      Intent intent4 = new Intent(MainActivity.this, BusinessSignInActivity.class);
                      startActivity(intent4);
                      break;
                  default:
                      break;
              }
        }
    };


    private void messageListener(){
        mSocket.on("company_showing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String serialize = JsonUtils.serialize(data);
                Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
                intent.putExtra("data", serialize);
                startActivity(intent);
                try {
                    Object data1 = data.get("data");
                    if(data1==null){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).on("url_showing", new Emitter.Listener() {
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
        }).on("img_showing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String serialize = JsonUtils.serialize(data);
                Intent intent = new Intent(MainActivity.this, ImageAdActivity.class);
                intent.putExtra("data", serialize);
                startActivity(intent);
            }
        }).on("offline_signup", new Emitter.Listener() {
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
        mSocket.off("company_showing");
        mSocket.off("url_showing");
        mSocket.off("img_showing");
        mSocket.off("offline_signup");
    }
}
