package com.qtimes.utils.java;

/**
 * author: liutao
 * date: 2016/6/30.
 */
public class HelpUtil {
    public static int DOUBLECLICKTIME=400;
    public static long DoubleClick[]=new long[2];

    /**
     * 采用自己的重复点击策略
     * @param mClicks
     * @param DoubleClickTime
     * @return
     */
    public synchronized static boolean isOnDoubleClick(long mClicks[],long DoubleClickTime){
        System.arraycopy(mClicks, 1, mClicks, 0, mClicks.length - 1);
        mClicks[mClicks.length - 1] = System.currentTimeMillis();
        return mClicks[0] >= (System.currentTimeMillis() - DoubleClickTime);
    }
    public synchronized static boolean isOnDoubleClick(){
        return isOnDoubleClick(DOUBLECLICKTIME);
    }

    /**
     * 重复点击的判断，采用全局策略
     * 即：全局所有事件共享点击的时长。
     * @param time
     * @return
     */
    public synchronized static boolean isOnDoubleClick(int time){
        System.arraycopy(DoubleClick, 1, DoubleClick, 0, DoubleClick.length - 1);
        DoubleClick[DoubleClick.length - 1] = System.currentTimeMillis();
        return DoubleClick[0] >= (System.currentTimeMillis() - time);
    }
}
