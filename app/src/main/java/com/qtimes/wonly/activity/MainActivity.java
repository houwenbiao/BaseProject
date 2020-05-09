package com.qtimes.wonly.activity;

import android.os.Bundle;
import android.view.View;

import com.qtimes.wonly.R;
import com.qtimes.wonly.base.activity.MvpActivity;
import com.qtimes.wonly.dagger.component.CommonActivityComponent;
import com.qtimes.wonly.events.AppUpgradeEvent;
import com.qtinject.andjump.api.QtInject;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.OnClick;

@QtInject
public class MainActivity extends MvpActivity<CommonActivityComponent, MainPresenter> implements MainView {

    @Inject
    MainPresenter presenter;

    @Override
    public MainPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        EventBus.getDefault().post(new AppUpgradeEvent());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    /**
     * 使用dagger2需要复写此方法
     */
    @Override
    public void initInject() {
        initCommon().inject(this);
    }

    /**
     * 点击事件
     *
     * @param v view
     */
    @OnClick()
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
