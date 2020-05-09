package com.qtimes.views.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * Created by liujun on 2016/9/15.
 * 实现任意view填充TextView
 */
public class ViewSpan extends DynamicDrawableSpan {

    private View view;
    private int width;
    private int height;

    /**
     * @param view
     * @param width
     * @param height
     */
    public ViewSpan(View view, int width, int height) {
        this.view = view;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = ((bottom - top) - drawable.getBounds().height()) / 2 + top; //垂直居中绘制
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public Drawable getDrawable() {
        //计算view尺寸，并获取view的位图
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //获取view的位图需要计算尺寸和布局，此处模拟view正常的绘制流程
        view.measure(getViewMeasureSpec(width), getViewMeasureSpec(height));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap drawingCache = view.getDrawingCache();
        Drawable drawable = new BitmapDrawable(view.getContext().getResources(), drawingCache);
        drawable.setBounds(0, 0, width, height);
        return drawable;
    }

    private int getViewMeasureSpec(int size) {
        return size > 0 ? MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY) : MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
    }
}
