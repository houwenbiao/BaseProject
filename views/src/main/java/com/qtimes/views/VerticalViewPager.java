package com.qtimes.views;

/**
 * Copyright (C) 2015 Kaelaela
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class VerticalViewPager extends ViewPager {
    private InnerPagerAdapter adapter;
    private String TAG = "VerticalViewPager";
    float preY = 0;
    private int scrollDuration = 150;//转场时间
    private long scrollTime;//上次滑动时间

    private OnPageFlipListener onPageFlipListener;
    private FragmentManager manager;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    private void log(String msg) {
//        Log.d(TAG, msg);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, null);
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();
        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;
        event.setLocation(swappedX, swappedY);
        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - scrollTime < 500) {
            scrollTime = System.currentTimeMillis();
            return false;
        }

        float width = getWidth();
        float height = getHeight();
        boolean isVrtical = width < height;
        if (isVrtical) {
            height = (float) (height * 0.75);
            width = (float) (width * 0.6);
        } else {
            height = (float) (height * 0.6);
            width = (float) (width * 0.5);
        }
        float y = event.getY();
        float x = event.getX();
        log("onInterceptTouchEvent height" + height + "|y" + y);
        if (y > height && x < width) {//不响应滑动
            return false;
        }
        boolean intercept = false;
        try {
            intercept = super.onInterceptTouchEvent(swapTouchEvent(event));
            //If not intercept, touch event should not be swapped.
            swapTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                preY = event.getY();
                log("onInterceptTouchEvent" + preY);
            } else {
                log("onInterceptTouchEvent last" + event.getY());
                if (Math.abs(event.getY() - preY) > 40) {
                    return true;
                } else {
                    preY = event.getY();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log("onTouchEvent");

        return super.onTouchEvent(swapTouchEvent(event));
    }


    public void setFragments(FragmentManager manager, List<Fragment> fragments) {
        if (adapter == null) {
            this.manager = manager;
            adapter = new InnerPagerAdapter(manager, fragments);
            setAdapter(adapter);
            addOnPageChangeListener(new InnerOnPageChangeListener());
            setCurrentItem(1);
            setOffscreenPageLimit(3);
            ViewPagerScroller scroller = new ViewPagerScroller(getContext());
            scroller.setScrollDuration(scrollDuration);
            scroller.initViewPagerScroll(this);  //这个是设置切换过渡时间为毫秒
        }
    }

    public class DefaultTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                alpha = position + 1;
            }
            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    private class InnerOnPageChangeListener implements OnPageChangeListener {
        private int position;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            log("onPageScrolled--" + position + "positionOffset:" + positionOffset + "positionOffsetPixels:" + positionOffsetPixels);
            if (onPageFlipListener != null) {
                onPageFlipListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            int scrollX;
            int childCount;
            int i;
            scrollX = getScrollX();
            childCount = getChildCount();

            for (i = 0; i < childCount; ++i) {
                View var15 = getChildAt(i);
                ViewPager.LayoutParams var16 = (ViewPager.LayoutParams) var15.getLayoutParams();
                if (!var16.isDecor) {
                    float var17 = (float) (var15.getLeft() - scrollX) / (float) getClientWidth();
                    float alpha = 0;
                    if (0 <= var17 && var17 <= 1) {
                        alpha = 1 - var17;
                    } else if (-1 < var17 && var17 < 0) {
                        alpha = var17 + 1;
                    }
//                    var15.setAlpha(alpha);

                    float xPosition = var15.getWidth() * -var17;
                    float yPosition = var17 * var15.getHeight();

                    var15.setTranslationX(xPosition);
                    var15.setTranslationY(yPosition);
//                    Log.v(TAG, "view :" + var15.getClass().getName() + "|x:" + xPosition + "y:" + yPosition);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            this.position = position;
            log("onPageSelected--" + position);
            if (onPageFlipListener != null && position == 1) {
                onPageFlipListener.onMainPageResume();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE && adapter != null) {
                log("onPageScrollStateChanged--" + position);
                if (position == adapter.getCount() - 1) {//滑动到最后页时切换到第一页
                    if (onPageFlipListener != null) {
                        onPageFlipListener.onPageScrollChanged(position);
                    }
                    setCurrentItem(1, false);
                } else if (position == 0) {
                    if (onPageFlipListener != null) {
                        onPageFlipListener.onPageScrollChanged(position);
                    }
                    setCurrentItem(1, false);
                }

                if (onPageFlipListener != null) {
                    onPageFlipListener.onPageScrollStateIdel();
                }
            }
        }
    }

    public void destroyFragment() {
        if (adapter != null) adapter.destroyAll();
    }

    private class InnerPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager fm;
        private List<Fragment> fragments = new ArrayList<>();

        public InnerPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return super.isViewFromObject(view, object);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            if (position == 0) {
//                position = getCount() - 1;
//            } else if (position == getCount() - 1) {
//                position = 0;
//            }
            return super.instantiateItem(container, position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        public void destroyAll() {
            int size = fragments.size();
            FragmentTransaction mCurTransaction = fm.beginTransaction();
            for (int i = 0; i < size; i++) {
                mCurTransaction.detach(fragments.get(i));
            }
        }
    }

    public void setOnPageFlipListener(OnPageFlipListener onPageFlipListener) {
        this.onPageFlipListener = onPageFlipListener;
    }

    public class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 2000;             // 滑动速度

        /**
         * 设置速度速度
         *
         * @param duration
         */
        public void setScrollDuration(int duration) {
            this.mScrollDuration = duration;
        }

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }


        public void initViewPagerScroll(ViewPager viewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnPageFlipListener {

        void onMainPageResume();

        void onPageScrollChanged(int position);

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageScrollStateIdel();
    }

}
