package com.yutils;

import android.content.Context;
import android.os.CountDownTimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ice bing1990 on 2016/3/11.
 * 时间格式帮助类
 */
public class TimeUtils {
    public static final String DATE_TYPE_YDS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TYPE_YD = "yyyy-MM-dd";
    public static final String DATE_TYPE_YDS_2 = "yyyy年MM月dd天HH:mm";
    public static final String DATE_TYPE_YD_CN = "yyyy年MM月";

    /****
     * 倒计时
     *
     * @param context           上下文
     * @param millisInFuture    参数依次为总时长  秒
     * @param countDownInterval 计时的时间间隔 秒
     ****/
    public static CountDownTimer countDown(final Context context, long millisInFuture, long countDownInterval, final CountDownListener listener) {
        CountDownTimer countDownTimer = new CountDownTimer(millisInFuture * 1000, countDownInterval * 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (listener != null && context != null) {
                    listener.onTick(millisUntilFinished, (context.getString(R.string.res_date) + "(" + millisUntilFinished / 1000 + ")" + context.getString(R.string.second)));
                }
            }

            @Override
            public void onFinish() {
                if (listener != null && context != null) {
                    listener.onFinish(context.getResources().getString(R.string.wb_get_verification_code));
                }
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }

    /***
     * 倒计时接口
     **/
    public interface CountDownListener {
        /***
         * 计时完毕时触发
         *
         * @param text 计时完毕时 默认文本
         */
        void onFinish(String text);

        /***
         * @param millisUntilFinished 计时过程数
         * @param text                计时过程显示文本
         **/
        void onTick(long millisUntilFinished, String text);
    }

    /**
     * 初始化时间格式  用于展示视频时长的时间
     *
     * @param time 秒
     * @return String
     */
    public static String  fromVideoTime(int time) {//
        StringBuffer stringBuilder = new StringBuffer();
        if (time >= 3600) {//大于一个小时
            stringBuilder.append(time % 3600 > 9 ? time % 3600 + "" : "0" + (time % 3600));
        }
        if (time % 3600 < 60) {
            stringBuilder.append("00");
        } else if (time % 3600 / 60 > 0 && time % 3600 / 60 < 10) {
            stringBuilder.append("0");
            stringBuilder.append(time % 3600 / 60);
        } else if (time % 3600 / 60 >= 10) {
            stringBuilder.append(time % 3600 / 60);
        }
        stringBuilder.append(time % 3600 % 60 > 9 ? time % 3600 % 60 + "" : "0" + time % 3600 % 60);
        return stringBuilder.toString();
    }



    /**
     * 将秒转成分秒
     *
     * @param time 秒
     * @return String
     */
    public static String  fromVoiceRecorderTime(int time) {
        int minute = time / 60;
        int second = time % 60;
        if (minute == 0) {
            return String.valueOf(time);
        }
        return minute + ":" + second;

    }

    /**
     * 将秒转成分秒   冒号的
     *
     * @param time 用于展示视频时长的时间
     * @return String
     */
    public static String  fromVideoColon(int time) {
        StringBuilder softReference = new StringBuilder();
        if (time == 0) {
            return "00:00";
        }
        if (time < 60) {
            softReference.append("00:");
            if (time < 10) {
                softReference.append("0");
                softReference.append(time);
            } else {
                softReference.append(time);
            }

        } else {
            int minute = time / 60;
            int second = time % 60;
            if (minute > 10) {
                softReference.append(minute);
                softReference.append(":");
            } else {
                softReference.append("0");
                softReference.append(minute);
                softReference.append(":");
            }
            if (second > 10) {
                softReference.append(second);
            } else {
                softReference.append("0");
                softReference.append(second);
            }
        }
        return softReference.toString();
    }

    /**
     * 将秒转成分秒  中文
     *
     * @param context 上下文
     * @param time    转化小数
     * @return String
     */
    public static String fromVideoZN(Context context, int time) {
        StringBuilder softReference = new StringBuilder();
        if (time == 0) {
            return context.getString(R.string.unknown);
        }
        if (time < 60) {
            softReference.append("00");
            softReference.append(context.getString(R.string.branch));
            if (time < 10) {
                softReference.append("0");
                softReference.append(time);
                softReference.append(context.getString(R.string.second));
            } else {
                softReference.append(time);
                softReference.append(context.getString(R.string.second));
            }

        } else {
            int minute = time / 60;
            int second = time % 60;
            if (minute > 10) {
                softReference.append(minute);
                softReference.append(context.getString(R.string.branch));
            } else {
                softReference.append("0");
                softReference.append(minute);
                softReference.append(context.getString(R.string.branch));
            }
            if (second > 10) {
                softReference.append(second);
                softReference.append(context.getString(R.string.second));
            } else {
                softReference.append("0");
                softReference.append(second);
                softReference.append(context.getString(R.string.second));
            }
        }
        return softReference.toString();
    }

    /***
     * 格式化时间
     *
     * @param timeString 需要格式化时间 .使用聊天展示时间
     **/
    public static String  fromTalkTime(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return formDateString(new Date(), DATE_TYPE_YD);
        }
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YD);
        try {
            String timeString1 = timeString.substring(0, 10);
            String today = formatter.format(new Date());
            //是否今天
            if (timeString1.equals(today)) {
                return timeString.substring(11, 16);
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            String yes = formatter.format(cal.getTime());
            //是否昨天
            if (timeString1.equals(yes)) {
                return "昨天 " + timeString.substring(11, 16);
            }
            return timeString.substring(0, 16);
        } catch (Exception e) {
            e.printStackTrace();
            return timeString;
        }
    }


