package com.qtimes.pavilion.utils;

import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.app.AppConstant;

import java.util.Map;

import cc.qtimes.esam.ESAM;

/**
 * Author: JackHou
 * Date: 2020/3/17.
 */
public class ESAMUtils {

    public static Map getDeviceInfo() {
        Map mAcquireCiphers = ESAM.getInstance().acquireCiphers();
        PluLog.i(mAcquireCiphers.toString());
        return mAcquireCiphers;
    }

    public static String getDeviceName() {
        Map mAcquireCiphers = ESAM.getInstance().acquireCiphers();
        PluLog.i(mAcquireCiphers.toString());
        return (String) mAcquireCiphers.get(AppConstant.DeviceKey.DEVICE_NAME);
    }

    public static String getProductKey() {
        Map mAcquireCiphers = ESAM.getInstance().acquireCiphers();
        PluLog.i(mAcquireCiphers.toString());
        return (String) mAcquireCiphers.get(AppConstant.DeviceKey.PRODUCT_KEY);
    }

    public static String getDeviceSecret() {
        Map mAcquireCiphers = ESAM.getInstance().acquireCiphers();
        PluLog.i(mAcquireCiphers.toString());
        return (String) mAcquireCiphers.get(AppConstant.DeviceKey.DEVICE_SECRET);
    }
}
