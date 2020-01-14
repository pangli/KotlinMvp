package com.zorro.kotlin.baselibs.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zorro on 2019/4/22 17:51
 * 备注：  日期处理类
 */
public class DateUtils {
    /**
     * 获取两个日期字符串之间的日期集合
     *
     * @param startDateString:String==null?使用当前时间前50天:startDateString（"2019年04月22日"）
     * @param endDateString:String==null?使用当前时间后50天:endDateString（"2020年04月22日"）
     * @return list:yyyy年MM月dd日
     */
    public static List<String> getBetweenDate(String startDateString, String endDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        if (startDateString == null) {
//            startDateString = sdf.format(new Date());
            startDateString = getBeforeTheCurrentDateString(50);
        }
        if (endDateString == null) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.YEAR, 1);
//            endDateString = sdf.format(calendar.getTime());
            endDateString = getAfterTheCurrentDateString(50);
        }
        // 声明保存日期集合
        List<String> list = new ArrayList<>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startDateString);
            Date endDate = sdf.parse(endDateString);
            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取当前日期前n天的日期
     *
     * @param day 之前多少天
     * @return "2019年04月22日"
     */
    public static String getBeforeTheCurrentDateString(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - day);
        calendar.add(Calendar.DAY_OF_YEAR, -day);
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前日期后n天的日期
     *
     * @param day 之后多少天
     * @return "2019年06月22日"
     */
    public static String getAfterTheCurrentDateString(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return sdf.format(calendar.getTime());
    }

    /**
     * 计算两个日期相差天数
     *
     * @param startDateString（"2019年04月22日"）
     * @param endDateString?（"2020年04月22日"）如果为空则用当前日期进行计算
     * @return 返回两个日期相差天数
     */
    public static int getDateDifference(String startDateString, String endDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        try {
            Date date1 = sdf.parse(startDateString);
            Date date2;
            if (TextUtils.isEmpty(endDateString)) {
                date2 = sdf.parse(sdf.format(new Date()));
            } else {
                date2 = sdf.parse(endDateString);
            }
            long diff = date2.getTime() - date1.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取一天24小时
     *
     * @return 9-18点小时集合
     */
    public static List<String> getHour() {
        List<String> list = new ArrayList<>();
        for (int i = 9; i < 19; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    /**
     * 获取一天24小时
     *
     * @return 小时集合
     */
    public static List<String> get24Hour() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    /**
     * 获取8个小时
     *
     * @return 0.5-8小时集合
     */
    public static List<String> getStepHour() {
        List<String> list = new ArrayList<>();
        for (float i = 0.5f; i < 8.5; i = i + 0.5f) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 获取8个小时
     *
     * @return 4-8小时集合
     */
    public static List<String> getStepOneHour() {
        List<String> list = new ArrayList<>();
        for (int i = 4; i < 9; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 获取一小时60分钟
     *
     * @return 60分钟集合
     */
    public static List<String> getMinute() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add(String.valueOf(i));
            }
        }
        return list;
    }

    /**
     * 获取一小时60分钟
     *
     * @return 60分钟30分步长集合
     */
    public static List<String> getStepMinute() {
        String[] stepMinute = {"00", "30"};
        return Arrays.asList(stepMinute);
    }

    /**
     * 日期格式转换
     *
     * @param dateString "yyyy年MM月dd日 HH:mm"
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String dateTimeFormatTransform(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 日期格式转换
     *
     * @param dateString "yyyy-MM-dd"
     * @return "MM-dd"
     */
    public static String dateFormatTransform(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 日期格式转换
     *
     * @param dateString "yyyy年MM月dd"
     * @return "MM-dd"
     */
    public static String getEventTime(String dateString,String whichDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (getCurrentDateWithChina().equals(dateString)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日", Locale.getDefault());
            return "今天  "+simpleDateFormat.format(date)+"  "+whichDay;
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年  MM月dd日", Locale.getDefault());
            return simpleDateFormat.format(date);
        }


    }


    /**
     * 获取当前日期
     *
     * @return "yyyy-MM-dd"
     */
    public static String getCurrentDateWithChina() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return "yyyy-MM-dd"
     */
    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return "yyyy-MM-dd hh:mm:ss"
     */
    public static String getCurrentDateWithHour() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return "yyyyMMddhhmmss"
     */
    public static String getQiniuCurrentDateWithHour() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return "yyyyMMdd"
     */
    public static String getQiniuCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    /**
     * 日期Date转换为自己想要的格式
     *
     * @param pattern "yyyy-MM-dd"
     * @param date    {@link Date}
     * @return "yyyy-MM-dd"
     */
    public static String getTime(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(date);
    }

    /**
     * 日期Date转换为自己想要的格式
     *
     * @param date {@link Date}
     * @return "yyyy-MM-dd"
     */
    public static int getAge(Date date) {
        return new Date().getYear() - date.getYear();
    }

    /**
     * 日期Date转换为自己想要的格式
     *
     * @param birthDay {@link String}
     * @return "yyyy-MM-dd"
     */
    public static int getAge(Date birthDay, int month) throws Exception {

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);

        if (monthBirth + month <= 12) {
            monthBirth = monthBirth + month;
        } else {
            monthBirth = monthBirth + month - 12;
            yearBirth = yearBirth + 1;
        }
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;

    }
}
