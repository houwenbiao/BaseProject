package com.qtimes.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by liutao on 16-10-24.
 */

public class LongzhuScrollview extends ScrollView {
    public LongzhuScrollview(Context context) {
        super(context);
    }

    public LongzhuScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LongzhuScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(null!=scrollChangedListener){
            scrollChangedListener.onScrollChanged(l,t,oldl,oldt);
        }
    }

    public void setScrollChangedListener(onScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    onScrollChangedListener scrollChangedListener;
    public  interface  onScrollChangedListener{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

}
