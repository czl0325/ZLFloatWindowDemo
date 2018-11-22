package com.github.zlfloatwindow;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

public class ZLFloatWindowSmallView extends LinearLayout {
    private Context context;
    public static int viewWidth;                //记录小悬浮窗的宽度
    public static int viewHeight;               //记录小悬浮窗的高度
    private static int statusBarHeight;         //记录系统状态栏的高度
    private WindowManager mWindowManager;       //用于更新小悬浮窗的位置
    private WindowManager.LayoutParams mParams; //小悬浮窗的参数
    private float xInScreen;                    //记录当前手指位置在屏幕上的横坐标值
    private float yInScreen;                    //记录当前手指位置在屏幕上的纵坐标值
    private float xDownInScreen;                //记录手指按下时在屏幕上的横坐标的值
    private float yDownInScreen;                //记录手指按下时在屏幕上的纵坐标的值=
    private float xInView;                      //记录手指按下时在小悬浮窗的View上的横坐标的值
    private float yInView;                      //记录手指按下时在小悬浮窗的View上的纵坐标的值

    public ZLFloatWindowSmallView(Context context) {
        this(context, null);
    }

    public ZLFloatWindowSmallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupUI();
    }

    private void setupUI() {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small,this);
        View view = findViewById(R.id.layout_small);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY()-getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY()-getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                Point spt = new Point();
                mWindowManager.getDefaultDisplay().getSize(spt);
                int screenWidth = spt.x;
                if (xInScreen < screenWidth/2) {
                    xInScreen = 0;
                } else {
                    xInScreen = screenWidth;
                }
                updateViewPosition();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateViewPosition() {
        mParams.x = (int)(xInScreen - xInView);
        mParams.y = (int)(yInScreen - yInView);
        mWindowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
