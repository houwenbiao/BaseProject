package com.qtimes.utils.android;

import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;

/**
 * Author: JackHou
 * Date: 2020/1/11.
 * Camera工具类
 */
public class CameraUtil {

    /**
     * 获取camera数
     *
     * @return
     */
    public static int getNumberOfCameras() {
        return Camera.getNumberOfCameras();
    }


    /**
     * 判断摄像头是否可用
     *
     * @return boolean
     */
    public static boolean cameraCanUse() {
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras < 1) {
            return false;
        }
        boolean canUse = false;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            PluLog.e("cameraCanUse: " + e);
            canUse = false;
        }

        if (mCamera != null) {
            mCamera.release();
            canUse = true;
        }
        return canUse;
    }
}
