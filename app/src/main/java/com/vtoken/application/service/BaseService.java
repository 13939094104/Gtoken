package com.vtoken.application.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.vtoken.application.ApplicationLoader;

import vdsMain.message.Message;
import vdsMain.observer.ASyncMessageObserver;

public class BaseService extends Service implements ASyncMessageObserver {
    protected Handler handler;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.handler = new Handler();
        ApplicationLoader.getVcashCore().addObserver((Object) this);
    }

    public void onDestroy() {
        super.onDestroy();
        ApplicationLoader.getVcashCore().removeObserver((Object) this);
        stopSelf();
    }

    public void onMessageReceived(Message message) {

    }

    /* access modifiers changed from: private */
    public void createFail(String str, String str2) {

    }
}