package com.qtimes.utils.android;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/*
 * Author: JackHou
 * Date: 2019/8/1.
 * 手电筒（开启闪光灯）工具类
 * 使用前，先申请闪光灯权限 <uses-permission android:name="android.permission.FLASHLIGHT" />
 * 部分手机需要摄像头权限： <uses-permission android:name="android.permission.CAMERA" /> 注：注意android5.0以上权限调用
 * 使用前，请先用hasFlashlight()方法判断设备是否有闪光灯
 * 务必在activity或fragment的onDestroy()方法里调用lightsOff()方法，确保Camera被释放
 *
 * 使用方法
 *  开启sos:new FlashlightUtils().sos()
 *  开启闪光灯: new FlashlightUtils().lightsOn()
 */
public class FlashlightUtils {

    static {
        try {
            Class.forName("android.hardware.Camera");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static FlashlightUtils mFlashlightUtils;
    private static final String TAG = "FlashlightUtils";
    private Camera mCamera;
    private CameraManager manager;
    private boolean isSos = false;//sos ing
    private boolean isLightOn = false; //flash on ?
    private Disposable disposable;
    private boolean mInt = false;


    public static FlashlightUtils Instance() {
        if (mFlashlightUtils == null) {
            mFlashlightUtils = new FlashlightUtils();
        }
        return mFlashlightUtils;
    }


    /**
     * 释放资源
     */
    public void release() {
        mFlashlightUtils.lightsOff();
        offSos();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (isVersionM()) {
            manager = null;
        } else {
            mCamera = null;
        }

    }

    /**
     * 打开手电筒
     *
     * @param context 上下文
     */
    public void lightsOn(Context context) {
        offSos();
        if (hasFlashlight(context) && !isLightOn) {
            if (isVersionM()) {
                lightOn23(context);
            } else {
                lightOn22();
            }
        } else {
            Log.w(TAG, "您的手机不支持开启闪光灯");
        }
    }

    /**
     * 安卓6.0以上打开手电筒
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void lightOn23(Context context) {
        try {
            manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            if (manager != null) {
                manager.setTorchMode("0", true);// "0"是主闪光灯
                isLightOn = true;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * android6.0以下打开手电筒
     */
    private void lightOn22() {
        if (mCamera == null) {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
            isLightOn = true;
        }
    }

    /**
     * 关闭sos
     */
    public void offSos() {
        if (isSos) {
            isSos = false;
            disposable.dispose();
            lightsOff();
        }

    }

    /**
     * 是否sos
     *
     * @return true/false
     */
    public boolean isSos() {
        return isSos;
    }

    /**
     * 是否light on
     *
     * @return true/false
     */
    public boolean isLightOn() {
        return isLightOn;
    }

    /**
     * 打开sos
     *
     * @param context context
     * @param speed   闪烁速度，建议取值1~6
     */
    public void sos(final Context context, int speed) {
        lightsOff();
        if (speed <= 0) {
            throw new RuntimeException("speed不能小于等于0");
        }
        isSos = true;
        disposable = Observable.interval(speed, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mInt = !mInt;
                if (mInt) {
                    lightsOn(context);
                } else {
                    lightsOff();
                }
            }
        });

    }


    /**
     * 关闭闪光灯
     */
    public void lightsOff() {
        if (isVersionM()) {
            lightsOff23();
        } else {
            lightsOff22();
        }
    }

    //安卓6.0以下关闭手电筒
    private void lightsOff22() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.release();
            mCamera = null;
            isLightOn = false;
        }
    }

    //安卓6.0以上打关闭电筒
    @TargetApi(Build.VERSION_CODES.M)
    private void lightsOff23() {
        try {
            if (manager == null) {
                return;
            }
            manager.setTorchMode("0", false);
            isLightOn = false;
            manager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 判断设备是否有闪光灯
     *
     * @param context context
     * @return true/false
     */
    private boolean hasFlashlight(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
