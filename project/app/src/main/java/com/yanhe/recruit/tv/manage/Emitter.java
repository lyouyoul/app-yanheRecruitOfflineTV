package com.yanhe.recruit.tv.manage;

import android.content.Intent;
import android.os.Bundle;

import com.mx.mnyun.App;

/**
 * 消息推送者
 * @author yangtxiang
 */
public class Emitter {
    public static final String APP_EMIT_ACTION = "com.mx.mnyun.intent.action.emit";

    /**
     * 广播消息
     * @param msg
     * @param data
     */
    public static void emit(String msg, Bundle data) {
        Intent intent = new Intent();
        intent.setAction(APP_EMIT_ACTION);
        Bundle params = new Bundle();
        params.putString("msg", msg);
        params.putBundle("data", data);
        intent.putExtras(params);
        App.getContext().sendBroadcast(intent);
    }
}
