package com.qtimes.pavilion.views.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.qtimes.pavilion.R;


/**
 * RecyclerView的FooterView，简单的展示一个View
 */
public class CommonFooter extends RelativeLayout {

    public CommonFooter(Context context,int resId) {
        super(context);
        init(context, resId);
    }

    public CommonFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.layout_recyclerview_footer, this);
    }

    public void init(Context context,int resId) {

        inflate(context, resId, this);
    }
}