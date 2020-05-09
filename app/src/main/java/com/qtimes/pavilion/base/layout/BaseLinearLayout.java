package com.qtimes.pavilion.base.layout;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qtimes.utils.android.PluLog;
import com.qtimes.pavilion.events.NothingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import com.qtimes.pavilion.base.rx.RxLinearLayout;

/**
 * Created by gufei on 2016/9/5 0005.
 */

public abstract class BaseLinearLayout extends RxLinearLayout implements BaseView  {
    protected Context mContext;
    protected View rootView;
    private boolean isRootView;
    private boolean releaseSelf;


    public BaseLinearLayout(Context context) {
        this(context, null);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        } else if (rootView.getClass() == LinearLayout.class) {
            //减少层级
            LinearLayout linearLayout = (LinearLayout) rootView;
            List<View> childViewList = new ArrayList<>();
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View childView = linearLayout.getChildAt(i);
                childViewList.add(childView);
            }
            ((LinearLayout) rootView).removeAllViews();
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
    @LayoutRes
    protected abstract int getLayout();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (releaseSelf) {
            return;
        }

        if (!isUseEventBus()) return;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        PluLog.e("BaseLinearLayout --》释放了");
        if (releaseSelf) {
            return;
        }
        release();
        super.onDetachedFromWindow();
    }

    @Subscribe
    public void doNothing(NothingEvent event) {

    }

    public void setReleaseSelf(boolean self) {
        releaseSelf = self;
    }

    protected boolean isUseEventBus() {
        return true;
    }

    public void release() {
        PluLog.e("BaseLinearLayout --》release");

         if (!isUseEventBus()) return;

        EventBus.getDefault().unregister(this);
    }
}
