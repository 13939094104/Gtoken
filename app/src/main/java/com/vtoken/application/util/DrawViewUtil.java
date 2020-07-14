package com.vtoken.application.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

//bbg
public class DrawViewUtil {
    public static Bitmap m6937a(View view) {
        view.setDrawingCacheEnabled(true);
        view.layout(0, 0, 1080, 1920);
        view.measure(View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        return getBitmapFromView(view);
    }

    //m6938b
    public static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
