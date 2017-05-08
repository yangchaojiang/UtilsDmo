package com.gilelibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.gilelibrary.utils.SimpleTarget;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */
public class ImageLoader {
    public static final String TAG = "ImageLoader";
    private volatile static ImageLoader instance;
    private int defaultRes;
    private int defaultError;
    private volatile static LruCache cache;

    public static ImageLoader getInstace() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    private ImageLoader() {

    }

    /***
     * 设置默认图
     *
     * @param defaultRes   默认加载图
     * @param defaultError 默认失败图
     **/
    public void defaultImage(int defaultRes, int defaultError) {
        this.defaultRes = defaultRes;
        this.defaultError = defaultError;
    }

    /***
     * 初始化日志
     *
     * @param context application
     * @param isDebug 开启调试模式,指示器
     * @param isLog   开启日志
     ***/
    public static void init(Context context, boolean isDebug, boolean isLog) {
        //配置缓存
        cache = new LruCache(5 * 1024 * 1024);// 设置缓存大小
        //配置线程池
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Picasso.Builder builder = new Picasso.Builder(context);
        //配置下载器
        builder.downloader(new OkHttpDownloader(context));
        builder.defaultBitmapConfig(Bitmap.Config.ARGB_4444);
        builder.memoryCache(cache);
        builder.executor(executorService);
        //配置调试指示器
        builder.indicatorsEnabled(isDebug);
        //构造一个Picasso
        Picasso picasso = builder.build();
        //配置日志
        picasso.setLoggingEnabled(isLog);
        //设置全局单列instance
        Picasso.setSingletonInstance(picasso);
    }

    private void test() {
        //  .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//跳过内存缓存
        //  .networkPolicy(NetworkPolicy.NO_CACHE)//跳过磁盘缓存
        //  NO_CACHE:表示处理请求的时候跳过处理磁盘缓存
        //  NO_STORE:表示请求成功后，不将结果缓存到Disk, 但是这个只对OkHttp有效。
    }

    public void displayImage(Context context, String path, ImageView imageView) {
        Picasso.with(context).load(path).tag(context).centerCrop().placeholder(defaultError).error(defaultRes).into(imageView);
    }

    public void displayImage(Context context, String path, ImageView imageView, final SimpleTarget<Bitmap> simpleTarget) {
        Picasso.with(context).load(path).tag(context).centerCrop().placeholder(defaultError).error(defaultRes).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                simpleTarget.onResourceReady(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                simpleTarget.onLoadFailed(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                simpleTarget.onLoadStarted(placeHolderDrawable);
            }
        });
    }

    public void displayImage(Context context, String path, ImageView imageView, int defaultRes, int width, int height) {
        Picasso.with(context).load(path).tag(context).centerCrop().resize(width, height).placeholder(defaultError).error(defaultRes).onlyScaleDown().into(imageView);
    }


    public void displayImage(Context context, String path, ImageView imageView, int defaultRes, int defaultError, int width, int height) {
        Picasso.with(context).load(path).tag(context).centerCrop().resize(width, height).placeholder(defaultError).error(defaultRes).onlyScaleDown().into(imageView);

    }


    public void clearDiskCache(Context context) {
    }


    public void clearMemoryCache(Context context) {
        cache.clear();
    }

    /***
     * //监听onScrollStateChanged的时候调用执行 e 被暂停的给定tag的所有请求s
     **/
    public void resumeTag(Context context) {
        Picasso.with(context).resumeTag(context);
    }

    /***
     * 监听onScrollStateChanged的时候调用执行 滑动暂停加载图片
     **/
    public void pauseTag(Context context) {
        Picasso.with(context).pauseTag(context);
    }

    /***
     * 取消设置了给定tag的所有请求
     **/
    public void cancelTag(Context context) {
        Picasso.with(context).cancelTag(context);
    }


}
