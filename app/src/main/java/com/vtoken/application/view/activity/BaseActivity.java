package com.vtoken.application.view.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import com.trello.rxlifecycle2.components.RxActivity;
import com.vtoken.application.util.MyProgressDialog;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.service.SyncService;
import com.vtoken.application.viewModel.BaseViewModel;
import vdsMain.ActivityManager;
import vdsMain.callback.SyncCallBack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends RxActivity {
    public MyProgressDialog progressDialog;
    public static AssetManager assetManager;
    BaseViewModel baseViewModel;

    public VCashCore vCashCore;

    private SyncCallBack syncCallBack;

    public SyncService.SyncServiceBinder syncServiceBinder;


    protected ServiceConnection syncServiceConn;

    protected List<SyncCallBack> syncCallBackList = new ArrayList();

    public MediaPlayer mediaPlayer = null;

    Uri uri;

    public SyncService syncService;

    protected RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.vCashCore = ApplicationLoader.getVcashCore();
        ActivityManager.getInstance().addActivity(this);
        progressDialog = new  MyProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceConnection serviceConnection = this.syncServiceConn;
        if (serviceConnection != null) {
            unbindService(serviceConnection);
            this.syncServiceConn = null;
        }
        unRegisterSyncCallBack();
        ActivityManager.getInstance().removeActivity(this);
        List<SyncCallBack> list = this.syncCallBackList;
        if (list != null) {
            list.clear();
            this.syncCallBackList = null;
        }
    }

    public void startRecyclerAnimation() {
        RecyclerView recyclerView2 = this.recyclerView;
        if (recyclerView2 != null) {
            recyclerView2.scheduleLayoutAnimation();
        }
    }

    public void bindSyncService() {
        Intent intent = new Intent(getApplicationContext(), SyncService.class);
        this.syncServiceConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                BaseActivity.this.syncServiceBinder = (SyncService.SyncServiceBinder) iBinder;
                BaseActivity baseActivity = BaseActivity.this;
                baseActivity.syncService = baseActivity.syncServiceBinder.getSyncService();
                if (BaseActivity.this.syncCallBackList != null && BaseActivity.this.syncCallBackList.size() > 0) {
                    for (SyncCallBack syncCallBack : BaseActivity.this.syncCallBackList) {
                        BaseActivity.this.syncServiceBinder.addSyncCallBack(syncCallBack);
                    }
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                BaseActivity.this.syncService = null;
                if (BaseActivity.this.syncCallBackList != null && BaseActivity.this.syncCallBackList.size() > 0) {
                    for (SyncCallBack syncCallBack : BaseActivity.this.syncCallBackList) {
                        BaseActivity.this.syncServiceBinder.removeSyncCallBack(syncCallBack);
                    }
                }
            }
        };
        bindService(intent, this.syncServiceConn, Context.BIND_AUTO_CREATE);
    }



    public void registerSyncCallBack(SyncCallBack syncCallBack) {
        this.syncCallBack = syncCallBack;
        if (syncCallBack != null) {
            if (this.syncCallBackList == null) {
                this.syncCallBackList = new ArrayList();
            }
            this.syncCallBackList.add(syncCallBack);
            SyncService.SyncServiceBinder syncServiceBinder = this.syncServiceBinder;
            if (syncServiceBinder != null) {
                syncServiceBinder.addSyncCallBack(syncCallBack);
            }
        }
    }

    public void unRegisterSyncCallBack() {
        List<SyncCallBack> list = this.syncCallBackList;
        if (list != null) {
            list.remove(this.syncCallBack);
        }
        SyncService.SyncServiceBinder aVar = this.syncServiceBinder;
        if (aVar != null) {
            aVar.removeSyncCallBack(this.syncCallBack);
        }
    }

    public MediaPlayer playRing(String str) {
        MediaPlayer mediaPlayer2 = this.mediaPlayer;
        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
            this.mediaPlayer = null;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case 119091001:
                if (str.equals("raw/arbitration.ogg")) {
                    c = 0;
                    break;
                }
                break;
            case 661090887:
                if (str.equals("raw/entrybtc.ogg")) {
                    c = 2;
                    break;
                }
                break;
            case 735634107:
                if (str.equals("raw/msg.ogg")) {
                    c = 1;
                    break;
                }
                break;
            case 1118705107:
                if (str.equals("raw/adwin.ogg")) {
                    c = 4;
                    break;
                }
                break;
            case 1777978156:
                if (str.equals("raw/entryv.ogg")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.uri = Uri.parse("android.resource://v.dimensional/2131558401");
                break;
            case 1:
                this.uri = Uri.parse("android.resource://v.dimensional/2131558405");
                break;
            case 2:
                this.uri = Uri.parse("android.resource://v.dimensional/2131558402");
                break;
            case 3:
                this.uri = Uri.parse("android.resource://v.dimensional/2131558403");
                break;
            case 4:
                this.uri = Uri.parse("android.resource://v.dimensional/2131558400");
                break;
        }
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.reset();
        try {
            this.mediaPlayer.setDataSource(getApplicationContext(), this.uri);
            this.mediaPlayer.setAudioStreamType(5);
            this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    if (BaseActivity.this.mediaPlayer != null) {
                        BaseActivity.this.mediaPlayer.release();
                        BaseActivity.this.mediaPlayer = null;
                    }
                    return false;
                }
            });
            this.mediaPlayer.prepare();
            this.mediaPlayer.setLooping(false);
            this.mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.mediaPlayer;
    }

    public String getString(String str) {
        return ApplicationLoader.getSingleApplicationContext().getStringFromLocal(str);
    }

    public void toast(String str) {
        Toast makeText = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }
}
