package com.yang.sharelogin.platform.weibo.listener;

import android.os.Bundle;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

import com.yang.sharelogin.listener.StateListener;

/**
 * Created by lujun on 2015/9/7.
 */
public class AuthListener implements WeiboAuthListener {

    private StateListener<Bundle> mStateListener;

    @Override
    public void onComplete(Bundle bundle) {
        if (mStateListener != null){
            mStateListener.onComplete(bundle);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        if (mStateListener != null){
            mStateListener.onError(e.getMessage());
        }
    }

    @Override
    public void onCancel() {
        if (mStateListener != null){
            mStateListener.onCancel();
        }
    }

    public void setStateListener(StateListener listener){
        mStateListener = listener;
    }
}
