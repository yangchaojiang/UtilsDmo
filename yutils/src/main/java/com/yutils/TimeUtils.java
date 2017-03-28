package com.yutils;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ice bing1990 on 2016/3/11.
 * 时间格式帮助类
 */
public class TimeUtils extends CountDownTimer {
    private static final String DATE_TYPE_YDS = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TYPE_YD = "yyyy-MM-dd";
    private static final String DATE_TYPE_YDM = "yyyy-MM-dd HH:mm";
    private static final String DATE_TYPE_YDS_2 = "yyyy年MM月dd天HH:mm";
    private static final String DATE_TYPE_YD_CN = "yyyy年MM月";
    public Context context;
    private TextView showTimeView;

    public TimeUtils(long millisInFuture, long countDownInterval, View view, Context context) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.context = context;
        showTimeView = (TextView) view;
    }

    /**
     * 初始化时间格式  用于展示视频时长的时间
     *
     * @param time 秒
     * @return String
     */
    public static String formetVideoTime(int time) {//
        String timeStr;
        String hour, min = "", second;

        if (time >= 3600) {
            hour = time % 3600 > 9 ? time % 3600 + "" : "0" + (time % 3600);
        } else {
            hour = "00";
        }
        if (time % 3600 < 60) {
            min = "00";
        } else if (time % 3600 / 60 > 0 && time % 3600 / 60 < 10) {
            min = "0" + time % 3600 / 60;
        } else if (time % 3600 / 60 >= 10) {
            min = time % 3600 / 60 + "";
        }
        second = time % 3600 % 60 > 9 ? time % 3600 % 60 + "" : "0" + time % 3600 % 60;
        timeStr = hour + ":" + min + ":" + second;
        return timeStr;
    }

    /**
     * 完整时间 精确到秒
     *
     * @param date 格式化字符
     * @return Date
     **/
    public static Date getDateTime(String date) {
        try {
            SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
            formatter.applyLocalizedPattern(DATE_TYPE_YDS);
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }

    }

    /**
     * 将秒转成分秒
     *
     * @param time 秒
     * @return String
     */
    public static String getVoiceRecorderTime(int time) {
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
    public static String getVideoTime(int time) {
         StringBuilder softReference =new StringBuilder();
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
    public static String getVideoTime2(Context context, int time) {
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
    public static String getTalkTime(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return getDataString1(new Date());
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

    /*****
     * 得到完整的时间字符  英文 到秒
     *
     * @param date 格式化的时间
     * @return String
     **/
    public static String getDataTimeString(Date date) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YDS);
        return formatter.format(date);
    }


    /*****
     * 得到完整的时间字符  中文 到秒
     *
     * @param date 格式化的时间字符
     * @return String
     **/
    public static String getDataTimeCNString(Date date) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YDS_2);
        return formatter.format(date);
    }

    /*****
     * 得到完整的时间字符  - 天
     *
     * @param date 格式化的时间字符
     * @return String
     **/
    public static String getDataMString(String date) {
        if (date == null || date.isEmpty()) {
            return getDataString1(new Date());
        }
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YD);
        String timeString;
        Date date2 = getDateTime(date);
        timeString = getDataString1(date2);
        return timeString;
    }

    private static String getDataString1(Date date) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YD);
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
        return getDateTime(formatter.format(date));
    }

    /*****
     * 得到完整的时间字符
     *
     * @param date 格式化的时间
     **/
    public static String getDataBranchString(String date) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YDM);
        if (date == null || date.isEmpty()) {
            return formatter.format(new Date());
        }
        String timeString;
        try {
            Date date2 = formatter.parse(date);
            timeString = formatter.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
            return formatter.format(new Date());
        }
        return timeString;
    }

    @Override
    public void onFinish() {//计时完毕时触发
        showTimeView.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        showTimeView.setText(context.getResources().getText(R.string.wb_get_verification_code));
        showTimeView.setClickable(true);
    }


    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
        showTimeView.setClickable(false);
        showTimeView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        showTimeView.setText("重新获取(" + millisUntilFinished / 1000 + "秒)");
    }


    /*****
     * 得到中文年月的时间字符
     *
     * @param date 格式化的时间
     **/
    public static String getDateYmString(String date) {
        SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        formatter.applyLocalizedPattern(DATE_TYPE_YD_CN);
        if (date == null || date.isEmpty()) {
            return formatter.format(new Date());
        }
        String timeString;
        Date date2 = getDateTime(date);
        timeString = formatter.format(date2);
        return timeString;
    }

}
