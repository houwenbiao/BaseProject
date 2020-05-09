package com.qtimes.pavilion.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;


/**fragment的生命周期
 * 需要在fragment相应的生命周期中调用
 * Created by liutao on 2016/12/13.
 */

public interface FragmentDelegate {


    public void onCreate(Bundle saved);


    public void onDestroy();


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState);


    public void onDestroyView();


    public void onPause();


    public void onResume();


    public void onStart();


    public void onStop();

    public void onActivityCreated(Bundle savedInstanceState);


    public void onAttach(Activity activity);

    public void onDetach();


    public void onSaveInstanceState(Bundle outState);
}
