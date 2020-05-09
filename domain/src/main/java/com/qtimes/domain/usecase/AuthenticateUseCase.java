package com.qtimes.domain.usecase;

import com.qtimes.domain.base.BaseCallback;
import com.qtimes.domain.base.BaseReqParameter;
import com.qtimes.domain.base.BaseUseCase;
import com.qtimes.domain.base.SimpleSubscriber;
import com.qtimes.domain.bean.AuthenticateBean;
import com.qtimes.domain.bean.AuthenticateRes;
import com.qtimes.domain.repository.DeviceDataRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Author: JackHou
 * Date: 2018/2/26.
 * 认证设备
 */

public class AuthenticateUseCase extends BaseUseCase<DeviceDataRepository, AuthenticateUseCase.AuthenticateReq, AuthenticateUseCase.AuthenticateCallBack, AuthenticateRes> {

    @Inject
    public AuthenticateUseCase(DeviceDataRepository dataRepository) {
        super(dataRepository);
    }

    @Override
    public Observable<AuthenticateRes> buildObservable(AuthenticateReq reqParameter, AuthenticateCallBack callback) {
        return dataRepository.authenticate(new AuthenticateBean(reqParameter.productKey, reqParameter.deviceName, reqParameter.deviceSecret));
    }

    @Override
    public Subscriber<AuthenticateRes> buildSubscriber(AuthenticateReq reqParameter, final AuthenticateCallBack callback) {
        return new SimpleSubscriber<AuthenticateRes>() {
            @Override
            public void onNext(AuthenticateRes s) {
                super.onNext(s);
                if (callback != null) {
                    callback.authenticateSuccess(s);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (callback != null) {
                    callback.authenticateFailed(e);
                }
            }
        };
    }

    public static class AuthenticateReq extends BaseReqParameter {
        private String productKey;
        private String deviceName;
        private String deviceSecret;

        public AuthenticateReq(String productKey, String deviceName, String deviceSecret) {
            this.productKey = productKey;
            this.deviceName = deviceName;
            this.deviceSecret = deviceSecret;
        }
    }


    public interface AuthenticateCallBack extends BaseCallback {
        void authenticateSuccess(AuthenticateRes res);

        void authenticateFailed(Throwable e);
    }
}
