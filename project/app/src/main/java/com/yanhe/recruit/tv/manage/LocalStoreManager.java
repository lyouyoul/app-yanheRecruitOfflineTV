package com.yanhe.recruit.tv.manage;

/**
 * 本地存储管理器
 * @author yangtxiang
 */
public class LocalStoreManager {

    private static LocalStoreManager localStoreManager;
    private LocalStore localStore = null;

    public static LocalStoreManager getInstance() {
        if (localStoreManager == null) {
            localStoreManager = new LocalStoreManager();
        }
        return localStoreManager;
    }

    public void registerStore(LocalStore store) {
        localStore = store;
    }

    /**
     * 写入配置
     * @param key
     * @param value
     */
    public void write (String key, Object value) {
        if (localStore != null) {
            localStore.write(key, value);
        }
    }

    /**
     * 读取配置
     * @param key
     * @param def
     * @return
     */
    public Object read (String key, Object def) {
        if (localStore != null) {
            return localStore.read(key, def);
        }
        return def;
    }

}
