package vdsMain.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

    /* renamed from: c */
    private static SPUtils spUtils;

    //915 f13423a
    private SharedPreferences sharedPreferences;

    //915 f13424b
    private SharedPreferences.Editor editor;

    //m11575a
    //915 m12128a
    public static SPUtils getSPUtils() {
        if (spUtils == null) {
            spUtils = new SPUtils();
        }
        return spUtils;
    }

    //mo43702a
    //915 mo44008a
    @SuppressLint("CommitPrefEdits")
    public void startSPEdit(String str, Context context) {
        this.sharedPreferences = context.getSharedPreferences(str, 0);
        this.editor = this.sharedPreferences.edit();
        this.editor.apply();
    }

    //915 mo44005a
    public String getStringNoDefault(String str) {
        return getString(str, (String) null);
    }

    //915 mo44014b
    public String getString(String str, String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    //915 mo44009a
    public void putEditorString(String str, String str2) {
        this.editor.putString(str, str2).apply();
    }

    //915 mo44006a
    public void putInt(String str, int i) {
        this.editor.putInt(str, i).apply();
    }

    //915 mo44012b
    public int getInt(String str) {
        return getInt(str, -1);
    }

    /* renamed from: b */
    public int getInt(String str, int i) {
        return this.sharedPreferences.getInt(str, i);
    }

    /* renamed from: a */
    public void putLong(String str, long defaultValue) {
        this.editor.putLong(str, defaultValue).apply();
    }

    /* renamed from: b */
    public long getLong(String str) {
        return getLong(str, -1);
    }

    /* renamed from: b */
    public long getLong(String str, long defaultValue) {
        return this.sharedPreferences.getLong(str, defaultValue);
    }

    //mo43703a
    public void putBoolean(String str, boolean defaultValue) {
        this.editor.putBoolean(str, defaultValue).apply();
    }

    //mo43707b
    //915 mo44015b
    public boolean getBoolean(String key, boolean defaultValue) {
        return this.sharedPreferences.getBoolean(key, defaultValue);
    }
}