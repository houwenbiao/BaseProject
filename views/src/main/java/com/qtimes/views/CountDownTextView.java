package com.qtimes.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuj on 2016/6/21.
 * 倒计时文本框
 */
public class CountDownTextView extends TextView {

    private static final int DEFAULT_SECONDS = 60;


    private String initTxt;
    private String formatString;
    private int initTxtColor;
    private int countDownTxtColor;

    private int totalSecond;
    private int currentSecond;
    private ScheduledExecutorService executorService;

    private CountDownListener countDownListener;

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCountDown();
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CountDownTextView);
        formatString = typedArray.getString(R.styleable.CountDownTextView_countdown_format);
        totalSecond = typedArray.getInt(R.styleable.CountDownTextView_countdown_seconds, DEFAULT_SECONDS);
        initTxt = typedArray.getString(R.styleable.CountDownTextView_countdown_init_txt);
        initTxtColor = typedArray.getColor(R.styleable.CountDownTextView_countdown_init_txtcolor, Color.BLACK);
        countDownTxtColor = typedArray.getColor(R.styleable.CountDownTextView_countdown_txtcolor, Color.BLACK);
        typedArray.recycle();
    }



    /**
     * 开始计时
     */
    public void startCountDown() {
        setEnabled(false);
        if (executorService == null) {
            currentSecond = totalSecond;
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    currentSecond--;
                    if (currentSecond > 0) {
                        updateText(currentSecond);
                    } else {
                        post(new Runnable() {
                            @Override
                            public void run() {
                                if (countDownListener != null) {
                                    countDownListener.onCountDownEnd();
                                }
                            }
                        });
                        reset();
                    }
                }
            }, 0, 1, TimeUnit.SECONDS);
            if (countDownListener != null) {
                countDownListener.onCountDownStart();
            }
        }
    }

    /**
     * 停止计时
     */
    public void stopCountDown() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    /**
     * 重置
     */
    public void reset() {
        stopCountDown();
        currentSecond = totalSecond;
        updateText(currentSecond);
    }

    /**
     * 更新时间
     *
     * @param currentSecond
     */
    private void updateText(final int currentSecond) {
        if (currentSecond >= 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (currentSecond == totalSecond) {
                        setText(initTxt);
                        setTextColor(initTxtColor);
                        setEnabled(true);
                    } else {
                        setTextColor(countDownTxtColor);
                        setText(String.format(formatString, currentSecond));
                    }
                }
            });
        }
    }


    public void setCountDownListener(CountDownListener countDownListener) {
        this.countDownListener = countDownListener;
    }

    public interface CountDownListener {
        void onCountDownStart();

        void onCountDownEnd();
    }

}
