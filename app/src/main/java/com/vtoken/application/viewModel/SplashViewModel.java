package com.vtoken.application.viewModel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import androidx.core.app.ActivityCompat;
import androidx.databinding.ObservableField;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.PointerIconCompat;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vcwallet.core.part.PartWallet;
import com.vtoken.application.constant.Constant;
import com.vtoken.vdsecology.vcash.InitInfo;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.R;
import com.vtoken.application.service.SyncService;
import com.vtoken.application.util.PartLoader;
import com.vtoken.application.view.activity.create.WalletCreateFirstActivity;
import com.vtoken.application.view.activity.wallet.ChooseAccountActivity;
import com.vtoken.application.view.activity.wallet.WalletMainActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.io.FileUtils;
import vdsMain.Constants;
import vdsMain.FileToolkit;
import vdsMain.observer.CoreEventObserver;
import vdsMain.tool.DeviceUtil;
import vdsMain.tool.PeerUtils;
import vdsMain.tool.SPUtils;
import vdsMain.tool.SystemToolkit;
import vdsMain.wallet.WalletType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashViewModel extends BaseViewModel {


    public static final String[] permissionArr = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.INTERNET", "android.permission.CAMERA"};

    //915 f10226B
    public ArrayList<PartWallet> partWalletArrayList;

    public final ObservableField<String> loadingText=new ObservableField<>();

    //915 f10228z
    private CoreEventObserver coreEventObserver;

    List<Permission> permissionList;

    //915 f10225A
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message message) {
//            if (message.what == 10002) {
//                SplashViewModel.this.checkAndJumpActivity();
//            }
        }
    };

    @SuppressLint({"HandlerLeak"})
    public SplashViewModel(Context context) {
        super(context);
//        new Thread() {
//            public void run() {
//                ApplicationLoader.getSingleApplicationContext().initLocale();
//            }
//        }.start();
        loadingText.set(context.getString(R.string.loading));
        PartLoader.getSingleInstance(context).setTitle(getString((int) R.string.initial_wallet));
        String[] denyList = SystemToolkit.getDenyPermissionArr(context, permissionArr);
        if (denyList == null || denyList.length == 0) {
            finishPermissionAndContinue();
        } else {
            requestPermission(denyList);
        }
    }

    private boolean areNotificationsEnabled() {
        return NotificationManagerCompat.from(this.context).areNotificationsEnabled();
    }

    //m7776a
    public void createWalletWithPath(String path) {
        Constants.InitFileDir(this.vCashCore);
        startService(new Intent(this.context, SyncService.class));
        //startService(new Intent(this.context, ImService.class));
        PeerUtils.checkPeersStatus(null, null);
        SPUtils spUtils = SPUtils.getSPUtils();
        spUtils.startSPEdit("config_vds", (Context) ApplicationLoader.getSingleApplicationContext());
//        if (spUtils.getBoolean("first_open", true)) {
//            Intent intent = new Intent(mo41595j(), GuideActivity.class);
//            intent.putExtra("path", path);
//            mo41518a(intent);
//        } else {
//            startActivity(new Intent(this.context, WalletCreateFirstActivity.class).putExtra("path", path));
//        }
        startActivity(new Intent(this.context, WalletCreateFirstActivity.class).putExtra("path", path));
        finish();
    }

    //m7393Q
    private void checkSettingAndJumpActivity() {
        Constants.InitFileDir(this.vCashCore);
        //mo41368b(new Intent(this.context, SyncService.class));
        // mo41368b(new Intent(this.context, ImService.class));
        //节点
        ///bbq.m6971a(null, null);
        SPUtils spUtils = SPUtils.getSPUtils();
        spUtils.startSPEdit("config_vds", (Context) ApplicationLoader.getSingleApplicationContext());
        boolean hasHdAccount = true;
        //第一次进显示导航
//        if (spUtils.getBoolean("first_open", true)) {
//            startActivity(new Intent(mo41396i(), GuideActivity.class));
//            finish();
//            return;
//        }
        boolean isWalletInitSuccess = this.vCashCore.getWalletType() != WalletType.UNKNOWN;
        boolean hasSetPwd = this.vCashCore.hasSetWalletPwd();
        List hdAccountList = this.vCashCore.getHDAccountList();
        if (hdAccountList == null || hdAccountList.isEmpty()) {
                hasHdAccount = false;
        }
        if (!isWalletInitSuccess || !hasSetPwd || !hasHdAccount) {
            this.vCashCore.InitWalletIfNeed(false);
            this.handler.post(new Runnable() {
                public void run() {
                    SplashViewModel splashViewModel = SplashViewModel.this;
                    splashViewModel.startActivity(new Intent(splashViewModel.context, WalletCreateFirstActivity.class));
                    ApplicationLoader.getSingleApplicationContext().setActivityWidthPixels(DeviceUtil.getActivityWidthPixels((Activity) SplashViewModel.this.context));
                    ApplicationLoader.getSingleApplicationContext().setActivityHeightPixels(DeviceUtil.getActivityHeightPixels((Activity) SplashViewModel.this.context));
                    SplashViewModel.this.finish();
                }
            });
            return;
        }
        this.handler.post(new Runnable() {
            public void run() {
                if (SplashViewModel.this.vCashCore.getWalletType() == WalletType.HOT) {
                    SplashViewModel splashViewModel = SplashViewModel.this;
                    splashViewModel.startActivity(new Intent(splashViewModel.context, WalletMainActivity.class));
                } else {
                    throw new RuntimeException("只支持热钱包");
//                    SplashViewModel bcv2 = SplashViewModel.this;
//                    bcv2.startActivity(new Intent(bcv2.context, ColdWalletActivity.class));
                }
                ApplicationLoader.getSingleApplicationContext().setActivityWidthPixels(DeviceUtil.getActivityWidthPixels((Activity) SplashViewModel.this.context));
                ApplicationLoader.getSingleApplicationContext().setActivityHeightPixels(DeviceUtil.getActivityHeightPixels((Activity) SplashViewModel.this.context));
                SplashViewModel.this.finish();
            }
        });
    }

    //mo39924a
    public void checkAndJumpActivity() {
            checkSettingAndJumpActivity();
    }

    //m7390N
    private void jumpNotificationSetting() {
        Activity activity = (Activity) this.context;
        ApplicationInfo applicationInfo = activity.getApplicationInfo();
        String packageName = activity.getApplicationContext().getPackageName();
        int uid = applicationInfo.uid;
        if (Build.VERSION.SDK_INT >= 21) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName);
            intent.putExtra("android.provider.extra.CHANNEL_ID", uid);
            intent.putExtra("app_package", packageName);
            intent.putExtra("app_uid", uid);
            activity.startActivityForResult(intent, PointerIconCompat.TYPE_ZOOM_OUT);
        } else if (Build.VERSION.SDK_INT == 19) {
            Intent intent2 = new Intent();
            intent2.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent2.addCategory("android.intent.category.DEFAULT");
            StringBuilder sb = new StringBuilder();
            sb.append("package:");
            sb.append(ApplicationLoader.getSingleApplicationContext().getPackageName());
            intent2.setData(Uri.parse(sb.toString()));
            activity.startActivityForResult(intent2, PointerIconCompat.TYPE_ZOOM_OUT);
        } else {
            activity.startActivityForResult(new Intent("android.settings.SETTINGS"), PointerIconCompat.TYPE_ZOOM_OUT);
        }
    }

    //mo40044a
    public void checkPartWallet() {
//        HashMap z = ApplicationLoader.getSingleApplicationContext().mo38497z();
//        if (z == null || z.isEmpty()) {
//            this.handler.sendEmptyMessageDelayed(10002, 500);
//        } else {
//            m7771S();
//        }
        checkAndExecutePartWalletList();
    }

    //m7398b
    public void finishPermissionAndContinue() {
        if (!areNotificationsEnabled()) {
            jumpNotificationSetting();
        } else {
            //startInitApp();
            //选择账户路径
            initPartLoaderAndWalletList();
        }
    }



    //915 m7772T
    private void crateDefaultWallet() {
        File file = new File(PartLoader.getSingleInstance(this.context).getDefaultWalletPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.getFreeSpace() < FileUtils.ONE_GB) {
            showToast(getStringRescourcesByResName("space_not_enough"));
            return;
        }
        this.coreEventObserver = new CoreEventObserver() {
            /* renamed from: a */
            public void mo39596a() {
            }

            /* renamed from: a */
            public void mo39598a(WalletType jfVar) {
            }

            /* renamed from: a */
            public void onResynched(boolean z) {
            }

            /* renamed from: b */
            public void mo39600b() {
            }

            /* renamed from: a */
            public void handleInitInfoEnum(InitInfo initInfo) {
                switch (initInfo.getInitInfo().ordinal()) {
                    case 2:
                        SplashViewModel.this.vCashCore.removeObserver((Object) this);
                        SplashViewModel viewModel = SplashViewModel.this;
                        viewModel.createWalletWithPath(viewModel.vCashCore.getAppWalletPath());
                        return;
                    case 3:
                        Process.killProcess(Process.myPid());
                        return;
                    default:
                        return;
                }
            }
        };
        this.vCashCore.addObserver((Object) this.coreEventObserver);
        this.vCashCore.initAppWithPath((Activity) this.context, PartLoader.getSingleInstance(this.context.getApplicationContext()).getDefaultWalletPath());
    }

    //915 m7771S
    private void checkAndExecutePartWalletList() {
        ArrayList<PartWallet> arrayList = this.partWalletArrayList;
        if (arrayList == null || arrayList.isEmpty()) {
            crateDefaultWallet();
            return;
        }
        PartLoader.getSingleInstance(this.context).setPartWalletList(this.partWalletArrayList);
        startActivity(new Intent(this.context, ChooseAccountActivity.class).putParcelableArrayListExtra("part_wallet", this.partWalletArrayList));
        finish();
    }

    //915 m7769Q
    private void initPartLoaderAndWalletList() {
        Observable.create(new ObservableOnSubscribe<ArrayList<PartWallet>>() {
            public void subscribe(ObservableEmitter<ArrayList<PartWallet>> observableEmitter) {
                PartLoader partLoader = PartLoader.getSingleInstance(SplashViewModel.this.context.getApplicationContext());
                StringBuilder sb = new StringBuilder();
                sb.append(FileToolkit.getVaildPath(Environment.getExternalStorageDirectory().getAbsolutePath()));
                sb.append("/");
                //sb.append(SplashViewModel.this.context.getPackageName());
                //TODO 先改成固定路径
                sb.append(Constant.INSTANCE.getWalletConstPath());
                String defaultPathString = sb.toString();
                try {
                    FileToolkit.checkDirectory(defaultPathString, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<PartWallet> walletList = partLoader.getPartWalletList(defaultPathString);
                if (walletList == null) {
                    walletList = new ArrayList<>();
                }
                observableEmitter.onNext(walletList);
            }
        }).compose(bindDestoryEvent()).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe((Observer)
                new Observer<ArrayList<PartWallet>>() {
            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
            }

            /* renamed from: a */
            public void onNext(ArrayList<PartWallet> arrayList) {
                SplashViewModel.this.partWalletArrayList = arrayList;
                SplashViewModel.this.checkPartWallet();
            }

            public void onError(Throwable th) {
                th.printStackTrace();
                SplashViewModel.this.checkPartWallet();
            }
        });
    }

    //m7391O
    private void startInitApp() {
        if (this.vCashCore.getInitInfo().getInitInfo() == InitInfo.InitInfoEnum.INIT_SUCCESS) {
            checkAndJumpActivity();
            return;
        }
        this.coreEventObserver = new CoreEventObserver() {
            /* renamed from: a */
            public void mo39596a() {
            }

            /* renamed from: a */
            public void mo39598a(WalletType walletType) {
            }

            /* renamed from: a */
            public void onResynched(boolean z) {
            }

            /* renamed from: b */
            public void mo39600b() {
            }

            //noticeHandleInitInfoEnum
            public void handleInitInfoEnum(InitInfo initInfo) {
                switch (initInfo.getInitInfo().ordinal()) {
                    case 2:
                        SplashViewModel.this.vCashCore.removeObserver((Object) this);
                        SplashViewModel.this.checkAndJumpActivity();
                        return;
                    case 3:
                        Process.killProcess(Process.myPid());
                        return;
                    default:
                        return;
                }
            }
        };
        this.vCashCore.addObserver((Object) this.coreEventObserver);
        this.vCashCore.initApp((Activity) this.context);
    }

    @SuppressLint({"CheckResult"})
    /* renamed from: a */
    private void requestPermission(String[] strArr) {
        final ArrayList arrayList = new ArrayList<String>(Arrays.asList(strArr));
        StringBuilder sb = new StringBuilder();
        sb.append("requestPermission: ");
        sb.append(arrayList);
        Log.i("SplashViewModel", sb.toString());
        new RxPermissions((Activity) getContext()).requestEach(strArr).compose(bindDestoryEvent()).subscribe(
                new Consumer<Permission>() {
            /* renamed from: a */
            public void accept(Permission permission) {
                arrayList.remove(permission.name);
                if (!permission.granted) {
                    if (SplashViewModel.this.permissionList == null) {
                        SplashViewModel.this.permissionList = new ArrayList();
                    }
                    SplashViewModel.this.permissionList.add(permission);
                }
                if (!arrayList.isEmpty()) {
                    return;
                }
                if (SplashViewModel.this.permissionList == null || SplashViewModel.this.permissionList.isEmpty()) {
                    getNativePhoneNumber();
                    SplashViewModel.this.finishPermissionAndContinue();
                } else {
                    System.exit(0);
                }
            }
        });
    }

    private void getNativePhoneNumber(){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        String nativePhoneNumber = telephonyManager.getLine1Number();
        Logger.d(nativePhoneNumber);
    }

    public void removeHandlerAndClearDialog() {
        super.removeHandlerAndClearDialog();
        if (this.coreEventObserver != null) {
            this.vCashCore.removeObserver((Object) this.coreEventObserver);
        }
        this.handler.removeCallbacksAndMessages(null);
    }
}
