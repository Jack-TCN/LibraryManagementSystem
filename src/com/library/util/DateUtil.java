package com.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期转字符串
     */
    public static String dateToString(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * 日期时间转字符串
     */
    public static String datetimeToString(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * 字符串转日期
     */
    public static Date stringToDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算两个日期之间的天数
     */
    public static int daysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0;
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long diff = Math.abs(time2 - time1);
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    /**
     * 增加天数
     */
    public static Date addDays(Date date, int days) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 获取当前日期
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 判断日期是否过期
     */
    public static boolean isOverdue(Date dueDate) {
        if (dueDate == null) return false;
        return dueDate.before(new Date());
    }
}
