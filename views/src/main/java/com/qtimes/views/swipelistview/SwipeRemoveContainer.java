package com.qtimes.views.swipelistview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qtimes.views.R;


/**
 * Created by liuj on 2016/7/4.
 */
public class SwipeRemoveContainer extends RelativeLayout {

    private static final long ANIM_DURATION = 150;

    private Button btnRemove;
    private int downX;
    private int downY;
    private int x;
    private int y;

    private int touchSlop;
    private boolean isMove;
    private boolean isOpen;

    private OnRemoveListener listener;


    private Animator.AnimatorListener removeAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (listener != null) {
                listener.onRemoveClick();
            }
        }
    };

    public SwipeRemoveContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        btnRemove = (Button) LayoutInflater.from(context).inflate(R.layout.btn_remove, this, false);
        addView(btnRemove);
        btnRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRemove();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("Swiperemove", isOpen + "");
        if (isOpen) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (!canClickRemoveBtn(ev.getX(), ev.getY())) {
                    close();
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    super.dispatchTouchEvent(cancelEvent);
                    return false;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX = (int) event.getX();
        int currentY = (int) event.getY();
        Log.d("swipe", "onTouchEvent()------------action:" + event.getAction() + "," + currentX + "---------" + x);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            autoScroll();
            downX = currentX;
            downY = currentY;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isMove) {
                scrollBy(x - currentX);
            } else {
                if (judgeCanMove(currentX - downX)) {
                    return true;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isMove = false;
            getParent().requestDisallowInterceptTouchEvent(false);
            autoScroll();
        }
        x = currentX;
        y = currentY;
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int currentX = (int) ev.getX();
        int currentY = (int) ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            autoScroll();
            downX = currentX;
            downY = currentY;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (isMove) {
                Log.d("swipe", "onInterceptTouchEvent()1");
                scrollBy(x - currentX);
            } else {
                if (judgeCanMove(currentX - downX)) {
                    return true;
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            isMove = false;
            getParent().requestDisallowInterceptTouchEvent(false);
            autoScroll();
        }
        x = currentX;
        y = currentY;
        return super.onInterceptTouchEvent(ev);
    }

    private boolean canMove(int distance) {
        return Math.abs(distance) > touchSlop;
    }

    private boolean judgeCanMove(int dx) {
        if (canMove(dx)) {
            if (dx < 0) {
                isMove = true;
            } else if (isOpen) {
                isMove = true;
            }
        }
        getParent().requestDisallowInterceptTouchEvent(isMove);
        return isMove;
    }

    private void scrollBy(int dx) {
        scrollBy(dx, 0);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (x > getMaxScrollWidth()) {
            x = getMaxScrollWidth();
        } else if (x < 0) {
            x = 0;
        }
        super.scrollTo(x, y);
    }

    private void autoScroll() {
        isOpen = false;
        int scrollX = getScrollX();
        if (scrollX != 0) {
            if (scrollX > getMaxScrollWidth() / 2) {
                isOpen = true;
                Log.d("swiperemove", "isopen = true");
                animScroll(getMaxScrollWidth(), null);
            } else {
                isOpen = false;
                animScroll(0, null);
            }
        }
    }

    /**
     * 动画滚动
     *
     * @param to
     */
    private void animScroll(int to, Animator.AnimatorListener animatorListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollX(), to);
        valueAnimator.setDuration(getDuration(Math.abs(to - getScrollX())));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(value, 0);
            }
        });
        if (animatorListener != null) {
            valueAnimator.addListener(animatorListener);
        }
        valueAnimator.start();
    }

    /**
     * 根据距离计算时间
     *
     * @param distance
     * @return
     */
    private long getDuration(int distance) {
        return (long) (ANIM_DURATION * ((distance * 1.0f) / getMaxScrollWidth()));
    }

    /**
     * 获取最大的滚动距离
     *
     * @return
     */
    private int getMaxScrollWidth() {
        return btnRemove.getWidth();
    }

    public void close() {
        Log.d("Swiperemove", "close");
        isOpen = false;
        isMove = false;
        if (getScrollX() > 0) {
            animScroll(0, null);
        }
    }

    /**
     * 关闭并触发移除回调
     */
    public void closeRemove() {
        isOpen = false;
        isMove = false;
        animScroll(0, removeAnimatorListener);
    }

    /**
     * 重置状态
     */
    public void reset() {
        isOpen = false;
        isMove = false;
        scrollTo(0, 0);
    }

    /**
     * 判断是否被删除按钮处理
     *
     * @param x
     * @param y
     * @return
     */
    public boolean canClickRemoveBtn(float x, float y) {
        boolean canRemove = false;
        if (isOpen) {
            canRemove = (x > getWidth() - btnRemove.getWidth()) && x < getWidth();
        }
        return canRemove;
    }

    public boolean isMove() {
        return isMove;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean handleEvent() {
        return isMove() || isOpen() || getScrollX() != 0;
    }

    public void setListener(OnRemoveListener listener) {
        this.listener = listener;
    }

    public interface OnRemoveListener {
        void onRemoveClick();
    }
}
