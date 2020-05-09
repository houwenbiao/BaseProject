package com.qtimes.domain.repository;

import com.qtimes.domain.bean.UpdateAppInfo;

import java.util.List;

import rx.Observable;

/**
 * Author: JackHou
 * Date: 2019/3/6.
 */
public interface ApiDataRepository extends DataRepository {


    /**
     * 查询版本更新信息
     *
     * @param app app name
     * @param avc app version code
     * @return
     */
    Observable<UpdateAppInfo> queryUpdateAppInfo(String app, String avc);
}
