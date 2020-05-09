package com.qtimes.wonly.base.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import com.qtimes.utils.android.ActivityMgr;
import com.qtimes.views.TitleBarView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.qtimes.wonly.utils.ToastUtil;

import com.qtimes.wonly.R;
import com.qtimes.wonly.base.layout.BaseView;
import com.qtimes.wonly.base.rx.RxAppCompatActivity;
import com.qtimes.wonly.app.AppConst;

/**
 * 使用activity delegate代理
 * Created by liutao on 2016/12/17.
 */
public abstract class BaseActivity extends RxAppCompatActivity implements TitleBarView.TitleBarListener {
    @Nullable
    @BindView(R.id.titleBar)
    TitleBarView titleBarView;
    protected Context mContext;
    private List<BaseView> releaseView = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityMgr.addActivity(this);
        initView();
        ButterKnife.bind(this);
        registerEventBus();
        initData(savedInstanceState);
        initListener();
    }

    public void attachViews(BaseView... views) {
        if (views == null) {
            return;
        }
        for (int i = 0, len = views.length; i < len; i++) {
            if (views[i] != null) {
                releaseView.add(views[i]);
            }
        }
    }

    public void releaseView() {
        if (releaseView == null || releaseView.size() == 0) {
            return;
        }
        for (BaseView view : releaseView) {
            if (view != null && !view.isRelease()) {
                view.release();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseView();
        ActivityMgr.destroyActivity(this);
        unRegisterEventBus();
        ToastUtil.destory();
    }

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected void initListener() {
        if (titleBarView != null) {
            titleBarView.setTitleBarListener(this);
        }
    }

    protected void registerEventBus() {
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unRegisterEventBus() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onClickLeft() {
        finish();
    }

    @Override
    public void onClickTitle() {

    }

    @Override
    public void onClickRight() {

    }

    @Override
    public void onDoubleClickTitle() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConst.Default.ASK_WINDOW) {

        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (titleBarView != null && title != null) {
            titleBarView.setTitleText(title);
        }
    }

    @Override
    public void onMoreViewClick(View view) {

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
