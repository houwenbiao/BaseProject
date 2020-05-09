package com.qtimes.data.net.service;

import com.qtimes.data.bean.BaseResponse;
import com.qtimes.domain.bean.AuthenticateBean;
import com.qtimes.domain.bean.AuthenticateRes;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Author: JackHou
 * Date: 2019/3/6.
 * device Service
 */
public interface DeviceService {


    /**
     * 认证设备
     *
     * @param bean bean
     * @return
     */
    @POST("device/activate")
    Observable<BaseResponse<AuthenticateRes>> authenticate(@Body AuthenticateBean bean);
}
