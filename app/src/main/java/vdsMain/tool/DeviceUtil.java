package vdsMain.tool;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import com.vtoken.application.ApplicationLoader;

//bbf
//915 bct
public class DeviceUtil {
    //m6935a
    //915 m7167a
    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    //m6934a
    //915 m7166a
    public static int getActivityWidthPixels(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    //m6936b
    //915 m7168b
    public static int getActivityHeightPixels(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /* renamed from: a */
    public static int m6932a() {
        return (ApplicationLoader.getSingleApplicationContext().getActivityWidthPixels() * 2) / 9;
    }

    /* renamed from: a */
    public static int m6933a(int i, int i2) {
        if (i % i2 != 0) {
            i = ((i / i2) + 1) * i2;
        }
        return i * 3;
    }
}
