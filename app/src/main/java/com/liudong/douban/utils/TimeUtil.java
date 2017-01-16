package com.liudong.douban.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liudong on 17-1-11.
 * 时间格式处理
 */

public class TimeUtil {
    private static final long ONE_MINUTE_MILLIONS = 60 * 1000;
    private static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
    private static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

    public static String getShortTime(String dateStr) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = sdf.parse(dateStr);
            Date curDate = new Date();
            long durTime = curDate.getTime() - date.getTime();
            int dayStatus = calculateDayStatus(date, curDate);
            int monthStatus = calculateMonthStatus(date, curDate);
            if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
                str = "刚刚";
            } else if (durTime < ONE_HOUR_MILLIONS) {
                str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
            } else if (dayStatus == 0) {
                str = durTime / ONE_HOUR_MILLIONS + "小时前";
            } else if (dayStatus == -1) {
                str = "1天前";
            } else if (monthStatus == 0 && dayStatus < -1) {
                str = durTime / ONE_DAY_MILLIONS + "天前";
            } else {
                str = dateStr.substring(0, 10);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    //判断是否同一个月
    private static int calculateMonthStatus(Date targetTime, Date compareTime) {
        Calendar tar = Calendar.getInstance();
        tar.setTime(targetTime);
        int tarMonth = tar.get(Calendar.MONTH);

        Calendar compare = Calendar.getInstance();
        compare.setTime(compareTime);
        int comMonth = tar.get(Calendar.MONTH);

        return tarMonth - comMonth;
    }

    //判断是否同一天
    private static int calculateDayStatus(Date targetTime, Date compareTime) {
        Calendar tar = Calendar.getInstance();
        tar.setTime(targetTime);
        int tarDayOfYear = tar.get(Calendar.DAY_OF_YEAR);

        Calendar compare = Calendar.getInstance();
        compare.setTime(compareTime);
        int comDayOfYear = compare.get(Calendar.DAY_OF_YEAR);

        return tarDayOfYear - comDayOfYear;
    }
}
