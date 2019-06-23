package com.yanhe.recruit.tv.manage;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 消息接收管理者, 单实例
 * @author yangtxiang
 */
public class EmitManager {
    public static EmitManager                         emitManager;
    private       Map<String, Map<Object, ReceiverItem>> receivers;
    private EmitManager () {
        receivers = new HashMap<>();
    }

    public static EmitManager getInstance () {
        if (emitManager == null) {
            emitManager = new EmitManager();
        }
        return emitManager;
    }

    public void on (String msg, Object instance, EmitReceiver receiver) {
        Map<Object, ReceiverItem> items;
        if (receivers.containsKey(msg)) {
            items = receivers.get(msg);
        } else {
            items = new HashMap<>(0);
        }
        if (!items.containsKey(instance)) {
            items.put(instance, new ReceiverItem(receiver));
        }
        receivers.put(msg, items);
    }

    public void once (String msg, Object instance, EmitReceiver receiver) {
        Map<Object, ReceiverItem> items;
        if (receivers.containsKey(msg)) {
            items = receivers.get(msg);
        } else {
            items = new HashMap<>(0);
        }
        if (!items.containsKey(instance)) {
            items.put(instance, new ReceiverItem(receiver, ReceiverItemType.ONCE));
        }
        receivers.put(msg, items);
    }

    public void off (String msg, Object instance) {
        if (receivers.containsKey(msg)) {
            Map<Object, ReceiverItem> items = receivers.get(msg);
            if (items.containsKey(instance)) {
                items.remove(instance);
                receivers.put(msg, items);
            }
        }
    }

    public void onMessage (String msg, Bundle data) {
        if (receivers.containsKey(msg)) {
            Map<Object, ReceiverItem> items = receivers.get(msg);
            Iterator itr = items.entrySet().iterator();
            List<Object> dropItems = new ArrayList<>();
            while (itr.hasNext()) {
                Map.Entry<Object, ReceiverItem> item = (Map.Entry<Object, ReceiverItem>)itr.next();
                item.getValue().receiver.onMessage(data);
                if (item.getValue().type == ReceiverItemType.ONCE) {
                    dropItems.add(item.getKey());
                }
            }
            for (Object item: dropItems) {
                items.remove(item);
            }
            if (dropItems.size() > 0) {
                receivers.put(msg, items);
            }
        }
    }

    enum ReceiverItemType {
        /** 只使用一次 */
        ONCE,
        /** 解绑 */
        OFF
    }

    class ReceiverItem {
        ReceiverItemType type = ReceiverItemType.OFF;
        EmitReceiver receiver;
        public ReceiverItem(EmitReceiver receiver) {
            this.type = ReceiverItemType.OFF;
            this.receiver = receiver;
        }

        public ReceiverItem(EmitReceiver receiver, ReceiverItemType type) {
            this.type = type;
            this.receiver = receiver;
        }
    }
}
