package com.yang.sharelogin.platform.weibo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.yang.sharelogin.TPManager;
import com.yang.sharelogin.bean.Config;
import com.yang.sharelogin.bean.WBShareContent;
import com.yang.sharelogin.listener.StateListener;

/**
 * Created by lujun on 2015/9/6.
 */
public class WBManager {

    private Context mContext;
    private Intent mIntent;
    private StateListener<String> mListener;
    private ReceiveBroadCast mReceiveBroadCast;

    public WBManager(Context context){
        mContext = context;
        mIntent = new Intent(mContext, AssistActivity.class);
        mIntent.putExtra(Config.KEY_OF_APPID, TPManager.getInstance().getWBAppId());
        mIntent.putExtra(Config.KEY_OF_APPSECRET, TPManager.getInstance().getWBAppSecret());
        mIntent.putExtra(Config.KEY_OF_REDIRECT_URL, TPManager.getInstance().getWBRedirectUrl());
        mReceiveBroadCast = new ReceiveBroadCast();
    }

    /**
     * 微博三方登录
     */
    public void onLoginWithWB(){
        mIntent.putExtra(Config.KEY_OF_TYPE, Config.LOGIN_TYPE);
        mIntent.putExtra(Config.KEY_OF_BUNDLE, new Bundle());
        mContext.registerReceiver(mReceiveBroadCast, new IntentFilter(Config.KEY_OF_WB_BCR_ACTION));
        mContext.startActivity(mIntent);
    }

    /**
     * 取消微博授权
     */
    public void onLoginOut(){
        //TODO 取消授权

    }

    /**
     * 微博分享
     * @param content
     */
    public void share(WBShareContent content){
        mIntent.putExtra(Config.KEY_OF_TYPE, Config.SHARE_TYPE);
        if (content.getBundle().getInt("share_method") ==  WBShareContent.API_SHARE){
            mIntent.putExtra(Config.KEY_OF_TYPE, Config.LOGIN_TYPE);
        }
        mIntent.putExtra(Config.KEY_OF_BUNDLE, content.getBundle());
        mContext.registerReceiver(mReceiveBroadCast, new IntentFilter(Config.KEY_OF_WB_BCR_ACTION));
        mContext.startActivity(mIntent);
    }

    /**
     * 设置登录callback
     * @param listener
     */
    public void setListener(StateListener<String> listener){
        this.mListener = listener;
    }

    /**
     * 接受WB授权回调得到的用户信息
     */
    private class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener != null){
                mListener.onComplete(intent.getStringExtra(Config.KEY_OF_WB_BCR));
                mContext.unregisterReceiver(mReceiveBroadCast);
            }
        }
    }
}
