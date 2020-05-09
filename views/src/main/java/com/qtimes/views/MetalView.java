package com.qtimes.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by gufei on 2016/10/24 0024.
 */

public class MetalView extends View {
    private SparseArray<Drawable> mHonorSparseArray;
    private SparseArray<Integer> mHonorColorArray;
    private Resources mResources;
    private Drawable mDrawable;
    private String name;

    private Paint mPaint;
    int textSize;
    public static final int TEXT_SIZE = 6;//sp
    public static final int PADING_LEFT = 25;//dp

    public MetalView(Context context) {
        this(context, null, 0);
    }

    public MetalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MetalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Context mContext = getContext();
        mResources = mContext.getResources();
        mHonorSparseArray = new SparseArray();
        mHonorSparseArray.put(1, mResources.getDrawable(R.drawable.icon_honor_1));
        mHonorSparseArray.put(2, mResources.getDrawable(R.drawable.icon_honor_2));
        mHonorSparseArray.put(3, mResources.getDrawable(R.drawable.icon_honor_3));
        mHonorSparseArray.put(4, mResources.getDrawable(R.drawable.icon_honor_4));
        mHonorSparseArray.put(5, mResources.getDrawable(R.drawable.icon_honor_5));
        mHonorSparseArray.put(6, mResources.getDrawable(R.drawable.icon_honor_6));
        mHonorSparseArray.put(7, mResources.getDrawable(R.drawable.icon_honor_7));

        mHonorSparseArray.put(8, (ContextCompat.getDrawable(mContext, R.drawable.icon_live_fg)));//房管
        mHonorSparseArray.put(9, (ContextCompat.getDrawable(mContext, R.drawable.icon_live_cg)));//超管

        mHonorColorArray = new SparseArray();
        mHonorColorArray.put(0, mResources.getColor(R.color.blue_metal));
        mHonorColorArray.put(1, mResources.getColor(R.color.pink_metal));
        mHonorColorArray.put(2, mResources.getColor(R.color.yellow_metal));
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 防止边缘的锯齿
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG); // 消除锯齿

        textSize = ViewUtils.sp2px(mContext, TEXT_SIZE);
        mPaint.setTextSize(textSize);
    }


    public void setMetalStyle(int level, String name) {
        if (level <= 0) level = 1;
        else if (level > 7) level = 7;
        int color = 0;//设置字体颜色
        switch (level) {  //根据勋章用户等级，设置勋章样式
            case 1:
            case 2:
            case 3:
                color = mHonorColorArray.get(0);
                break;
            case 4:
            case 5:
                color = mHonorColorArray.get(1);
                break;
            case 6:
            case 7:
                color = mHonorColorArray.get(2);
                break;
        }

        mDrawable = mHonorSparseArray.get(level);
        this.name = name;
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Drawable drawable = mDrawable;
        int hheight = drawable.getIntrinsicHeight();

        drawable.setBounds(0, 0, width, hheight);


        drawable.draw(canvas);

        if (!TextUtils.isEmpty(name)) {
            canvas.drawText(name, ViewUtils.dp2px(getContext(), PADING_LEFT), (height + textSize) / 2, mPaint);
        }
        canvas.restore();
    }
}
