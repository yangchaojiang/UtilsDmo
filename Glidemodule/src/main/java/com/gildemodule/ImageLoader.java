package com.gildemodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gildemodule.utils.MySimpleTarget;
import com.yutils.YUtils;

import java.io.File;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  glide 加载封装
 */
public class ImageLoader {
    public static final String TAG = "ImageLoader";
    private volatile static ImageLoader instance;
    private int defaultRes;
    private int defaultError;

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
     * 初始化日志
     *
     * @param context application
     * @param isDebug 开启调试模式,指示器
     * @param isLog   开启日志
     ***/
    public static void init(Context context, boolean isDebug, boolean isLog) {
//
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

    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).centerCrop().placeholder(defaultError).error(defaultRes).into(imageView);
    }

    public void displayImage(Context context, String path, ImageView imageView, final MySimpleTarget<Bitmap> simpleTarget) {
        Glide.with(context).load(path).asBitmap().centerCrop().fitCenter().placeholder(defaultError).error(defaultRes).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                simpleTarget.onResourceReady(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                simpleTarget.onLoadFailed(errorDrawable);
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                simpleTarget.onLoadStarted(placeholder);
            }
        });
    }

    public void displayImage(Context context, String path, ImageView imageView, int defaultRes, int width, int height) {
        Glide.with(context).load(path).override(width, height).centerCrop().placeholder(defaultRes).into(imageView);
    }


    public void displayImage(Context context, String path, ImageView imageView, int defaultRes, int defaultError, int width, int height) {
        Glide.with(context).load(path).override(width, height).centerCrop().placeholder(defaultRes).error(defaultError).into(imageView);

    }

    public void displayImage(Context context, String path, ImageView imageView, int defaultRes, int defaultError) {
        Glide.with(context).load(path).placeholder(defaultRes).error(defaultError).into(imageView);
    }

    public void displayImage(Context activity, String path, SimpleTarget<Bitmap> target) {
        DrawableTypeRequest glide;
        if (YUtils.isHttp(path)) {
            glide = Glide.with(activity).load(path);
        } else {
            glide = Glide.with(activity).load(new File(path));
        }
        if (Build.VERSION.SDK_INT > 22) {
            glide.dontAnimate();
        }
        glide.asBitmap()
                .placeholder(defaultRes)
                .error(defaultError)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(target);
    }
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }


    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /***
     * //监听onScrollStateChanged的时候调用执行 e 被暂停的给定tag的所有请求s
     **/
    public void resumeTag(Context context) {
        Glide.with(context).resumeRequests();
    }

    /***
     * 监听onScrollStateChanged的时候调用执行 滑动暂停加载图片
     **/
    public void pauseTag(Context context) {
        Glide.with(context).pauseRequests();
    }

    /***
     * 取消设置了给定tag的所有请求
     **/
    public void cancelTag(Context context) {
        Glide.with(context).onDestroy();
    }


}
