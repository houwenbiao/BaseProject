package com.qtimes.utils.android;

import android.text.TextUtils;
import android.util.Log;

import com.qtimes.utils.BuildConfig;


/**
 * 专门检测是否为空的util
 * 检测到Null会在log中打印出来，并提示相应的行数
 * 请及时解决。
 * author: liutao
 * date: 2016/7/26.
 */
public class NullUtil {
    public static String tagPrefix = "NullUtil";//log前缀
    public static boolean debug = BuildConfig.DEBUG;

    public static boolean isNull(Object... objects) {
        if (debug) {
            return isNullDeubug(objects);
        } else {
            return isNullRelease(objects);
        }
    }

    private static boolean isNullDeubug(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (int i = 0, len = objects.length; i < len; i++) {
            Object o = objects[i];
            if (null == o) {
                String tag = getTag(getCallerStackTraceElement());
                Log.e(tag, "index = " + i + " is Null");
                return true;
            }
        }
        return false;
    }

    private static boolean isNullRelease(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (Object o : objects) {
            if (null == o) {
                return true;
            }
        }
        return false;
    }

    private static String getTag(StackTraceElement element) {
        try {
            String tag = "%s.%s(Line:%d)"; // 占位符
            String callerClazzName = element.getClassName(); // 获取到类名
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
            tag = String.format(tag, callerClazzName, element.getMethodName(), element.getLineNumber()); // 替换
            tag = TextUtils.isEmpty(tagPrefix) ? tag : tagPrefix + ":" + tag;
            return tag;
        } catch (Exception e) {
            return tagPrefix;
        }
    }

    /**
     * 获取线程状态
     *
     * @return
     */
    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[5];
    }
}
