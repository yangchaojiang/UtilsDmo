package com.translatemodule;



import com.translatemodule.bean.TransBean;
import com.translatemodule.bean.TranslationBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by yangjiang on 2017/1/10.
 * E-Mail:1007181167@qq.com
 * Description:
 */

public interface TranslateService {
    @GET("api/trans/vip/translate")
    Observable<TransBean> getBaiDu(@QueryMap Map<String,String> stringStringMap);
    @GET("openapi.do")
    Observable<TranslationBean> getYouDao(@QueryMap Map<String,String> stringStringMap);
    @GET("openapi.do")
    Observable<String> getYouDaoString(@QueryMap Map<String,String> stringStringMap);
}
