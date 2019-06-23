package com.yanhe.recruit.tv.manage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Emit广播接收分发器
 * @author yangtxiang
 */
public class EmitBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        String action = intent.getAction();
        if (Emitter.APP_EMIT_ACTION.equals(action)) {
            Bundle params = intent.getExtras();
            String msg = params.getString("msg");
            Bundle data = params.getBundle("data");
            EmitManager.getInstance().onMessage(msg, data);
        }
    }
}
