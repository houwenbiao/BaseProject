package com.qtimes.pavilion.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.qtimes.utils.android.ActivityMgr;
import com.qtimes.pavilion.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**baseactivity的代理
 * Created by liutao on 2016/12/13.
 */

public class BaseActivityDelegateImpl extends ActivityDelegateImpl{
    private Unbinder unbinder;

    public BaseActivityDelegateImpl(Activity context){
        super(context);
    }

    @Override
    public void onCreate(Bundle bundle) {
        ActivityMgr.addActivity(context);
        unbinder = ButterKnife.bind(context);
        EventBus.getDefault().register(context);
        //每次可见发送一条检查互T的信息
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ToastUtil.destory();
        EventBus.getDefault().unregister(context);
        ActivityMgr.destroyActivity(context);
    }

}
