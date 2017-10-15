package com.yutils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by yangjiang on 2016/12/7.
 * E-Mail:1007181167@qq.com
 * Description:[调用系统打开文件]
 **/
public class FileIntentUtil {

    /**
     * 打开下载文件打开文件方式的intent
     **/

    /****
     * android获取一个用于打开HTML文件的intent
     *
     * @param paramPath 地址
     * @return Intent
     */
    public static Intent getHtmlFileIntent(@NonNull String paramPath)

    {

        Uri uri = Uri.parse(paramPath).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(paramPath).build();

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.setDataAndType(uri, "text/html");

        return intent;

    }

    /****
     * android获取一个用于打开图片文件的intent
     *
     * @param paramPath 地址
     * @return Intent
     */
    public static Intent getImageFileIntent(@NonNull String paramPath)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(paramPath));

        intent.setDataAndType(uri, "image/*");

        return intent;

    }

    /****
     * android获取一个用于打开PDF文件的intent
     *
     * @param paramPath 地址
     * @return Intent
     */
    public static Intent getPdfFileIntent(@NonNull String paramPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(paramPath));
        intent.setDataAndType(uri, "application/pdf");
        return intent;

    }

    /****
     * android获取一个用于打开文本文件的intent
     *
     * @param paramString  地址
     * @param paramBoolean 是否网络连接
     * @return Intent
     */
    public static Intent getTextFileIntent(@NonNull String paramString,
                                           boolean paramBoolean)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (paramBoolean)

        {

            Uri uri1 = Uri.parse(paramString);

            intent.setDataAndType(uri1, "text/plain");

        }

        while (true)

        {


            Uri uri2 = Uri.fromFile(new File(paramString));

            intent.setDataAndType(uri2, "text/plain");
            return intent;
        }

    }

    /****
     * android获取一个用于打开音频文件的intent
     *
     * @param paramStringPath 地址
     * @return Intent
     */
    public static Intent getAudioFileIntent (@NonNull String paramStringPath)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("oneshot", 0);

        intent.putExtra("configchange", 0);

        Uri uri = Uri.fromFile(new File(paramStringPath));

        intent.setDataAndType(uri, "audio/*");

        return intent;

    }

    /****
     * android获取一个用于打开视频文件的intent
     *
     * @param path 地址
     * @return Intent
     */
    public static Intent playInternetVideo(String path) {
        Uri uri = Uri.parse(path);
        // 调用系统自带的播放器来播放流媒体视频
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/mp4");
        return intent;
    }

    /****
     * android获取一个用于打开视频文件的intent
     *
     * @param path 地址
     * @return Intent
     */
    public static Intent getVideoFileIntent(String path)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("oneshot", 0);

        intent.putExtra("configchange", 0);

        Uri uri = Uri.fromFile(new File(path));

        intent.setDataAndType(uri, "video/*");

        return intent;

    }


    /****
     * android获取一个用于打开CHM文件的intent
     *
     * @param path 地址
     * @return Intent
     */
    public static Intent getChmFileIntent(String path)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(path));

        intent.setDataAndType(uri, "application/x-chm");

        return intent;

    }

    /****
     * android获取一个用于打开Word文件的intent
     *
     * @param path 地址
     * @return Intent
     */
    public static Intent getWordFileIntent(@NonNull String path)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(path));

        intent.setDataAndType(uri, "application/msword");

        return intent;

    }

    /****
     * android获取一个用于打开Excel文件的intent
     *
     * @param path 地址
     * @return Intent
     */
    public static Intent getExcelFileIntent(@NonNull String path)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(path));

        intent.setDataAndType(uri, "application/vnd.ms-excel");

        return intent;

    }

    /****
     * android获取一个用于打开PPT文件的intent
     *
     * @param path 地址
     * @return Intent
     */
    public static Intent getPptFileIntent(@NonNull String path)

    {

        Intent intent = new Intent("android.intent.action.VIEW");

        intent.addCategory("android.intent.category.DEFAULT");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(path));

        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

        return intent;

    }

    public static boolean openFile( @NonNull Context context,@NonNull String path, boolean falg) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("OpenMode", "ReadOnly");
// 	   bundle.putBoolean("ClearTrace", true);
//	   bundle.putString(THIRD_PACKAGE, selfPackageName);
//	   bundle.putBoolean(CLEAR_BUFFER, true);
//	   bundle.putBoolean(CLEAR_TRACE, true);
        //bundle.putBoolean(CLEAR_FILE, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
