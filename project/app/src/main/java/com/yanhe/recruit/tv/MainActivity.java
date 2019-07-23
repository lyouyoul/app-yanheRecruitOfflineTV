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

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yanhe.recruit.tv.constent.BroadcastActions;
import com.yanhe.recruit.tv.fragments.CompanyFragment;
import com.yanhe.recruit.tv.fragments.ConfigFragment;
import com.yanhe.recruit.tv.fragments.HomeFragment;
import com.yanhe.recruit.tv.fragments.ImageAdFragment;
import com.yanhe.recruit.tv.fragments.IntentFragment;
import com.yanhe.recruit.tv.fragments.OfflineSignupRQFragment;
import com.yanhe.recruit.tv.fragments.PositionRecruitRQFragment;
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
        filter.addAction(BroadcastActions.POSITION_RECRUIT_RQ_MSG);
        filter.addAction(BroadcastActions.COMPANY_SHOWING_RQ_MSG);
        filter.addAction(BroadcastActions.OFFLINE_SIGNUP_RQ_MSG);
        filter.addAction(BroadcastActions.URL_SHOWING_RQ_MSG);
        filter.addAction(BroadcastActions.IMG_SHOWING_RQ_MSG);
        socketReceiver = new SocketReceiver();
        registerReceiver(socketReceiver, filter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
        }
    }

    protected void initView() {
        flClient = findViewById(R.id.fl_client);
        homeFragment = new HomeFragment();
        mFragmentManager = getSupportFragmentManager();
        mTransation = mFragmentManager.beginTransaction();
        mTransation.add(R.id.fl_client, homeFragment).commit();
       /* String anim ="1";
        String sleep = "1000";
        String timeout = "6000";
        String imgs = "[{\"url\":\"https://img-bss.csdn.net/1563440498324.jpg\"},{\"url\":\"https://img-bss.csdn.net/1563440498324.jpg\"}]";
        Logger.d("anim:%s, sleep: %s , timeout: %s ,imgs:%s", anim, sleep,timeout,imgs);
        ImageAdFragment imageAdFragment = ImageAdFragment.newInstance(anim,sleep,timeout,imgs);
        mFragmentManager = MainActivity.this.getSupportFragmentManager();
        mTransation = mFragmentManager.beginTransaction();
        mTransation.replace(R.id.fl_client, imageAdFragment).commit();*/
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
            LocalStoreManager store = LocalStoreManager.getInstance();
            Logger.d("socketReceiver recv:%s", intent.getAction());
            String action = intent.getAction();
            Bundle data = intent.getExtras();
            switch (action) {
                case BroadcastActions.TV_CONFIG_MSG:
                    String roomCode = data.getString("roomCode");
                    String code = data.getString("code");
                    Logger.d("roomCode:%s, code: %s", roomCode, code);
                    if (!StringUtils.isEmpty(roomCode) && !StringUtils.isEmpty(code)) {
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
                    store.write("previousPage",BroadcastActions.NEED_CONFIG_MSG);
                    String socketId = data.getString("socketId");
                    Logger.d("socketReceiver socketId:%s", socketId);
                    ConfigFragment configFragment = ConfigFragment.newInstance(socketId);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, configFragment).commit();
                    break;
                case BroadcastActions.POSITION_RECRUIT_RQ_MSG:
                    store.write("previousPage",BroadcastActions.POSITION_RECRUIT_RQ_MSG);
                    String recruitId = data.getString("recruitId");
                    String companyId = data.getString("companyId");
                    Logger.d("recruitId:%s, companyId: %s", recruitId, companyId);
                    PositionRecruitRQFragment rqFragment = PositionRecruitRQFragment.newInstance(recruitId,companyId);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, rqFragment).commit();
                    break;
                case BroadcastActions.OFFLINE_SIGNUP_RQ_MSG:
                    store.write("previousPage",BroadcastActions.OFFLINE_SIGNUP_RQ_MSG);
                    String recruitIdOffline = data.getString("recruitId");
                    String companyIdOffline = data.getString("companyId");
                    Logger.d("recruitId:%s, companyId: %s", recruitIdOffline, companyIdOffline);
                    OfflineSignupRQFragment offlineSignupRQFragment = OfflineSignupRQFragment.newInstance(recruitIdOffline,companyIdOffline);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, offlineSignupRQFragment).commit();
                    break;
                case BroadcastActions.COMPANY_SHOWING_RQ_MSG:
                    store.write("previousPage",BroadcastActions.COMPANY_SHOWING_RQ_MSG);
                    String recruitIdCom = data.getString("recruitId");
                    String companyIdCom = data.getString("companyId");
                    String contextCom = data.getString("context");
                    Logger.d("recruitId:%s, companyId: %s , context: %s", recruitIdCom, companyIdCom,contextCom);
                    CompanyFragment companyFragment = CompanyFragment.newInstance(recruitIdCom,companyIdCom,contextCom);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, companyFragment).commit();
                    break;
                case BroadcastActions.URL_SHOWING_RQ_MSG:
                    store.write("previousPage",BroadcastActions.URL_SHOWING_RQ_MSG);
                    String url = data.getString("url");
                    Logger.d("url:%s", url);
                    IntentFragment intentFragment = IntentFragment.newInstance(url);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, intentFragment).commit();
                    break;
                case BroadcastActions.IMG_SHOWING_RQ_MSG:
                    String anim = data.getString("anim");
                    String sleep = data.getString("sleep");
                    String timeout = data.getString("timeout");
                    String imgs = data.getString("imgs");
                    Logger.d("anim:%s, sleep: %s , timeout: %s ,imgs:%s", anim, sleep,timeout,imgs);
                    ImageAdFragment imageAdFragment = ImageAdFragment.newInstance(anim,sleep,timeout,imgs);
                    mFragmentManager = MainActivity.this.getSupportFragmentManager();
                    mTransation = mFragmentManager.beginTransaction();
                    mTransation.replace(R.id.fl_client, imageAdFragment).commit();
                    break;
                default:
                    break;
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
