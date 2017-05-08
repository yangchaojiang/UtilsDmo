package com.yang.sharelogin.platform.qq.listener;

import android.util.Log;

import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * Created by lujun on 2015/9/6.
 */
public class BaseApiListener implements IRequestListener {

    @Override
    public void onComplete(JSONObject jsonObject) {
        Log.i("BaseApiListener", jsonObject.toString());
    }

    @Override
    public void onIOException(IOException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onMalformedURLException(MalformedURLException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onJSONException(JSONException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onConnectTimeoutException(ConnectTimeoutException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onSocketTimeoutException(SocketTimeoutException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onHttpStatusException(HttpUtils.HttpStatusException e) {
        Log.i("BaseApiListener", e.toString());
    }

    @Override
    public void onUnknowException(Exception e) {
        Log.i("BaseApiListener", e.toString());
    }
}
