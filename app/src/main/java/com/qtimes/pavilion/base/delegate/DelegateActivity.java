package com.qtimes.pavilion.base.delegate;

import android.os.Bundle;

import com.qtimes.pavilion.base.rx.RxAppCompatActivity;

/**使用activity delegate代理
 * Created by liutao on 2016/12/17.
 */

public class DelegateActivity extends RxAppCompatActivity {

    protected ActivityDelegate activityDelegate;


    private ActivityDelegate getActivityDelegate() {
        if(activityDelegate==null){
            activityDelegate=initActivityDelegate();
        }
        return activityDelegate;
    }

    protected ActivityDelegate initActivityDelegate() {
        return  new ActivityDelegateImpl(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getActivityDelegate().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getActivityDelegate().onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActivityDelegate().onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getActivityDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getActivityDelegate().onstop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