    /***
     * 格式化时间
     *
     * @param longTime 毫秒
     */
    public static String fromDataTime(Long longTime) {
        StringBuilder stringBuilder = new StringBuilder();
        Calendar symbols = Calendar.getInstance();
        symbols.setTimeInMillis(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(longTime);
        if (calendar.get(Calendar.YEAR) != symbols.get(Calendar.YEAR)) {
            String test = SimpleDateFormat.getDateTimeInstance().format(calendar.getTime());
            stringBuilder.append(test);
        } else {
            if (calendar.get(Calendar.MONTH) != symbols.get(Calendar.MONTH) || calendar.get(Calendar.DAY_OF_MONTH) != symbols.get(Calendar.DAY_OF_MONTH)) {
                stringBuilder.append((calendar.get(Calendar.MONTH)));
                stringBuilder.append("-");
                stringBuilder.append(calendar.get(Calendar.DAY_OF_MONTH));
                stringBuilder.append(" ");
                stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
                stringBuilder.append(":");
                stringBuilder.append(calendar.get(Calendar.MINUTE));
            } else {
                if (calendar.get(Calendar.HOUR_OF_DAY) != symbols.get(Calendar.HOUR_OF_DAY)) {
                    int hour=symbols.get(Calendar.HOUR_OF_DAY) - calendar.get(Calendar.HOUR_OF_DAY);
                    if (hour<10){
                        stringBuilder.append(hour);
                        stringBuilder.append("小时前");
                    }else {
                        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
                        stringBuilder.append(":");
                        stringBuilder.append(calendar.get(Calendar.MINUTE));
                    }

                } else {
                    if (calendar.get(Calendar.MINUTE) != symbols.get(Calendar.MINUTE)) {
                        stringBuilder.append(symbols.get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE));
                        stringBuilder.append("分钟前");
                    } else {
                        int second = symbols.get(Calendar.SECOND) - calendar.get(Calendar.SECOND);
                        if (second < 10) {
                            stringBuilder.append("刚刚");
                        } else {
                            stringBuilder.append(second);
                            stringBuilder.append("秒前");
                        }
                    }
                }
            }
        }
        return stringBuilder.toString();
    }


    /*****
     * 得到完整的时间字符  英文 到秒
     *
     * @param date    格式化的时间
     * @param pattern 类型
     * @return String
     **/
    public static String formDateString(Date date, String pattern) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(pattern);
        return formatter.format(date);
    }


    /*****
     * 将毫秒数转换成时间
     *
     * @param date 转换毫秒数
     **/
    public static Date formTimeToDate(long date) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YDS);
        return formTimeToDate(formatter.format(date),DATE_TYPE_YDS);

    }
    /*****
     * 将毫秒数转换成时间
     *
     * @param date 转换毫秒数
     **/
    public static Date formTimeToDate(long date,String pattern) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(pattern);
        return formTimeToDate(formatter.format(date),pattern);
    }
    /**
     * 完整时间 精确到秒
     *
     * @param date 格式化字符
     * @return Date
     **/
    public static Date formTimeToDate(String date,String pattern) {
        try {
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
            formatter.applyLocalizedPattern(pattern);
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
