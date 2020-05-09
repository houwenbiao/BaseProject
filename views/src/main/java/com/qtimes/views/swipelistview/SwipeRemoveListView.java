package com.qtimes.views.swipelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by liuj on 2016/7/4.
 */
public class SwipeRemoveListView extends ListView {

    private SwipeRemoveContainer swipeRemoveItem;

    public SwipeRemoveListView(Context context) {
        super(context);
    }

    public SwipeRemoveListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int position = pointToPosition((int) ev.getX(), (int) ev.getY());
        position = position - getFirstVisiblePosition();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (swipeRemoveItem != null) {
                if (indexOfChild(swipeRemoveItem) != position && swipeRemoveItem.handleEvent()) {
                    swipeRemoveItem.close();
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    super.dispatchTouchEvent(cancelEvent);
                    return false;
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            View childView = getChildAt(position);
            if (childView instanceof SwipeRemoveContainer) {
                SwipeRemoveContainer swipeRemoveContainer = (SwipeRemoveContainer) childView;
                if (swipeRemoveContainer.handleEvent()) {
                    swipeRemoveItem = swipeRemoveContainer;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
