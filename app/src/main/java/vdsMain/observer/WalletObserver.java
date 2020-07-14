package vdsMain.observer;

import bitcoin.UInt256;
import vdsMain.CTxDestination;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.ChainSyncStatus;
import vdsMain.message.Message;
import vdsMain.model.Account;
import vdsMain.model.Address;
import vdsMain.peer.Peer;
import vdsMain.peer.PeerManager;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;
import vdsMain.wallet.WalletType;

import java.util.HashMap;
import java.util.List;

public interface WalletObserver {
    //mo42764a
    void onCurBlockNoChange(Wallet wallet, int i, int cur, int max);

    /* renamed from: a */
    void mo42765a(Wallet izVar, UInt256 uInt256);

    /* renamed from: a */
    void mo42766a(Wallet izVar, Transaction dhVar);

    //mo42658a
    void onBlockNoUpdate(Wallet izVar, ChainSyncStatus ioVar, BlockHeader jtVar, int i, ChainSyncStatus ioVar2);

    /* renamed from: a */
    void mo42767a(Wallet izVar, List<Address> list);

    /* renamed from: a */
    void mo42768a(Wallet izVar, List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap);

    //mo42769a
    void OnUpdateWallTypeSuccess(Wallet izVar, WalletType jeVar);

    /* renamed from: a */
    void mo42770a(Wallet izVar, Account jfVar);

    //mo42771a
    void onUpdateBlock(Wallet izVar, BlockInfo juVar, List<Transaction> list, HashMap<CTxDestination, Address> hashMap);

    //mo42772a
    void onMessageReceived(Wallet wallet, Peer peer, Message message);

    //mo42773a
    void onConnectStatusChange(Wallet wallet, Peer peer, Peer.PeerStatus peerStatus);

    /* renamed from: a */
    void mo42774a(Wallet izVar, PeerManager.PeerManagerStatus dVar);

    //mo42775a
    void onWalletResync(Wallet wallet, boolean z);

    //mo43913a
    void onMainPeerChange(PeerManager peerManager, Peer peer);

    /* renamed from: a_ */
    void mo43914a_();

    /* renamed from: b */
    void mo42780b(Wallet izVar, Transaction dhVar);

    /* renamed from: b */
    void mo42781b(Wallet izVar, List<Address> list);

    //mo42783c
    void onTransactionsConfirmed(Wallet wallet, List<Transaction> list);


    void mo42785d(Wallet izVar, List<Transaction> list);
}

