package vdsMain.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Vector;

public class SystemToolkit {
    @SuppressLint({"NewApi"})
    /* renamed from: a */
    public static String[] getDenyPermissionArr(Context context, String[] strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            return null;
        }
        Vector vector = new Vector();
        for (String str : strArr) {
            if (context.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                vector.add(str);
            }
        }
        if (vector.isEmpty()) {
            return null;
        }
        String[] strArr2 = new String[vector.size()];
        vector.toArray(strArr2);
        return strArr2;
    }
}
