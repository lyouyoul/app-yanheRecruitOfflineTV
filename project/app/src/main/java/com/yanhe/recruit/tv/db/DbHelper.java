package com.yanhe.recruit.tv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类
 * @author yangtxiang
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mnyun.db";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS setting(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                           "  keyName VARCHAR," +
                           "  value TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS user(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                           " userName VARCHAR," +
                           " header VARCHAR," +
                           " password VARCHAR)");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
