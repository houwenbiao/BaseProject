package com.qtimes.utils.android;

import android.Manifest;
import android.content.Context;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Author: JackHou
 * Date: 1/19/2018.
 */

public class PermissionUtil {
    public static String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static String[] RECORD_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    public static String[] CAMERA_PERMISSIONS ={
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    public static boolean checkPermission(Context context, String[] permissions) {
        final boolean[] hasPermission = {false};
        RxPermissions.getInstance(context)
                .request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        hasPermission[0] = aBoolean;
                    }
                });
        return hasPermission[0];
    }
}
