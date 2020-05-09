package com.qtimes.views;

/**
 * Created by jie on 16/10/21.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jie on 16/10/21.
 */

public class ShakeButton extends FrameLayout {


    private static final int DURATION=80;

    private ShakeAnimationListener shakeAnimationListener=new ShakeAnimationListener();

    private static final float[][] aq=new float[][]{
            {
                    1f,1.4f
            },
            {
                    1.4f,1.0f
            },
            {
                    1.0f,1.2f
            },
            {
                    1.2f,1.0f
            }
    };


    public ShakeButton(Context context) {
        this(context,null);
    }

    public ShakeButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShakeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(context,attrs);
        if (isInEditMode()){
            return;
        }
        show();
    }

    private ImageView iv_shake;

    private TextView tv_desc;

    private void initView(){
        rootView= LayoutInflater.from(getContext()).inflate(R.layout.view_shake,this);
        iv_shake= (ImageView) rootView.findViewById(R.id.iv_shake);
        tv_desc= (TextView) rootView.findViewById(R.id.tv_desc);
    }

    private CharSequence descText;

    private Drawable drawable;

    private View rootView;

    private boolean isChecked=false;

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShakeButton);
        for (int i=0;i<typedArray.getIndexCount();i++){
            int attr=typedArray.getIndex(i);
            if (attr==R.styleable.ShakeButton_text){
                descText=typedArray.getText(attr);
            }
            else if (attr==R.styleable.ShakeButton_drawable){
                drawable=typedArray.getDrawable(attr);
            }
            else if (attr==R.styleable.ShakeButton_checked){
                isChecked=typedArray.getBoolean(attr,false);
            }
        }
        typedArray.recycle();
    }

    private void show(){
        if (drawable!=null){
            iv_shake.setImageDrawable(drawable);
        }
        if (descText!=null){
            tv_desc.setText(descText);
        }
        if (isChecked){
            rootView.setSelected(true);
        }
        else {
            rootView.setSelected(false);
        }
    }

    private ScaleAnimation generateAnimation(float fromX, float toX, float fromY, float toY){
        ScaleAnimation sa=new ScaleAnimation(fromX,toX,fromY,toY, ScaleAnimation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(DURATION);
        return sa;
    }

    public void startAnimation(){
        Animation a1 = animations[0];
        a1.setAnimationListener(shakeAnimationListener);
        iv_shake.startAnimation(a1);
    }

    private  Animation[] animations={
            generateAnimation(aq[0][0],aq[0][1],aq[0][0],aq[0][1]),
            generateAnimation(aq[1][0],aq[1][1],aq[1][0],aq[1][1]),
            generateAnimation(aq[2][0],aq[2][1],aq[2][0],aq[2][1]),
            generateAnimation(aq[3][0],aq[3][1],aq[3][0],aq[3][1])
    };

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected){
            this.startAnimation();
        }
        else if (animations!=null){
            for (Animation animation:animations){
                animation.cancel();
                animation.reset();
            }
            iv_shake.clearAnimation();
        }
    }




    private class ShakeAnimationListener implements Animation.AnimationListener {

        private int aIndex=0;

        private ShakeAnimationListener(){
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            aIndex++;
            if (aIndex>3){
                aIndex=0;
                iv_shake.clearAnimation();
                return;
            }
            animations[aIndex].setAnimationListener(this);
            iv_shake.startAnimation(animations[aIndex]);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
