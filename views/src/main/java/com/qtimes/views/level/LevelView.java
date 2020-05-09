package com.qtimes.views.level;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qtimes.views.R;
import com.qtimes.views.ViewUtils;

/**
 * Created by gufei on 2016/8/23 0023.
 */

public class LevelView extends RelativeLayout {
    private String TAG = "LevelView";
    NumView numView;

    public LevelView(Context context) {
        super(context);
        init();
    }

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources resources = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_user_lv_bg_1);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        setLayoutParams(params);


        numView = new NumView(getContext());
        LayoutParams layoutParams = new LayoutParams(width / 2, height);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.rightMargin = ViewUtils.dp2px(getContext(), 1);
        numView.setLayoutParams(layoutParams);
        addView(numView);
    }


    public void setLevel(int level) {
        setLevel(LevelType.ANCHOR, level);
    }

    /**
     * 等级类型
     *
     * @param type
     * @param level
     */
    public void setLevel(String type, int level) {
        setLevelBackground(type, level);
    }

    public int formatLevel(String type, int level) {
        if (level <= 0) level = 1;
        switch (type) {
            case LevelType.USER:
                if (level > 150) level = 150;
                break;
            case LevelType.ANCHOR:
                if (level > 50) level = 50;
                break;
        }
        return level;
    }

    private void setLevelBackground(String type, int level) {
        int resBack = 0;
        level = formatLevel(type, level);
        switch (type) {
            case LevelType.USER:
                if (level <= 30) {
                    resBack = R.drawable.ic_user_lv_bg_1;
                } else if (level > 30 && level <= 60) {
                    resBack = R.drawable.ic_user_lv_bg_2;
                } else if (level > 60 && level <= 90) {
                    resBack = R.drawable.ic_user_lv_bg_3;
                } else if (level > 90 && level <= 120) {
                    resBack = R.drawable.ic_user_lv_bg_4;
                } else if (level > 120 & level <= 150) {
                    resBack = R.drawable.ic_user_lv_bg_5;
                }
                break;
            case LevelType.ANCHOR:
                if (level <= 10) {
                    resBack = R.drawable.ic_zhubo_lv_bg_1;
                } else if (level > 10 && level <= 20) {
                    resBack = R.drawable.ic_zhubo_lv_bg_2;
                } else if (level > 20 && level <= 30) {
                    resBack = R.drawable.ic_zhubo_lv_bg_3;
                } else if (level > 30 && level <= 40) {
                    resBack = R.drawable.ic_zhubo_lv_bg_4;
                } else if (level > 40 && level <= 50) {
                    resBack = R.drawable.ic_zhubo_lv_bg_5;
                }
                break;
        }
        if (resBack != 0) {
            setBackgroundResource(resBack);
            numView.setLevel(level);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public interface LevelType {
        String USER = "user";//用户等级
        String ANCHOR = "anchor";//主播等级
    }
}
