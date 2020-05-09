package com.qtimes.views;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by liuj on 2016/7/22.
 * 容器手势帮助类
 */
public class ViewGroupGestureHelper {

    private GestureDetector gestureDetector;
    private boolean canVerticalMove;
    private boolean canHorizontalMove;

    private int horizontalTouchSlop;
    private int verticalTouchSlop;

    private int downX;
    private int downY;


    public static ViewGroupGestureHelper createViewGroupGestureHelper(GestureDetector gestureDetector) {
        return new ViewGroupGestureHelper(gestureDetector);
    }

    public ViewGroupGestureHelper() {

    }

    public ViewGroupGestureHelper(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (gestureDetector == null || !canVerticalMove && !canHorizontalMove) {
            return false;
        }
        gestureDetector.onTouchEvent(ev);
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = x;
            downY = y;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return canIntercept(x, y);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            downX = 0;
            downY = 0;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector != null && gestureDetector.onTouchEvent(event);
    }

    public void setGuestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public void setCanVerticalMove(boolean canVerticalMove) {
        this.canVerticalMove = canVerticalMove;
    }

    public void setCanHorizontalMove(boolean canHorizontalMove) {
        this.canHorizontalMove = canHorizontalMove;
    }


    public void setHorizontalTouchSlop(int touchSlop) {
        this.horizontalTouchSlop = touchSlop;
    }


    public void setVerticalTouchSlop(int touchSlop) {
        this.verticalTouchSlop = touchSlop;
    }

    private boolean canIntercept(int x, int y) {
        if ((downX == 0) && downY == 0) {
            return false;
        }
        int distanceX = Math.abs(x - downX);
        int distanceY = Math.abs(y - downY);
        return isHorizontalEventHandled(distanceX) || isVertiacalEventHandled(distanceY);
    }

    private boolean isHorizontalEventHandled(int distanceX) {
        return canHorizontalMove && distanceX >= horizontalTouchSlop;
    }

    private boolean isVertiacalEventHandled(int distanceY) {
        return canVerticalMove && distanceY >= verticalTouchSlop;
    }


}
