package com.qtimes.pavilion.base.delegate;

import android.app.Activity;
import android.os.Bundle;

/**代理activity的生命周期
 * Created by liutao on 2016/12/13.
 */

public class ActivityDelegateImpl implements ActivityDelegate {
    protected Activity context;
    public ActivityDelegateImpl(Activity context){
        this.context=context;
    }

    @Override
    public void onCreate(Bundle bundle) {
    }
    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onstop() {

    }


    @Override
    public void onDestroy() {

    }

}
