package com.vtoken.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.ObservableField;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.StrictMode;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.lzy.okgo.OkGo;
import com.vtoken.application.util.PreferenceUtil;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.vdsecology.vcash.VCashCoreCreator;
import com.vtoken.application.model.Node;
import com.vtoken.application.service.SyncService;
import com.vtoken.application.viewModel.BaseViewModel;

import io.realm.Realm;

import org.apache.commons.cli.HelpFormatter;

import vdsMain.ActivityManager;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.transaction.TxOut;
import vdsMain.transaction.Utxo;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ApplicationLoader extends Application implements Thread.UncaughtExceptionHandler {

    //f3262i
    private static int activityWidthPixels;

    //f3261h
    private static int activityHeightPixels;

    private static volatile boolean applicationInited = false;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;
    public static ObservableField<String> f3257a = new ObservableField<>(HelpFormatter.DEFAULT_LONG_OPT_PREFIX);

    //f3258c
    public static Activity sActivity;

    @SuppressLint("StaticFieldLeak")
    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;

    public static VCashCore vCashCore;

    boolean f3275e = false;

    //f3273b
    boolean[] chainNetWorkIsNormal = new boolean[2];

    private List<TxOut> f3276p;

    private static String f3263j;

    private static String f3264k;

    private List<Node> f3277s;

    //f3259f
    private static ApplicationLoader sApplicationLoader;

    private static List<Utxo> f3269q;

    //f3268o
    private static BLOCK_CHAIN_TYPE blockChainType;

    //915 f3296d
    public HashMap<String, String> f3274d;

    private static Realm realm;

    public BaseViewModel baseView;

    //m3620a
    //915 m3654a
    public static synchronized ApplicationLoader getSingleApplicationContext() {
        ApplicationLoader applicationLoader;
        synchronized (ApplicationLoader.class) {
            applicationLoader = sApplicationLoader;
        }
        return applicationLoader;
    }
    //m3629j
    public static VCashCore getVcashCore() {
        return vCashCore;
    }

    public String mo38426o() {
        return f3263j;
    }

    public void mo38403a(List<Node> list) {
        this.f3277s = list;
    }

//    public HashMap<String, String> getHashMap() {
//        return this.hashMap;
//    }

    //m3630k
    public static BLOCK_CHAIN_TYPE getBlockChainType() {
        return blockChainType;
    }

    public static File getFilesDirFixed() {
        for (int a = 0; a < 10; a++) {
            File path = ApplicationLoader.applicationContext.getFilesDir();
            if (path != null) {
                return path;
            }
        }
        try {
            ApplicationInfo info = getSingleApplicationContext().getApplicationInfo();
            File path = new File(info.dataDir, "files");
            path.mkdirs();
            return path;
        } catch (Exception e) {

        }
        return new File("/data/data/com.vdser.vdselology/files");
    }

    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Thread.setDefaultUncaughtExceptionHandler(this);
        Realm.init(this);
        sApplicationLoader = this;
        blockChainType = BLOCK_CHAIN_TYPE.VCASH;
        vCashCore = (VCashCore) VCashCoreCreator.INSTANCE.getVCashCore(this);
        registerActivityLifecycleCallbacks();
        applicationHandler = new Handler(applicationContext.getMainLooper());
        OkGo.init(this);
        PreferenceUtil.init(this);
    }





    //mo38397A
    public boolean hasVcashPeer() {
        return this.chainNetWorkIsNormal[0];
    }

    public void initLocale(){

    }

    public static boolean isApkInDebug() {
        try {
            ApplicationInfo info = applicationContext.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


    //mo38416d
    public void setVcashNetworkIsNormal(boolean hasPeer) {
        this.chainNetWorkIsNormal[0] = hasPeer;
    }

    public HashMap<String, String> mo38497z() {
        return this.f3274d;
    }

    public boolean mo38400D() {
        return this.f3275e;
    }

    private void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                ApplicationLoader.sActivity = activity;
            }

            public void onActivityStarted(Activity activity) {
                ApplicationLoader.sActivity = activity;
            }

            public void onActivityResumed(Activity activity) {
                ApplicationLoader.sActivity = activity;
            }

            public void onActivityPaused(Activity activity) {
                if (ApplicationLoader.sActivity == activity) {
                    ApplicationLoader.sActivity = null;
                }
            }

            public void onActivityStopped(Activity activity) {
                if (ApplicationLoader.sActivity == activity) {
                    ApplicationLoader.sActivity = null;
                }
            }

            public void onActivityDestroyed(Activity activity) {
                if (ApplicationLoader.sActivity == activity) {
                    ApplicationLoader.sActivity = null;
                }
            }
        });
    }

    //915 mo38463E
    public void stopApp() {
        //stopService(new Intent(m3654a(), ImService.class));
        stopService(new Intent(getSingleApplicationContext(), SyncService.class));
        ActivityManager.getInstance().removeAll();
        Process.killProcess(Process.myPid());
    }


    public void uncaughtException(Thread thread, Throwable th) {
        th.printStackTrace();
        Crashlytics.logException(th);
        Crashlytics.getInstance().crash();
        //stopService(new Intent(getSingleApplicationContext(), ImService.class));
//        stopService(new Intent(getSingleApplicationContext(), SyncService.class));
//        ActivityManager.getInstance().removeAll();
//        Process.killProcess(Process.myPid());
    }


    //mo38406b
    public String getStringFromLocal(String str) {
        HashMap<String, String> hashMap = this.f3274d;
        if (hashMap == null || hashMap.isEmpty() || !this.f3274d.containsKey(str)) {
            return getResources().getString(getResources().getIdentifier(str, "string", getPackageName()));
        }
        String str2 = (String) this.f3274d.get(str);
        if (!TextUtils.isEmpty(str2)) {
            return str2;
        }
        return getResources().getString(getResources().getIdentifier(str, "string", getPackageName()));
    }

    public List<Utxo> mo38399C() {
        return f3269q;
    }

    public void mo38412c(List<Utxo> list) {
        f3269q = list;
    }

    public void mo38418e(String str) {
        f3264k = str;
    }

    //mo38408b
    public void setTxOutList(List<TxOut> list) {
        this.f3276p = list;
    }

    //mo38425l
    public List<TxOut> getTxOutList() {
        return this.f3276p;
    }

    //mo38423h
    public int getActivityHeightPixels() {
        return activityHeightPixels;
    }

    //mo38424i
    public int getActivityWidthPixels() {
        return activityWidthPixels;
    }

    //mo38407b
    //915 mo38470b
    public void setActivityWidthPixels(int i) {
        activityWidthPixels = i;
    }

    //mo38401a
    //915 mo38464a
    public void setActivityHeightPixels(int i) {
        activityHeightPixels = i;
    }

}
