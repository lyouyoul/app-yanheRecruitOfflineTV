package com.yanhe.recruit.tv.manage;

import android.os.Bundle;

/**
 * emit订阅者
 * @author yangtxiang
 */
public interface EmitReceiver {
    void onMessage(Bundle data);
}
