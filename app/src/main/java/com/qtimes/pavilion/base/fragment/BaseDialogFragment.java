package com.qtimes.pavilion.base.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.qtimes.utils.android.PluLog;
import com.qtimes.views.TitleBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.qtimes.pavilion.events.NothingEvent;
import com.qtimes.pavilion.utils.ToastUtil;

import com.qtimes.pavilion.R;
import com.qtimes.pavilion.base.rx.RxDialogFragment;

/**
 * Created by plu on 2016/9/2.
 */

public abstract class BaseDialogFragment extends RxDialogFragment implements TitleBarView.TitleBarListener {
    protected Activity mContext;
    protected String mUmengTag = null; //友盟统计标签
    private TitleBarView activityTitle;
    private boolean hasAttach;
    private Unbinder unbinder;
    private boolean isFirstVisible = true;

    public void setFullscreen(boolean fullscreen) {
        mIsFullScreen = fullscreen;
    }

    protected boolean mIsFullScreen = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        View v = getInflateView();
        if (v == null) {
            v = View.inflate(getContext(), getLayout(), null);
        }
        unbinder = ButterKnife.bind(this, v);
        registerEventBus();
        activityTitle = (TitleBarView) v.findViewById(R.id.titleBar);
        initView(v);
        return v;
    }

    private void setWindowFullscreen() {
        if (mIsFullScreen) {
            final Window window = getDialog().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(-1, -2);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWindowFullscreen();
        initData(savedInstanceState);
        initListener();
        hasAttach = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && hasAttach) {
            onUiVisible(isFirstVisible);
            isFirstVisible = false;
        }
    }


    protected void onUiVisible(boolean first) {

    }


    protected boolean hasAttachView() {
        return hasAttach;
    }

    protected void initListener() {
        if (activityTitle != null) {
            activityTitle.setTitleBarListener(this);
        }
    }

    protected abstract void initData(Bundle bundle);

    protected void initView(View v) {

    }

    /**
     * 设置友盟标签
     */
    protected void setUmengTag(String umengTag) {
        mUmengTag = umengTag;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unRegisterEventBus();
        ToastUtil.destory();
        PluLog.e("销毁Fragment");
    }

    @LayoutRes
    protected abstract int getLayout();

    public View getInflateView() {
        return null;
    }

    //可在activity按返回键回掉。
    public boolean onBack() {
        return false;
    }


    @Override
    public void onClickLeft() {

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
    public void onMoreViewClick(View view) {

    }

    public void setTitle(CharSequence title) {
        if (activityTitle != null) {
            activityTitle.setTitleText(title);
        }
    }

    public void setRightTitle(CharSequence title) {
        if (activityTitle != null) {
            activityTitle.setRightText(title);
        }

    }

    public void setRightTitle(int title) {
        if (activityTitle != null) {
            activityTitle.setRightText(title);
        }

    }

    protected void registerEventBus() {
        if (useEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    protected void unRegisterEventBus() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    protected TitleBarView getTitleBarView() {
        return activityTitle;
    }

    public boolean useEventBus() {
        return false;
    }

    @Subscribe
    public void noTing(NothingEvent suiPaiEvent) {

    }
}