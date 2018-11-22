package com.github.zlfloatwindow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ZLFloatWindowBigView extends LinearLayout {
    private Context context;
    public static int viewWidth;
    public static int viewHeight;

    public ZLFloatWindowBigView(Context context) {
        this(context, null);
    }

    public ZLFloatWindowBigView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupUI();
    }

    private void setupUI() {
        View view = LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;

    }

}
