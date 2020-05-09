package com.qtimes.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 一个自定义的titlebar
 * author: liutao
 * date: 2016/6/13.
 */
public class TitleBarView extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private AppCompatCheckedTextView mTitleCtv;
    private AppCompatCheckedTextView mLeftCtv;
    private AppCompatCheckedTextView mRightCtv;
    private AppCompatCheckedTextView mRight2Ctv;//// TODO: 2016/12/15  第四个icon,坑爹设计!
    private RelativeLayout rlView;
    private TitleBarListener titleBarListener;

    //默认字体大小
    private static final int Titlebar_titlebar_leftAndRightTextSize = 12;
    private static final int Titlebar_titlebar_titleTextSize = 18;

    //默认间距
    private static final int Titlebar_titlebar_titleDrawablePadding = 3;
    private static final int Titlebar_titlebar_leftDrawablePadding = 3;
    private static final int Titlebar_titlebar_rightDrawablePadding = 3;
    private static final int Titlebar_titlebar_leftAndRightPadding = 5;

    //默认长度
    private static final int Titlebar_titlebar_leftMaxWidth = 65;
    private static final int Titlebar_titlebar_rightMaxWidth = 85;
    private static final int Titlebar_titlebar_titleMaxWidth = 144;

    //默认是否粗体
    private static final boolean Titlebar_titlebar_isTitleTextBold = true;
    private static final boolean Titlebar_titlebar_isLeftTextBold = false;
    private static final boolean Titlebar_titlebar_isRightTextBold = false;
    // 默认有分割线
    private static final boolean Titlebar_titlebar_divider = true;


    //逻辑
    //判断双击事件
    private static final int DoubleClickTime = 500;
    long[] mClicks = new long[2];
    boolean canDoubleClick = true;//单击事件与双击事件冲突,只启用一个
    boolean isIgoneTitleWithDouble = false;//是否忽略title和双击事件的冲突
    private static int DOUBLECLICK_INTERVAL = 1000;//两次双击事件的间隔时间


    //
    private TitleBarDrawable leftDrawable, rightDrawable, titleDrawable,right2Drawable;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_titlebar, this);
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
//        rlView = getViewById(R.id.rlView);
        mLeftCtv = getViewById(R.id.ctv_titlebar_left);
        mRightCtv = getViewById(R.id.ctv_titlebar_right);
        mTitleCtv = getViewById(R.id.ctv_titlebar_title);
        mRight2Ctv = getViewById(R.id.ctv_titlebar_righ_2);
    }

    protected void setListener() {
        mLeftCtv.setOnClickListener(this);
        mRightCtv.setOnClickListener(this);
        mRight2Ctv.setOnClickListener(this);
        mTitleCtv.setClickable(false);
        setOnTouchListener(this);
    }


    protected void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.Titlebar_titlebar_leftText) {
            setLeftText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleText) {
            setTitleText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_rightText) {
            setRightText(typedArray.getText(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_leftDrawable) {
            setLeftDrawable(typedArray.getDrawable(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleDrawable) {
            setTitleDrawable(typedArray.getDrawable(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_rightDrawable) {
            setRightDrawable(typedArray.getDrawable(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_leftAndRightTextSize) {
            int textSize = typedArray.getDimensionPixelSize(attr, ViewUtils.sp2px(getContext(), Titlebar_titlebar_leftAndRightTextSize));
            mLeftCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mRightCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } else if (attr == R.styleable.Titlebar_titlebar_titleTextSize) {
            int textSize = typedArray.getDimensionPixelSize(R.styleable.Titlebar_titlebar_titleTextSize, ViewUtils.sp2px(getContext(), Titlebar_titlebar_titleTextSize));
            mTitleCtv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        } else if (attr == R.styleable.Titlebar_titlebar_leftAndRightTextColor) {
            mLeftCtv.setTextColor(typedArray.getColorStateList(attr));
            mRightCtv.setTextColor(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleTextColor) {
            mTitleCtv.setTextColor(typedArray.getColorStateList(attr));
        } else if (attr == R.styleable.Titlebar_titlebar_titleDrawablePadding) {
            mTitleCtv.setCompoundDrawablePadding(typedArray.getDimensionPixelSize(attr, ViewUtils.dp2px(getContext(), Titlebar_titlebar_titleDrawablePadding)));
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
            mTitleCtv.getPaint().setFakeBoldText(typedArray.getBoolean(attr, Titlebar_titlebar_isTitleTextBold));
        } else if (attr == R.styleable.Titlebar_titlebar_isLeftTextBold) {
            mLeftCtv.getPaint().setFakeBoldText(typedArray.getBoolean(attr, Titlebar_titlebar_isLeftTextBold));
        } else if (attr == R.styleable.Titlebar_titlebar_isRightTextBold) {
            mRightCtv.getPaint().setFakeBoldText(typedArray.getBoolean(attr, Titlebar_titlebar_isRightTextBold));
        } else if (attr == R.styleable.Titlebar_titlebar_divider) {//分割线

        }
    }

    public void setLeftCtvMaxWidth(int maxWidth) {
        mLeftCtv.setMaxWidth(maxWidth);
    }

    public void setRightCtvMaxWidth(int maxWidth) {
        mRightCtv.setMaxWidth(maxWidth);
    }

    public void setTitleCtvMaxWidth(int maxWidth) {
        mTitleCtv.setMaxWidth(maxWidth);
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
        if (leftDrawable == null) {
            leftDrawable = new TitleBarDrawable(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else {
            leftDrawable.setWidth(drawable.getIntrinsicWidth());
            leftDrawable.setHeight(drawable.getIntrinsicHeight());
        }
        showLeftCtv();
    }

    public void hiddenTitleCtv() {
        mTitleCtv.setVisibility(GONE);
    }

    public void showTitleCtv() {
        mTitleCtv.setVisibility(VISIBLE);
    }

    public void setTitleText(CharSequence text) {
        mTitleCtv.setText(text);
        showTitleCtv();
    }

    public void setTitleText(@StringRes int resid) {
        setTitleText(getResources().getString(resid));
    }

    public void setTitleDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mTitleCtv.setCompoundDrawables(null, null, drawable, null);
        if (titleDrawable == null) {
            titleDrawable = new TitleBarDrawable(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else {
            titleDrawable.setWidth(drawable.getIntrinsicWidth());
            titleDrawable.setHeight(drawable.getIntrinsicHeight());
        }
        showTitleCtv();
    }

    public void hiddenRightCtv() {
        mRightCtv.setVisibility(GONE);
    }
    public void showRight2Ctv() {
        mRight2Ctv.setVisibility(VISIBLE);
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
        if (rightDrawable == null) {
            rightDrawable = new TitleBarDrawable(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else {
            rightDrawable.setWidth(drawable.getIntrinsicWidth());
            rightDrawable.setHeight(drawable.getIntrinsicHeight());
        }
        showRightCtv();
    }
    public void setRight2Drawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        mRight2Ctv.setCompoundDrawables(null, null, drawable, null);
        if (right2Drawable == null) {
            right2Drawable = new TitleBarDrawable(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        } else {
            right2Drawable.setWidth(drawable.getIntrinsicWidth());
            right2Drawable.setHeight(drawable.getIntrinsicHeight());
        }
        showRight2Ctv();
    }
    public void setLeftCtvChecked(boolean checked) {
        mLeftCtv.setChecked(checked);
    }

    public void setTitleCtvChecked(boolean checked) {
        mTitleCtv.setChecked(checked);
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
    public AppCompatCheckedTextView getRight2Ctv() {
        return mRight2Ctv;
    }
    public AppCompatCheckedTextView getTitleCtv() {
        return mTitleCtv;
    }

    public void setTitleBarListener(TitleBarListener titleBarListener) {
        this.titleBarListener = titleBarListener;
    }

    public void setCanDoubleClick(boolean canDoubleClick) {
        this.canDoubleClick = canDoubleClick;
    }

    /**
     * 当标题点击事件和双击事件同时存在
     *
     * @param igoneTitleWithDouble 忽略双击事件 true 忽略
     */
    public void setIgoneTitleWithDouble(boolean igoneTitleWithDouble) {
        isIgoneTitleWithDouble = igoneTitleWithDouble;
        mTitleCtv.setOnClickListener(this);
        mTitleCtv.setOnTouchListener(null);
        setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (titleBarListener != null) {
            int id = v.getId();
            if (id == R.id.ctv_titlebar_left) {
                titleBarListener.onClickLeft();
            } else if (id == R.id.ctv_titlebar_title) {
                if (isIgoneTitleWithDouble || !canDoubleClick) {//忽略冲突或者双击事件时
                    titleBarListener.onClickTitle();
                }
            } else if (id == R.id.ctv_titlebar_right) {
                titleBarListener.onClickRight();
            }else if(id==R.id.ctv_titlebar_righ_2){
                titleBarListener.onMoreViewClick(v);
            }
        }
    }

    long lastDoubleClickTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!canDoubleClick) {
            return super.onTouchEvent(event);
        }
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            System.arraycopy(mClicks, 1, mClicks, 0, mClicks.length - 1);
            mClicks[mClicks.length - 1] = System.currentTimeMillis();
            if (mClicks[0] >= (System.currentTimeMillis() - DoubleClickTime) && mClicks[0] > lastDoubleClickTime + DOUBLECLICK_INTERVAL) {
                if (titleBarListener != null) {
                    //防止重复响应双击事件
                    lastDoubleClickTime = System.currentTimeMillis();
                    titleBarListener.onDoubleClickTitle();
                }
            }
        }
        return true;
    }

    public TitleBarDrawable getLeftDrawable() {
        return leftDrawable == null ? new TitleBarDrawable(0, 0) : leftDrawable;

    }

    public TitleBarDrawable getRightDrawable() {
        return rightDrawable == null ? new TitleBarDrawable(0, 0) : rightDrawable;

    }

    public TitleBarDrawable getTitleDrawable() {
        return titleDrawable == null ? new TitleBarDrawable(0, 0) : titleDrawable;
    }

    public static class TitleBarDrawable {
        int width;
        int height;

        public TitleBarDrawable(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
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
    public interface TitleBarListener {
        void onClickLeft();

        void onClickTitle();

        void onClickRight();

        void onDoubleClickTitle();

        //兼容方法，以后多余的icon掉这个
        void onMoreViewClick(View view);

    }

}
