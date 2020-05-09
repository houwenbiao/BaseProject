package com.qtimes.pavilion.status;

import android.content.Context;

import com.qtimes.domain.dagger.qualifier.ContextLevel;
import com.qtimes.utils.android.PluLog;
import com.qtimes.utils.rx.RxNetUtil;

import javax.inject.Inject;

/**
 * 处理网络变化的数据
 * Created by liutao on 2017/1/8.
 */

public class NetBean {
    public final static int CONFIRM = 0X001;//当开始在线请求是，同时需要一个在线查询的动作
    public final static int DEFAULT = 0X000;//当开始在线请求是，同时需要一个在线查询的动作
    private Context context;

    @Inject
    public NetBean(@ContextLevel(ContextLevel.APPLICATION) Context context) {
        this.context = context;
    }


    public void doAction(RxNetUtil.NetStatus status, int action) {
        if (action == CONFIRM) {//开启检查BindMap轮询
            PluLog.e("------> Query binding status...");
        } else {
            if (status.hasNet()) {//如果有网络，通知切换在线
                PluLog.e("发送一条在线信息");
            } else {
                PluLog.e("发送一条断网信息");
            }
        }

    }
}
