package com.qtimes.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.qtimes.views.R;


/**
 * Created by liujun on 16-7-4.
 * 自定义对话框
 */
public class MyDialog extends Dialog implements MyDialogInterface {

    public final static int TYPE_TOAST = 1;

    private OnShowListener onShowListener;
    private OnDismissListener onDismissListener;
    private MyDialogController dialogController;

    public MyDialog(Context context) {
        super(context, R.style.MyDialogTheme);
        dialogController = new MyDialogController(getContext(), this, getWindow());
        initListener();
    }


    public void setTitle(CharSequence title) {
        dialogController.setTitle(title);
    }


    public void setContentMsg(String contentMsg) {
        dialogController.setContentMsg(contentMsg);
    }

    public void setConfirmButton(CharSequence txt) {
        dialogController.setConfirmButton(txt);
    }

    public void setCancelButton(CharSequence txt) {
        dialogController.setCancelButton(txt);
    }

    public EditText getInputView() {
        return dialogController.getInputView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogController.setupView();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return dialogController.onTouchEvent(event) || super.onTouchEvent(event);
    }


    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }


    private void initListener() {
        super.setOnShowListener(dialogController);
        super.setOnDismissListener(dialogController);
    }

    @Override
    public void dismiss() {
        dialogController.dismiss();
    }

    private void superDismiss() {
        super.dismiss();
    }


    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        dialogController.setCancelable(flag);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        dialogController.setCancelable(cancel);
    }


    @Override
    public void dismissMyDialog() {
        superDismiss();
    }


    public static class Builder {

        private Context context;

        private MyDialogController.Params params;

        public Builder(Context context) {
            this(context, R.style.MyDialogTheme);
        }

        public Builder(Context context, int theme) {
            this.context = new ContextThemeWrapper(context, theme);
            params = new MyDialogController.Params(context);
        }

        public Builder setType(int type) {
            params.type = type;
            return this;
        }

        public Builder setTitle(CharSequence charSequence) {
            params.title = charSequence;
            return this;
        }

        public Builder setConentMsg(CharSequence charSequence) {
            params.contentMsg = charSequence;
            return this;
        }

        public Builder setConfirmButton(CharSequence txt, View.OnClickListener onClickListener) {
            params.confirmTxt = txt;
            params.confirmBtnListener = onClickListener;
            return this;
        }

        public Builder setConfirmButton(CharSequence txt, int textColor, View.OnClickListener onClickListener) {
            params.confirmTxt = txt;
            params.confirmBtnListener = onClickListener;
            params.color_confirmTxt = textColor;
            return this;
        }

        public Builder setCancelButton(CharSequence txt, View.OnClickListener onClickListener) {
            params.cancelTxt = txt;
            params.cancelBtnListener = onClickListener;
            return this;
        }

        public Builder setCancelButton(CharSequence txt, int textColor, View.OnClickListener onClickListener) {
            params.cancelTxt = txt;
            params.cancelBtnListener = onClickListener;
            params.color_cancelTxt = textColor;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }


        public Builder setContentView(int contentViewId) {
            params.customView = null;
            params.layoutId = contentViewId;
            return this;
        }

        public Builder setContentView(View contentView) {
            params.layoutId = 0;
            params.customView = contentView;
            return this;
        }

        public Builder setEditInput(boolean isEdit) {
            params.edit = isEdit;
            return this;
        }


        public MyDialog create() {
            MyDialog myDialog = new MyDialog(context);
            params.apply(myDialog.dialogController);
            return myDialog;
        }
    }
}
