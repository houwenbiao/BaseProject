package com.qtimes.pavilion.base.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qtimes.utils.android.ViewUtil;
import com.qtimes.views.TitleBarView;
import com.qtimes.pavilion.R;
import com.qtimes.pavilion.dagger.component.CommonActivityComponent;
import com.qtinject.andjump.api.QtInject;

import butterknife.BindView;

/**
 * BASE WEB页面，展示对应URL的网页
 * <p>
 * TODO：做成Fragment，提高复用性
 */
@SuppressLint("JavascriptInterface")
public class WebViewActivity extends DaggerActiviy<CommonActivityComponent> {

    private final String TAG = WebViewActivity.class.getSimpleName();
    @BindView(R.id.webview)
    WebView webview;

    @QtInject
    String url;

    @QtInject
    String activityTitle;
    /**
     * 是否是签到页面进入
     */
    @QtInject
    boolean signFlag;


    /**
     * 初始化参数数据
     *
     * @param intent [url, title]
     */
    private void initContentData(Intent intent) {
        QtWebViewActivity.inject(this);
        if (activityTitle != null) {
            setTitle(activityTitle);
        }
        if (!TextUtils.isEmpty(url)) {
            webview.loadUrl(url);
        }
    }

    @Override
    public void initInject() {
        initCommon().inject(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        titleBarView.setTitleText(title);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initContentData(intent);
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setVisible(false);
    }

    /**
     * 返回操作
     * <p>
     * 在webview不可以返回上一页时，finish当前页
     */
    private void handleNoFinishBoundClick() {
        if (webview.canGoBack()) {
            webview.goBack();   //后退
        } else {
            finish();
        }
    }


    /**
     * 返回键退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            handleNoFinishBoundClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_webview);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        QtWebViewActivity.inject(this);
        initWeb();
        initContentData(getIntent());

    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBarView.setTitleBarListener(new TitleBarView.TitleBarListener() {
            @Override
            public void onClickLeft() {
                finish();
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
        });
    }
    //    @OnClick({R.id.tvCollect})
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.tvCollect:
////                daoSession.getSubjectBeanDao().
//                break;
//        }
//    }
    protected void initWeb() {
        webview.requestFocusFromTouch();
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (activityTitle == null) {
                    setTitle(title);
                }
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            /* 这个事件，将在用户点击链接时触发。
      * 通过判断url，可确定如何操作，
      * 如果返回true，表示我们已经处理了这个request，
      * 如果返回false，表 示没有处理，
      * 那么浏览器将会根据url获取网页*/
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //跳转到某activity 跟据url内容匹配出信息，添加Bundle
                return false; //
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  //接受证书
            }
        });
        //webview的安全问题：移除存在漏洞的接口http://www.droidsec.cn/webview-%E8%BF%9C%E7%A8%8B%E4%BB%A3%E7%A0%81%E6%89%A7%E8%A1%8C%E6%BC%8F%E6%B4%9E%E6%B5%85%E6%9E%90/
        webview.removeJavascriptInterface("accessibility");
        webview.removeJavascriptInterface("accessibilityTraversal");
        webview.removeJavascriptInterface("searchBoxJavaBridge_");

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("TGA/2.0" + webSettings.getUserAgentString());
        if (signFlag) {
            webSettings.setBuiltInZoomControls(true);
        } else {
            webSettings.setBuiltInZoomControls(false);
        }
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSavePassword(false);//使用Webview时需要关闭webview的自动保存密码功能，防止用户密码被webview明文存储。

        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  //表示按返回键
                        handleNoFinishBoundClick();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public void onDestroy() {
        if (null != webview) {
            ViewUtil.safelyDestroyWebView(webview);
        }
        super.onDestroy();
    }


    /**
     * 启动web 页
     *
     * @param context
     * @param title
     * @param url
     */
    public static void start(Context context, String title, String url) {
        if (url == null) url = "";
        url = url.startsWith("http") ? url : "http://" + url;
        if (context instanceof Activity) {
            QtWebViewActivity.getInstance().setUrl(url).setActivityTitle(title).start(context);
        } else {
            Intent intent = QtWebViewActivity.getInstance().setUrl(url).setActivityTitle(title).createIntent(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}