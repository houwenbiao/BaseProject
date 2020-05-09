package com.qtimes.pavilion.base.delegate;

import android.os.Bundle;

/**activity生命的周期
 * Created by liutao on 2016/12/13.
 */

public interface ActivityDelegate {

    public void onCreate(Bundle bundle);

    public void onDestroy();

    public void onPause();

    public void onResume();

    public void onStart();

    public void  onstop();

}
