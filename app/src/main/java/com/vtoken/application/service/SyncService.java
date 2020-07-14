package com.vtoken.application.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.vtoken.vdsecology.vcash.InitInfo;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.application.ApplicationLoader;
import com.vtoken.application.view.activity.wallet.WalletMainActivity;
import vdsMain.ActivityManager;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.CTxDestination;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.ChainSyncStatus;
import vdsMain.callback.SyncCallBack;
import vdsMain.observer.BlockChainObserver;
import vdsMain.observer.CoreEventObserver;
import vdsMain.observer.NetworkObserver;
import vdsMain.observer.TransactionObserver;
import vdsMain.peer.Peer;
import vdsMain.peer.PeerManager;
import vdsMain.tool.PeerUtils;
import vdsMain.transaction.Transaction;
import vdsMain.model.Address;
import vdsMain.wallet.WalletType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SyncService extends BaseService {
    /* access modifiers changed from: private */
    public static volatile List<SyncCallBack> syncCallBackList = new CopyOnWriteArrayList();
    private IntentFilter intentFilter;
    LocalBroadcastManager localBroadcastManager;
    private NetworkObserver networkObserver;
    private BlockChainObserver blockChainObserver;
    VCashCore vCashCore;

    //C2650a
    public class SyncServiceBinder extends Binder {
        /* renamed from: b */
        public void mo39612b() {
        }

        public SyncServiceBinder() {
        }

        //mo39610a
        public SyncService getSyncService() {
            return SyncService.this;
        }

        //mo39611a
        public void addSyncCallBack(SyncCallBack syncCallBack) {
            if (SyncService.syncCallBackList == null) {
                SyncService.syncCallBackList = new ArrayList();
            }
            if (!SyncService.syncCallBackList.contains(syncCallBack)) {
                SyncService.syncCallBackList.add(syncCallBack);
            }
        }

        //mo39613b
        public void removeSyncCallBack(SyncCallBack syncCallBack) {
            if (SyncService.syncCallBackList != null && SyncService.syncCallBackList.size() != 0) {
                SyncService.syncCallBackList.remove(syncCallBack);
            }
        }
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return new SyncServiceBinder();
    }

    public void onCreate() {
        super.onCreate();
        this.vCashCore = ApplicationLoader.getVcashCore();
        this.vCashCore.addObserver((Object) this);
        this.blockChainObserver = new BlockChainObserver() {
            /* renamed from: a */
            public void onCurBlockNoChange(BLOCK_CHAIN_TYPE blockChainType, int i, int i2, int i3) {

            }

            /* renamed from: a */
            public void onUpdateBlock(BLOCK_CHAIN_TYPE blockChainType, BlockInfo blockInfo, List<Transaction> list, HashMap<CTxDestination, Address> hashMap) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onUpdateBlock(blockChainType, blockInfo, list, hashMap);
                    }
                }
            }

            /* renamed from: a */
            public void onBlockNoUpdate(BLOCK_CHAIN_TYPE blockChainType, ChainSyncStatus chainSyncStatus, BlockHeader blockHeader, int maxBlockNo) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onBlockNoUpdate(blockChainType, chainSyncStatus, blockHeader, maxBlockNo);
                    }
                }
                if (!ApplicationLoader.getSingleApplicationContext().mo38400D() && chainSyncStatus == ChainSyncStatus.SYNCHED && blockHeader.getBlockNo() == maxBlockNo) {
                    SyncService.this.handler.post(new Runnable() {
                        public final void run() {
                        }
                    });
                }
            }

            public void onTransactionsInvalid(BLOCK_CHAIN_TYPE blockChainType, List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onTransactionsInvalid(blockChainType, list, list2, hashMap);
                    }
                }
            }
        };
        this.networkObserver = new NetworkObserver() {
            /* renamed from: a */
            public void onPeerManagerStatusChange(PeerManager.PeerManagerStatus peerManagerStatus) {
            }

            /* renamed from: a */
            public void onMainPeerChange(PeerManager peerManager, Peer peer) {
            }

            /* renamed from: a */
            public void onConnectStatusChange(Peer peer, Peer.PeerStatus peerStatus){
                PeerUtils.checkPeersStatus(peer, peerStatus);
            }
        };
        ApplicationLoader.getVcashCore().addObserver((Object) new CoreEventObserver() {
            /* renamed from: a */
            public void mo39596a() {
            }

            /* renamed from: a */
            public void handleInitInfoEnum(InitInfo isVar) {
            }

            /* renamed from: a */
            public void mo39598a(WalletType jeVar) {
            }

            /* renamed from: b */
            public void mo39600b() {
            }

            /* renamed from: a */
            public void onResynched(boolean z) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onResynched(z);
                    }
                }
            }
        });
        ApplicationLoader.getVcashCore().addObserver((Object) new TransactionObserver() {
            /* renamed from: a */
            public void onTransactionSent(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onTransactionSent(blockChainType, transaction);
                    }
                }
            }

            /* renamed from: a */
            public void onTransactionsConfirmed(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onTransactionsConfirmed(blockChainType, list);
                    }
                }
            }

            /* renamed from: b */
            public void mo39605b(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.mo39687c(blockChainType, list);
                    }
                }
            }

            /* renamed from: c */
            public void onTransactionsReceived(BLOCK_CHAIN_TYPE blockChainType, List<Transaction> list) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onTransactionsReceived(blockChainType, list);
                    }
                }
            }

            /* renamed from: b */
            public void onTransactionAbanded(BLOCK_CHAIN_TYPE blockChainType, Transaction transaction) {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.onTransactionAbanded(blockChainType, transaction);
                    }
                }
            }

            /* renamed from: a */
            public void mo39601a() {
                if (SyncService.syncCallBackList != null) {
                    for (SyncCallBack syncCallBack : SyncService.syncCallBackList) {
                        syncCallBack.OnAdUpdate();
                    }
                }
            }
        });
        ApplicationLoader.getVcashCore().addObserver((Object) this.blockChainObserver);
        ApplicationLoader.getVcashCore().addObserver((Object) this.networkObserver);
        initNetworkReceiver();
    }

    private void initNetworkReceiver() {
        BroadcastReceiver r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") && ActivityManager.getInstance().hasClass(WalletMainActivity.class)) {
                    NetworkInfo activeNetworkInfo = ((ConnectivityManager) SyncService.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                    if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                        ApplicationLoader.getVcashCore().checkAndStopPeer();
                        return;
                    }
                    if (activeNetworkInfo.getType() != 0) {
                        new Thread() {
                            public void run() {
                                ApplicationLoader.getVcashCore().checkAndStartPeerManagerNetwork();
                            }
                        }.start();
                    }
                }
            }
        };
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(r0, intentFilter2);
    }

    public void onDestroy() {
        super.onDestroy();
        ApplicationLoader.getVcashCore().removeObserver((Object) this.blockChainObserver);
        ApplicationLoader.getVcashCore().removeObserver((Object) this.networkObserver);
        this.vCashCore.removeObserver((Object) this);
        if(syncCallBackList!=null)
            syncCallBackList.clear();
        syncCallBackList = null;
        this.blockChainObserver = null;
    }

}
