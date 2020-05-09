package com.qtimes.utils.android;

/**
 * Author: JackHou
 * Date: 2020/4/20.
 */

import java.lang.reflect.Method;

public class PropertyUtils {

    private static volatile Method set = null;
    private static volatile Method get = null;

    public static void set(String prop, String value) {
        try {
            if (null == set) {
                synchronized (PropertyUtils.class) {
                    if (null == set) {
                        Class cls = Class.forName("android.os.SystemProperties");
                        set = cls.getDeclaredMethod("set", new Class[]{String.class, String.class});
                    }
                }
            }
            set.invoke(null, new Object[]{prop, value});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String get(String prop, String defaultvalue) {
        String value = defaultvalue;
        try {
            if (null == get) {
                synchronized (PropertyUtils.class) {
                    if (null == get) {
                        Class cls = Class.forName("android.os.SystemProperties");
                        get = cls.getDeclaredMethod("get", new Class[]{String.class, String.class});
                    }
                }
            }
            value = (String) (get.invoke(null, new Object[]{prop, defaultvalue}));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取系统版本名称
     *
     * @return
     */
    public static String getSystemVersionName() {
        return get("ro.product.version", "");
    }

    /**
     * 获取系统版本名称
     *
     * @return
     */
    public static String getSystemSN() {
        return get("ro.serialno", "");
    }

    /**
     * open wireless adb
     */
    public static void openAdb() {
        PropertyUtils.set("persist.internet.adb.enable", "1");
    }

    /**
     * close wireless adb
     */
    public static void closeAdb() {
        PropertyUtils.set("persist.internet.adb.enable", "0");
    }
}
