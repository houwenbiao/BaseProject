package com.qtimes.views.clipimglayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.qtimes.views.R;


/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 *
 * @author zhy
 */
public class ClipImageLayout extends RelativeLayout {

    private static final int DEF_DP_H_PADDING = 20; //默认水平内边距
    private static final float DEF_STANDARD_W = 320;
    private static final float DEF_STANDARD_H = 320;

    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    private Bitmap bitmap;
    private int mHorizontalPadding = 20;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        if (bitmap != null) {
            mZoomImageView.setImageBitmap(bitmap);
        }
        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClipImageLayout);
        mHorizontalPadding = typedArray.getDimensionPixelSize(R.styleable.ClipImageLayout_horiontalPadding, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, DEF_DP_H_PADDING, getResources()
                        .getDisplayMetrics()));
        mZoomImageView.setStandardSize(DEF_STANDARD_H, DEF_STANDARD_W);
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setStandardRatio(DEF_STANDARD_W/DEF_STANDARD_H, mHorizontalPadding);
    }


    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            if (mZoomImageView != null) {
                mZoomImageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 设置裁剪高宽比
     *
     * @param ratio
     */
    public void setCropRatio(float ratio) {
        mZoomImageView.setStandardRatio(ratio, mHorizontalPadding);
        mClipImageView.setStandardRatio(ratio, mHorizontalPadding);
    }

    public void setCropSize(float width, float height) {
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setStandardRatio(width/height, mHorizontalPadding);
        mZoomImageView.setStandardSize(width, height);
        mClipImageView.setStandardRatio(width/height, mHorizontalPadding);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }


    public void rotate() {
        mZoomImageView.rotate();
    }

}
