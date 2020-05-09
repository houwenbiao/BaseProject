package com.qtimes.utils.android;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by gufei on 2016/9/18 0018.
 */

public class AnimatorUtil {
    /**
     * 横向位移动画
     *
     * @param view
     * @param from
     * @param to
     * @return
     */
    public static ObjectAnimator createTranslationX(View view, float from, float to) {
        return ObjectAnimator.ofFloat(view, "translationX", from, to);
    }

    /**
     * 纵向位移动画
     *
     * @param view
     * @param from
     * @param to
     * @return
     */
    public static ObjectAnimator createTranslationY(View view, float from, float to) {
        return ObjectAnimator.ofFloat(view, "translationY", from, to);
    }

    /**
     * 渐变动画
     * @param view
     * @param from
     * @param to
     * @return
     */
    public static ObjectAnimator createalpha(View view, float from, float to) {
        return ObjectAnimator.ofFloat(view, "alpha", from, to);
    }
}
