package com.yang.sharelogin.platform.weibo.listener;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import com.yang.sharelogin.listener.StateListener;

/**
 * Created by lujun on 2015/9/7.
 */
public class AsyncRequestListener implements RequestListener {

    private StateListener<String> mStateListener;

    @Override
    public void onComplete(String s) {
        if (mStateListener != null){
            mStateListener.onComplete(s);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        if (mStateListener != null){
            mStateListener.onError(e.getMessage());
        }
    }

    public void setStateListener(StateListener listener){
        mStateListener = listener;
    }
}
