package demo.com.aliosslibrary.utils;


/**
 * Created by yangc on 2017/5/6.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public interface MyOSSProgressCallback<T> {
    void onProgress(T var1, long var2, long var4);
    /***
     * 成功
     **/
    void onSuccess(T var, String key);

    /***
     * 失败
     **/
    void onFailure(Exception e, String errMsg);

    /***
     * 取消任务
     **/
    void onCancelled();
}
