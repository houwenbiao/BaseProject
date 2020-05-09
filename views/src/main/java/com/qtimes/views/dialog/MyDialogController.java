package com.qtimes.views.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.qtimes.views.R;


/**
 * Created by liujun on 16-7-4
 * 对话框控制器,控制绘制和行为.
 */
public class MyDialogController implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    private final static long TOAST_LENGTH_LONG = 2000;
    private final static long TOAST_LENGTH_SHORT = 1000;

    private Context context;
    private MyDialogInterface dialogInterface;
    private Window window;

    private int toastLayoutId;
    private int dialogLayoutId;
    private int enterAnimResId;
    private int exitAnimResId;

    private int type;
    public View customView;
    public int layoutId;

    private View parentView;
    private TextView tvTitle;
    private View contentView;
    private TextView tvContent;
    private TextView btnConfirm;
    private TextView btnCancel;
    private View btnDivider;
    private EditText editText;
    private View bottomLayout;

    private boolean isInput = false;
    private boolean cancelable = true;
    public long toastDuration = TOAST_LENGTH_LONG;
    public CharSequence title;
    public CharSequence contentMsg;
    public CharSequence confirmTxt;
    public CharSequence cancelTxt;
    public int color_confirmTxt;
    public int color_cancelTxt;

    public View.OnClickListener confirmBtnListener;
    public View.OnClickListener cancelBtnListener;
    private DialogInterface.OnShowListener onShowListener;
    private DialogInterface.OnDismissListener onDismissListener;

    private boolean isDismiss;

    public MyDialogController(Context context, MyDialogInterface dialogInterface, Window window) {
        this.context = context;
        this.dialogInterface = dialogInterface;
        this.window = window;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }


    public void setContentMsg(CharSequence contentMsg) {
        this.contentMsg = contentMsg;
        if (tvContent != null) {
            tvContent.setText(contentMsg);
        }
    }

    public void setConfirmButton(CharSequence txt) {
        setConfirmButton(txt, 0);
    }

    public void setConfirmButton(CharSequence txt, int color) {
        this.confirmTxt = txt;
        this.color_confirmTxt = color;
        if (btnConfirm != null) {
            btnConfirm.setText(confirmTxt);
        }

    }

    public void setCancelButton(CharSequence txt) {
        setCancelButton(txt, 0);
    }

    public void setCancelButton(CharSequence txt, int color) {
        this.cancelTxt = txt;
        this.color_cancelTxt = color;
        if (btnCancel != null) {
            btnCancel.setText(cancelTxt);
        }

    }

    public View.OnClickListener getConfirmBtnListener() {
        return confirmBtnListener;
    }

    public void setConfirmBtnListener(View.OnClickListener confirmBtnListener) {
        this.confirmBtnListener = confirmBtnListener;
    }

    public View.OnClickListener getCancelBtnListener() {
        return cancelBtnListener;
    }

    public void setCancelBtnListener(View.OnClickListener cancelBtnListener) {
        this.cancelBtnListener = cancelBtnListener;
    }

    public void setCustomView(View customView) {
        if (customView != null) {
            this.customView = customView;
            layoutId = 0;
        }
    }

    public void setEditInput(boolean isInput) {
        this.isInput = isInput;
    }

    public long getToastDuration() {
        return toastDuration;
    }

    public void setToastDuration(long toastDuration) {
        this.toastDuration = toastDuration;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCancelable() {
        return cancelable && type != MyDialog.TYPE_TOAST;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public View getCustomView() {
        return customView;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public EditText getInputView() {
        return editText;
    }

    public void setLayoutId(int layoutId) {
        if (layoutId != 0) {
            customView = null;
            this.layoutId = layoutId;
        }
    }

    public void setupView() {
        setupTheme();
        installContentView();
        initView();
    }

    private void setupTheme() {
        window.getAttributes().windowAnimations = -1;
        toastLayoutId = resolveAttribute(R.attr.toastLayout);
        dialogLayoutId = resolveAttribute(R.attr.dialogLayout);
        enterAnimResId = resolveAttribute(R.attr.mwindowEnterTransition);
        exitAnimResId = resolveAttribute(R.attr.mwindowExitTransition);
    }

    private int resolveAttribute(int attrId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrId, typedValue, true);
        return typedValue.resourceId;
    }

    private void installContentView() {
        parentView = generateContentLayout();
        window.setContentView(parentView);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        window.setLayout(point.x, point.y);
        contentView = window.findViewById(R.id.id_mydialog_content);
        if (contentView == null) {
            ViewGroup viewGroup = (ViewGroup) parentView;
            if (viewGroup.getChildCount() > 0) {
                contentView = viewGroup.getChildAt(0);
            }
        }
    }

    private void initView() {
        tvTitle = (TextView) window.findViewById(R.id.id_mydialog_title);
        tvContent = (TextView) window.findViewById(R.id.id_mydialog_msg);
        btnConfirm = (TextView) window.findViewById(R.id.id_mydialog_confirm);
        btnDivider = window.findViewById(R.id.id_mydialog_btn_divider);
        btnCancel = (TextView) window.findViewById(R.id.id_mydialog_cancel);
        editText = (EditText) window.findViewById(R.id.id_mydialog_input);
        bottomLayout = window.findViewById(R.id.id_mydialog_confirm_layout);
        if (tvTitle != null) {
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setText(title);
            }
        }
        if (tvContent != null) {
            if (TextUtils.isEmpty(contentMsg)) {
                tvContent.setVisibility(View.GONE);
            } else {
                tvContent.setText(contentMsg);
            }
        }
        if (isInput) {
            editText.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.GONE);
        }

        if (btnConfirm != null && btnDivider != null) {
            if (TextUtils.isEmpty(confirmTxt)) {
                btnConfirm.setVisibility(View.GONE);
                btnDivider.setVisibility(View.GONE);
            } else {
                btnConfirm.setText(confirmTxt);
                if (color_confirmTxt != 0) {
                    btnConfirm.setTextColor(color_confirmTxt);
                }
            }
            if (TextUtils.isEmpty(cancelTxt)) {
                btnCancel.setVisibility(View.GONE);
                btnDivider.setVisibility(View.GONE);
            } else {
                btnCancel.setText(cancelTxt);
                if (color_cancelTxt != 0) {
                    btnCancel.setTextColor(color_cancelTxt);
                }
            }
            if (TextUtils.isEmpty(confirmTxt) && TextUtils.isEmpty(cancelTxt)) {
                bottomLayout.setVisibility(View.GONE);
            }
        }
        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (confirmBtnListener != null) {
                        confirmBtnListener.onClick(v);
                    }
                    dismiss();
                }
            });
        }
        if (btnCancel != null) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelBtnListener != null) {
                        cancelBtnListener.onClick(v);
                    }
                    dismiss();
                }
            });
        }
    }


    private View generateContentLayout() {
        if (customView != null) {
            return customView;
        }

        int contentLayoutId = 0;
        if (layoutId != 0) {
            contentLayoutId = layoutId;
        }
        if (type == MyDialog.TYPE_TOAST) {
            contentLayoutId = toastLayoutId;
        } else {
            contentLayoutId = dialogLayoutId;
        }
        return LayoutInflater.from(context).inflate(contentLayoutId, null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isCancelable() && isOutOfBounds(context, event)) {
            dismiss();
            return true;
        }
        return false;
    }


    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        Rect rect = new Rect();
        contentView.getHitRect(rect);
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        return (x < rect.left - slop) || (y < rect.top - slop)
                || (x > (rect.right + slop))
                || (y > (rect.bottom + slop));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        isDismiss = false;
        if (enterAnimResId != 0) {
            Animation animation = AnimationUtils.loadAnimation(context, enterAnimResId);
            contentView.startAnimation(animation);
        }
        backgroundAlphaAnim(0.3f, 0.5f);
        if (onShowListener != null) {
            onShowListener.onShow(dialog);
        }
        if (type == MyDialog.TYPE_TOAST) {
            contentView.postDelayed(toastDismissAction, toastDuration);
        }
    }

    public void dismiss() {
        if (isDismiss) {
            return;
        }
        Log.d("mydlg", "MydialogController dismiss");
        isDismiss = true;
        if (exitAnimResId != 0) {
            backgroundAlphaAnim(0.4f, 0.2f);
            Animation animation = AnimationUtils.loadAnimation(context, exitAnimResId);
            contentView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    contentView.post(dismissAction);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            backgroundAlphaAnim(0.4f, 0.2f);
            contentView.postDelayed(dismissAction, 200);
        }
    }

    private final Runnable dismissAction = new Runnable() {
        @Override
        public void run() {
            Log.d("mydlg", "dismissAction");
            dialogInterface.dismissMyDialog();
        }
    };
    private final Runnable toastDismissAction = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    private void backgroundAlphaAnim(float from, float to) {
        final View decorView = window.getDecorView();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ColorDrawable colorDrawable = new ColorDrawable();
                colorDrawable.setColor(Color.BLACK);
                colorDrawable.setAlpha((int) (alpha * 255));
                decorView.setBackground(colorDrawable);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    public static class Params {

        public Context context;
        public long toastDuration;
        public int type;
        public View customView;
        public int layoutId;
        public CharSequence title;
        public CharSequence contentMsg;
        public CharSequence confirmTxt;
        public CharSequence cancelTxt;

        public View.OnClickListener confirmBtnListener;
        public View.OnClickListener cancelBtnListener;
        public boolean cancelable = true;
        public boolean edit = false;

        public int color_confirmTxt;
        public int color_cancelTxt;

        public Params(Context context) {
            this.context = context;
        }

        public void apply(MyDialogController dialog) {
            if (type != 0) {
                dialog.setType(type);
            }

            if (customView != null) {
                dialog.setCustomView(customView);
            } else if (layoutId != 0) {
                dialog.setLayoutId(layoutId);
            }

            if (!TextUtils.isEmpty(title)) {
                dialog.setTitle(title);
            }

            if (!TextUtils.isEmpty(contentMsg)) {
                dialog.setContentMsg(contentMsg);
            }

            if (!TextUtils.isEmpty(confirmTxt)) {
                if (color_confirmTxt != 0) {
                    dialog.setConfirmButton(confirmTxt, color_confirmTxt);
                } else {
                    dialog.setConfirmButton(confirmTxt);
                }
                dialog.setConfirmBtnListener(confirmBtnListener);
            }

            if (!TextUtils.isEmpty(cancelTxt)) {
                if (color_cancelTxt != 0) {
                    dialog.setCancelButton(cancelTxt, color_cancelTxt);
                } else {
                    dialog.setCancelButton(cancelTxt);
                }
                dialog.setCancelBtnListener(cancelBtnListener);
            }

            if (toastDuration > 0) {
                dialog.setToastDuration(toastDuration);
            }
            dialog.setCancelable(cancelable);
            dialog.setEditInput(edit);
        }
    }
}
