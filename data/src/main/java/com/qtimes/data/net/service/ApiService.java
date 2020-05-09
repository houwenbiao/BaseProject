package com.qtimes.data.net.service;

import com.qtimes.data.bean.BaseResponse;
import com.qtimes.domain.bean.UpdateAppInfo;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author: JackHou
 * Date: 2019/3/6.
 * API Service
 */
public interface ApiService {
    /**
     * 检测升级
     *
     * @param app
     * @param avc
     * @return
     */
    @GET("upgrade")
    Observable<BaseResponse<UpdateAppInfo>> queryUpdateAppInfo(@Query("app") String app, @Query("avc") String avc);
}
