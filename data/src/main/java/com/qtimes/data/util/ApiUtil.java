package com.qtimes.data.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.qtimes.utils.android.AndroidUtil;
import com.qtimes.utils.android.PluLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

/**
 * Created by hong on 16-12-21.
 */

public class ApiUtil {
    public static String KEY_TOKEN = "token";
    public static String KEY_DEVICE_NAME = "deviceName";
    public static String KEY_EXPIRED_IN = "expired_in";
    public static String KEY_USER = "user";
    public static String KEY_API = "api";
    public static String KEY_OS = "os";
    public static String KEY_UUID = "uuid";

    public static JSONObject buildHeader(String token,
                                         String deviceName,
                                         String expired_in,
                                         String user,
                                         String uuid,
                                         String api,
                                         String os) {
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_TOKEN, token);
            json.put(KEY_DEVICE_NAME, deviceName);
            json.put(KEY_EXPIRED_IN, expired_in);
            json.put(KEY_USER, user);
            json.put(KEY_UUID, uuid);
            json.put(KEY_API, api);
            json.put(KEY_OS, os);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static String buildAuthorization(String accessToken) {
        return "Bearer " + accessToken;
    }

    private static String signPlain(String plain) {
        String code = "";

        try {
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            byte[] hash = msgDigest.digest(plain.getBytes());

            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash)
                sb.append(String.format("%02x", b & 0xff));

            code = sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return code;
    }

    public static String generateSignCode(String appId, String appSecret, String ts) {
        String plain = appId + ts + appSecret;
        return signPlain(plain);
    }

    public static String signMap(String appId, String appSecret, String ts, Map<String, String> map) {
        // Sort original parameters
        String[] keys = map.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // Compose signed parameters
        StringBuilder sb = new StringBuilder();
        sb.append(appId);
        sb.append(ts);
        for (String key : keys)
            sb.append(key).append(map.get(key));
        sb.append(appSecret);

        return signPlain(sb.toString());
    }

    public static String signQuery(String appId, String appSecret, String ts, String query) {
        // Compose signed parameters
        StringBuilder sb = new StringBuilder();
        sb.append(appId);
        sb.append(ts);
        sb.append(query);
        sb.append(appSecret);
        PluLog.i("appId:" + appId + " appSecret:" + " ts:" + ts + " query:" + query);
        return signPlain(sb.toString());
    }


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
            value = getAndroidSystemProperties(key);
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
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer mInteger) {
                    if (AndroidUtil.isScreenOn(ctx)) {
                        Toast.makeText(ctx, "请打开手机权限", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        String imei = tm.getDeviceId();
        String ssn = tm.getSimSerialNumber();
        String aid = android.provider.Settings.Secure.getString(
                ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if (ssn == null) {
            ssn = getSerialNumber();
        }
        if (imei == null) {
            imei = "000000";
        }
        UUID uuid = new UUID(aid.hashCode(), ((long) imei.hashCode() << 32) | ssn.hashCode());
        return uuid.toString();
    }

}

