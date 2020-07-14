package vcash.network;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import bitcoin.script.WitnessV0KeyHash;
import bitcoin.script.WitnessV0ScriptHash;
import generic.network.AddressInfo;
import generic.utils.AddressUtils;
import vdsMain.*;
import vdsMain.message.*;
import vdsMain.peer.Peer;
import vdsMain.peer.PeerFile;
import vdsMain.peer.PeerManager;
import vdsMain.peer.VPeer;
import vdsMain.transaction.CScriptID;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.util.*;

public class VPeerManager extends PeerManager {

    /* renamed from: r */
    private long f4721r = 0;

    //f4722s
    private Map<String, Integer> defaultRandomAddressMap = new HashMap();

    private native void getDefaultRandomSeed(List<AddressInfo> list, HashMap<String, Peer> hashMap, int i);

    public VPeerManager(@NonNull Wallet wallet) {
        super(wallet);
        ArrayList<AddressInfo> arrayList = new ArrayList<>();
        getDefaultRandomSeed(arrayList, new HashMap(), 9);
        for (AddressInfo keyHex : arrayList) {
            this.defaultRandomAddressMap.put(keyHex.getKeyHex(), Integer.valueOf(0));
        }
    }

    //mo40475a
    public boolean isDefaultRandomAdressMapContains(String str) {
        return this.defaultRandomAddressMap.containsKey(str);
    }

    /* renamed from: e */
    public synchronized void checkAndStartMainThread() {
        super.checkAndStartMainThread();
        notifyPeerManagerStart();
    }

