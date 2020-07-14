package vdsMain.peer;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import bitcoin.CPubKey;
import bitcoin.UInt256;
import com.vc.libcommon.exception.AddressFormatException;
import crypto.CCrypto;
import generic.network.AddressInfo;
import vcash.network.VPeerManager;
import vdsMain.*;
import vdsMain.block.*;
import vdsMain.db.Db;
import vdsMain.message.*;
import vdsMain.model.AddressModel;
import vdsMain.model.BlockChainModel;
import vdsMain.model.VBlockChainModel;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.VWallet;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.*;
import java.util.Collection;

//bpl
public class VPeer extends Peer {

    //f12028w
    private static HashMap<String, String> messageTypeMap = new HashMap<>();

    /* renamed from: x */
    private static CPubKey f12029x = null;

    /* renamed from: y */
    private static byte[] f12030y = null;

    //910 f12166v
    private final String canonicalName1 = getClass().getCanonicalName();

    /* renamed from: z */
    private long f12032z = 0;

    //f12165B
    private boolean syncing = false;

    private Pair<UInt256, UInt256> f12167x;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public boolean checkPeerVersion(VersionMessageInterface versionMessageInterface) {
        return true;
    }

    public VPeer(Wallet wallet) {
        super(wallet);
        m10118P();
    }

    /* renamed from: P */
    private void m10118P() {
        if (f12030y == null) {
            try {
                f12029x = new CPubKey(Db.m9805b());
            } catch (AddressFormatException e) {
                e.printStackTrace();
            }
            f12030y = CCrypto.getInstance().Sign(this.wallet.getChainParams().labelResAndVersionName.getBytes());
        }
    }

