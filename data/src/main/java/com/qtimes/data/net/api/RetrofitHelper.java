package com.qtimes.data.net.api;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qtimes.data.R;
import com.qtimes.data.net.Api;
import com.qtimes.data.net.convert.GsonConverterFactory;
import com.qtimes.domain.constant.Constant;
import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.HttpUtil;
import com.qtimes.utils.android.PluLog;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by liujun on 16-5-19.
 */
public class RetrofitHelper {

    private Map<Class<?>, String> apiMap;
    private Set<Interceptor> defInterceptors;
    private Context context;

    @Inject
    public RetrofitHelper(@ContextLevel(ContextLevel.APPLICATION) Context context, Map<Class<?>, String> apiMap, Set<Interceptor> defInterceptors) {
        this.context = context;
        this.apiMap = apiMap;
        this.defInterceptors = defInterceptors;
    }

    public OkHttpClient createClient(Interceptor... interceptors) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(Api.Timeout.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        if (defInterceptors != null) {
            for (Interceptor interceptor : defInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                /*域名校验---首先检验是否是我们信任的域名然后再检验域名和服务端传输过来的证书里的域名(CN)是否一致*/
                boolean is = verifyHostname(hostname, HttpUtil.HTTPS.HOST_REGEX);
                PluLog.i("域名校验：" + is);
                if (is) {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    is = hv.verify(hostname, session);
                    PluLog.i("域名校验1: " + is + hostname);
                }
                return is;
            }
        });

        InputStream qtimesIs = null;
        qtimesIs = context.getResources().openRawResource(R.raw.server);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{qtimesIs}, null, null);
        OkHttpClient okHttpClient = builder.sslSocketFactory(sslParams.socketFactory, sslParams.trustManager)
                .build();
        return okHttpClient;
    }

    private Gson getGsonObject() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        //skip  fields if there is an @ExcludeField annotation.
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        //skip a specified class
                        return false;
                    }
                })
                .create();
    }

    public <T> T createService(Class<T> apiClazz, Interceptor... interceptors) {
//        PluLog.e("zx baseUrl:" + apiMap.get(apiClazz));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiMap.get(apiClazz))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGsonObject()))
                .client(createClient(interceptors)).build();
        return retrofit.create(apiClazz);
    }


    private boolean verifyHostname(String hostname, String pattern) {
        // Basic sanity checks
        // Check length == 0 instead of .isEmpty() to support Java 5.
        if ((hostname == null) || (hostname.length() == 0) || (hostname.startsWith("."))
                || (hostname.endsWith(".."))) {
            // Invalid domain name
            return false;
        }
        if ((pattern == null) || (pattern.length() == 0) || (pattern.startsWith("."))
                || (pattern.endsWith(".."))) {
            // Invalid pattern/domain name
            return false;
        }

        // Normalize hostname and pattern by turning them into absolute domain names if they are not
        // yet absolute. This is needed because server certificates do not normally contain absolute
        // names or patterns, but they should be treated as absolute. At the same time, any hostname
        // presented to this method should also be treated as absolute for the purposes of matching
        // to the server certificate.
        //   www.android.com  matches www.android.com
        //   www.android.com  matches www.android.com.
        //   www.android.com. matches www.android.com.
        //   www.android.com. matches www.android.com
        if (!hostname.endsWith(".")) {
            hostname += '.';
        }
        if (!pattern.endsWith(".")) {
            pattern += '.';
        }
        // hostname and pattern are now absolute domain names.

        pattern = pattern.toLowerCase(Locale.US);
        // hostname and pattern are now in lower case -- domain names are case-insensitive.

        if (!pattern.contains("*")) {
            // Not a wildcard pattern -- hostname and pattern must match exactly.
            return hostname.equals(pattern);
        }
        // Wildcard pattern

        // WILDCARD PATTERN RULES:
        // 1. Asterisk (*) is only permitted in the left-most domain name label and must be the
        //    only character in that label (i.e., must match the whole left-most label).
        //    For example, *.example.com is permitted, while *a.example.com, a*.example.com,
        //    a*b.example.com, a.*.example.com are not permitted.
        // 2. Asterisk (*) cannot match across domain name labels.
        //    For example, *.example.com matches test.example.com but does not match
        //    sub.test.example.com.
        // 3. Wildcard patterns for single-label domain names are not permitted.

        if ((!pattern.startsWith("*.")) || (pattern.indexOf('*', 1) != -1)) {
            // Asterisk (*) is only permitted in the left-most domain name label and must be the only
            // character in that label
            return false;
        }

        // Optimization: check whether hostname is too short to match the pattern. hostName must be at
        // least as long as the pattern because asterisk must match the whole left-most label and
        // hostname starts with a non-empty label. Thus, asterisk has to match one or more characters.
        if (hostname.length() < pattern.length()) {
            // hostname too short to match the pattern.
            return false;
        }

        if ("*.".equals(pattern)) {
            // Wildcard pattern for single-label domain name -- not permitted.
            return false;
        }

        // hostname must end with the region of pattern following the asterisk.
        String suffix = pattern.substring(1);
        if (!hostname.endsWith(suffix)) {
            // hostname does not end with the suffix
            return false;
        }

        // Check that asterisk did not match across domain name labels.
        int suffixStartIndexInHostname = hostname.length() - suffix.length();
        if ((suffixStartIndexInHostname > 0)
                && (hostname.lastIndexOf('.', suffixStartIndexInHostname - 1) != -1)) {
            // Asterisk is matching across domain name labels -- not permitted.
            return false;
        }

        // hostname matches pattern
        return true;
    }

}
