package com.qtimes.pavilion.base.layout;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.events.NothingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import com.qtimes.pavilion.base.rx.RxRelativeLayout;

/**
 * author: liutao
 * date: 2016/7/25.
 */
public abstract class BaseRelativeLayout extends RxRelativeLayout implements BaseView  {
    protected Context mContext;
    protected View rootView;

    private boolean isRootView;
    private int KEY_CACHE = -1;//fresco缓存的key

    public BaseRelativeLayout(Context context) {
        this(context, null);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttrs(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        initView();
        initData();
        initListener();
    }

    protected void initListener() {

    }

    protected void initData() {
    }

    protected void handleAttrs(Context context, AttributeSet attributeSet, int defStyleAttr) {

    }

    protected void initView() {
        int resLayoutId = getLayout();
        if (resLayoutId != 0) {
            rootView = LayoutInflater.from(mContext).inflate(getLayout(), this, false);
        }
        if (rootView == null) {
            isRootView = true;
            ButterKnife.bind(this);
        } else if (rootView.getClass() == RelativeLayout.class) {
            //减少层级
            RelativeLayout relativeLayout = (RelativeLayout) rootView;
            List<View> childViewList = new ArrayList<>();
            for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                View childView = relativeLayout.getChildAt(i);
                childViewList.add(childView);
            }
            ((RelativeLayout) rootView).removeAllViews();
            for (int i = 0; i < childViewList.size(); i++) {
                addView(childViewList.get(i));
            }
            isRootView = true;
            ButterKnife.bind(this);
        } else {
            addView(rootView);
            ButterKnife.bind(this, rootView);
        }

        if (!isUseEventBus()) return;
        EventBus.getDefault().register(this);
    }

    @LayoutRes
    protected abstract int getLayout();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isUseEventBus()) return;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    @Subscribe
    public void doNothing(NothingEvent event) {

    }

    public int getKey() {
        if (KEY_CACHE == -1) {
            KEY_CACHE = hashCode();
        }
        return KEY_CACHE;
    }

    @Override
    public boolean isRelease() {
        if(!isUseEventBus()){
            return true;
        }
        if(EventBus.getDefault().isRegistered(this)){
            return false;
        }
        return true;
    }

    @Override
    public void release() {
        PluLog.e("释放了"+getClass().getSimpleName());
        if (!isUseEventBus()) return;
        PluLog.e("调用EventBus 释放了"+getClass().getSimpleName());
        EventBus.getDefault().unregister(this);
    }



    protected boolean isUseEventBus() {
        return true;
    }
}