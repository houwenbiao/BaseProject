package com.qtimes.wonly.tip;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtimes.domain.base.SimpleSubscriber;
import com.qtimes.domain.cache.AccountCache;
import com.qtimes.utils.android.NullUtil;
import com.qtimes.wonly.R;
import com.qtimes.wonly.app.AppConstant;
import com.qtimes.wonly.base.activity.DaggerActiviy;
import com.qtimes.wonly.dagger.component.CommonActivityComponent;
import com.qtimes.wonly.events.FinishEvent;
import com.qtimes.wonly.utils.CommonTransformer;
import com.qtinject.andjump.api.QtInject;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by liutao on 2017/1/16.
 */
public class QuDialogActivity extends DaggerActiviy<CommonActivityComponent> {

    @BindView(R.id.dialog_content)
    TextView dialogContent;
    @BindView(R.id.dialog_msg)
    TextView dialogMsg;
    @BindView(R.id.dialog_cancel)
    Button dialogCancel;
    @BindView(R.id.dialog_confirm)
    Button dialogConfirm;
    @BindView(R.id.dialog_confirm_layout)
    LinearLayout dialogConfirmLayout;
    @BindView(R.id.dialog_content_layout)
    RelativeLayout dialogContentLayout;
    @BindView(R.id.dialog_head)
    ImageView dialogHead;
    @Inject
    AccountCache accountCache;
    @QtInject
    int dialogType;
    //内容
    String cancelText;//取消内容
    String confirmText;//确定内容
    String contentText;//内容
    String txtMsg;//
    @Inject
    Ringer ringer;
    public static int RING_CHECK_DELAY = 5000;
    AudioManager mAudioManager;

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_common);
        setFinishOnTouchOutside(false);
    }

    @Override
    public void initInject() {
        initCommon().inject(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        QtQuDialogActivity.inject(this);
        switch (dialogType) {
            case DialogType.DIALOG_NET_UNCONNECTED:
                contentText = getResources().getString(R.string.net_unconnected_warning);
                txtMsg = getResources().getString(R.string.txt_please_checked_net);
                dialogHead.setImageResource(R.mipmap.alert_q_5);
                cancelText = getResources().getString(R.string.txt_cancel);
                confirmText = getResources().getString(R.string.txt_confirm);
                break;
        }
        dialogContent.setText(contentText);
        dialogCancel.setText(cancelText);
        dialogMsg.setText(txtMsg);
        dialogConfirm.setText(confirmText);
    }

    public void ring() {//五秒轮询
        Observable.interval(100, RING_CHECK_DELAY, TimeUnit.MILLISECONDS, Schedulers.io())
                .compose(new CommonTransformer.Builder<Long>(this).build())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mAudioManager == null) {
                            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                            mAudioManager.requestAudioFocus(null,
                                    AudioManager.STREAM_ALARM,
                                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                        }
                        mAudioManager.abandonAudioFocus(null);
                        if (!NullUtil.isNull(ringer)) {
                            ringer.stopRing();
                        }
                    }
                })
                .subscribe(new SimpleSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
                        if (!ringer.isRinging()) {
                            ringer.ring();
                        }
                    }
                });
    }

    @OnClick({R.id.dialog_cancel, R.id.dialog_confirm})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                EventBus.getDefault().post(new FinishEvent(dialogType, AppConstant.FinishEventType.CANCEL));
                finish();
                break;
            case R.id.dialog_confirm:
                EventBus.getDefault().post(new FinishEvent(dialogType, AppConstant.FinishEventType.CONFIRM));
                dialogConfirmResult();
                finish();
                break;
        }
    }

    private void dialogConfirmResult() {
        switch (dialogType) {

        }
    }
}

