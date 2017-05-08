package com.tengxunyun.utils;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.tengxunyun.config.ParamPreference;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yangc on 2017/5/6.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */
public class TencentUtils {
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";
    private static String Original = "a=%s&b=%s&k=%s&e=%s&t=%s&r=10000s&f=";
     private static final  String TAG="TencentUtils";
    /**
     * @param SecretKey   密钥
     * @param EncryptText 签名串
     * @return
     * @throws Exception
     */
    public static byte[] HmacSHA1Encrypt(String SecretKey, String EncryptText)
            throws Exception {
        byte[] data = SecretKey.getBytes(ENCODING);
        SecretKeySpec secretKey = new SecretKeySpec(data, MAC_NAME);
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(secretKey);
        byte[] text = EncryptText.getBytes(ENCODING);
        return mac.doFinal(text);
    }

    public static long getLinuxDateSimple() {
        try {
            long unixTimestamp = System.currentTimeMillis() / 1000L;
            return unixTimestamp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @SuppressLint("SimpleDateFormat")
    public static long getFurureLinuxDate() {
        return 7776000;
    }

    private static String getRandomTenStr() {
        String  randomstr = String.valueOf(new Random().nextInt(8) + 1);
        int random = new Random().nextInt(3) + 5;
        for (int i = 0; i < random; i++) {
            randomstr += String.valueOf(new Random().nextInt(9));
        }
        return randomstr;
    }

    public static String getSignOriginal() {
        return String.format(Original,
                ParamPreference.TENCENT_COS_APPID,
                ParamPreference.TENCENT_COS_BUCKET,
                ParamPreference.TENCENT_COS_SECRET_ID,
                String.valueOf(getFurureLinuxDate()),
                String.valueOf(getLinuxDateSimple()+getFurureLinuxDate()), getRandomTenStr());
    }

    /****
     * 获取签名文件
     **/
    public static String getTencentSign() {
        try {
            String Original = TencentUtils.getSignOriginal();
            byte[] HmacSHA1 = TencentUtils.HmacSHA1Encrypt(
                    ParamPreference.TENCENT_COS_SECRET_KEY, Original);
            byte[] all = new byte[HmacSHA1.length
                    + Original.getBytes(ENCODING).length];
            System.arraycopy(HmacSHA1, 0, all, 0, HmacSHA1.length);
            System.arraycopy(Original.getBytes(ENCODING), 0, all,
                    HmacSHA1.length, Original.getBytes(ENCODING).length);
            String SignData = Base64.encodeToString(all,Base64.NO_WRAP);
            return SignData;
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
        return "get sign failed";
    }

}
