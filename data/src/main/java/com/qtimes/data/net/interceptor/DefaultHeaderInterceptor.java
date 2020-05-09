package com.qtimes.data.net.interceptor;

import android.content.Context;

import com.qtimes.data.constant.Key;
import com.qtimes.data.util.ApiUtil;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.domain.cache.DataCache;
import com.qtimes.domain.constant.Constant;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.PluLog;

import java.io.IOException;
import java.util.Random;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class DefaultHeaderInterceptor extends CommonInterceptor {


    private Context context;
    private AccountCache accountCache;
    private DataCache dataCache;
    private Random random = new Random();

    @Inject
    public DefaultHeaderInterceptor(@ContextLevel(ContextLevel.APPLICATION) Context context,
                                    AccountCache accountCache,
                                    DataCache dataCache) {
        this.context = context;
        this.accountCache = accountCache;
        this.dataCache = dataCache;
        startTime = System.currentTimeMillis();
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder = interceptBuilder(builder);
        request = buildRequest(builder.build());
        return chain.proceed(request);
    }

    public Request.Builder interceptBuilder(Request.Builder builder) {
        builder.addHeader("Accept", "application/json");
        return builder;
    }

    public Request buildRequest(Request oldReq) {
        String query = oldReq.url().query();
        String params = query != null ? query.replaceAll("=|&", "") : "";
        String appkey = Integer.toHexString(random.nextInt());
        String secret = getAppSecret();
        String ts = ApiUtil.getTimestamp();
        String hash = ApiUtil.signQuery(appkey, secret, ts, params);

        HttpUrl.Builder ub = oldReq.url().newBuilder()
                .scheme(oldReq.url().scheme())
                .host(oldReq.url().host())
                .addQueryParameter(Constant.API.PARAM_APP_ID, appkey)
                .addQueryParameter(Constant.API.PARAM_TIMESTAMP, ts)
                .addQueryParameter(Constant.API.PARAM_HASH_CODE, hash);

        Request.Builder rb = oldReq.newBuilder()
                .method(oldReq.method(), oldReq.body())
                .url(ub.build());

        String header = genHeaderWithToken();
        if (header != null) {
//            rb.addHeader(Constant.API.AUTH_HEADER, ApiUtil.buildAuthorization(header));
            PluLog.i("addHeader Authorization:" + header);
            rb.addHeader(Constant.API.AUTH_HEADER, header);
        }
        Request newReq = rb.build();
        return newReq;
    }

    private String getAppSecret() {
        String token = accountCache.getUserInfo().getToken();
        if (token == null) {
            return "secret-sign9527";
        }

        return token;
    }

    private String genHeaderWithToken() {
//        String token = accountCache.getUserInfo().getToken();
        String token = dataCache.getStringSp(Key.Account.ACCESS_TOKEN, "");
        PluLog.i("-------------------token:" + token);
        if (token == null) {
            return null;
        }
        String expired_in = accountCache.getUserInfo().getExpired_in();
        String user = accountCache.getUserInfo().getUsername();
//        String device_name = accountCache.getUserInfo().getDeviceName();
        String device_name = dataCache.getStringSp(Key.Account.DEVICE_NAME, "");
        PluLog.i("-------------------device_name:" + device_name);
        return ApiUtil
                .buildHeader(token, device_name,
                        expired_in, user, accountCache.getUserInfo().getUuid(),
                        Constant.API.API_VERSION, accountCache.getUserInfo().getOs()).toString();
    }


}
