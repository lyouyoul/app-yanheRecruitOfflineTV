package com.yanhe.recruit.tv.manage;

import com.yanhe.recruit.tv.db.SettingManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 数据库本地存储
 */
public class DbLocalStore extends Object implements LocalStore {
    private SettingManager manager;

    public DbLocalStore () {
        super();
        manager = new SettingManager();
    }

    @Override
    protected void finalize () throws Throwable {
        manager.close();
        super.finalize();
    }

    @Override
    public void write (String key, Object value) {
        JSONObject json = new JSONObject();
        try {
            json.put("value", value);
            manager.write(key, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object read (String key, Object def) {
        String szJson = manager.read(key, "");
        if (!"".equals(szJson)) {
            try {
                JSONObject jsObj = new JSONObject(szJson);
                return jsObj.get("value");
            } catch (JSONException e) {
                return def;
            }
        }
        return def;
    }
}
