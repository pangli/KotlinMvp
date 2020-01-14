package com.zorro.kotlin.baselibs.utils;

import android.content.Context;
import android.provider.Settings;

import com.zorro.kotlin.baselibs.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Zorro on 2019/12/11.
 * 备注：即时通讯时间展示规则
 */
public class IMDateUtils {
    private static final int OTHER = 2014;
    private static final int TODAY = 6;
    private static final int YESTERDAY = 15;

    public IMDateUtils() {
    }

    public static int judgeDate(Date date) {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.set(11, 0);
        calendarToday.set(12, 0);
        calendarToday.set(13, 0);
        calendarToday.set(14, 0);
        Calendar calendarYesterday = Calendar.getInstance();
        calendarYesterday.add(5, -1);
        calendarYesterday.set(11, 0);
        calendarYesterday.set(12, 0);
        calendarYesterday.set(13, 0);
        calendarYesterday.set(14, 0);
        Calendar calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.add(5, 1);
        calendarTomorrow.set(11, 0);
        calendarTomorrow.set(12, 0);
        calendarTomorrow.set(13, 0);
        calendarTomorrow.set(14, 0);
        Calendar calendarTarget = Calendar.getInstance();
        calendarTarget.setTime(date);
        if (calendarTarget.before(calendarYesterday)) {
            return 2014;
        } else if (calendarTarget.before(calendarToday)) {
            return 15;
        } else {
            return calendarTarget.before(calendarTomorrow) ? 6 : 2014;
        }
    }

    private static String getWeekDay(Context context, int dayInWeek) {
        String weekDay = "";
        switch (dayInWeek) {
            case 1:
                weekDay = context.getResources().getString(R.string.im_sunsay_format);
                break;
            case 2:
                weekDay = context.getResources().getString(R.string.im_monday_format);
                break;
            case 3:
                weekDay = context.getResources().getString(R.string.im_tuesday_format);
                break;
            case 4:
                weekDay = context.getResources().getString(R.string.im_wednesday_format);
                break;
            case 5:
                weekDay = context.getResources().getString(R.string.im_thuresday_format);
                break;
            case 6:
                weekDay = context.getResources().getString(R.string.im_friday_format);
                break;
            case 7:
                weekDay = context.getResources().getString(R.string.im_saturday_format);
        }

        return weekDay;
    }

    public static boolean isTime24Hour(Context context) {
        String timeFormat = Settings.System.getString(context.getContentResolver(), "time_12_24");
        return timeFormat != null && timeFormat.equals("24");
    }

    private static String getTimeString(long dateMillis, Context context) {
        if (dateMillis <= 0L) {
            return "";
        } else {
            Date date = new Date(dateMillis);
            String formatTime = null;
            if (isTime24Hour(context)) {
                formatTime = formatDate(date, "HH:mm");
            } else {
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.setTimeInMillis(dateMillis);
                int hour = calendarTime.get(10);
                if (calendarTime.get(9) == 0) {
                    if (hour < 6) {
                        if (hour == 0) {
                            hour = 12;
                        }

                        formatTime = context.getResources().getString(R.string.im_daybreak_format);
                    } else if (hour >= 6 && hour < 12) {
                        formatTime = context.getResources().getString(R.string.im_morning_format);
                    }
                } else if (hour == 0) {
                    formatTime = context.getResources().getString(R.string.im_noon_format);
                    hour = 12;
                } else if (hour >= 1 && hour <= 5) {
                    formatTime = context.getResources().getString(R.string.im_afternoon_format);
                } else if (hour >= 6 && hour <= 11) {
                    formatTime = context.getResources().getString(R.string.im_night_format);
                }

                int minuteInt = calendarTime.get(12);
                String minuteStr = Integer.toString(minuteInt);
                String timeStr = null;
                if (minuteInt < 10) {
                    minuteStr = "0" + minuteStr;
                }

                timeStr = Integer.toString(hour) + ":" + minuteStr;
                if (context.getResources().getConfiguration().locale.getCountry().equals("CN")) {
                    formatTime = formatTime + timeStr;
                } else {
                    formatTime = timeStr + " " + formatTime;
                }
            }

            return formatTime;
        }
    }

    private static String getDateTimeString(long dateMillis, boolean showTime, Context context) {
        if (dateMillis <= 0L) {
            return "";
        } else {
            String formatDate = null;
            Date date = new Date(dateMillis);
            int type = judgeDate(date);
            long time = System.currentTimeMillis();
            Calendar calendarCur = Calendar.getInstance();
            Calendar calendardate = Calendar.getInstance();
            calendardate.setTimeInMillis(dateMillis);
            calendarCur.setTimeInMillis(time);
            int month = calendardate.get(2);
            int year = calendardate.get(1);
            int weekInMonth = calendardate.get(4);
            int monthCur = calendarCur.get(2);
            int yearCur = calendarCur.get(1);
            int weekInMonthCur = calendarCur.get(4);
            switch (type) {
                case 6:
                    formatDate = getTimeString(dateMillis, context);
                    break;
                case 15:
                    String formatString = context.getResources().getString(R.string.im_yesterday_format);
                    if (showTime) {
                        formatDate = formatString + " " + getTimeString(dateMillis, context);
                    } else {
                        formatDate = formatString;
                    }
                    break;
                case 2014:
                    if (year == yearCur) {
                        if (month == monthCur && weekInMonth == weekInMonthCur) {
                            formatDate = getWeekDay(context, calendardate.get(7));
                        } else if (context.getResources().getConfiguration().locale.getCountry().equals("CN")) {
                            formatDate = formatDate(date, "M" + context.getResources().getString(R.string.im_month_format) + "d" + context.getResources().getString(R.string.im_day_format));
                        } else {
                            formatDate = formatDate(date, "M/d");
                        }
                    } else if (context.getResources().getConfiguration().locale.getCountry().equals("CN")) {
                        formatDate = formatDate(date, "yyyy" + context.getResources().getString(R.string.im_year_format) + "M" + context.getResources().getString(R.string.im_month_format) + "d" + context.getResources().getString(R.string.im_day_format));
                    } else {
                        formatDate = formatDate(date, "M/d/yy");
                    }

                    if (showTime) {
                        formatDate = formatDate + " " + getTimeString(dateMillis, context);
                    }
            }

            return formatDate;
        }
    }

    public static String getConversationListFormatDate(long dateMillis, Context context) {
        String formatDate = getDateTimeString(dateMillis, false, context);
        return formatDate;
    }

    public static String getConversationFormatDate(long dateMillis, Context context) {
        String formatDate = getDateTimeString(dateMillis, true, context);
        return formatDate;
    }

    public static boolean isShowChatTime(long currentTime, long preTime, int interval) {
        int typeCurrent = judgeDate(new Date(currentTime));
        int typePre = judgeDate(new Date(preTime));
        if (typeCurrent == typePre) {
            return currentTime - preTime > (long) (interval * 1000);
        } else {
            return true;
        }
    }

    public static String formatDate(Date date, String fromat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromat);
        return sdf.format(date);
    }
}
