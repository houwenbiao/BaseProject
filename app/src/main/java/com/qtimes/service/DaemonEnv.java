package com.qtimes.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class DaemonEnv {

    private DaemonEnv() {
    }

    public static final int DEFAULT_WAKE_UP_INTERVAL = 6 * 60 * 1000;
    private static final int MINIMAL_WAKE_UP_INTERVAL = 3 * 60 * 1000;

    static Context sApp;
    static Class<? extends AbsWorkService> sServiceClass;
    private static int sWakeUpInterval = DEFAULT_WAKE_UP_INTERVAL;
    static boolean sInitialized;

    static final Map<Class<? extends Service>, ServiceConnection> BIND_STATE_MAP = new HashMap<>();

    /**
     * @param app            Application Context.
     * @param wakeUpInterval 定时唤醒的时间间隔(ms).
     */
    public static void initialize(@NonNull Context app, @NonNull Class<? extends AbsWorkService> serviceClass, @Nullable Integer wakeUpInterval) {
        sApp = app;
        sServiceClass = serviceClass;
        if (wakeUpInterval != null) {
            sWakeUpInterval = wakeUpInterval;
        }
        sInitialized = true;
    }

    public static void startServiceMayBind(@NonNull final Class<? extends Service> serviceClass, ServiceConnection serviceConnection) {
        if (!sInitialized) {
            return;
        }
        final Intent i = new Intent(sApp, serviceClass);
        startServiceSafely(i);
        ServiceConnection bound = BIND_STATE_MAP.get(serviceClass);
        if (bound == null) {
            sApp.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public static void startServiceMayBind(@NonNull final Class<? extends Service> serviceClass) {
        startServiceMayBind(serviceClass, new DaemonServiceCon());
    }

    public static class DaemonServiceCon implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BIND_STATE_MAP.put(sServiceClass, this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            BIND_STATE_MAP.remove(sServiceClass);
            final Intent i = new Intent(sApp, sServiceClass);
            startServiceSafely(i);
            if (!sInitialized) {
                return;
            }
            sApp.bindService(i, this, Context.BIND_AUTO_CREATE);
        }
    }

    static void startServiceSafely(Intent i) {
        if (!sInitialized) {
            return;
        }
        try {
            sApp.startService(i);
        } catch (Exception ignored) {
        }
    }

    static int getWakeUpInterval() {
        return Math.max(sWakeUpInterval, MINIMAL_WAKE_UP_INTERVAL);
    }
}
