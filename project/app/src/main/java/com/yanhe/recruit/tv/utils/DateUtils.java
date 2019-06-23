package com.yanhe.recruit.tv.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author yangtxiang
 */
public class DateUtils {

    public static String formatDate(String format, Date value) {
        return new Mount(value).format(format);
    }

    /**
     * 字符串转日期
     * @param value 日期格式化字符串 默认使用长日期格式化字符串
     * @return 返回日期，如果失败将返回unix纪元日期
     */
    public static Date string2Date(String value) {
        return new Mount(value, Mount.DATE_LONG_FORMAT).getValue();
    }

    /**
     * 字符串传日期，如果失败返回unix纪元日期
     * @param value
     * @param format 格式化字符串
     * @return
     */
    public static Date string2Date(String value, String format) {
       return new Mount(value, format).getValue();
    }


    public static Date makeDate(int year, int month, int day) {
        return new Mount(year, month, day).getValue();
    }

    public static Calendar Date2Calendar(Date value) {
        return new Mount(value).getCalendar();
    }

    public static Calendar makeCalendar(int year, int month, int day) {
       return new Mount(year, month, day).getCalendar();
    }

}
