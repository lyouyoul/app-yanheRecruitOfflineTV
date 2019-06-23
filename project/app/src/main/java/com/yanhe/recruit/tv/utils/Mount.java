package com.yanhe.recruit.tv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yangtxiang
 */
public class Mount {

    public enum DateParts {
        /** 年份 */
        year,
        /** 月份 */
        month,
        /** 日期 */
        day,
        /** 小时 */
        hour,
        /** 分钏 */
        minute,
        /** 秒钟 */
        second
    }

    public static final Date   MIN_DATE         = new Date(0);
    public static final String DATE_FORMAT      = "yyyy-MM-dd";
    public static final String DATE_LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Date     value;
    private Calendar calendar = Calendar.getInstance();

    /**
     * 无参数构造器，初始化为当前日期
     */
    public Mount () {
        value = new Date();
        calendar.setTime(value);
    }

    public Mount (Date value) {
        this.value = value;
        calendar.setTime(value);
    }

    public Mount (String value) {
        SimpleDateFormat df = new SimpleDateFormat();
        try {
            this.value = df.parse(value);
        }
        catch (ParseException e) {
            this.value = MIN_DATE;
        }
        calendar.setTime(this.value);
    }

    public Mount (String value, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            this.value = df.parse(value);
        }
        catch (ParseException e) {
            this.value = MIN_DATE;
        }
        calendar.setTime(this.value);
    }

    public Mount (int year, int month, int day) {
        calendar.set(year, month - 1, day);
        value = calendar.getTime();
    }

    public Mount (int year, int month, int day, int hour, int minute) {
        calendar.set(year, month - 1, day, hour, minute);
    }

    public Mount (int year, int month, int day, int hour, int minute, int second) {
        calendar.set(year, month - 1, day, hour, minute, second);
        value = calendar.getTime();
    }

    /**
     * 复制日期
     *
     * @param src
     *
     * @return
     */
    public Mount assign (Mount src) {
        value = src.value;
        calendar.setTime(value);
        return this;
    }

    /**
     * 复制到目标
     *
     * @param dest
     *
     * @return
     */
    public Mount assignTo (Mount dest) {
        dest.value = value;
        dest.calendar.setTime(value);
        return this;
    }

    /**
     * 获取格式化后的时间
     *
     * @param format
     *
     * @return
     */
    public String format (String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(value);
    }

    /**
     * 增加日期
     *
     * @param part
     *         要增加的部份 年，月，日，小时，分，秒
     * @param value
     *
     * @return
     */
    public Mount add (DateParts part, int value) {
        calendar.setTime(this.value);
        switch (part) {
            case minute:
                calendar.add(Calendar.MINUTE, value);
                break;
            case hour:
                calendar.add(Calendar.HOUR, value);
                break;
            case day:
                calendar.add(Calendar.DAY_OF_MONTH, value);
                break;
            case month:
                calendar.add(Calendar.MONTH, value);
                break;
            case year:
                calendar.add(Calendar.YEAR, value);
                break;
            default:
                calendar.add(Calendar.SECOND, value);
        }
        this.value = calendar.getTime();
        return this;
    }

    /**
     * 计算时间差
     *
     * @param m2
     * @param part
     *         计算时间部份
     *
     * @return
     */
    public long diffFrom (Mount m2, DateParts part) {
        Calendar c1   = this.getCalendar();
        Calendar c2   = m2.getCalendar();
        long     diff = 0;
        long     milSecond;
        switch (part) {
            case year:
                diff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
                break;
            case month:
                int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
                diff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH) + yearDiff * 12;
                break;
            case day:
                milSecond = value.getTime() - m2.value.getTime();
                diff = (long) (milSecond / (24 * 60 * 60 * 1000));
                break;
            case hour:
                milSecond = value.getTime() - m2.value.getTime();
                diff = (long) (milSecond / (60 * 60 * 1000));
                break;
            case minute:
                milSecond = value.getTime() - m2.value.getTime();
                diff = (long) (milSecond / (60 * 1000));
                break;
            default:
                diff = (long) ((value.getTime() - m2.value.getTime()) / 1000);
        }
        return diff;
    }

    /**
     * 比较时间大小
     *
     * @param m2
     *
     * @return
     */
    public int compareTo (Mount m2) {
        Calendar c1 = getCalendar();
        Calendar c2 = m2.getCalendar();
        return c1.compareTo(c2);
    }

    /**
     * 获取时间
     *
     * @return
     */
    public Date getValue () {
        return value;
    }

    /**
     * 获取日历
     *
     * @return
     */
    public Calendar getCalendar () {
        calendar.setTime(value);
        return calendar;
    }

    public int getYear () {
        calendar.setTime(value);
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth () {
        calendar.setTime(value);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getDay () {
        calendar.setTime(value);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour () {
        calendar.setTime(value);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute () {
        calendar.setTime(value);
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond () {
        calendar.setTime(value);
        return calendar.get(Calendar.SECOND);
    }
}
