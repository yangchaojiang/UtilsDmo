package com.gilelibrary.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by yangc on 2017/5/7.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public interface SimpleTarget<T> {
    /**
     * Callback when an image has been successfully loaded.
     * <p>
     * <strong>Note:</strong> You must not recycle the bitmap.
     */
    void onResourceReady(T bitmap);

    /**
     * Callback indicating the image could not be successfully loaded.
     * <p>
     * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
     * specified via {@link RequestCreator#error(android.graphics.drawable.Drawable)}
     * or {@link RequestCreator#error(int)}.
     */
    void onLoadFailed(Drawable errorDrawable);

    /**
     * Callback invoked right before your request is submitted.
     * <p>
     * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
     * specified via {@link RequestCreator#placeholder(android.graphics.drawable.Drawable)}
     * or {@link RequestCreator#placeholder(int)}.
     */
    void onLoadStarted(Drawable placeHolderDrawable);

}
