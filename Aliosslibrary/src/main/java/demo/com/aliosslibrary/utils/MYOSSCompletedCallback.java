package demo.com.aliosslibrary.utils;


/**
 * Created by yangc on 2017/5/6.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */
public interface MyOSSCompletedCallback<T> {
    /***
     * 成功
     **/
    void onSuccess(T var, String key);

    /***
     * 失败
     **/
    void onFailure(String code, String errMsg);

    /***
     * 取消任务
     **/
    void onCancelled();

}
