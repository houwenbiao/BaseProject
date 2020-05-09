package com.qtimes.wonly.events;

import com.qtimes.domain.bean.UpdateAppInfo;
import com.qtimes.utils.android.PluLog;

/**
 * Author: JackHou
 * Date: 2017/10/26.
 */

public class DialogEvent {
    private int type;//dialog类型
    private UpdateAppInfo updateAppInfo;//App版本更新信息，这个后面要设置成泛型

    public DialogEvent(int dialogType) {
        PluLog.e("创建一个DialogEvent");
        this.type = dialogType;
    }

    public DialogEvent(int type, UpdateAppInfo content) {
        this.type = type;
        this.updateAppInfo = content;
    }

    public UpdateAppInfo getInfo() {
        return updateAppInfo;
    }

    public int getType() {
        return type;
    }
}
