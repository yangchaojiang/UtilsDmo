package com.yutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangjiang on 2017/1/6.
 * E-Mail:1007181167@qq.com
 * Description:  帮助类
 */
public class YUtils {
    private static String TAG;
    private static boolean DEBUG = false;
    private static final ThreadLocal<Context> mApplicationContent = new ThreadLocal<>();
    private static Toast mToast = null;
    private static int gravity = Gravity.BOTTOM;

    public static void initialize(Application app) {
        mApplicationContent.set(app.getApplicationContext());

    }

    /****
     * 是否Debug模式
     *
     * @param isDebug true  调试
     * @param TAG     删除日志的TAG  名称
     ***/
    public static void setDebug(boolean isDebug, String TAG) {
        YUtils.TAG = TAG;
        YUtils.DEBUG = isDebug;
    }

    /***
     * 设置Toast出现位置
     *
     * @param gravity 设置Toast出现位置
     ***/
    public static void setGravity(int gravity) {
        YUtils.gravity = gravity;
    }

    /****
     * 打印日志
     *
     * @param TAG  自定义日志的TAG  名称
     * @param text 日志内容
     ***/
    public static void Log(String TAG, String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    /****
     * 打印日志
     *
     * @param text 日志内容
     ***/
    public static void Log(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    /****
     * toast 短提示封装
     *
     * @param text 提示内容 字符
     ***/
    public static void Toast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent.get(), text, android.widget.Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /****
     * toast 短提示封装
     *
     * @param text    提示内容 字符
     * @param gravity ttoast 出现位置
     ***/
    public static void Toast(String text, int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent.get(), text, android.widget.Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /****
     * toast提示封装
     *
     * @param resId 提示内容  资源Id
     ***/
    public static void Toast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent.get(), resId, android.widget.Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /****
     * toast 长提示封装
     *
     * @param text 提示内容 字符
     ***/
    public static void ToastLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent.get(), text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /****
     * toast 长提示封装
     *
     * @param resId 提示内容 资源id
     ***/
    public static void ToastLong(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent.get(), resId, Toast.LENGTH_LONG);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /**
     * dp转px
     *
     * @param dpValue dp单位
     */
    public static int dip2px(float dpValue) {
        final float scale = mApplicationContent.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp单位
     */
    public static int sp2px(float spValue) {
        final float fontScale = mApplicationContent.get().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px单位
     */
    public static int px2dip(float pxValue) {
        final float scale = mApplicationContent.get().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 取屏幕宽度
     *
     * @return int
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = mApplicationContent.get().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 取屏幕高度
     *
     * @return int
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = mApplicationContent.get().getResources().getDisplayMetrics();
        return dm.heightPixels - getStatusBarHeight();
    }

    /**
     * 取屏幕高度包含状态栏高度
     *
     * @return int
     */
    public static int getScreenHeightWithStatusBar() {
        DisplayMetrics dm = mApplicationContent.get().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 取导航栏高度
     *@param activity 上下文
     * @return int
     */
    public static int getNavigationBarHeight(Activity activity) {
        int result = 0;
        if (isNavigationBarExist2(activity)) {
            int resourceId = mApplicationContent.get().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = mApplicationContent.get().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 此方法在模拟器还是在真机
     *
     * @param activity 当前对象的上下文
     * @return boolean
     */
    public static boolean isNavigationBarExist2(Activity activity) {
        if (Build.VERSION.SDK_INT > 19) {
            WindowManager windowManager = activity.getWindowManager();
            Display d = windowManager.getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                d.getRealMetrics(realDisplayMetrics);
            }
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;
            return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            return checkDeviceHasNavigationBar(activity);
        }
    }

    /**
     * 检查设备是否有导航栏
     *
     * @param activity 当前对象的上下文
     * @return boolean
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {
//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = false;
        boolean hasBackKey = false;

        try {
            hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
            hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        } catch (java.lang.NoSuchMethodError e) {
            return false;
        }
        if (!hasMenuKey && !hasBackKey) {
// 做任何你需要做的,这个设备有一个导航栏
            return true;
        } else {
            return false;
        }

    }

    /**
     * 取状态栏高度
     *
     * @return int
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = mApplicationContent.get().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.get().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 取ActionBarHeight高度
     *
     * @return int
     */
    public static int getActionBarHeight() {
        int actionBarHeight = 0;

        final TypedValue tv = new TypedValue();
        if (mApplicationContent.get().getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, mApplicationContent.get().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    /**
     * 关闭输入法
     *
     * @param act 当前显示
     */
    public static void closeInputMethod(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mApplicationContent.get().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 关闭输入法
     *
     * @param activity 当前显示
     */
    public static void openInputMethod(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mApplicationContent.get().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断应用是否处于后台状态
     *
     * @return boolean
     */
    public static boolean isBackground() {
        ActivityManager am = (ActivityManager) mApplicationContent.get().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mApplicationContent.get().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 复制内容
     */
    public static void copyToClipboard(String text) {
        ClipboardManager cbm = (ClipboardManager) mApplicationContent.get().getSystemService(Activity.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(mApplicationContent.get().getPackageName(), text));
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference() {
        return mApplicationContent.get().getSharedPreferences(mApplicationContent.get().getPackageName(), Activity.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference(String name) {
        return mApplicationContent.get().getSharedPreferences(name, Activity.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference(String name, int mode) {
        return mApplicationContent.get().getSharedPreferences(name, mode);
    }


    /**
     * 经纬度测距
     *
     * @param jingdu1
     * @param weidu1
     * @param jingdu2
     * @param weidu2
     * @return
     */
    public static double distance(double jingdu1, double weidu1, double jingdu2, double weidu2) {
        double a, b, R;
        R = 6378137; // 地球半径
        weidu1 = weidu1 * Math.PI / 180.0;
        weidu2 = weidu2 * Math.PI / 180.0;
        a = weidu1 - weidu2;
        b = (jingdu1 - jingdu2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(weidu1)
                * Math.cos(weidu2) * sb2 * sb2));
        return d;
    }

    /**
     * 是否有网络
     *
     * @return boolean
     */
    public static boolean isNetWorkAvailable() {
        if (mApplicationContent.get() == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) mApplicationContent.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }

    /*****
     * 是否连接WIFI
     * @param  context 上下文
     *  @return     boolean
     * ***/
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return false;
            }
        }
        return false;
    }

    /**
     * 网络连接类型
     *@param  context 上下文
     * @return  1:wifi 0:4G 3:no internet connection
     */
    public static int internetConnType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) { // connected to the internet
            return activeNetwork.getType();
        }
        return -1;
    }

    /**
     * 判断url是否为网址
     *
     * @param url
     * @return URL 链接
     */
    public static boolean isHttp(String url) {
        if (null == url) return false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 取APP版本号  code
     *
     * @return int
     */
    public static int getAppVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 取APP版本名  name
     *
     * @return String
     */
    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }


    /****
     * Bitmap 放大缩小
     *
     * @param b 放大缩小的Bitmap
     * @param x 放大缩小比例值 宽
     * @param y 放大缩小比例值 宽
     ***/
    public static Bitmap BitmapZoom(Bitmap b, float x, float y) {
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = x / w;
        float sy = y / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }


    /***
     * 通过资源id 得到uri
     * @param id
     * @return id;
     * ***/
    public static Uri getUriFromRes(int id) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + mApplicationContent.get().getResources().getResourcePackageName(id) + "/"
                + mApplicationContent.get().getResources().getResourceTypeName(id) + "/"
                + mApplicationContent.get().getResources().getResourceEntryName(id));
    }
    /***
     * 保留两位小数
     * @param a  保留数字
     * **/
    public static String setDoubleZero(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    /***
     * 保留两位小数
     * @param a  保留字符
     * **/
    public static String setDoubleZero(String a) {
        if (a != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(Double.parseDouble(a));
        }
        return "";
    }


    /**
     * 通知相册更新资源
     *
     * @param context  上下文
     * @param path    更新资源的路径
     */
    public static void sendUpdataAlbum(Context context, String path) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
    }

    /**
     * 获得独一无二的Psuedo ID
     * @@return String
     ***/
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 转换文件大小 字符显示
     * @param size 文件长度单位 b
     * @return      String
     */
    public static String formatFileSizeAll(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.2f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ?"%.00f MB":"%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ?"%.00f KB":"%.2f KB", f);
        } else
            return String.format("%d B", size);
    }
}
