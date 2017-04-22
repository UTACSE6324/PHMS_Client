package com.cse6324.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Jarvis on 2017/2/28.
 */

public class DisplayUtil {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }
}
