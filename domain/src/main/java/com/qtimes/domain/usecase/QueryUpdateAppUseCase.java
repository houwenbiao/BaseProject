package com.qtimes.domain.usecase;

import com.qtimes.domain.base.BaseCallback;
import com.qtimes.domain.base.BaseReqParameter;
import com.qtimes.domain.base.BaseUseCase;
import com.qtimes.domain.base.SimpleSubscriber;
import com.qtimes.domain.bean.UpdateAppInfo;
import com.qtimes.domain.repository.ApiDataRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Author: JackHou
 * Date: 2018/2/26.
 */

public class QueryUpdateAppUseCase extends BaseUseCase<ApiDataRepository, QueryUpdateAppUseCase.QueryUpdateAppReq, QueryUpdateAppUseCase.QueryUpdateAppCallBack, UpdateAppInfo> {

    @Inject
    public QueryUpdateAppUseCase(ApiDataRepository dataRepository) {
        super(dataRepository);
    }

    @Override
    public Observable<UpdateAppInfo> buildObservable(QueryUpdateAppReq reqParameter, QueryUpdateAppCallBack callback) {
        return dataRepository.queryUpdateAppInfo(reqParameter.app, reqParameter.avc);
    }

    @Override
    public Subscriber<UpdateAppInfo> buildSubscriber(QueryUpdateAppReq reqParameter, final QueryUpdateAppCallBack callback) {
        return new SimpleSubscriber<UpdateAppInfo>() {
            @Override
            public void onNext(UpdateAppInfo updateAppInfo) {
                super.onNext(updateAppInfo);
                if (callback != null) {
                    callback.queryUpdateAppSuccess(updateAppInfo);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (callback != null) {
                    callback.queryUpdateAppFail(e);
                }
            }
        };
    }

    public static class QueryUpdateAppReq extends BaseReqParameter {
        String app;
        String avc;

        public QueryUpdateAppReq(String app, String avc) {
            this.app = app;
            this.avc = avc;
        }
    }


    public interface QueryUpdateAppCallBack extends BaseCallback {
        void queryUpdateAppSuccess(UpdateAppInfo info);

        void queryUpdateAppFail(Throwable e);
    }
}