    //mo42765H
    public boolean isSyncing() {
        return this.syncing;
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public CachedBlockCreator getCachedBlockCreator() {
        return new VCachedBlockCreator();
    }

    /* access modifiers changed from: protected */
    //mo42712a
    public Message getPingMessage(long j) {
        return new PingMessage(this.wallet);
    }

    /* access modifiers changed from: protected */
    //mo42714a
    public Message getNewPongMessage(Message message) {
        return new PongMessage(this.wallet, ((PingMessage) message).getCurTimeMills());
    }

    /* access modifiers changed from: protected */
    //mo42732a
    //910 mo42791a
    public void addRejectMessage(Message message, CValidationState atVar, UInt256 uInt256) {
        try {
            addMessageToLinkedList(new RejectMessage(message, atVar, uInt256));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    //mo42711a
    public Message getNewInvMessage() {
        return new InvMessage(this.wallet);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42726a(String str, int i, String str2) {
        Wallet wallet = this.wallet;
        StringBuilder sb = new StringBuilder();
        sb.append("Mobile client do not support command ");
        sb.append(str);
        try {
            addMessageToLinkedList(new RejectMessage(wallet, str, 64, sb.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    //mo42737b
    public void addVerackMessage() {
        try {
            addMessageToLinkedList(new VerackMessage(this.wallet));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //mo42715a
    public Message getVersionMessage(PeerInfo peerInfo) {
        long currentBlockNo = this.wallet.getCurrentBlockNo();
        ChainParams chainParams = this.wallet.getChainParams();
        Wallet wallet = this.wallet;
        chainParams.getClass();
        VersionMessage versionMessage = new VersionMessage(wallet, 209, 0, chainParams.labelResAndVersionName, peerInfo.mo44616g(), currentBlockNo, peerInfo.mo44610c(), peerInfo.getMainAddressInfo(), f12029x, f12030y);
        versionMessage.mo42596a(peerInfo.mo44618i());
        versionMessage.mo42599e().mo41279b(peerInfo.mo44618i());
        versionMessage.getAddressFrom().mo41279b(peerInfo.mo44612d());
        versionMessage.getAddressFrom().mo41276a(peerInfo.mo44614e());
        return versionMessage;
    }

    /* renamed from: J */
    public void mo42705J() throws IOException {
        if (this.f12032z < this.wallet.getCurrentBlockNo()) {
            this.f12032z = this.wallet.getCurrentBlockNo();
            if (this.peerInfo == null || this.peerInfo.getProtocalVersion() < 170004) {
                addMessageToLinkedList(new GetVibWaitQueneMessage(this.wallet));
            } else {
                addMessageToLinkedList(new GetVibWaitQueue2Message(this.wallet));
            }
            addMessageToLinkedList(new GetAdListMessage(this.wallet));
            addMessageToLinkedList(new GetBidListMessage(this.wallet));
        }
    }

    /* renamed from: i */
    public synchronized void checkAndReConnectingPeer() throws IOException {
        mo42706K();
        super.checkAndReConnectingPeer();
    }

    /* access modifiers changed from: protected */
    /* renamed from: K */
    public void mo42706K() {
        synchronized (messageTypeMap) {
            if (messageTypeMap.isEmpty()) {
                messageTypeMap.put("clprecreate", "");
                messageTypeMap.put("topclist", "");
                messageTypeMap.put("cl", "");
                messageTypeMap.put("clrk", "");
                messageTypeMap.put("luckynodes", "");
                messageTypeMap.put("crtgroupack", "");
                messageTypeMap.put("stbinfoack", "");
                messageTypeMap.put("addmemberack", "");
                messageTypeMap.put("refmemberack", "");
                messageTypeMap.put("rmvmemberack", "");
                messageTypeMap.put("incmaxack", "");
                messageTypeMap.put("psnaddgrack", "");
                messageTypeMap.put("psncancelack", "");
                messageTypeMap.put("psnexitgack", "");
                messageTypeMap.put("setgnameack", "");
                messageTypeMap.put("setgannack", "");
                messageTypeMap.put("setgruleack", "");
                messageTypeMap.put("setotcpack", "");
                messageTypeMap.put("vcount", "");
                messageTypeMap.put("clurtoprec", "");
                messageTypeMap.put("setgfeeack", "");
                messageTypeMap.put("setgfotcpack", "");
                messageTypeMap.put("setotccack", "");
                messageTypeMap.put("setcack", "");
                messageTypeMap.put("setotcack", "");
                messageTypeMap.put("isgpaddrack", "");
                messageTypeMap.put("createotcack", "");
                messageTypeMap.put("pandfack", "");
                messageTypeMap.put("payfeeack", "");
                messageTypeMap.put("approveack", "");
                messageTypeMap.put("judgeack", "");
                messageTypeMap.put("cancelack", "");
                messageTypeMap.put("otcstatusack", "");
                messageTypeMap.put("iqconactack", "");
                messageTypeMap.put("serviceport", "");
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public void processVersionMessage(Message message) {
        VersionMessage versionMessage = (VersionMessage) message;
        if (versionMessage.getHeight() <= this.wallet.getCurrentBlockNo() - 10) {
            checkAndDisconnectPeer(PeerErrorType.ERR_BLOCK_HEIGHT);
        } else if (this.peerManager.isDefaultRandomAdressMapContains(getAddressInfoKeyHex()) || message.getSelfProtocalVersion() >= 170008) {
            super.processVersionMessage(message);
            this.peerInfo.mo44606a(versionMessage.getPubKey().mo9455n());
        } else {
            checkAndDisconnectPeer(PeerErrorType.IllegalProtocol);
        }
    }


    //mo42731a
    //910 mo42790a
    //915 mo43026a
    public void processHeaderMessage(HeaderMessageInterface headerMessageInterface) throws IOException {
        this.lastProcessHeadsMessageTime = System.currentTimeMillis();
        String str = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" recv headers size:");
        sb.append(headerMessageInterface.getBlockHeaderVectorSize());
        Log.info(str, sb.toString());
        this.peerManager.checkAndAddToNormalAddressInfoQueue(getPeerInfo().getMainAddressInfo());
        this.wallet.startThreadLock();
        if(this.peerManager.hasSyncingPeer()){
            if (!this.syncing) {
                this.cachedBlockList.clear();
            }
            this.wallet.endThreadLock();
            return;
        }
        this.wallet.endThreadLock();
        boolean needWrite = false;
        if (headerMessageInterface.getBlockHeaderVectorSize() != 0) {
            headerMessageInterface.logHeadersInfo();
            CValidationState cValidationState = new CValidationState();
            UInt256 uInt256 = new UInt256();
            if (headerMessageInterface.isRejectMessage(cValidationState, uInt256)) {
                addRejectMessage((Message) headerMessageInterface, cValidationState, uInt256);
                String str2 = this.canonicalName1;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getPeerInfoHostAddress());
                sb2.append(String.format(Locale.getDefault(), " The header (%s) message from peer %s contains illegal blockchain relationship, abort this peer", new Object[]{uInt256, getPeerInfo().getMainAddressInfo().toString()}));
                Log.logWarning(str2, sb2.toString());
                return;
            }
            List<BlockHeader> blockHeaderList = headerMessageInterface.getBlockHeaderVector();
            boolean isLess2000 = blockHeaderList.size() < 2000;
            needWrite = isLess2000;
            this.wallet.startThreadLock();
            ArrayList<BlockHeader> unSynchedBlockHeaderList = new ArrayList<BlockHeader>(blockHeaderList.size());
            checkAndAddToUnSynchedBlockHeaderList(blockHeaderList, (List<BlockHeader>) unSynchedBlockHeaderList);
            if (!unSynchedBlockHeaderList.isEmpty()) {
                if (!this.cachedBlockList.mo43984a(this.wallet, (List<BlockHeader>) unSynchedBlockHeaderList)) {
                    String str3 = this.canonicalName1;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(getPeerInfoHostAddress());
                    sb3.append(String.format(Locale.getDefault(), " Received invalidate header message from %s to %s", new Object[]{((BlockHeader) blockHeaderList.get(0)).getBlockHash().toString(), ((BlockHeader) blockHeaderList.get(blockHeaderList.size() - 1)).getBlockHash().toString()}));
                    Log.LogObjError(str3, sb3.toString());
                    this.cachedBlockList.clear();
                    this.wallet.endThreadLock();
                    return;
                } else if (this.cachedBlockList.getCachedBlockInfoMapSize() >= 100000) {
                    needWrite = true;
                    //this.wallet.mo44189k();
                } else {
                    getBlockHashListAndAddGetHeaderMessage();
                }
            }
            this.wallet.endThreadLock();
        } else if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            needWrite = true;
        } else {
            sendNonymousBlockRequest();
        }
        if (needWrite && !this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            this.wallet.startThreadLock();
            if (this.peerManager.hasSyncingPeer()) {
                this.cachedBlockList.clear();
                this.wallet.endThreadLock();
                return;
            }
            this.syncing = true;
            this.wallet.endThreadLock();
            ArrayList<CTxDestination> desArr = new ArrayList<>();
            desArr.addAll(this.wallet.getSelfAddressModel().getAllCTxDestination());
            this.cachedBlockList.clearAndInitAddressTxsMapByDesList(desArr);
            CTxDestination cTxDestination = this.cachedBlockList.getFirstNullCTxDestination();
            if (cTxDestination != null) {
                GetAddrTxsMessage getAddrTxsMessage = new GetAddrTxsMessage(this.wallet);
                getAddrTxsMessage.setDes(cTxDestination);
                getAddrTxsMessage.setFirstBlockHash(this.cachedBlockList.getFirstCachedBlockInfoHash());
                getAddrTxsMessage.setLastBlockHash(this.cachedBlockList.getLastCachedBlockInfoHash());
                addMessageToLinkedList(getAddrTxsMessage);
            } else {
                GetBaseTxMessage getBaseTxMessage = this.cachedBlockList.getNewBaseTxMessage(this.wallet);
                if (getBaseTxMessage != null) {
                    addMessageToLinkedList(getBaseTxMessage);
                } else {
                    getBlockHashListAndAddGetHeaderMessage();
                }
            }
        }
    }

    //910 m10238Q
    private void sendNonymousBlockRequest() {
        if (this.f12167x == null && ((VBlockChainModel) this.wallet.getSelfBlockChainModel()).syncAymousTransaction()) {
            BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
            this.wallet.startThreadLock();
            this.f12167x = blockChainModel.getFirstUnsyncBlockHashToCurBlockHashPair();
            this.wallet.endThreadLock();
            if (this.f12167x != null) {
                String str = this.canonicalName1;
                StringBuilder sb = new StringBuilder();
                sb.append("send nonymous block request: ");
                sb.append(((UInt256) this.f12167x.key).toString().toLowerCase());
                sb.append(" to ");
                sb.append(((UInt256) this.f12167x.value).toString().toLowerCase());
                Log.info(str, sb.toString());
                GetAnonTxMessage getAnonTxMessage = new GetAnonTxMessage(this.wallet);
                getAnonTxMessage.initStartAndEndBlockHash((UInt256) this.f12167x.key, (UInt256) this.f12167x.value);
                try {
                    addMessageToLinkedList(getAnonTxMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* access modifiers changed from: protected */
    /* renamed from: L */
    public void mo42707L() throws IOException {
        int i = -1;
        int i2 = -1;
        for (CachedBlockInfo imVar : this.cachedBlockList.cachedBlockInfoList) {
            if (!imVar.checkSumTransactionCountAndLinkedMap()) {
                int d = imVar.blockHeader.getBlockNo();
                if (d != -1) {
                    if (i2 == -1) {
                        i2 = d;
                    } else if (d > i2) {
                        i = d;
                    }
                }
            }
        }
        if (i == -1) {
            i = i2;
        }
        String str = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" update unlinked blocks : ");
        sb.append(i2);
        sb.append(" , ");
        sb.append(i);
        Log.info(str, sb.toString());
        if (i != -1) {
            try {
                getPeerInfo().setHeight((long) i);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (((long) i) > this.wallet.getCurrentBlockNo()) {
            sendGetTxMessage(i2, i);
        }
    }


    public void processVerackMessage(Message message) throws IOException {
        if (this.peerInfo.getProtocalVersion() <= 170007) {
            addMessageToLinkedList(((VPeerManager) this.peerManager).getNewAddrUpdateMessage());
        }
        super.processVerackMessage(message);
        if (this.wallet.getSelfBlockChainModel().isNewestBlock()) {
            addMessageToLinkedList(new GetVibMessage(this.wallet));
        }
        if (TextUtils.isEmpty(this.peerInfo.getMainAddressInfo().mo19005c())) {
            this.peerManager.checkSizeAndAddAddressInfo(this.peerInfo.getMainAddressInfo());
        }
    }


    /* access modifiers changed from: protected */
    /* renamed from: H */
    public void mo42704H() {
        BlockChainModel C = this.wallet.getSelfBlockChainModel();
        if (this.merkleBlockFragments.isAllPreBlockInCache(this.wallet, this.cachedBlockList)) {
            for (Pair gsVar : this.merkleBlockFragments.getBlockHeaderToHashListPairList(this.wallet, this.cachedBlockList)) {
                this.cachedBlockList.mo43986a(C, (BlockHeader) gsVar.key, false);
            }
        }
        try {
            mo42707L();
        } catch (Exception e) {
            e.printStackTrace();
            checkAndDisconnectPeer(PeerErrorType.Exception);
        }
    }

    /* access modifiers changed from: protected */
    //mo42722a
    public void processSTX2Message(STX2Message stx2Message) throws IOException {
        int startHeight = stx2Message.startHeight;
        int endHeight = stx2Message.endHeight;
        if (this.wallet.getCurrentBlockNo() >= ((long) endHeight)) {
            this.cachedBlockList.clear();
            getBlockHashListAndAddGetHeaderMessage();
            return;
        }
        Locale locale = Locale.getDefault();
        String str = " process stx from %d to %d , count %d";
        Object[] objArr = new Object[3];
        objArr[0] = startHeight;
        objArr[1] = endHeight;
        objArr[2] =stx2Message.cMerkleTxBlockList == null ? 0 : stx2Message.cMerkleTxBlockList.size();
        StringBuffer stringBuffer = new StringBuffer(String.format(locale, str, objArr));
        HashMap hashMap = new HashMap(stx2Message.cMerkleTxBlockList != null ? stx2Message.cMerkleTxBlockList.size() : 0);
        if (stx2Message.cMerkleTxBlockList != null) {
            for (CMerkleTxBlockSample cMerkleTxBlockSample : stx2Message.cMerkleTxBlockList) {
                hashMap.put(cMerkleTxBlockSample.blockHash, cMerkleTxBlockSample);
            }
        }
        for (CachedBlockInfo cachedBlockInfo : this.cachedBlockList.cachedBlockInfoList) {
            VCachedBlockInfo vCachedBlockInfo = (VCachedBlockInfo) cachedBlockInfo;
            if (vCachedBlockInfo.blockHeader.getBlockNo() >= startHeight && vCachedBlockInfo.blockHeader.getBlockNo() <= endHeight) {
                CMerkleTxBlockSample merkleTxBlockSample = (CMerkleTxBlockSample) hashMap.get(vCachedBlockInfo.blockHeader.getBlockHash());
                if (merkleTxBlockSample != null) {
                    vCachedBlockInfo.mSaplingMerkleTree = merkleTxBlockSample.saplingMerkleTree;
                    cachedBlockInfo.setSumNumberAndAddtoLinkedMap(merkleTxBlockSample.transactionVector, true);
                } else {
                    cachedBlockInfo.setSumNumberAndAddtoLinkedMap(null, true);
                }
            }
        }
        String str2 = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(stringBuffer.toString());
        Log.info(str2, sb.toString());
        Pair pair = new Pair();
        if (!this.cachedBlockList.getTroubleAndUNSynchedBlockStartToEndHeight(pair, (List<CachedBlockInfo>[]) new List[0])) {
            sendGetTxMessage(((Integer) pair.key).intValue(), ((Integer) pair.value).intValue());
        } else if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            connectAndAddBlocks(this.cachedBlockList);
        }
    }

    //mo42727a
    public void addGetBlockDataMessage(Collection<UInt256> collection) throws IOException {
        String str = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" send get block data size:");
        sb.append(collection.size());
        Log.info(str, sb.toString());
        addMessageToLinkedList(GetDataMessage.getNewGetDataMessageByHashCollection(this.wallet, collection, 3));
    }

    //mo42728a
    public void addGetDataMessage(List<Inv> list) throws IOException {
        if (!list.isEmpty()) {
            addMessageToLinkedList(GetDataMessage.getNewGetDataMessage(this.wallet, list));
        }
    }

    //mo42729a
    //910 mo42789a
    public void addGetHeadersMessage(List<byte[]> list, byte[] bArr) throws IOException {
        addMessageToLinkedList(new GetHeadersMessage(this.wallet, list, bArr));
    }

    /* access modifiers changed from: protected */
    //mo42739c
    public Message getNewNotFoundMessage() {
        return new NotFoundMessage(this.wallet);
    }

    /* access modifiers changed from: protected */
    //mo42713a
    public Message getNewTxMessage(Transaction dhVar) {
        return new TxMessage(dhVar);
    }

    //mo42716a
    public void sendGetTxMessage(int i, int i2) throws IOException {
        GetTxMessage getTxMessage;
        String str = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" send get tx message: ");
        sb.append(i);
        sb.append(" , ");
        sb.append(i2);
        Log.info(str, sb.toString());
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        if (this.peerInfo == null || this.peerInfo.getProtocalVersion() < 170004) {
            long j = (long) i;
            GetTxMessage blo = new GetTxMessage(this.wallet, j, (long) i2, addressModel.getAllUsingTxDesMapAddress(), addressModel.getTxShadowMapMutiKeyList());
            getTxMessage = blo;
        } else {
            GetTx2Message bln = new GetTx2Message(this.wallet, (long) i, (long) i2, addressModel.getAllUsingTxDesMapAddress(), addressModel.getTxShadowMapMutiKeyList());
            getTxMessage = bln;
        }
        addMessageToLinkedList(getTxMessage);
    }

    //910 mo42797b
    public void mo42738b(List<UInt256> list) throws IOException {
        if (list != null && !list.isEmpty()) {
            this.wallet.getSelfTransactionModel().checkAndAddUnConfirmTxid(list);
            for (UInt256 txid : list) {
                addMessageToLinkedList(new GetTxDataMessage(this.wallet, txid));
            }
            addMessageToLinkedList(new GetTxsMessage(this.wallet, list));
        }
    }

    //910 m10239a
    private void processAddrTxsMessage(AddrTxsMessage addrTxsMessage) throws IOException {
        this.cachedBlockList.mo44045a(addrTxsMessage.getDes(), addrTxsMessage.mo42599b());
        CTxDestination firstNullCTxDestination = this.cachedBlockList.getFirstNullCTxDestination();
        //this.cachedBlockList.testFull();
        if (firstNullCTxDestination != null) {
            GetAddrTxsMessage getAddrTxsMessage = new GetAddrTxsMessage(this.wallet);
            getAddrTxsMessage.setDes(firstNullCTxDestination);
            getAddrTxsMessage.setFirstBlockHash(this.cachedBlockList.getFirstCachedBlockInfoHash());
            getAddrTxsMessage.setLastBlockHash(this.cachedBlockList.getLastCachedBlockInfoHash());
            addMessageToLinkedList(getAddrTxsMessage);
            return;
        }
        GetBaseTxMessage getBaseTxMessage = this.cachedBlockList.getNewBaseTxMessage(this.wallet);
        if (getBaseTxMessage != null) {
            addMessageToLinkedList(getBaseTxMessage);
        } else if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            this.wallet.startThreadLock();
            connectAndAddBlocks(this.cachedBlockList);
            this.wallet.endThreadLock();
            this.syncing = false;
            Log.LogObjError(this.canonicalName1, "----- syncing false -----");
            getBlockHashListAndAddGetHeaderMessage();
        }
    }

    //910 m10241a
    private void processBaseTxMessage(BaseTxMessage baseTxMessage) throws IOException {
        if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            CachedBlockInfo cachedBlockInfo = this.cachedBlockList.getCachedBlockInfoByBlockHash(baseTxMessage.getBlockHash());
            if (cachedBlockInfo != null) {
                cachedBlockInfo.setSumNumberAndAddtoLinkedMap(baseTxMessage.getTransactionList(), true);
                if (((long) cachedBlockInfo.blockHeader.getBlockNo()) - this.wallet.getCurrentBlockNo() >= 200) {
                    CachedBlockList cachedBlockList = this.cachedBlockList.getHasTransactionCachedBlockList();
                    if (!cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                        this.wallet.startThreadLock();
                        connectAndAddBlocks(cachedBlockList);
                        this.wallet.endThreadLock();
                    }
                }
                GetBaseTxMessage getBaseTxMessage = this.cachedBlockList.getNewBaseTxMessage(this.wallet);
                if (getBaseTxMessage != null) {
                    addMessageToLinkedList(getBaseTxMessage);
                } else if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                    this.wallet.startThreadLock();
                    connectAndAddBlocks(this.cachedBlockList);
                    this.wallet.endThreadLock();
                    this.syncing = false;
                    Log.LogObjError(this.canonicalName1, "----- syncing false -----");
                    getBlockHashListAndAddGetHeaderMessage();
                }
            }
        }
    }

    //910 mo42792a
    //mo42733a
    public void processOtherMessage(Message message, String str) throws IOException {
        char c;
        switch (str.hashCode()) {
            case -1396202219:
                if (str.equals("basetx")) {
                    c = 10;
                    break;
                }
            case -1147677474:
                if (str.equals("addrtxs")) {
                    c = 8;
                    break;
                }
            case -851341711:
                if (str.equals("anonytx")) {
                    c = 9;
                    break;
                }
            case 3107:
                if (str.equals("ad")) {
                    c = 4;
                    break;
                }
            case 3541211:
                if (str.equals("stx2")) {
                    c = 0;
                    break;
                }
            case 92899676:
                if (str.equals("alert")) {
                    c = 7;
                    break;
                }
            case 93732676:
                if (str.equals("bidls")) {
                    c = 5;
                    break;
                }
            case 98246137:
                if (str.equals("getad")) {
                    c = 6;
                    break;
                }
            case 451054909:
                if (str.equals("vibinfo")) {
                    c = 1;
                    break;
                }
            case 1105396105:
                if (str.equals("vibquene")) {
                    c = 2;
                    break;
                }
            case 1105396322:
                if (str.equals("vibqueue")) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                try {
                    processSTX2Message((STX2Message) message);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new IOException(e);
                }
            case 1:
                processVibMessage((VibMessage) message);
                return;
            case 2:
                mo42724a((VibWaitQueneMessage) message);
                return;
            case 3:
                mo42725a((VibWaitQueue2Message) message);
                return;
            case 4:
                //mo42718a((bkk) kwVar);
                return;
            case 5:
                //mo42720a((bkr) kwVar);
                return;
            case 6:
                //mo42721a((blb) kwVar);
                return;
            case 7:
                mo42719a((AlertMessage) message);
                return;
            case 8:
                processAddrTxsMessage((AddrTxsMessage) message);
                return;
//            case 9:
//                m10240a((blb) kwVar);
//                return;
            case 10:
                processBaseTxMessage((BaseTxMessage) message);
                return;
            default:
                if (message instanceof ServicePortMessage) {
                    ((ServicePortMessage) message).mo42588a((Peer) this);
                }
                if (messageTypeMap.containsKey(str)) {
                    this.peerManager.notifyMessageReceived((Peer) this, message);
                    return;
                }
                return;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42719a(AlertMessage alertMessage) {
        if (alertMessage.mo42554b((VChainParam) this.wallet.getChainParams())) {
        }
    }

    //mo42708M
    public void addGetServicePortMessage() throws IOException {
        addMessageToLinkedList(new GetServicePortMessage(this.wallet));
    }

    //mo42734a
    public void sendPreCreateMessage(@NonNull CTxDestination oVar, @NonNull CTxDestination oVar2) throws IOException {
        addMessageToLinkedList(new PreCreateMessage(this.wallet, oVar, oVar2));
    }

    //mo42709N
    public void sendGetLastSeasonTopClueListMessage() throws IOException {
        addMessageToLinkedList(new GetLastSeasonTopClueListMessage(this.wallet));
    }

    public void sendCallContractMessage() throws IOException {
        addMessageToLinkedList(new TestCallContractMessage(this.wallet));
    }

    /* renamed from: O */
    public void mo42710O() throws IOException {
        addMessageToLinkedList(new GetLuckyNodesMessage(this.wallet));
    }

    /* access modifiers changed from: protected */
    //mo42723a
    public void processVibMessage(VibMessage vibMessage) {
        ((VWallet) this.wallet).getSelfVibModel().addVibInfoToMap((Peer) this, vibMessage.getVibInfo());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42724a(VibWaitQueneMessage bmt) {
        ((VWallet) this.wallet).getSelfVibModel().mo42696a(bmt.mo42606a());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42725a(VibWaitQueue2Message bmu) {
        ((VWallet) this.wallet).getSelfVibModel().mo42696a(bmu.mo42607a());
    }

    //TODO 广告相关
    /* access modifiers changed from: protected */
    /* renamed from: a */
//    public void mo42720a(bkr bkr) {
//        bpc af = ((VWallet) this.wallet).mo42396af();
//        long R = this.wallet.mo44033R();
//        ArrayList<biz> arrayList = new ArrayList<>();
//        arrayList.add(bkr.mo42555a());
//        arrayList.addAll(bkr.mo42556b());
//        for (biz biz : arrayList) {
//            biw k = ((VWallet) this.wallet).mo42396af().mo42655k();
//            k.mo42444a((long) biz.mo42485a());
//            k.mo42449b(af.mo42647e(k.mo42453d()));
//            k.mo42445a(new UInt256((BaseBlob) biz.mo42486b()));
//            k.mo42451c(biz.mo42487c());
//            af.mo42635b(k);
//            if (((long) biz.mo42485a()) <= R && R < ((long) (biz.mo42485a() + af.mo42639c()))) {
//                bkk c = af.mo42641c(biz.mo42486b());
//                if (c != null) {
//                    this.wallet.mo44125z().mo44630b((Message) c);
//                }
//            }
//        }
//    }

    //TODO 广告相关
    /* access modifiers changed from: protected */
    /* renamed from: a */
//    public void mo42718a(bkk bkk) {
//        TransactionModel lfVar;
//        boolean z;
//        TransactionModel lfVar2;
//        bpc af = ((VWallet) this.wallet).mo42396af();
//        TransactionModel F = this.wallet.getSelfTransactionModel();
//        ArrayList<biy> arrayList = new ArrayList<>();
//        arrayList.addAll(bkk.mo42549a());
//        ArrayList arrayList2 = new ArrayList();
//        for (biy biy : arrayList) {
//            biw f = af.mo42650f((long) biy.mo42481b());
//            if (f == null) {
//                f = af.mo42655k();
//            }
//            f.mo42444a((long) biy.mo42481b());
//            f.mo42449b(af.mo42647e(f.mo42453d()));
//            f.mo42445a(new UInt256((BaseBlob) biy.mo42480a()));
//            f.mo42451c(biy.mo42484e());
//            af.mo42635b(f);
//            if (arrayList.indexOf(biy) == 0) {
//                af.mo42627a(f);
//            }
//            f.mo42446a(af.mo42621a(biy.mo42480a().hashString()));
//            String d = biy.mo42483d();
//            if (d == null || d.isEmpty()) {
//                lfVar = F;
//                bkk c = af.mo42641c(biy.mo42480a());
//                if (c != null) {
//                    this.wallet.mo44125z().mo44630b((Message) c);
//                }
//            } else {
//                biv l = af.mo42656l();
//                l.mo42426b(d);
//                UInt256 d2 = l.mo42428d();
//                biv a = af.mo42620a(d2);
//                int i = 0;
//                if (a == null) {
//                    l.mo42424a(false);
//                } else {
//                    l.mo42424a(a.mo42440k());
//                }
//                Transaction b = F.mo44504b(biy.mo42480a());
//                if (b == null) {
//                    arrayList2.add(biy.mo42480a());
//                } else {
//                    List d3 = b.mo43218d();
//                    if (d3 != null && !d3.isEmpty()) {
//                        Iterator it = d3.iterator();
//                        while (true) {
//                            if (!it.hasNext()) {
//                                z = false;
//                                break;
//                            }
//                            TxOut dnVar = (TxOut) it.next();
//                            if (dnVar.mo43286e() == 8 && (dnVar instanceof bqi) && d2.equals(((bqi) dnVar).mo42842p())) {
//                                z = true;
//                                break;
//                            }
//                        }
//                        if (z) {
//                            List<TxIn> e = b.mo43219e();
//                            HashSet hashSet = new HashSet();
//                            if (e != null && e.isEmpty()) {
//                                for (TxIn i2 : e) {
//                                    hashSet.add(i2.mo42838i());
//                                }
//                            }
//                            String[] strArr = af.f11997a;
//                            if (hashSet.isEmpty()) {
//                                l.mo42425a(false, new boolean[0]);
//                                lfVar = F;
//                            } else {
//                                Iterator it2 = hashSet.iterator();
//                                boolean z2 = false;
//                                while (it2.hasNext()) {
//                                    String str = (String) it2.next();
//                                    int length = strArr.length;
//                                    while (true) {
//                                        if (i >= length) {
//                                            lfVar2 = F;
//                                            break;
//                                        }
//                                        lfVar2 = F;
//                                        if (TextUtils.equals(strArr[i], str)) {
//                                            z2 = true;
//                                            break;
//                                        } else {
//                                            i++;
//                                            F = lfVar2;
//                                        }
//                                    }
//                                    F = lfVar2;
//                                    i = 0;
//                                }
//                                lfVar = F;
//                                l.mo42425a(z2, new boolean[0]);
//                            }
//                            af.mo42626a(l);
//                        } else {
//                            lfVar = F;
//                        }
//                        bix bix = new bix(this.wallet);
//                        bix.mo42470b(d2);
//                        bix.mo42466a(new UInt256((BaseBlob) biy.mo42480a()));
//                        bix.mo42465a(f.mo42453d());
//                        bix.mo42469b(biy.mo42484e());
//                        bix.mo42467a(biy.mo42482c());
//                        bix.mo42471c(0);
//                        af.mo42628a(bix);
//                    }
//                }
//            }
//            F = lfVar;
//        }
//        for (bix e2 : af.mo42657m()) {
//            Transaction b2 = this.wallet.mo44080b(e2.mo42473e());
//            if (b2 != null && b2.mo43271w()) {
//                bkk c2 = af.mo42641c(b2.mo43223h_());
//                if (c2 != null) {
//                    this.wallet.mo44125z().mo44630b((Message) c2);
//                }
//            }
//        }
//        mo42738b(arrayList2);
//        if (this.peerEvent != null) {
//            this.peerEvent.mo44594a();
//        }
//    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
//    public void mo42721a(blb blb) {
//        bkk c = ((VWallet) this.wallet).mo42396af().mo42641c(blb.mo42562a());
//        if (c != null) {
//            mo44578j(c);
//        }
//    }

    /* access modifiers changed from: protected */
    //mo42743h
    public void processAddrMessage(Message message) {
        AddrMessage addrMessage = (AddrMessage) message;
        ArrayList arrayList = new ArrayList();
        for (AddressInfo addressInfo : addrMessage.mo42551a()) {
            if (addressInfo.mo19006d()) {
                arrayList.add(addressInfo);
            }
        }
        String str = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" recv address size:");
        sb.append(arrayList.size());
        Log.LogErrorNoThrow(str, sb.toString());
        this.wallet.getSelfPeerManager().mo44633c((List<AddressInfo>) arrayList);
    }

    /* renamed from: a */
    public void mo42735a(boolean z) throws IOException {
        if (z) {
            sendFilterLoadMessage(true);
        }
        if (this.peerManager.getMainPeer() == this) {
            clearCacheBlockAndAdd1000FromDb();
            if (this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                getBlockHashListAndAddGetHeaderMessage();
            } else {
                sendGetTxMessage(this.cachedBlockList.getFirstBlockHeader().getBlockNo(), this.cachedBlockList.getLastIndexBlockHeader().getBlockNo());
            }
        } else {
            getBlockHashListAndAddGetHeaderMessage();
        }
    }


    //915 mo43038f
    public void processMerkleBlockMessage(Message message) throws IOException {
        String str = this.canonicalName1;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" ----- recv merkle block -----");
        Log.LogObjError(str, sb.toString());
        getBlockHashListAndAddGetHeaderMessage();
    }


    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo42717a(UInt256 uInt256) throws IOException {

    }

    /* access modifiers changed from: protected */
    /* renamed from: z */
    public void checkAndAddGetHeaderMessage() throws IOException {
        long currentTimeMillis = System.currentTimeMillis() - this.lastProcessHeadsMessageTime;
        if (this.lastProcessHeadsMessageTime > this.lastSendHeaderMessageTime && currentTimeMillis > ((long) this.f13241c)) {
            getBlockHashListAndAddGetHeaderMessage();
        }
    }

    public void addMessageToLinkedList(Message message) throws IOException {
        if ((message instanceof AddrUpdateMessage) || (message instanceof AddrAddMessage) || (message instanceof AddrDeleteMessage)) {
            VChainParam chainParams = (VChainParam) this.wallet.getChainParams();
            if (this.peerInfo.getProtocalVersion() > 170007) {
                return;
            }
        }
        super.addMessageToLinkedList(message);
    }
}

