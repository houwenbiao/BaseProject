package com.qtimes.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;

/**
 * Created by plu on 2016/6/23.
 */
public class TitleInputView extends FrameLayout implements View.OnClickListener {

    private View mParentView;
    private AppCompatEditText mInputCet;
    private AppCompatCheckedTextView mLeftCtv;
    private AppCompatCheckedTextView mRightCtv;
    private TitleInputListener titleBarListener;

    //默认字体大小
    private static final int Titlebar_titlebar_leftAndRightTextSize = 12;
    private static final int Titlebar_titlebar_titleTextSize = 16;

    //默认间距
    private static final int Titlebar_titlebar_titleDrawablePadding = 3;
    private static final int Titlebar_titlebar_leftDrawablePadding = 3;
    private static final int Titlebar_titlebar_rightDrawablePadding = 3;
    private static final int Titlebar_titlebar_leftAndRightPadding = 10;

    //默认长度
    private static final int Titlebar_titlebar_leftMaxWidth = 85;
    private static final int Titlebar_titlebar_rightMaxWidth = 85;
    private static final int Titlebar_titlebar_titleMaxWidth = 144;

    //默认是否粗体
    private static final boolean Titlebar_titlebar_isTitleTextBold = true;
    private static final boolean Titlebar_titlebar_isLeftTextBold = false;
    private static final boolean Titlebar_titlebar_isRightTextBold = false;

    //判断双击事件
    private static final int DoubleClickTime = 500;
    long[] mClicks = new long[2];
    boolean canDoubleClick = true;//单击事件与双击事件冲突,只启用一个


    public TitleInputView(Context context) {
        this(context, null);
    }

