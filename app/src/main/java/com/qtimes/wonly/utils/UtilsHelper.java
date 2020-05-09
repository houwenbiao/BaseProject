package com.qtimes.wonly.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.qtimes.wonly.app.App;
import com.qtimes.wonly.app.AppConstant;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;


/**
 * Utility wrapper for the preference operations.
 */
public class UtilsHelper {
    private static final String TAG = "UtilsHelper";

    public static String getLocalIpAddress() {
        String localIp = "";
        Enumeration<NetworkInterface> en;
        Enumeration<InetAddress> enumIpAddr;

        try {
            for (en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        // Set the remote ip address the same as
                        // the local ip address of the last netif
                        localIp = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(TAG, "Unable to get local IP address. Not the end of the world", e);
        }
        return localIp;
    }

    /**
     * Because android.os.SystemProperties.get(key) can't work without export from SDK.
     */
    public static String getAndroidSystemProperties(String key) {
        Method prop;
        String value = null;
        try {
            prop = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            value = (String) prop.invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * Get the device serial number
     */
    public static String getSerialNumber() {
        String[] propKeys = {"ro.boot.serialno", "ro.serialno"};
        String value = "";

        for (String key : propKeys) {
            value = UtilsHelper.getAndroidSystemProperties(key);
            Log.i(TAG, "get " + key + ": " + value);
            if (value.length() > 0) {
                break;
            }
        }

        return value;
    }

    /**
     * Create the UUID for the network service identify.
     */
    public static String getUniqueId(Context ctx) {
        if (!TextUtils.isEmpty(AppConstant.DEVICEID)) {
            return AppConstant.DEVICEID;
        }
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            Log.i(TAG, "Telephony manager is not ready, invalid uuid return");
            return "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
        }

        if (ActivityCompat.checkSelfPermission(App.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        String imei = tm.getDeviceId();
        String ssn = "qtimes_libs"/*tm.getSimSerialNumber()*/;
        String aid = android.provider.Settings.Secure.getString(
                ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if (ssn == null) {
            ssn = getSerialNumber();
        }
        Log.i(TAG, "---> IMEI:" + imei + " SIM SN:" + ssn + " AID:" + aid);

        if (imei == null || ssn == null || aid == null) {
            return "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
        }

        UUID uuid = new UUID(aid.hashCode(), ((long) imei.hashCode() << 32) | ssn.hashCode());
        AppConstant.DEVICEID = uuid.toString();

        Log.i(TAG, "---> UUID:" + AppConstant.DEVICEID);
        return AppConstant.DEVICEID;
    }

    public static void saveRemoteId(Context ctx, String uuid) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remote_id", uuid);
        editor.commit();
    }

    public static String getRemoteId(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        String uuid = preferences.getString("remote_id", "");
        return uuid;
    }

    public static void saveLocalId(Context ctx, String uuid) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("local_id", uuid);
        editor.commit();
    }

    public static String getLocalId(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        String uuid = preferences.getString("local_id", "");
        return uuid;
    }

    public static void saveDeviceState(Context ctx, int state) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("state", state);
        editor.commit();
    }

    public static int getDeviceState(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        int state = preferences.getInt("state", 0);
        return state;
    }

    public static void clearDeviceInfo(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("device", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static String getUserAccount(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("username", Activity.MODE_PRIVATE);
        String usr = preferences.getString("usr", "");
        return usr;
    }

    public static void saveAccountInfo(Context ctx, String usr, String pwd) {
        SharedPreferences preferences = ctx.getSharedPreferences("username", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usr", usr);
        editor.putString("pwd", pwd);
        editor.commit();
    }

    public static void clearAccountInfo(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("username", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


}
