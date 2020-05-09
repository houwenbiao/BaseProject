package com.qtimes.data.net.interceptor;

import android.content.Context;

import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.PluLog;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: JackHou
 * Date: 2020/4/18.
 */
public class HttpErrorInterceptor extends CommonInterceptor {

    private Context context;

    @Inject
    public HttpErrorInterceptor(@ContextLevel(ContextLevel.APPLICATION) Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder = interceptBuilder(builder);
        request = chain.request();
        okhttp3.Response proceed = chain.proceed(request);
        PluLog.i("-----------------" + proceed.toString());
        if (proceed.code() == 401) {
            PluLog.e("设备尚未认证！");
        }
        return proceed;
    }
}
