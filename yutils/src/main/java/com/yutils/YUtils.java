package com.yutils;

import android.annotation.SuppressLint;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.*;

/**
 *
 * @author yangjiang
 * @date 2017/1/6
 * E-Mail:1007181167@qq.com
 * Description:  帮助类
 */
public class YUtils {
    @SuppressLint("StaticFieldLeak")
    private static   final  ThreadLocal<Application> mApplicationContent = new ThreadLocal<>();
    private static Toast mToast = null;
    private static int gravity =0x00000051;
    public static void initialize(Application app) {
        mApplicationContent.set(app);

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
     * toast 短提示封装
     *
     * @param text 提示内容 字符
     ***/
    public static void Toast( String text) {
        if (text==null) {
            return;
        }
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
     * @param gravity gravity 出现位置
     ***/
    public static void Toast( String text, int gravity) {
        if (text==null) {
            return;
        }
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
    public static void Toast(@StringRes int resId) {
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
    public static void ToastLong( String text) {
        if (text==null){
            return;
        }
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
    public static void ToastLong(@StringRes int resId) {
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
     *
     * @param activity 上下文
     * @return int
     */
    public static int getNavigationBarHeight(@NonNull Activity activity) {
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
    public static boolean isNavigationBarExist2(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
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
    public static boolean checkDeviceHasNavigationBar(@NonNull Context activity) {
//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey ;
        boolean hasBackKey ;

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
     * @param v 当前显示view
     */
    public static void closeInputMethod(@NonNull EditText v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
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
     * 开启软键盘
     * @param v
     */
    public static void showKeyboard(@NonNull View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 开启软键盘
     * @param et
     */
    public static void openSoftKeyboard(@NonNull EditText et) {
        InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputManager != null;
        inputManager.showSoftInput(et,0);
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 复制内容
     */
    public static void copyToClipboard(@NonNull String text) {
        ClipboardManager cbm = (ClipboardManager) mApplicationContent.get().getSystemService(Activity.CLIPBOARD_SERVICE);
        assert cbm != null;
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
    public static SharedPreferences getSharedPreference(@NonNull String name) {
        return mApplicationContent.get().getSharedPreferences(name, Activity.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreference(@NonNull String name, int mode) {
        return mApplicationContent.get().getSharedPreferences(name, mode);
    }



    /**
     * 判断url是否为网址
     *
     * @param url
     * @return URL 链接
     */
    public static boolean isHttp(@NonNull String url) {
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
    public static boolean isAppAlive(@NonNull Context context,@NonNull String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch",
                        format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 取APP版本号  code
     *
     * @return int
     */
    public static int getAppVersionCode() {
        int version = 1;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mApplicationContent.get().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mApplicationContent.get().getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 取APP版本名  name
     *
     * @return String
     */
    public static String getAppVersionName() {
        String version = "1.0";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mApplicationContent.get().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mApplicationContent.get().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }





    /***
     * 通过资源id 得到uri
     *
     * @param id
     * @return id;
     ***/
    public static Uri getUriFromRes(int id) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + mApplicationContent.get().getResources().getResourcePackageName(id) + "/"
                + mApplicationContent.get().getResources().getResourceTypeName(id) + "/"
                + mApplicationContent.get().getResources().getResourceEntryName(id));
    }

    /***
     * 保留两位小数
     *
     * @param a 保留数字
     **/
    public static String setDoubleZero(double a) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    /***
     * 保留两位小数
     *
     * @param a 保留字符
     **/
    public static String setDoubleZero(@NonNull String a) {
        if (a != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(Double.parseDouble(a));
        }
        return "";
    }


    /**
     * 通知相册更新资源
     *
     * @param context 上下文
     * @param path    更新资源的路径
     */
    public static void sendUpdataSystemAlbum(@NonNull Context context,@NonNull String path) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
    }

    /**
     * 获得独一无二的Psuedo ID
     *
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
     * 1、获取main在窗体的可视区域
     * 2、获取main在窗体的不可视区域高度
     * 3、判断不可视区域高度
     * 1、大于100：键盘显示  获取Scroll的窗体坐标
     * 算出main需要滚动的高度，使scroll显示。
     * 2、小于100：键盘隐藏
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public static void addLayoutListener(@NonNull final View main,@NonNull final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 100) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, srollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }


    private YUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void install(Context context, File file, boolean force) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (force) {
            System.exit(0);
        }
    }
    /**
     * MD5 32位加密方法一 小写
     *
     * @param string
     * @return
     */
    public static String get32MD5(@NonNull String string) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = string.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取Application
     *
     * @return Application
     */
    public static Application getApp() {
        if (mApplicationContent.get() != null) {
            return mApplicationContent.get();
        }
        throw new NullPointerException("u should init first");
    }
}
