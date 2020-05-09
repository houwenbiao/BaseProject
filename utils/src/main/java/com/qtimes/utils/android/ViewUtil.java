package com.qtimes.utils.android;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by hong on 17-1-8.
 */

public class ViewUtil {

    public static String getText(EditText editText){
        String t= editText.getText().toString().trim();
        return TextUtils.isEmpty(t)?"":t;
    }
    public static String getText(TextView editText){
        String t= editText.getText().toString().trim();
        return TextUtils.isEmpty(t)?"":t;
    }

    /**
     * 解决Receiver not registered: android.widget.ZoomButtonsController
     *
     * @param webView
     */
    public static void safelyDestroyWebView(final WebView webView) {
        if (webView != null) {
            webView.setVisibility(View.GONE);// 把destroy()延后
            long timeout = ViewConfiguration.getZoomControlsTimeout();
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (NullUtil.isNull(webView)){
                        return;
                    }
                    try {
                        webView.removeAllViews();
                        webView.destroy();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, timeout);
        }
    }
}
