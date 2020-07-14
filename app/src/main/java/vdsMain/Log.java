package vdsMain;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public final class Log {

    //f12749a
    private static String packageName = "";

    //f12750b
    private static File logFile;

    //f12751c
    private static BufferedWriter bufferedWriter;

    /* renamed from: a */
    private static void m11476a(String str, String str2, String str3, Throwable th) {
    }

    //m11478a
    public static final boolean createLogFile(Context context, String str) {
        packageName = context.getPackageName();
        if (str == null || str.isEmpty()) {
            return false;
        }
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf == -1) {
            return false;
        }
        int i = lastIndexOf + 1;
        String substring = str.substring(0, i);
        String substring2 = str.substring(i);
        if (substring.isEmpty() || substring2.isEmpty()) {
            String str2 = packageName;
            StringBuilder sb = new StringBuilder();
            sb.append("can not create log file, invalidate file path ");
            sb.append(str);
            android.util.Log.w(str2, sb.toString());
            return false;
        }
        if (substring2.indexOf(".log") == -1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(substring2);
            sb2.append(".log");
            substring2 = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(substring);
        sb3.append(DateTimeToolkit.getCurrentTimeForamtDefaultCalendar());
        sb3.append("_");
        sb3.append(substring2);
        try {
            logFile = new File(sb3.toString());
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(logFile), 524288);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logFile = null;
            bufferedWriter = null;
            return false;
        }
    }

    /* renamed from: a */
    public static final void m11473a(Object obj, String str) {
        LogDebug(obj.getClass().getName(), str);
    }

    //910 m11600a
    //m11475a
    public static final void LogDebug(String str, String str2) {
        android.util.Log.d(str, str2);
        m11476a("DEBUG", str, str2, null);
    }

    //m11479b
    //910  m11604b
    public static final void infoObject(Object obj, String str) {
        info(obj.getClass().getName(), str);
    }

    /* renamed from: a */
    public static final void m11472a(Class<?> cls, String str) {
        info(cls.getName(), str);
    }

    //m11480b
    //910 m11605b
    public static final void info(String str, String str2) {
        android.util.Log.i(str, str2);
        m11476a("INFO", str, str2, null);
    }

    //m11481c
    //910 m11607c
    public static final void LogObjError(Object obj, String str) {
        LogErrorNoThrow(obj.getClass().getName(), str);
    }

    //m11482c
    public static final void LogErrorNoThrow(String str, String str2) {
        LogError(str, str2, (Throwable) null);
    }

    //m11474a
    public static final void LogObjError(Object obj, String str, Throwable th) {
        LogError(obj.getClass().getName(), str, th);
    }

    //m11477a
    public static final void LogError(String str, String str2, Throwable th) {
        android.util.Log.e(str, str2, th);
        m11476a("ERROR", str, str2, th);
    }

    //m11483d
    public static final void logObjectWarning(Object obj, String str) {
        logWarning(obj.getClass().getName(), str);
    }

    //m11484d
    //910 m11609d
    public static final void logWarning(String str, String str2) {
        android.util.Log.w(str, str2);
        m11476a("WARNNING", str, str2, null);
    }

    /* renamed from: e */
    public static final void m11485e(String str, String str2) {
        android.util.Log.v(str, str2);
        m11476a("VERBOSE", str, str2, null);
    }
}

