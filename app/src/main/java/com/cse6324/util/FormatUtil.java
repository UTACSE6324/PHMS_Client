package com.cse6324.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jarvis on 2017/2/10.
 */

public class FormatUtil {
    public static boolean checkEmail(String email) {
        String format = "[a-zA-Z0-9._]{1,20}[@][a-z0-9]{3,}[.]\\p{Lower}{2,}";
        //p{Alpha}:内容是必选的，和字母字符[\p{Lower}\p{Upper}]等价。如：200896@163.com不是合法的。
        //w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的。
        //[a-z0-9]{3,}：至少三个[a-z0-9]字符,[]内的是必选的；如：dyh200896@16.com是不合法的。
        //[.]:'.'号时必选的； 如：dyh200896@163com是不合法的。
        //p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的。
        if (email.matches(format)) {
            return true;// 邮箱名合法，返回true
        } else {
            return false;// 邮箱名不合法，返回false
        }
    }

    public static String getCurrentDateAll() {
        Calendar cal = Calendar.getInstance();

        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format1.format(date);
    }

    public static String getDateOffset(Calendar acal,int offset) {
        Calendar cal = Calendar.getInstance();
        cal.set(acal.get(Calendar.YEAR),acal.get(Calendar.MONTH),acal.get(Calendar.DATE));
        cal.add(Calendar.DAY_OF_MONTH, offset);

        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(date);
    }

    public static String getCurrentDateOffset(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, offset);

        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(date);
    }

    public static String getDate(Calendar cal){
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(date);
    }

    public static Calendar[] getWeekOfDate(Calendar cal){
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if(dayOfWeek == -1){
            dayOfWeek = 6;
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        Calendar[] list = new Calendar[2];

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.set(year, month, date - dayOfWeek);
        list[0] = startCal;
        endCal.set(year, month, date + 6 - dayOfWeek);
        list[1] = endCal;

        return list;
    }

    public static Calendar[] getMonthOfDate(Calendar cal){
        Calendar[] list = new Calendar[2];
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.set(year, month, date);
        startCal.set(Calendar.DATE, 1);
        list[0] = startCal;

        endCal.set(year, month, date);
        endCal.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        endCal.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        list[1] = endCal;

        return list;
    }

    public static int getYearOfStr(String str) {
        return Integer.parseInt(str.split("-")[0]);
    }

    public static int getMonthOfStr(String str) {
        return Integer.parseInt(str.split("-")[1]);
    }

    public static int getDayOfStr(String str) {
        return Integer.parseInt(str.split("-")[2]);
    }

}
