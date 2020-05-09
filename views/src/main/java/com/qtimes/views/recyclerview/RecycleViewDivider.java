package com.qtimes.views.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.qtimes.views.ViewUtils;

/**
 * RecycleView Divider setting
 * <p/>
 * Created by plu on 2016/6/23.
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = {android.R.attr.listDivider};
    private Drawable mDivider;
    private Paint mPaint;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mDividerHeight = 2;//分割线高度，默认为1px

    /**
     * 默认分隔线
     *
     * @param dividerHeight 分割线高度(dp)
     */
    public RecycleViewDivider(Context context, float dividerHeight) {
        mDividerHeight = ViewUtils.dp2px(context, dividerHeight);

        TypedArray array = context.obtainStyledAttributes(ATTRS);
        // 获取分隔条
        mDivider = array.getDrawable(0);
        array.recycle();
    }

    public RecycleViewDivider(Context context, float dividerHeight, int dividerColor) {
        this(context, 0, 0, dividerHeight, dividerColor);
    }

    /**
     * 自定义分割线
     *
     * @param paddingLeft   左间距(dp)
     * @param paddingRight  右间距(dp)
     * @param dividerHeight 分割线高度(dp)
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, int paddingLeft, int paddingRight, float dividerHeight, int dividerColor) {
        mPaddingLeft = ViewUtils.dp2px(context, paddingLeft);
        mPaddingRight = ViewUtils.dp2px(context, paddingRight);
        mDividerHeight = ViewUtils.dp2px(context, dividerHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDividerHeight);
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawHorizontal(c, parent);
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mPaint != null) {
                canvas.drawRect(left + mPaddingLeft, top, right - mPaddingRight, bottom, mPaint);
            } else if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
        }
    }
}