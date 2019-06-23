package com.yanhe.recruit.tv.manage;

/**
 * 本地存储
 * @author yangtxiang
 */
public interface LocalStore {
    /**
     * 写入配置
     * @param key
     * @param value
     */
    void write(String key, Object value);

    /**
     * 读取配置
     * @param key
     * @param def
     * @return
     */
    Object read(String key, Object def);
}
