package com.yanhe.recruit.tv.manage;

import android.app.Activity;
import android.content.Context;

import com.yanhe.recruit.tv.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:wang_sir
 * Time:2018/6/7 20:15
 * Description:This is ActivityManager
 */
public class ActivityManager {

    private List<Activity> activityList = new ArrayList<>();

    private ActivityManager() {
        super();
    }

    public static ActivityManager getInstance() {
        return ActivityManagerHolder.Instantce;
    }

    /**
     * 静态内部类获取单例
     */
    static class ActivityManagerHolder {
        public static ActivityManager Instantce = new ActivityManager();

    }

    /**
     * 添加activity
     * @param activity
     */
    public void addActivity(Activity activity){
        int idx = activityList.indexOf(activity);
        if (idx == -1) {
            activityList.add(activity);
        } else {
            activityList.remove(activity);
            activityList.add(activity);
        }
    }

    /**
     * 移除activity
     * @param activity
     */
    public void removeActivity(Activity activity){
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    public Activity getTopActivity() {
        if (activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        } else {
            return null;
        }
    }

    public Activity getPrevActivity() {
        if (activityList.size() > 1) {
            return activityList.get(activityList.size() - 2);
        } else {
            return null;
        }
    }

    /**
     * 获取顶层context
     * @return
     */
    public Context getTopContext() {
        Activity topActivity = getTopActivity();
        if (topActivity == null) {
            return App.getContext();
        } else {
            return topActivity;
        }
    }

    /**
     * 关闭所有的activity，退出应用
     */
    public void finishActivitys() {
        if (activityList != null&&!activityList.isEmpty()) {
            for (Activity activity1 : activityList) {
                activity1.finish();
            }
            activityList.clear();
        }
    }

}