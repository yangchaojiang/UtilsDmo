package com.yang.sharelogin.platform.qq.listener;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import com.yang.sharelogin.listener.StateListener;

/**
 * Created by lujun on 2015/9/6.
 */
public class BaseUiListener implements IUiListener {

    private StateListener<Object> mListener;

    @Override
    public void onComplete(Object o) {
        if (mListener != null){
            mListener.onComplete(o);
        }
    }

    @Override
    public void onError(UiError uiError) {
        if (mListener != null){
            mListener.onError(uiError.toString());
        }
    }

    @Override
    public void onCancel() {
        if (mListener != null){
            mListener.onCancel();
        }
    }

    /**
     * set state listener
     * @param listener
     */
    public void setListener(StateListener<Object> listener){
        this.mListener = listener;
    }
}
