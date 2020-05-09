package com.qtimes.views;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liutao on 2016/12/17.
 */

public class ScrollViewPager extends ViewPager
{
    private boolean canScroll = false;

    public ScrollViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ScrollViewPager(Context context)
    {
        super(context);
    }

    public void setScroll(boolean canScroll)
    {
        this.canScroll = canScroll;
    }

    @Override
    public void scrollTo(int x, int y)
    {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0)
    {
        /* return false;//super.onTouchEvent(arg0); */
        if (!canScroll)
        {
            return false;
        } else
        {
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0)
    {
        if (!canScroll)
        {
            return false;
        } else
        {
            return super.onInterceptTouchEvent(arg0);
        }
    }


}
