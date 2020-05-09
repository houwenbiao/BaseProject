package com.qtimes.wonly.app;

import com.qtimes.wonly.R;

/**
 * Author: JackHou
 * Date: 2017/10/25.
 */

public class AppConstant {

    public interface DeviceKey {
        String DEVICE_NAME = "device_name";
        String PRODUCT_KEY = "product_key";
        String DEVICE_SECRET = "device_secret";
    }

    public static String DEVICEID = "";


    public static int[] voices = {
            R.raw.no_notice,
            R.raw.ding_ding,
    };

    public interface VoiceType {
        int NO_NOTICE = 0;
        int DING_DING = 1;
    }

    public interface PlayerAction {
        int RESTART = 0;//直接重头播放
        int START = 1;//如果当前没有播放，就重头播放
        int STOP = 2;
        int PAUSE = 3;
        int LOOP_START = 4;//循环播报

    }

    public interface PlayerType {
        int CLASSIC = 0;//古典
        int LULLABY = 1;//摇篮
    }

    public interface FinishEventType {
        int CANCEL = 0;
        int CONFIRM = 1;
    }

    public interface FunctionType {
        int TRACKER = 0;
        int BACKGROUND_DETECTION = 1;
    }

    public static final int REFRESH_TIMEOUT = 3_000;//下拉刷新的超时时间ms

    //阈值
    public static final float PERSON_THRESHOLD = 0.1f;//判断为人的阈值0.6
    public static final float BG_THRESHOLD = 0.82f;//判断为人的阈值0.6
    public static final float MOVING_THRESHOLD = 14;//两点间距判定为移动的阈值
}
