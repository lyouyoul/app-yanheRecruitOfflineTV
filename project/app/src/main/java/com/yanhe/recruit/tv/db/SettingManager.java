package com.yanhe.recruit.tv.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * @author mx
 */
public class SettingManager extends DbManager {
    private final static String TABLE_NAME = "setting";

    /**
     * 保存配置
     * @param keyName
     * @param value
     */
    public void write (String keyName, String value) {
        ContentValues values = new ContentValues();
        values.put("value", value);
        if (exists(TABLE_NAME, " keyName = ? ", new String[]{ keyName })) {
            db.update(TABLE_NAME, values, "keyName=?", new String[]{keyName});
        } else {
            values.put("keyName", keyName);
            db.insert(TABLE_NAME, null, values);
        }
    }

    /**
     * 读取配置
     * @param keyName
     * @param defaultValue
     * @return
     */
    public String read (String keyName, String defaultValue) {
        if (exists(TABLE_NAME, " keyName = ? ", new String[]{ keyName })) {
            Cursor cr = db.rawQuery("select value from " + TABLE_NAME + " where keyName = ?", new String[]{keyName});
            if (cr.moveToNext()) {
                return cr.getString(0);
            }
        }
        return defaultValue;
    }
}
