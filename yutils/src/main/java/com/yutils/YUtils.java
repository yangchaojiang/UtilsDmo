package com.yutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
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
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by yangjiang on 2017/1/6.
 * E-Mail:1007181167@qq.com
 * Description:  帮助类
 */
public class YUtils {
    private static String TAG;
    private static boolean DEBUG = false;
    private static Context mApplicationContent;
    private static Toast mToast = null;

    public static void initialize(Application app) {
        mApplicationContent = app.getApplicationContext();
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
            mToast = Toast.makeText(mApplicationContent, text, android.widget.Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /****
     * toast提示封装
     *
     * @param resId 提示内容  资源Id
     ***/
    public static void Toast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent, resId, android.widget.Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /****
     * toast 长提示封装
     *
     * @param text 提示内容 字符
     ***/
    public static void ToastLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /****
     * toast 长提示封装
     *
     * @param resId 提示内容 资源id
     ***/
    public static void ToastLong(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(mApplicationContent, resId, Toast.LENGTH_LONG);
        } else {
            mToast.setText(resId);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * dp转px
     *
     * @param dpValue dp单位
     */
    public static int dip2px(float dpValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp单位
     */
    public static int sp2px(float spValue) {
        final float fontScale = mApplicationContent.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px单位
     */
    public static int px2dip(float pxValue) {
        final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 取屏幕宽度
     *
     * @return int
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 取屏幕高度
     *
     * @return int
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.heightPixels - getStatusBarHeight();
    }

    /**
     * 取屏幕高度包含状态栏高度
     *
     * @return int
     */
    public static int getScreenHeightWithStatusBar() {
        DisplayMetrics dm = mApplicationContent.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 取导航栏高度
     *
     * @return int
     */
    public static int getNavigationBarHeight(Activity activity) {
        int result = 0;
        if (navigationBarExist2(activity)) {
            int resourceId = mApplicationContent.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
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
    public static boolean navigationBarExist2(Activity activity) {
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
        int resourceId = mApplicationContent.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mApplicationContent.getResources().getDimensionPixelSize(resourceId);
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
        if (mApplicationContent.getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, mApplicationContent.getResources().getDisplayMetrics());
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
            ((InputMethodManager) mApplicationContent.getSystemService(Context.INPUT_METHOD_SERVICE)).
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
            ((InputMethodManager) mApplicationContent.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 判断应用是否处于后台状态
     *
     * @return boolean
     */
    public static boolean isBackground() {
        ActivityManager am = (ActivityManager) mApplicationContent.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mApplicationContent.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text  复制内容
     */
    public static void copyToClipboard(String text) {
        ClipboardManager cbm = (ClipboardManager) mApplicationContent.getSystemService(Activity.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(mApplicationContent.getPackageName(), text));
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference() {
        return mApplicationContent.getSharedPreferences(mApplicationContent.getPackageName(), Activity.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference(String name) {
        return mApplicationContent.getSharedPreferences(name, Activity.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference(String name, int mode) {
        return mApplicationContent.getSharedPreferences(name, mode);
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
        if (mApplicationContent == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) mApplicationContent
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

    /**
     * 取APP版本号  code
     *
     * @return  int
     */
    public static int getAppVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 取APP版本名  name
     *
     * @return    String
     */
    public static String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }


    /****
     *Bitmap 放大缩小
     * @param   b  放大缩小的Bitmap
     * @param   x  放大缩小比例值 宽
     * @param   y  放大缩小比例值 宽
     * ***/
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


    public static Uri getUriFromRes(int id) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + mApplicationContent.getResources().getResourcePackageName(id) + "/"
                + mApplicationContent.getResources().getResourceTypeName(id) + "/"
                + mApplicationContent.getResources().getResourceEntryName(id));
    }

}
