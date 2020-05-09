package com.qtimes.wonly.views.dialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by liutao on 2017/1/7.
 */

public class QuDialogHelp {

    public static QuDialog createDialog(Context context, String content, String msg, DialogType dialogType) {
        QuDialog.Builder builder = new QuDialog.Builder(context);
        builder.setContent(content).setMessage(msg).setDialogType(dialogType);
        builder.setCancelable(true);
        String cancelText = null;
        String confirmText = null;
        switch (dialogType) {
            case DIALOG_BLUE://蓝色头像
                cancelText = "知道了";
                confirmText = "查看情况";
                break;
            case DIALOG_CREEN:
                cancelText = "忽略";
                confirmText = "起床了";
                break;
            case DIALOG_PURPEL:
                cancelText = "忽略";
                confirmText = "睡着了";
                break;
            case DIALOG_RED:
                cancelText = "知道了";
                break;
        }
        builder.setConfirmButton(confirmText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelButton(cancelText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
