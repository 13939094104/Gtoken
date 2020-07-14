package vdsMain.tool;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@RequiresApi(api = 8)
/* renamed from: SharedPreferencesUtil */
//bbs
public class SharedPreferencesUtil {

    /* renamed from: a */
    private static SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil();

    /* renamed from: b */
    private static synchronized void getSharedPreferencesUtilSynchronized() {
        synchronized (SharedPreferencesUtil.class) {
            if (sharedPreferencesUtil == null) {
                sharedPreferencesUtil = new SharedPreferencesUtil();
            }
        }
    }

    //m6986a
    //915 m7221a
    public static SharedPreferencesUtil getSharedPreferencesUtil() {
        if (sharedPreferencesUtil == null) {
            getSharedPreferencesUtilSynchronized();
        }
        return sharedPreferencesUtil;
    }

    /* renamed from: a */
    private SharedPreferences getAndroidWalletSp(Context context) {
        return context.getSharedPreferences("android_wallet", 0);
    }

    //mo41234a
    public int getAndroidWalletInt(String str, int defaultValue, Context context) {
        try {
            SharedPreferences sp = getAndroidWalletSp(context);
            if (sp != null) {
                return sp.getInt(str, defaultValue);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /* renamed from: b */
    public void mo41245b(String str, int i, Context context) {
        try {
            SharedPreferences a = getAndroidWalletSp(context);
            if (a != null) {
                SharedPreferences.Editor edit = a.edit();
                edit.putInt(str, i);
                edit.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mo41236a
    public String getStringFromAndroidSp(String key, String defaultValue, Context context) {
        try {
            SharedPreferences sharedPreferences = getAndroidWalletSp(context);
            if (sharedPreferences != null) {
                return sharedPreferences.getString(key, defaultValue);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /* renamed from: a */
    public long mo41235a(String str, long j, Context context) {
        try {
            SharedPreferences a = getAndroidWalletSp(context);
            if (a != null) {
                return a.getLong(str, j);
            }
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            return j;
        }
    }

    //mo41243a
    //915 mo41419a
    public boolean getBooleanValue(String str, boolean defaultValue, Context context) {
        try {
            SharedPreferences a = getAndroidWalletSp(context);
            if (a != null) {
                return a.getBoolean(str, defaultValue);
            }
            return defaultValue;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    //mo41247b
    public void putString(String key, String value, Context context) {
        try {
            SharedPreferences a = getAndroidWalletSp(context);
            if (a != null) {
                SharedPreferences.Editor edit = a.edit();
                edit.putString(key, value);
                edit.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: b */
    public void mo41246b(String str, long j, Context context) {
        try {
            SharedPreferences a = getAndroidWalletSp(context);
            if (a != null) {
                SharedPreferences.Editor edit = a.edit();
                edit.putLong(str, j);
                edit.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mo41248b
    public void putBooleanValue(String str, boolean z, Context context) {
        try {
            SharedPreferences a = getAndroidWalletSp(context);
            if (a != null) {
                SharedPreferences.Editor edit = a.edit();
                edit.putBoolean(str, z);
                edit.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mo41237a
    public String writeMapToBase64String(HashMap<String, String> hashMap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(hashMap);
        String str = new String(Base64.encode(byteArrayOutputStream.toByteArray(), 0));
        objectOutputStream.close();
        return str;
    }

    /* renamed from: a */
    public HashMap<String, String> mo41239a(String str) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(str.getBytes(), 0)));
        HashMap<String, String> hashMap = null;
        try {
            hashMap = (HashMap) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        objectInputStream.close();
        return hashMap;
    }

    /* renamed from: a */
    public boolean mo41242a(Context context, String str, HashMap<String, String> hashMap) {
        SharedPreferences.Editor edit = context.getSharedPreferences("android_wallet", 0).edit();
        try {
            edit.putString(str, writeMapToBase64String(hashMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edit.commit();
    }

    /* renamed from: a */
    public HashMap<String, String> mo41238a(Context context, String str) {
        try {
            return mo41239a(context.getSharedPreferences("android_wallet", 0).getString(str, ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public <T extends Serializable> void mo41240a(Context context, String str, T t) {
        try {
            m6987a(context, str, (Object) t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: b */
    public <T extends Serializable> T mo41244b(Context context, String str) {
        try {
            return (T) readObjFromKeyBySP(context, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    public void mo41241a(Context context, String str, List<? extends Serializable> list) {
        try {
            m6987a(context, str, (Object) list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //915 mo41425c
    public <E extends Serializable> List<E> mo41249c(Context context, String str) {
        try {
            return (List) readObjFromKeyBySP(context, str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: a */
    private void m6987a(Context context, String str, Object obj) throws IOException {
        if (obj != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            String str2 = new String(Base64.encode(byteArrayOutputStream.toByteArray(), 0));
            byteArrayOutputStream.close();
            objectOutputStream.close();
            putString(str, str2, context);
        }
    }

    //915 m7224d
    //m6989d
    private Object readObjFromKeyBySP(Context context, String key) throws IOException {
        String value = getStringFromAndroidSp(key, "", context);
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(value.getBytes(), 0));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object readObject = null;
        try {
            readObject = objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        byteArrayInputStream.close();
        objectInputStream.close();
        return readObject;
    }
}
