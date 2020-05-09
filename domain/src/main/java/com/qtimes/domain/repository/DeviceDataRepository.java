package com.qtimes.domain.repository;

import com.qtimes.domain.bean.AuthenticateBean;
import com.qtimes.domain.bean.AuthenticateRes;

import rx.Observable;

/**
 * Author: JackHou
 * Date: 2019/3/6.
 */
public interface DeviceDataRepository extends DataRepository {

    Observable<AuthenticateRes> authenticate(AuthenticateBean bean);

}
