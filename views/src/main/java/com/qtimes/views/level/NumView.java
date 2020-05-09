package com.qtimes.views.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.qtimes.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gufei on 2016/8/24 0024.
 */

public class NumView extends View {
    Paint mPaint;
    List<Bitmap> numsBitmap;
    int pading = 2;

    public NumView(Context context) {
        super(context);
        mPaint = new Paint();
        numsBitmap = new ArrayList<>();
    }

    public void setLevel(int level) {
        numsBitmap.clear();
        if (level < 10) {
            numsBitmap.add(getNum(level));
        } else if (level >= 10 && level < 100) {
            numsBitmap.add(getNum(level / 10));
            numsBitmap.add(getNum(level % 10));
        } else {
            numsBitmap.add(getNum(level / 100));
            numsBitmap.add(getNum(level / 10));
            numsBitmap.add(getNum(level % 10));
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = numsBitmap.size();
        if (size == 0) return;
        int width = getWidth();
        int height = getHeight();
        Bitmap tempBitmap = numsBitmap.get(0);
        int bwidth = tempBitmap.getWidth();
        int bheight = tempBitmap.getHeight();
        int dwidth = width - bwidth;
        int dheight = height - bheight;
        if (size == 1) {
            canvas.drawBitmap(tempBitmap, dwidth / 2 + pading, dheight / 2 + pading, mPaint);
        } else if (size == 2) {
            dwidth = width - 2 * bwidth;
            canvas.drawBitmap(tempBitmap, dwidth / 2 + pading, dheight / 2 + pading, mPaint);
            tempBitmap = numsBitmap.get(1);
            canvas.drawBitmap(tempBitmap, dwidth / 2 + bwidth + pading, dheight / 2 + pading, mPaint);
        } else if (size == 3) {
            dwidth = width - 3 * bwidth;
            canvas.drawBitmap(tempBitmap, dwidth / 2 + pading, dheight / 2 + pading, mPaint);
            tempBitmap = numsBitmap.get(1);
            canvas.drawBitmap(tempBitmap, dwidth / 2 + bwidth + pading, dheight / 2 + pading, mPaint);
            tempBitmap = numsBitmap.get(2);
            canvas.drawBitmap(tempBitmap, dwidth / 2 + 2 * bwidth + pading, dheight / 2 + pading, mPaint);
        }

    }

    public Bitmap getNum(int num) {
        int resId = R.drawable.ic_user_lv_shuzi_0;
        if (num > 10) {
            num = num % 10;
        }
        switch (num) {
            case 0:
                resId = R.drawable.ic_user_lv_shuzi_0;
                break;
            case 1:
                resId = R.drawable.ic_user_lv_shuzi_1;
                break;
            case 2:
                resId = R.drawable.ic_user_lv_shuzi_2;
                break;
            case 3:
                resId = R.drawable.ic_user_lv_shuzi_3;
                break;
            case 4:
                resId = R.drawable.ic_user_lv_shuzi_4;
                break;
            case 5:
                resId = R.drawable.ic_user_lv_shuzi_5;
                break;
            case 6:
                resId = R.drawable.ic_user_lv_shuzi_6;
                break;
            case 7:
                resId = R.drawable.ic_user_lv_shuzi_7;
                break;
            case 8:
                resId = R.drawable.ic_user_lv_shuzi_8;
                break;
            case 9:
                resId = R.drawable.ic_user_lv_shuzi_9;
                break;
        }
        return BitmapFactory.decodeResource(getResources(), resId);
    }
}
