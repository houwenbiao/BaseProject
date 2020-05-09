package com.qtimes.utils.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Activity管理类
 */
public class ActivityMgr {

    private final static String TAG = "ActivityManager";
    private static Context mContext;
    private static Context mCurActivity;
    private static LinkedHashMap<Integer, Activity> mCacheActivities;


    /**
     * activity栈size
     */
    public static int getCacheSize() {
        return mCacheActivities.size();
    }

    public static Activity getCurActivity() {
        return (Activity) mCurActivity;
    }

    public static void init(Context context) {
        mContext = context;
    }


    /**
     * 通过调用对应activity的finish方法，将其从缓存列表中移除
     */
    public static void removeActivity(Class activity) {
        if (mCacheActivities == null || activity == null) {
            return;
        }
        List<Activity> activitys = new ArrayList();
        for (Entry<Integer, Activity> entry : mCacheActivities.entrySet()) {
            activitys.add(entry.getValue());
        }
        for (Activity act : activitys) {
            if (act.getClass().getName().equals(activity.getName())) {
                if (!act.isFinishing()) {
                    mCacheActivities.remove(activity.hashCode());
                    act.finish();
                }
            }
        }
    }

    /**
     * 添加当前Activity到ActivityManager
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        mCurActivity = activity;
        if (mCacheActivities == null) {
            mCacheActivities = new LinkedHashMap<Integer, Activity>();
        }
        int hashCode = activity.hashCode();
        if (mCacheActivities.containsKey(hashCode)) {
            mCacheActivities.remove(hashCode);
        }
        mCacheActivities.put(hashCode, activity);
        PluLog.i("addActivity.activity = "
                + activity.getClass().getSimpleName()
                + ", mCacheActivities.size() = " + mCacheActivities.size());
    }

    /**
     * 在ActivityManager中回收指定的Activity
     *
     * @param activity
     */
    public static void destroyActivity(Activity activity) {
        if (mCacheActivities != null) {
            mCacheActivities.remove(activity.hashCode());
            if (mCurActivity == activity) {
                mCurActivity = null;
            }
            PluLog.i("destroyActivity.activity = "
                    + activity.getClass().getSimpleName()
                    + ", mCacheActivities.size() = " + mCacheActivities.size());
        }
    }

    /**
     * 结束所有Activity
     *
     * @param isIgnoreCurrentActivity 是否忽略当前Activity
     * @return
     */
    public static int finishAllActivity(boolean isIgnoreCurrentActivity) {
        int finishCount = 0;
        PluLog.i("finishAllActivity.mCacheActivities.size() = "
                + (mCacheActivities == null ? 0 : mCacheActivities.size()));
        if (mCacheActivities != null && !mCacheActivities.isEmpty()) {
            List<Activity> activitys = new ArrayList<Activity>();
            for (Entry<Integer, Activity> entry : mCacheActivities.entrySet()) {
                activitys.add(entry.getValue());
            }
            for (Activity activity : activitys) {
                if (!isIgnoreCurrentActivity
                        || (isIgnoreCurrentActivity && activity != mCurActivity)) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                        finishCount++;
                        PluLog.i("finishAllActivity.activity = "
                                + activity.getClass().getSimpleName()
                                + " finished");
                    }
                } else {
                    mCacheActivities.remove(activity.hashCode());
                }
            }
        }

        mCurActivity = null;
        return finishCount;
    }

    public static boolean findActivity(String name) {
        boolean find = false;
        if (TextUtils.isEmpty(name)) {
            return find;
        }
        if (mCacheActivities != null && !mCacheActivities.isEmpty()) {
            List<Activity> activitys = new ArrayList<Activity>();
            for (Entry<Integer, Activity> entry : mCacheActivities.entrySet()) {
                activitys.add(entry.getValue());
            }
            for (Activity activity : activitys) {
                String activityName = activity.getClass().getSimpleName();
                if (name.equals(activityName)) {
                    find = true;
                    break;
                }
            }
        }
        return find;
    }

    public static boolean isTopActivity(Context context, String targetAppPackageName, String targetAppActivityName) {
        String TAG = "Check_Top_Activity";
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> mTasks = mActivityManager.getRunningTasks(1);//5.0

        if (mTasks.get(0) != null) {
            ComponentName topActivity = mTasks.get(0).topActivity;
            if (topActivity != null && !TextUtils.isEmpty(topActivity.getPackageName()) &&
                    !TextUtils.isEmpty(topActivity.getClassName())) {
                Log.w(TAG, "topActivity.getPackageName() -> " + topActivity.getPackageName());
                Log.w(TAG, "topActivity.getClassName() -> " + topActivity.getClassName());
                if (topActivity.getPackageName().equals(targetAppPackageName) &&
                        topActivity.getClassName().equals(targetAppActivityName)) {
                    Log.d(TAG, "target Activity is in top !");
                    return true;
                } else {
                    Log.w(TAG, "target Activity is not in top.");
                    return false;
                }
            } else {
                Log.w(TAG, "topActivity is null or PackageName is null or ClassName is null.");
                return false;
            }
        } else {
            Log.w(TAG, "mTasks.get(0) == null ");
            return false;
        }
    }

    public static boolean isTopActivity(String packageName, Context context) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo process : list) {
            if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTopActivity(Activity activity) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().contains(activity.getClass().getName())) {
            isTop = true;
        }
        return isTop;
    }

    public static boolean isActivityRunning(Context context, String className) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = am.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            PluLog.i("topActivityName: " + component.getClassName() + " className:" + className);
            if (className.equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

