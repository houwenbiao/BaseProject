package com.qtimes.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * 粉丝勋章
 * Created by plu on 2016/10/27.
 */

public class MetalUtils {

    private static MetalUtils metalUtils;
    private Context context;

    int backRes = 0;
    int textColorRes = 0;
    int backColor = 0;

    Paint mTextPaint;
    Paint mHonorPaint;
    Paint mBgPaint;
    private boolean lowDis = false;

    public static MetalUtils getInstance() {
        return metalUtils;
    }

    public static void init(Context context) {
        metalUtils = new MetalUtils(context.getApplicationContext());

    }

    public MetalUtils(Context context) {
        this.context = context;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mHonorPaint = new Paint();
        mHonorPaint.setAntiAlias(true);
        mHonorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 创建勋章图标
     *
     * @param level
     * @param name
     * @return
     */
    public Bitmap createHonorUserBitmap(int level, String name) {
        Bitmap honorBitmap = setHonor(level);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStrokeWidth(3);
        if (lowDis) {
            mTextPaint.setTextSize(ViewUtils.sp2px(context, 23));
        } else {
            mTextPaint.setTextSize(ViewUtils.sp2px(context, 35));
        }

        mTextPaint.setColor(context.getResources().getColor(textColorRes));
        float textSize = mTextPaint.measureText(name, 0, name.length());

        mBgPaint.setColor(context.getResources().getColor(backColor));

        int width = (int) (textSize + honorBitmap.getWidth() + ViewUtils.dp2px(context, 30));

        Bitmap bitmap = Bitmap.createBitmap(width, honorBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        RectF rect = new RectF((float) honorBitmap.getWidth() / 2, ViewUtils.dp2px(context, 4), (float) (honorBitmap.getWidth() + textSize + ViewUtils.dp2px(context, 25)), honorBitmap.getHeight() - ViewUtils.dp2px(context, 2));

        Canvas canvas = new Canvas(bitmap);
        canvas.drawRoundRect(rect, ViewUtils.dp2px(context, 10), ViewUtils.dp2px(context, 10), mBgPaint);

        canvas.drawBitmap(honorBitmap, 0, 0, mHonorPaint);
        canvas.drawText(name, honorBitmap.getWidth() + ViewUtils.dp2px(context, 8), honorBitmap.getHeight() / 2 + ViewUtils.dp2px(context, 12), mTextPaint);
        return bitmap;
    }

    private Bitmap setHonor(int level) {
        switch (level) {
            case 1:
                backRes = R.drawable.icon_honor1;
                textColorRes = R.color.blue_metal;
                backColor = R.color.metal_bg_blue;
                break;
            case 2:
                backRes = R.drawable.icon_honor2;
                textColorRes = R.color.blue_metal;
                backColor = R.color.metal_bg_blue;
                break;
            case 3:
                backRes = R.drawable.icon_honor3;
                textColorRes = R.color.blue_metal;
                backColor = R.color.metal_bg_blue;
                break;
            case 4:
                backRes = R.drawable.icon_honor4;
                textColorRes = R.color.pink_metal;
                backColor = R.color.metal_bg_pink;
                break;
            case 5:
                backRes = R.drawable.icon_honor5;
                textColorRes = R.color.pink_metal;
                backColor = R.color.metal_bg_pink;
                break;
            case 6:
                backRes = R.drawable.icon_honor6;
                textColorRes = R.color.yellow_metal;
                backColor = R.color.metal_bg_yellow;
                break;
            case 7:
                backRes = R.drawable.icon_honor7;
                textColorRes = R.color.yellow_metal;
                backColor = R.color.metal_bg_yellow;
                break;
        }
        return big(BitmapFactory.decodeResource(context.getResources(), backRes));
    }


    private Bitmap big(Bitmap bitmap) {
        Log.e("mddemo", "bitmap size : " + bitmap.getHeight() + ", " + bitmap.getWidth());
        float scaleValue = 2.2f;
        lowDis = false;
        if (bitmap.getHeight() < 100) {
            lowDis = true;
            scaleValue = 1.4f;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleValue, scaleValue); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

}