    public TitleInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_title_input, this);
        initView();
        setListener();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Titlebar);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    protected void initView() {
        mParentView = getViewById(R.id.parent);
        mLeftCtv = getViewById(R.id.ctv_titlebar_left);
        mRightCtv = getViewById(R.id.ctv_titlebar_right);
        mInputCet = getViewById(R.id.ctv_titlebar_input);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    protected void setListener() {
        mLeftCtv.setOnClickListener(this);
        mRightCtv.setOnClickListener(this);
    }

    protected void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.Titlebar_titlebar_leftText) {
            setLeftText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleText) {
            setTitleText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_hintText) {
            setHintText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_rightText) {
            setRightText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_leftDrawable) {
            setLeftDrawable(typedArray.getDrawable(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleDrawable) {
            setTitleDrawable(typedArray.getDrawable(attr), null);
        } else if (attr == R.styleable.Titlebar_titlebar_titleDrawable_right) {
            setTitleDrawable(null, typedArray.getDrawable(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_rightDrawable) {
            setRightDrawable(typedArray.getDrawable(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_leftAndRightTextSize) {
            int textSize = typedArray.getDimensionPixelSize(attr, ViewUtils.sp2px(getContext(), Titlebar_titlebar_leftAndRightTextSize));
            mLeftCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mRightCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } else if (attr == R.styleable.Titlebar_titlebar_titleTextSize) {
            mInputCet.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(attr, ViewUtils.sp2px(getContext(), Titlebar_titlebar_titleTextSize)));
        } else if (attr == R.styleable.Titlebar_titlebar_leftAndRightTextColor) {
            mLeftCtv.setTextColor(typedArray.getColorStateList(attr));
            mRightCtv.setTextColor(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleTextColor) {
            mInputCet.setTextColor(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleDrawablePadding) {
            mInputCet.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_titleDrawablePadding)));
        } else if (attr == R.styleable.Titlebar_titlebar_leftDrawablePadding) {
            mLeftCtv.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_leftDrawablePadding)));
        } else if (attr == R.styleable.Titlebar_titlebar_rightDrawablePadding) {
            mRightCtv.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_rightDrawablePadding)));
        } else if (attr == R.styleable.Titlebar_titlebar_leftAndRightPadding) {
            int leftAndRightPadding = typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_leftAndRightPadding));
            mLeftCtv.setPadding(leftAndRightPadding, 0, leftAndRightPadding, 0);
            mRightCtv.setPadding(leftAndRightPadding, 0, leftAndRightPadding, 0);
        } else if (attr == R.styleable.Titlebar_titlebar_leftMaxWidth) {
            setLeftCtvMaxWidth(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_leftMaxWidth)));
        } else if (attr == R.styleable.Titlebar_titlebar_rightMaxWidth) {
            setRightCtvMaxWidth(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_rightMaxWidth)));
        } else if (attr == R.styleable.Titlebar_titlebar_titleMaxWidth) {
            setTitleCtvMaxWidth(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_titleMaxWidth)));
        } else if (attr == R.styleable.Titlebar_titlebar_isTitleTextBold) {
            mInputCet.getPaint().setFakeBoldText(typedArray.getBoolean(attr, Titlebar_titlebar_isTitleTextBold));
        } else if (attr == R.styleable.Titlebar_titlebar_isLeftTextBold) {
            mLeftCtv.getPaint().setFakeBoldText(typedArray.getBoolean(attr, Titlebar_titlebar_isLeftTextBold));
        } else if (attr == R.styleable.Titlebar_titlebar_isRightTextBold) {
            mRightCtv.getPaint().setFakeBoldText(typedArray.getBoolean(attr, Titlebar_titlebar_isRightTextBold));
        }
    }

    public void setParentBackground(int color) {
        mParentView.setBackgroundColor(color);
    }

    public void setImeOptions(int ime) {
        mInputCet.setImeOptions(ime);
    }

    public void setLeftCtvMaxWidth(int maxWidth) {
        mLeftCtv.setMaxWidth(maxWidth);
    }

    public void setRightCtvMaxWidth(int maxWidth) {
        mRightCtv.setMaxWidth(maxWidth);
    }

    public void setTitleCtvMaxWidth(int maxWidth) {
        mInputCet.setMaxWidth(maxWidth);
    }

    public void hiddenLeftCtv() {
        mLeftCtv.setVisibility(GONE);
    }

    public void showLeftCtv() {
        mLeftCtv.setVisibility(VISIBLE);
    }

    public void setLeftText(@StringRes int resid) {
        setLeftText(getResources().getString(resid));
    }

    public void setLeftText(CharSequence text) {
        mLeftCtv.setText(text);
        showLeftCtv();
    }

    public void setLeftDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mLeftCtv.setCompoundDrawables(drawable, null, null, null);
        showLeftCtv();
    }

    public void hiddenTitleCtv() {
        mInputCet.setVisibility(GONE);
    }

    public void showTitleCtv() {
        mInputCet.setVisibility(VISIBLE);
    }

    public void setTitleText(CharSequence text) {
        mInputCet.setText(text);
        showTitleCtv();
    }

    public void setTitleText(@StringRes int resid) {
        setTitleText(getResources().getString(resid));
    }

    public void setHintText(CharSequence text) {
        mInputCet.setHint(text);
        showTitleCtv();
    }

    public void setHintText(@StringRes int resid) {
        setHintText(getResources().getString(resid));
    }

    public void setTitleDrawable(Drawable drawableL, Drawable drawableR) {
        if (drawableL != null) {
            drawableL.setBounds(0, 0, drawableL.getIntrinsicWidth(), drawableL.getIntrinsicHeight());
        } else {
            drawableL = mInputCet.getCompoundDrawables()[0];
        }
        if (drawableR != null) {
            drawableR.setBounds(0, 0, drawableR.getIntrinsicWidth(), drawableR.getIntrinsicHeight());
        } else {
            drawableR = mInputCet.getCompoundDrawables()[2];
        }
        mInputCet.setCompoundDrawables(drawableL, null, drawableR, null);
        showTitleCtv();
    }

    public void hiddenRightCtv() {
        mRightCtv.setVisibility(GONE);
    }

    public void showRightCtv() {
        mRightCtv.setVisibility(VISIBLE);
    }

    public void setRightText(CharSequence text) {
        mRightCtv.setText(text);
        showRightCtv();
    }

    public void setRightText(@StringRes int resid) {
        setRightText(getResources().getString(resid));
    }

    public void setRightDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mRightCtv.setCompoundDrawables(null, null, drawable, null);
        showRightCtv();
    }

    public void setLeftCtvChecked(boolean checked) {
        mLeftCtv.setChecked(checked);
    }

    public void setRightCtvChecked(boolean checked) {
        mRightCtv.setChecked(checked);
    }

    public AppCompatCheckedTextView getLeftCtv() {
        return mLeftCtv;
    }

    public AppCompatCheckedTextView getRightCtv() {
        return mRightCtv;
    }

    public AppCompatEditText getTitleCet() {
        return mInputCet;
    }

    public void setTitleBarListener(TitleInputListener titleBarListener) {
        this.titleBarListener = titleBarListener;
    }

    public void setCanDoubleClick(boolean canDoubleClick) {
        this.canDoubleClick = canDoubleClick;
    }

    @Override
    public void onClick(View v) {
        if (titleBarListener != null) {
            int id = v.getId();
            if (id == R.id.ctv_titlebar_left) {
                titleBarListener.onClickLeft();
            } else if (id == R.id.ctv_titlebar_right) {
                titleBarListener.onClickRight();
            }
        }
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 根据实际业务重写相应地方法
     */
    public interface TitleInputListener {
        void onClickLeft();

        void onClickRight();
    }
}