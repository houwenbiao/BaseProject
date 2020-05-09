package com.qtimes.wonly.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtimes.wonly.R;


/**
 * 小趣dialog
 */
public class QuDialog extends Dialog {

    public QuDialog(Context context) {
        super(context);
    }

    public QuDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private String message;
        private String confirmButtonText;
        private String cancelButtonText;
        private DialogType dialogType;
        private boolean cancelable;
        private int layoutId;
        private DialogInterface.OnClickListener confirmButtonClickListener;
        private DialogInterface.OnClickListener cancelButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public void setDialogType(DialogType dialogType) {
            this.dialogType = dialogType;
        }

        public void setCancelable(boolean flag) {
            this.cancelable = flag;
        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param confirmButtonText
         * @return
         */
        public Builder setConfirmButton(int confirmButtonText,
                                        DialogInterface.OnClickListener listener) {
            this.confirmButtonText = (String) context
                    .getText(confirmButtonText);
            this.confirmButtonClickListener = listener;
            return this;
        }

        public Builder setConfirmButton(String confirmButtonText,
                                        DialogInterface.OnClickListener listener) {
            this.confirmButtonText = confirmButtonText;
            this.confirmButtonClickListener = listener;
            return this;
        }

        public Builder setCancelButton(int cancelButtonText,
                                       DialogInterface.OnClickListener listener) {
            this.cancelButtonText = (String) context
                    .getText(cancelButtonText);
            this.cancelButtonClickListener = listener;
            return this;
        }

        public Builder setCancelButton(String cancelButtonText,
                                       DialogInterface.OnClickListener listener) {
            this.cancelButtonText = cancelButtonText;
            this.cancelButtonClickListener = listener;
            return this;
        }


        private View createLayout() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int contentLayout = layoutId == 0 ? R.layout.dialog_common : layoutId;
            View layout = inflater.inflate(contentLayout, null);
            return layout;
        }

        public QuDialog create() {
            // instantiate the dialog with the custom Theme
            final QuDialog dialog = new QuDialog(context, R.style.QuDialog);
            dialog.setCancelable(cancelable);
            View layout = createLayout();
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView tvTitle = ((TextView) layout.findViewById(R.id.dialog_title));
            if (tvTitle != null) {
                if (title != null) {
                    tvTitle.setText(title);
                } else {
                    tvTitle.setVisibility(View.GONE);
                }
            }
            // set the cancel button
            Button dialogCancel= ((Button) layout.findViewById(R.id.dialog_cancel));
            if (cancelButtonText != null) {
                dialogCancel.setText(cancelButtonText);
                if (cancelButtonClickListener != null) {
                    dialogCancel .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }else{
                dialogCancel.setVisibility(View.GONE);
            }
            // set the confirm button
           Button dialogConfirm= ((Button) layout.findViewById(R.id.dialog_confirm));

            if (confirmButtonText != null) {
                dialogConfirm.setText(confirmButtonText);
                if (confirmButtonClickListener != null) {
                    dialogConfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    confirmButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            }else{
                dialogConfirm.setVisibility(View.GONE);
                //如果没有确定文本，那么取消更换背景
                dialogCancel.setBackgroundResource(R.mipmap.alert_btn_bg);
            }


            //如果确定和取消按钮都没有，那么隐藏这一行
            View llContent = layout.findViewById(R.id.dialog_confirm_layout);
            if (cancelButtonText == null && confirmButtonText == null&&llContent!=null) {
                if (llContent != null) {
                    llContent.setVisibility(View.GONE);
                }

            }
            // set the content message
           TextView dialogMsg= ((TextView) layout.findViewById(R.id.dialog_msg));
            if (message != null&&dialogMsg!=null) {
                dialogMsg.setText(message);
            }
            TextView dialogContent= ((TextView) layout.findViewById(R.id.dialog_content));
            if (content != null&&dialogContent!=null) {
                dialogContent.setText(content);
            }

            //set dialogtype
            ImageView head = ((ImageView) layout.findViewById(R.id.dialog_head));
            if (head != null&&dialogType!=null) {
                switch (dialogType) {
                    case DIALOG_BLUE://蓝色头像
                        head.setImageResource(R.mipmap.alert_q_3);
                        break;
                    case DIALOG_CREEN:
                        head.setImageResource(R.mipmap.alert_q_2);
                        break;
                    case DIALOG_PURPEL:
                        head.setImageResource(R.mipmap.alert_q_1);
                        break;
                    case DIALOG_RED:
                        head.setImageResource(R.mipmap.alert_q_4);
                        break;

                }
            }

            dialog.setContentView(layout);
            return dialog;
        }

        public QuDialog create(View view) {
            final QuDialog dialog = new QuDialog(context, R.style.QuDialog);
            dialog.setCancelable(cancelable);
            if (view != null) {
                dialog.addContentView(view, new LayoutParams(
                        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                dialog.setContentView(view);

            }
            return dialog;


        }

    }
}
