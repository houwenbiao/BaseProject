package com.qtimes.wonly.app;

/**
 * Created by hong on 17-1-7.
 */

public class AppConst {
    //保活等级
    public static final int KEEP_ALIVE_LEVEL = 1;

    public interface DialerHost {
        String OPEN_ADB = "616462";
        String CLOSE_ADB = "264616";
    }

    public interface KeepAliveLevel {
        int LEVEL1 = 1;
        int LEVEL2 = 2;
        int LEVEL3 = 3;
        int LEVEL4 = 4;
    }

    public interface Default {
        String APP_NAME = "com.qtimes.wonly";
        String APP_FIRST = "app_first";
        String EASY_DAY_FIRST = "easy_day_first";
        int ASK_WINDOW = 10086;
    }


    public interface AlarmFlag {
        int SINGLE = 0;//单次闹钟
        int DAY = 1;//按天循环
        int WEEK = 2;//按周循环
        int HOUR = 3;//按小时循环
        int MINUTE = 4;//按分钟循环
        int CUSTOM = 5;//自定义循环
    }
}
