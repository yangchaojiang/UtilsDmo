package com.yang.sharelogin.listener;

/**
 * Created by lujun on 2015/9/6.
 */
public interface StateListener<T> {

    void onComplete(T t);
    void onError(String err);
    void onCancel();
}
