package vdsMain.observer;

import bitcoin.UInt256;
import vdsMain.CTxDestination;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.ChainSyncStatus;
import vdsMain.message.Message;
import vdsMain.model.Account;
import vdsMain.peer.Peer;
import vdsMain.peer.PeerManager;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import vdsMain.wallet.WalletType;

import java.util.HashMap;
import java.util.List;

public class SimpleWalletObserver implements WalletObserver {
    /* renamed from: a */
    public void onCurBlockNoChange(Wallet wallet, int i, int cur, int max) {
    }

    /* renamed from: a */
    public void mo42765a(Wallet izVar, UInt256 uInt256) {
    }

    /* renamed from: a */
    public void mo42766a(Wallet izVar, Transaction dhVar) {
    }

    /* renamed from: a */
    public void onBlockNoUpdate(Wallet izVar, ChainSyncStatus ioVar, BlockHeader jtVar, int i, ChainSyncStatus ioVar2) {
    }

    /* renamed from: a */
    public void mo42767a(Wallet izVar, List<Address> list) {
    }

    /* renamed from: a */
    public void mo42768a(Wallet izVar, List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap) {
    }

    /* renamed from: a */
    public void OnUpdateWallTypeSuccess(Wallet izVar, WalletType jeVar) {
    }

    /* renamed from: a */
    public void mo42770a(Wallet izVar, Account jfVar) {
    }

    /* renamed from: a */
    public void onUpdateBlock(Wallet izVar, BlockInfo juVar, List<Transaction> list, HashMap<CTxDestination, Address> hashMap) {
    }

    /* renamed from: a */
    public void onMessageReceived(Wallet wallet, Peer peer, Message message) {
    }

    /* renamed from: a */
    public void onConnectStatusChange(Wallet wallet, Peer peer, Peer.PeerStatus peerStatus) {
    }

    /* renamed from: a */
    public void mo42774a(Wallet izVar, PeerManager.PeerManagerStatus dVar) {
    }

    /* renamed from: a */
    public void onWalletResync(Wallet wallet, boolean z) {
    }

    /* renamed from: a */
    public void onMainPeerChange(PeerManager peerManager, Peer peer) {
    }

    /* renamed from: a_ */
    public void mo43914a_() {
    }

    /* renamed from: b */
    public void mo42780b(Wallet izVar, Transaction dhVar) {
    }

    /* renamed from: b */
    public void mo42781b(Wallet izVar, List<Address> list) {
    }

    //mo42783c
    public void onTransactionsConfirmed(Wallet wallet, List<Transaction> list) {
    }

    /* renamed from: d */
    public void mo42785d(Wallet izVar, List<Transaction> list) {
    }
}
