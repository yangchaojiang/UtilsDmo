package com.yutils;

import android.support.annotation.NonNull;

/**
 * Created by yangjiang on 2017/1/6.
 * E-Mail:1007181167@qq.com
 * Description:  日志帮助类
 */
public final class Logger {
    public static boolean isPrint = true;
    private static String defaultTag = "Logger";

    private Logger() {
    }

    public static void setTag(@NonNull String tag) {
        defaultTag = tag;
    }

    public static int i(@NonNull Object o) {
        return isPrint ? android.util.Log.i(defaultTag, o.toString()) : -1;
    }

    public static int i(@NonNull String m) {
        return isPrint ? android.util.Log.i(defaultTag, m) : -1;
    }

    public static int v(@NonNull String tag,@NonNull String msg) {
        return isPrint ? android.util.Log.d(tag, msg) : -1;
    }

    public static int d(@NonNull String tag,@NonNull String msg) {
        return isPrint ? android.util.Log.d(tag, msg) : -1;
    }

    public static int i(@NonNull String tag,@NonNull String msg) {
        return isPrint ? android.util.Log.i(tag, msg) : -1;
    }

    public static int w(@NonNull String tag,@NonNull String msg) {
        return isPrint ? android.util.Log.w(tag, msg) : -1;
    }

    public static int e(@NonNull String tag,@NonNull String msg) {
        return isPrint ? android.util.Log.e(tag, msg) : -1;
    }

    public static int v(@NonNull String tag,@NonNull Object... msg) {
        return isPrint ? android.util.Log.v(tag, getLogMessage(msg)) : -1;
    }

    public static int d(@NonNull String tag,@NonNull Object... msg) {
        return isPrint ? android.util.Log.d(tag, getLogMessage(msg)) : -1;
    }

    public static int i(@NonNull String tag,@NonNull Object... msg) {
        return isPrint ? android.util.Log.i(tag, getLogMessage(msg)) : -1;
    }

    public static int w(@NonNull String tag,@NonNull Object... msg) {
        return isPrint ? android.util.Log.w(tag, getLogMessage(msg)) : -1;
    }

    public static int e(@NonNull String tag,@NonNull Object... msg) {
        return isPrint ? android.util.Log.e(tag, getLogMessage(msg)) : -1;
    }

    private static String getLogMessage(Object... msg) {
        if (msg != null && msg.length > 0) {
            StringBuilder sb = new StringBuilder();
            Object[] arr = msg;
            int len = msg.length;

            for (int i = 0; i < len; ++i) {
                Object s = arr[i];
                if ( s != null) {
                    sb.append(s.toString());
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public static int v(@NonNull String tag,@NonNull String msg, Throwable tr) {
        return isPrint ? android.util.Log.v(tag, msg, tr) : -1;
    }

    public static int d(@NonNull String tag,@NonNull String msg, Throwable tr) {
        return isPrint ? android.util.Log.d(tag, msg, tr) : -1;
    }

    public static int i(@NonNull String tag,@NonNull String msg, Throwable tr) {
        return isPrint ? android.util.Log.i(tag, msg, tr) : -1;
    }

    public static int w(@NonNull String tag,@NonNull String msg, Throwable tr) {
        return isPrint ? android.util.Log.w(tag, msg, tr) : -1;
    }

    public static int e(@NonNull String tag,@NonNull String msg, Throwable tr) {
        return isPrint ? android.util.Log.e(tag, msg, tr) : -1;
    }

    public static int v(@NonNull Object tag,@NonNull String msg) {
        return isPrint ? android.util.Log.v(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int d(@NonNull Object tag,@NonNull String msg) {
        return isPrint ? android.util.Log.d(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int i(@NonNull Object tag,@NonNull String msg) {
        return isPrint ? android.util.Log.i(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int w(@NonNull Object tag,@NonNull String msg) {
        return isPrint ? android.util.Log.w(tag.getClass().getSimpleName(), msg) : -1;
    }

    public static int e(@NonNull Object tag,@NonNull String msg) {
        return isPrint ? android.util.Log.e(tag.getClass().getSimpleName(), msg) : -1;
    }
}