    /* renamed from: b */
    public boolean mo40481b(String str) {
        VPeer vPeer = (VPeer) getMainPeer();
        if (vPeer == null) {
            return false;
        }
        try {
            vPeer.addMessageToLinkedList(new IsGroupAddrMessage(this.wallet, str));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: a */
    public boolean mo40476a(CTxDestination oVar) {
        VPeer vPeer = (VPeer) getMainPeer();
        if (vPeer == null) {
            return false;
        }
        try {
            vPeer.addMessageToLinkedList(new GetClueMessage(this.wallet, oVar));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //mo40471a
    public SendMsgResult checkAndGetPreCreateMessageResult(Peer peer, @NonNull CTxDestination des, @NonNull CTxDestination parentDes) {
        if (peer == null) {
            return sendPreCreateMessageAndGetResult(des, parentDes, (VPeer) getMainPeer());
        }
        Vector vector = new Vector(getNormalPeerVector());
        if (!vector.contains(peer)) {
            return sendPreCreateMessageAndGetResult(des, parentDes, (VPeer) getMainPeer());
        }
        int indexOf = vector.indexOf(peer) + 1;
        if (indexOf <= vector.size() - 1) {
            return sendPreCreateMessageAndGetResult(des, parentDes, (VPeer) vector.get(indexOf));
        }
        return sendPreCreateMessageAndGetResult(des, parentDes, (VPeer) getMainPeer());
    }

    @NonNull
    //m4809a
    private SendMsgResult sendPreCreateMessageAndGetResult(@NonNull CTxDestination des, @NonNull CTxDestination parentDes, VPeer vPeer) {
        if (vPeer == null) {
            return new SendMsgResult(false, null);
        }
        try {
            vPeer.sendPreCreateMessage(des, parentDes);
            return new SendMsgResult(true, vPeer);
        } catch (Exception e) {
            e.printStackTrace();
            return new SendMsgResult(false, vPeer);
        }
    }

    //mo40484r
    public boolean sendGetLastSeasonTopClueListMessage() {
        VPeer vPeer = (VPeer) getMainPeer();
        if (vPeer == null) {
            return false;
        }
        try {
            vPeer.sendGetLastSeasonTopClueListMessage();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendCallContractMessage() {
        VPeer vPeer = (VPeer) getMainPeer();
        if (vPeer == null) {
            return false;
        }
        try {
            vPeer.sendCallContractMessage();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: s */
    public boolean mo40485s() {
        VPeer vPeer = (VPeer) getMainPeer();
        if (vPeer == null) {
            return false;
        }
        try {
            vPeer.mo42710O();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: a */
    public boolean mo40477a(CTxDestination oVar, int i) {
        addMessageToAllNormalPeer((Message) new GetClueTopRecordMessage(this.wallet, oVar, i));
        return true;
    }

    //mo40486t
    //910 mo40514u
    public AddrUpdateMessage getNewAddrUpdateMessage() {
        return new AddrUpdateMessage(this.wallet, this.wallet.getSelfAddressModel().getAllCTxDestination());
    }

    /* renamed from: b */
    public boolean mo40480b(UInt256 uInt256) {
        try {
            sendMsgFromMainPeer((Message) new ContractTxResultMessage(this.wallet, uInt256.getHex()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* renamed from: a */
    public void mo40474a(List<Address> list) {
        super.mo40474a(list);
        Vector vector = new Vector(list.size());
        for (Address address : list) {
            CTxDestination u = address.getCTxDestination();
            vector.add(u);
            if (u instanceof CScriptID) {
                CPubkeyInterface t = address.getSelfPubKey();
                if (t != null && (t instanceof CMultisigPubkey)) {
                    vector.add(WitnessV0ScriptHash.m545a((CMultisigPubkey) t));
                }
            } else if (u instanceof CKeyID) {
                WitnessV0KeyHash witnessV0KeyHash = new WitnessV0KeyHash(u);
                vector.add(witnessV0KeyHash);
                vector.add(witnessV0KeyHash.mo9569d());
            }
        }
        addMessageToAllNormalPeer((Message) new AddrAddMessage(this.wallet, vector));
    }

    /* renamed from: b */
    public void mo40478b(List<Address> list) {
        mo44645n();
        Vector vector = new Vector(list.size());
        for (Address jjVar : list) {
            CTxDestination u = jjVar.getCTxDestination();
            vector.add(u);
            CTxDestination a = AddressUtils.m939a(u, jjVar);
            if (a != null && !this.wallet.isUsingDesAddressMapHasKey(a)) {
                vector.add(a);
            }
        }
        addMessageToAllNormalPeer((Message) new AddrDeleteMessage(this.wallet, vector));
    }

    //mo40487u
    public void sendGetVCountMessage() {
        try {
            sendMsgFromMainPeer((Message) new GetVCountMessage(this.wallet));
        } catch (Exception e) {
            e.printStackTrace();
            Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Failed to send get vcount message %s", new Object[]{StringToolkit.m11523a((Throwable) e)}));
        }
    }

    //mo40479b
    public void addGetAddressMsgAndInitNormalAddressInfoQueue(boolean z) {
        ChainParams chainParams = this.wallet.getChainParams();
        if (this.testAddressInfoBlockQueue.isEmpty() && System.currentTimeMillis() - this.f4721r > 30000) {
            this.f4721r = System.currentTimeMillis();
            addMessageToAllNormalPeer((Message) new GetAddrMessage(this.wallet));
        }
        if (chainParams.getSelfNetworkType() != NETWORK_TYPE.MAIN) {
            return;
        }
        if (this.normalAddressInfoBlockQueue.size() < 8 || z) {
            ArrayList<AddressInfo> arrayList = new ArrayList<>();
            getDefaultRandomSeed(arrayList, new HashMap(), 8);
            for (AddressInfo addressInfo : arrayList) {
                if (!this.normalAddressInfoBlockQueue.contains(addressInfo) && !this.normalPeerHashMap.containsKey(addressInfo.getKeyHex())) {
                    this.normalAddressInfoBlockQueue.offer(addressInfo);
                }
            }
        }
    }

    /* renamed from: a */
    public void initAddressInfoQueueFromFile(String peerPath, String banPath) {
        this.peerFile = new PeerFile(peerPath);
        this.banPeerFile = new PeerFile(banPath);
        StringBuilder sb = new StringBuilder();
        sb.append(peerPath);
        sb.append("_custom");
        this.customPeerFile = new PeerFile(sb.toString());
        for (AddressInfo addressInfo : this.customPeerFile.getAddressInfoList()) {
            if (!this.backupAddressInfoQueue.contains(addressInfo)) {
                this.backupAddressInfoQueue.offer(addressInfo);
            }
        }
        int i = this.wallet.getChainParams().peerCount;
        for (AddressInfo addressInfo2 : this.peerFile.getAddressInfoList()) {
            if (!this.normalAddressInfoBlockQueue.contains(addressInfo2) && !this.banAddressInfoBlockQueue.contains(addressInfo2) && this.normalAddressInfoBlockQueue.size() < i * 4) {
                this.normalAddressInfoBlockQueue.offer(addressInfo2);
            }
        }
    }

    /* renamed from: c */
    public void checkSizeAndAddAddressInfo(AddressInfo addressInfo) {
        String addKeyHex=addressInfo.getKeyHex();
        if (!this.defaultRandomAddressMap.containsKey(addKeyHex)) {
            this.peerFile.addAddressInfo(addressInfo);
        }
    }

    /* renamed from: a */
    public void mo40472a(String str, AddressInfo addressInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_custom");
        this.customPeerFile = new PeerFile(sb.toString());
        this.backupPeerHashMap.clear();
        List<AddressInfo> a = this.customPeerFile.getAddressInfoList();
        if (a.contains(addressInfo)) {
            a.remove(addressInfo);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("_custom");
        FileUtils.isFileExist(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append("_custom");
        this.customPeerFile = new PeerFile(sb3.toString());
        for (AddressInfo addressInfo1 : a) {
            mo44636d(addressInfo1);
        }
    }
}

