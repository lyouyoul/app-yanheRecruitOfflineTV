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
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.constent.BroadcastActions;
import com.yanhe.recruit.tv.fragments.ConfigFragment;
import com.yanhe.recruit.tv.fragments.HomeFragment;
import com.yanhe.recruit.tv.manage.LocalStoreManager;
import com.yanhe.recruit.tv.service.SocketBinder;
import com.yanhe.recruit.tv.service.SocketService;
import com.yanhe.recruit.tv.utils.StringUtils;

/**
 * @author mx
 */
public class MainActivity extends AppCompatActivity {
    FrameLayout flClient;
    SocketReceiver socketReceiver;
    private boolean mBound = false;
    private SocketBinder mSocketBinder;
    private HomeFragment homeFragment;
    FragmentTransaction mTransation;
    FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastActions.TV_CONFIG_MSG);
        filter.addAction(BroadcastActions.NEED_CONFIG_MSG);
        socketReceiver = new SocketReceiver();
        registerReceiver(socketReceiver, filter);
    }

    protected void initView() {
        flClient = findViewById(R.id.fl_client);
        homeFragment = new HomeFragment();
        mFragmentManager = getSupportFragmentManager();
        mTransation = mFragmentManager.beginTransaction();
        mTransation.add(R.id.fl_client, homeFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(socketReceiver);
        socketReceiver = null;
        super.onDestroy();
    }

    protected class SocketReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("socketReceiver recv:%s", intent.getAction());
            String action = intent.getAction();
            Bundle data = intent.getExtras();
            switch (action) {
                case BroadcastActions.TV_CONFIG_MSG:
                    String roomCode = data.getString("roomCode");
                    String code = data.getString("code");
                    Logger.d("roomCode:%s, code: %s", roomCode, code);
                    if (!StringUtils.isEmpty(roomCode) && !StringUtils.isEmpty(code)) {
                        LocalStoreManager store = LocalStoreManager.getInstance();
                        store.write("room", roomCode);
                        store.write("position", code);
                        if (mSocketBinder != null) {
                            mSocketBinder.reconnect(roomCode, code);
                        }
                        mFragmentManager = MainActivity.this.getSupportFragmentManager();
                        mTransation = mFragmentManager.beginTransaction();
                        mTransation.replace(R.id.fl_client, homeFragment).commit();
                    }
                    break;
                case BroadcastActions.NEED_CONFIG_MSG:
                    String socketId = data.getString("socketId");
                    Logger.d("socketReceiver socketId:%s", socketId);
                    ConfigFragment configFragment = ConfigFragment.newInstance(socketId);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, configFragment).commit();
                    break;
                    default:
            }
        }
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSocketBinder = (SocketBinder)service;
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mBound) {
                mBound = false;
                mSocketBinder = null;
            }
        }
    };

}
