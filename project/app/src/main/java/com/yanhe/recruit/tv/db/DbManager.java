package com.yanhe.recruit.tv.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yanhe.recruit.tv.App;
import com.yanhe.recruit.tv.utils.StringUtils;

/**
 * 数据库管理器
 * @author yangtxiang
 */
public class DbManager extends Object {
    protected DbHelper dbHelper;
    protected SQLiteDatabase db;
    private boolean isDisposing = false;

    public DbManager () {
        dbHelper = new DbHelper(App.getContext());
        db = dbHelper.getWritableDatabase();
        isDisposing = false;
    }

    @Override
    protected void finalize () throws Throwable {
        close();
        super.finalize();
    }

    public DbHelper getHelper () {
        return dbHelper;
    }

    public SQLiteDatabase getDb () {
        return db;
    }

    public void close () {
        if (!isDisposing) {
            isDisposing = true;
            db.close();
        }
    }

    /**
     * 检查表中是否存在记录
     * @param tableName
     * @param where
     * @param params
     * @return
     */
    public boolean exists(String tableName, String where, String[] params) {
        String szSql = " select count(*) from " + tableName;
        if (!StringUtils.isEmpty(where)) {
            szSql += " where " + where;
        }
        Cursor cr = db.rawQuery(szSql, params);
        if (cr.moveToNext()) {
            return cr.getInt(0) > 0;
        }
        return false;
    }

}
