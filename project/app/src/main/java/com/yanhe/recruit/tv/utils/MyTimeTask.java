package com.yanhe.recruit.tv.utils;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimeTask {
    private Timer timer;
    private TimerTask task;
    private long time;

    public MyTimeTask(long time, TimerTask task) {
        this.task = task;
        this.time = time;
        if (timer == null){
            timer=new Timer();
        }
    }

    public void start(){
        //每隔time时间段就执行一次
        timer.schedule(task, time);
    }

    public void stop(){
        if (timer != null) {
            timer.cancel();
            if (task != null) {
                task.cancel();  //将原任务从队列中移除
            }
        }
    }
}