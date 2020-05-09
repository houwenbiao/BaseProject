package com.qtimes.wonly.tip;

/**
 * Created by liutao on 2017/1/27.
 */

public interface DialogType {
    //APP版本更新相关
    int DIALOG_UPDATE_FORCE = 0x012;//强制版本升级
    int DIALOG_UPDATE_NORMAL = 0x013;//正常版本升级
    int DIALOG_UPDATE_PATCH = 0x014;//热更新
    int DIALOG_KILLPROCESS = 0x015;//关闭APP
    int DIALOG_UPDATE_QU = 0x016;//小趣端版本升级

    int DIALOG_NET_UNCONNECTED = 0x018;//网络未连接
}
