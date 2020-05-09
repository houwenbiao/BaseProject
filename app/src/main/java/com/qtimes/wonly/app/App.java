package com.qtimes.wonly.app;

import android.app.Application;
import android.app.Notification;
import android.content.Context;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.qtimes.data.module.ApiModule;
import com.qtimes.domain.base.SimpleSubscriber;
import com.qtimes.ipc.service.AirService;
import com.qtimes.ipc.service.AirServiceControlImpl;
import com.qtimes.ipc.service.DaemonEnv;
import com.qtimes.utils.android.AndroidUtil;
import com.qtimes.utils.android.FrescoUtil;
import com.qtimes.utils.android.PluLog;
import com.qtimes.utils.android.ScreenUtil;
import com.qtimes.utils.rx.RxNetUtil;
import com.qtimes.wonly.R;
import com.qtimes.wonly.dagger.component.ApplicationComponent;
import com.qtimes.wonly.dagger.component.DaggerApplicationComponent;
import com.qtimes.wonly.dagger.modules.ApplicationModule;
import com.qtimes.wonly.delegate.AppDelegateManager;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Author: JackHou
 * Date: 2017/10/13.
 */

public class App extends Application {

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimaryDark, android.R.color.white);//全局设置主题颜色
                return new WaterDropHeader(context);
            }
        });
    }

    private static App app;
    private ApplicationComponent applicationComponent;
    @Inject
    AppDelegateManager appDelegateManager;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (app == null) {
            app = this;
        }

        //FileDownloader init
        FileDownloadLog.NEED_LOG = false;
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();

        //双进程保活
        //双进程保活
        AirServiceControlImpl airServiceControl = new AirServiceControlImpl(this);
        DaemonEnv.initialize(this, AirService.class, 30 * 1000);
        PluLog.i("App________________________start AirService");
        airServiceControl.startService();
        PluLog.i("App________________________bind AirService");
        airServiceControl.bindService();

        //极光推送相关配置
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        JPushInterface.init(this);    // 初始化 JPush
        PluLog.i("App RegistrationID = " + JPushInterface.getRegistrationID(this));

        //友盟初始化
        UMConfigure.init(this, "5e66f79a978eea07251a0170", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(false);
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        if (AndroidUtil.isCurrentProcess(app, getApplicationContext().getPackageName())) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .apiModule(new ApiModule())
                    .applicationModule(new ApplicationModule(this))
                    .build();
            applicationComponent.inject(this);
            appDelegateManager.register();
            FrescoUtil.initFresco(getApplicationContext());
            ScreenUtil.init(getApplicationContext());

            RxNetUtil.getInstance().observeNet(App.getInstance())
                    .subscribe(new SimpleSubscriber<RxNetUtil.NetStatus>() {
                        @Override
                        public void onNext(RxNetUtil.NetStatus netStatus) {
                            super.onNext(netStatus);
                            PluLog.e("网络变化" + netStatus.getCurrentState().name());
                        }
                    });
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }

    public static App getInstance() {
        return app;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
