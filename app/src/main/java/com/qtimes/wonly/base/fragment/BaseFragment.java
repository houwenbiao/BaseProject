package com.qtimes.wonly.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qtimes.wonly.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.qtimes.wonly.base.layout.BaseView;
import com.qtimes.wonly.base.rx.RxFragment;

/**
 * baseFragment,集成dagger
 * author: liutao
 * date: 2016/6/12.
 */
public abstract class BaseFragment extends RxFragment
{
    protected Activity mContext;
    protected String mUmengTag = null; //友盟统计标签
    protected String mLRTag = null; //友盟统计标签
    private Unbinder unbinder;
    private boolean hasAttach;
    private boolean isFirstVisible = true;
    private int KEY_CACHE = -1;//frescoutil的缓存的key值，作用，对单个页面的缓存进行管理
    private List<BaseView> releaseView = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mUmengTag = this.getClass().getSimpleName();
        getLRTag();
        mContext = getActivity();
        View v = getInflateView();
        if (v == null)
        {
            v = View.inflate(getContext(), getLayout(), null);
        }
        unbinder = ButterKnife.bind(this, v);
        registerEventBus();
        initView(v);
        return v;
    }

    public String getLRTag()
    {
        if (mLRTag == null)
        {
            mLRTag = getClass().getSimpleName();
        }
        return mLRTag;
    }

    public void attachViews(BaseView... views)
    {
        if (views == null)
        {
            return;
        }
        for (int i = 0, len = views.length; i < len; i++)
        {
            if (views[i] != null)
            {
                releaseView.add(views[i]);
            }
        }
    }

    public void releaseView()
    {
        if (releaseView == null || releaseView.size() == 0)
        {
            return;
        }
        for (BaseView view : releaseView)
        {
            if (view != null && !view.isRelease())
            {
                view.release();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        hasAttach = true;
        initData();
        initListener();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && hasAttach)
        {
            onUiVisible(isFirstVisible);
            isFirstVisible = false;
        }
    }


    public void onUiVisible(boolean first)
    {

    }


    protected boolean hasAttachView()
    {
        return hasAttach;
    }

    protected void initListener()
    {

    }

    protected abstract void initData();

    protected void initView(View v)
    {

    }

    protected <T extends Fragment> T findFragment(Class<T> fragmentClazz, String tag, Bundle bundle)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null)
        {
            fragment = Fragment.instantiate(getActivity(), fragmentClazz.getName(), bundle);
        }
        return (T) fragment;
    }

    protected void hideFragment(String tag)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null)
        {
            fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    protected void removeFragment(String tag)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null)
        {
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        releaseView();
        unbinder.unbind();
        unRegisterEventBus();
        ToastUtil.destory();
    }

    @LayoutRes
    protected abstract int getLayout();

    public View getInflateView()
    {
        return null;
    }

    //可在activity按返回键回掉。
    public boolean onBack()
    {
        return false;
    }


    public int getKey()
    {
        if (KEY_CACHE == -1)
        {
            KEY_CACHE = hashCode();
        }
        return KEY_CACHE;
    }

    protected void registerEventBus()
    {
        if (useEventBus())
        {
            if (!EventBus.getDefault().isRegistered(this))
            {
                EventBus.getDefault().register(this);
            }
        }
    }

    protected void unRegisterEventBus()
    {
        if (useEventBus())
        {
            EventBus.getDefault().unregister(this);
        }
    }

    public boolean useEventBus()
    {
        return false;
    }
}
