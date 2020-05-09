package com.qtimes.pavilion.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.qtimes.utils.android.AndroidUtil;
import com.qtimes.pavilion.app.App;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * author: liutao
 * date: 2016/6/22.
 */
public class ToastUtil {

    private static Toast toast = null;

    private static ProgressDialog progressDialog;

    public static void tip(String s) {
        showToast(s);
    }

    public static void tip(int resId) {
        showToast(resId);
    }

    public static void tip(Context context, String s) {
        showToast(context, s);
    }

    public static void tip(Context context, int resId) {
        showToast(context, resId, Toast.LENGTH_SHORT);
    }

    public static void tip(Context context, String s, int duration) {
        showToast(context, s, duration);
    }

    /**
     * 弹出一个progressDialog
     *
     * @param context    context
     * @param message    提示信息
     * @param cancelable 能否返回取消
     */
    public synchronized static void showProgressDialog(Context context, String message, boolean cancelable) {
        if (progressDialog == null || progressDialog.getContext() != context) {
            progressDialog = null;
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(cancelable);// 设置是否可以通过点击Back键取消
            progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        }
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    public static void dismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    public static boolean isDismiss() {
        return progressDialog == null || !progressDialog.isShowing();
    }

    public static void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void destory() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        closeToast();
    }

    private static void showToast(Context context, String text, int duration) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (AndroidUtil.isScreenOn(context)) {
                    if (toast == null) {
                        toast = Toast.makeText(context, text, duration);
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                }
            }
        });
    }

    private static void showToast(String text, int duration) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (AndroidUtil.isScreenOn(App.getInstance())) {
                    if (toast == null) {
                        toast = Toast.makeText(App.getInstance(), text, duration);
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                }
            }
        });
    }

    private static void showToast(Context context, int resId, int duration) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (AndroidUtil.isScreenOn(context)) {
                    if (toast == null) {
                        toast = Toast.makeText(context, resId, duration);
                    } else {
                        toast.setText(App.getInstance().getString(resId));
                    }
                    toast.show();
                }
            }
        });
    }

    private static void showToast(Context context, String text) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (AndroidUtil.isScreenOn(context)) {
                    if (toast != null) {
                        toast.cancel();
                        toast = null;
                    }
                    toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private static void showToast(String text) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                if (AndroidUtil.isScreenOn(App.getInstance())) {
                    if (toast == null) {
                        toast = Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT);
                    } else {
                        toast.setText(text);
                    }
                    toast.show();
                }
            }
        });
    }

    private static void showToast(int resId) {
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (AndroidUtil.isScreenOn(App.getInstance())) {
                    if (toast == null) {
                        toast = Toast.makeText(App.getInstance(), App.getInstance().getResources().getString(resId), Toast.LENGTH_SHORT);
                    } else {
                        toast.setText(App.getInstance().getResources().getString(resId));
                    }
                    toast.show();
                }
            }
        });
    }

    private static void closeToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}

