package com.qtimes.utils.android;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Created by gufei on 2016/7/13.
 */
public class NetUtils {
    public static final int NETTYPE_NO = 0;
    public static final int NETTYPE_WIFI = 1;
    public static final int NETTYPE_2G = 2;
    public static final int NETTYPE_3G = 3;
    public static final int NETTYPE_4G = 4;
    public static final int NETTYPE_OTHER = 6;
    public static final int NETTYPE_UNKOWN = -1;
    public static final String China_Mobile = "CMCC";
    public static final String China_Unicom = "CUCC";
    public static final String China_Telecom = "CTCC";
    public static final String China_Unkown = "unkown";

    /**
     * 检测网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 检测系统是否已经设置代理，请参考HttpDNS API文档。
     */
    public static boolean detectIfProxyExist(Context context) {
        boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyHost;
        int proxyPort;
        boolean isProxy = true;
        if (IS_ICS_OR_LATER) {
            proxyHost = System.getProperty("http.proxyHost");
            String port = System.getProperty("http.proxyPort");
            proxyPort = StringUtil.String2Integer(port != null ? port : "-1", -1);
        } else {
            proxyHost = android.net.Proxy.getHost(context.getApplicationContext());
            proxyPort = android.net.Proxy.getPort(context.getApplicationContext());
        }
        if (TextUtils.isEmpty(proxyHost) && proxyPort <= 0) {
            isProxy = false;
        }
        return isProxy;
    }

    public static int getNetType(Context context) {
        try {
            if (context == null) return NETTYPE_NO;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()) {
                if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                    return NETTYPE_WIFI;
                } else {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext()
                            .getSystemService(Context.TELEPHONY_SERVICE);

                    switch (telephonyManager.getNetworkType()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            return NETTYPE_2G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETTYPE_4G;
                        default:
                            return NETTYPE_3G;
                    }
                }
            } else {
                return NETTYPE_NO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return NETTYPE_NO;
        }
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGps(final Context context) {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gps || network) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {

        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

}
