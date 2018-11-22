package com.github.zlfloatwindow;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;

public class ZLFloatWindowManager {
    private static ZLFloatWindowSmallView smallWindow;
    private static ZLFloatWindowBigView bigWindow;
    private static LayoutParams smallWindowParams;
    private static LayoutParams bigWindowParams;
    private static WindowManager mWindowManager;        //用于控制在屏幕上添加或移除悬浮窗。
    private static ActivityManager mActivityManager;

    public static void createSmallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        Point spt = new Point();
        windowManager.getDefaultDisplay().getSize(spt);
        int screenWidth = spt.x;
        int screenHeight = spt.y;
        if (smallWindow == null) {
            smallWindow = new ZLFloatWindowSmallView(context);
            if (smallWindowParams == null) {
                smallWindowParams = new LayoutParams();
                if (Build.VERSION.SDK_INT <= 19) {
                    smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                } else {
                    smallWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                }
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.x = screenWidth / 2;
                smallWindowParams.y = screenHeight / 2;
                smallWindowParams.width = ZLFloatWindowSmallView.viewWidth;
                smallWindowParams.height = ZLFloatWindowSmallView.viewHeight;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
        }
    }

    public static void removeSmallWindow(Context context) {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(smallWindow);
            smallWindow = null;
        }
    }

    public static boolean isWindowShowing() {
        return smallWindow!=null;
    }

    public static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
