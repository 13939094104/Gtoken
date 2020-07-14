package vdsMain.peer;

import bitcoin.UInt256;
import generic.exceptions.InvalidateProtocolException;
import generic.exceptions.UnSupportedProtocolMagicException;
import generic.network.AddressInfo;
import org.apache.commons.io.IOUtils;
import vdsMain.*;
import vdsMain.block.*;
import vdsMain.message.*;
import vdsMain.model.BlockChainModel;
import vdsMain.model.TransactionModel;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Peer {

    //f13230l
    protected static AtomicLong atomicLong = new AtomicLong(0);

    //f13231A
    private SocketChannel socketChannel = null;

    //f13232B
    private SelectionKey selectionKey = null;

    //f13233C
    private ByteBuffer byteBuffer;

    //f13234D
    private MessageHeader messageHeader = null;

    //f13235E
    private LinkedList<ByteBuffer> messageByteBufferLinkedList = new LinkedList<>();
    /* access modifiers changed from: private */

    //f13236F
    //910 f13375F
    public LinkedThread linkedThread = null;
    /* access modifiers changed from: private */

    //910 f13376G
    public HashMap<UInt256, Transaction> f13237G = new HashMap<>();
    /* access modifiers changed from: private */

    //910 f13377H
    public HashMap<UInt256, Transaction> f13238H = new HashMap<>();

    //f13239a
    protected int pingInterval = 40000;

    /* renamed from: b */
    protected int f13240b = 20000;

    /* renamed from: c */
    protected int f13241c = 120000;

    /* renamed from: d */
    protected int f13242d = 60000;

    /* renamed from: e */
    protected int f13243e = 20000;

    /* renamed from: f */
    protected int f13244f = 60000;

    //f13245g
    protected long lastLiveTime = 0;

    //f13246h
    protected long lastPongTimeMillis = 0;

    //f13247i
    protected long lastSendHeaderMessageTime = 0;

    //f13248j
    protected long lastProcessHeadsMessageTime = 0;

    //f13249k
    //910 f13388k
    protected long lastProcessMessageTimeMills = 0;

    //f13250m
    protected PeerEvent peerEvent;

    //f13251n
    //910 f13390n
    protected Wallet wallet;

    //f13252o
    //910 f13391o
    protected PeerInfo peerInfo;

    /* renamed from: p */
    protected boolean f13253p = false;

    //f13254q
    //910 f13393q
    protected PeerManager peerManager = null;

    //910 f13394r
    protected UInt256 lastBlockHash;

    //f13256s
    //f13395s
    protected CachedBlockList cachedBlockList;

    //f13257t
    //910 f13396t
    protected MerkleBlockFragments merkleBlockFragments = new MerkleBlockFragments();

    //f13258u
    protected CachedBlockCreator cachedBlockCreator;
    /* access modifiers changed from: private */

    //f13259v
    //910 f13398v
    public final String canonicalName = getClass().getCanonicalName();

    /* renamed from: w */
    private int f13260w = 0;

    //f13261x
    private long connectingStartTime = 0;

    //f13262y
    private PeerStatus peerStatus = PeerStatus.Disconnected;

    //f13263z
    private boolean isAvailable = false;

    //C3892c
    public interface PeerEvent {
        /* renamed from: a */
        void mo44594a();

        /* renamed from: a */
        void mo44595a(Peer lhVar);

        //mo44596a
        void onConnectStatusChange(Peer peer, PeerStatus aVar);
    }

    //C3890a
    public enum PeerStatus {
        Disconnected,
        Connecting,
        Connected,
        Update
    }

    /* renamed from: lh$b */
    //C3891b
    public enum PeerErrorType {
        NormalDisconnect,
        TimeOut,
        ConnectionRefused,
        ConnectionClosed,
        ReadWriteFailed,
        SPVClient,
        ErrorService,
        InvalidateProtocolVersion,
        IllegalProtocol,
        Exception,
        isReload,
        GetHeaderTimeOut,
        MORE_ERROR_TX,
        SEED_PEER,
        CONNECT_RESET,
        ERR_BLOCK_HEIGHT
    }

    //C3893d
    public class LinkedThread extends Thread {

        //f13287b
        //910 f13426b
        private int errorTimes = 0;

        protected LinkedThread() {
        }

        public void run() {
            CachedBlockList cachedBlockList;
            ArrayList<CachedBlockInfo> arrayList = new ArrayList<>(2000);
            ArrayList<UInt256> arrayList2 = new ArrayList<>(20);
            boolean z = true;
            while (z) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Peer.this.linkedThread == this && Peer.this.getPeerStatus() != PeerStatus.Disconnected) {
                    if (!Peer.this.merkleBlockFragments.isHashToBlockHeaderHashPairMapEmpty() || !Peer.this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                        this.errorTimes = 0;
                    } else {
                        this.errorTimes++;
                        try {
                            Thread.sleep(8000);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (!Peer.this.merkleBlockFragments.isHashToBlockHeaderHashPairMapEmpty()) {
                        Peer.this.mo42704H();
                    }
                    if (!Peer.this.cachedBlockList.getTroubleAndUNSynchedBlockStartToEndHeight(null, (List<CachedBlockInfo>[]) new List[]{arrayList}) || Peer.this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                        String b = Peer.this.canonicalName;
                        StringBuilder sb = new StringBuilder();
                        sb.append(Peer.this.getPeerInfoHostAddress());
                        sb.append(" block:");
                        sb.append(arrayList.size());
                        sb.append(" error tx:");
                        sb.append(Peer.this.cachedBlockList.getSumErrorTxNumber());
                        Log.LogObjError(b, sb.toString());
                        for (CachedBlockInfo cachedBlockInfo : arrayList) {
                            cachedBlockInfo.mo43973a((List<UInt256>) arrayList2);
                            if (!arrayList2.isEmpty()) {
                                ArrayList arrayList3 = new ArrayList();
                                for (UInt256 uInt256 : arrayList2) {
                                    if (Peer.this.f13237G.containsKey(uInt256)) {
                                        cachedBlockInfo.mo43972a(uInt256, (Transaction) Peer.this.f13237G.get(uInt256));
                                        Peer.this.f13237G.remove(uInt256);
                                    } else if (Peer.this.f13238H.containsKey(uInt256)) {
                                        cachedBlockInfo.mo43977b(uInt256);
                                        Peer.this.f13238H.remove(uInt256);
                                    } else {
                                        arrayList3.add(uInt256);
                                    }
                                }
                                if (arrayList3.size() > 0 && arrayList.size() < 50) {
                                    try {
                                        Peer.this.mo44566b((Collection<UInt256>) arrayList3);
                                    } catch (IOException e3) {
                                        e3.printStackTrace();
                                    }
                                }
                            }
                        }
                        cachedBlockList = null;
                    } else {
                        cachedBlockList = Peer.this.cachedBlockList.mo43989c();
                        Peer.this.cachedBlockList.clear();
                    }
                    if (cachedBlockList != null && !cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                        Peer.this.f13238H.clear();
                    }
                    if (cachedBlockList != null) {
                        try {
                            if (!cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                                String b2 = Peer.this.canonicalName;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(Peer.this.getPeerInfoHostAddress());
                                sb2.append(" error tx:");
                                sb2.append(cachedBlockList.getSumErrorTxNumber());
                                Log.LogObjError(b2, sb2.toString());
                                Peer.this.wallet.startThreadLock();
                                Peer.this.connectAndAddBlocks(cachedBlockList);
                                Peer.this.wallet.endThreadLock();
                            }
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            Log.LogObjError((Object) this, String.format(Locale.getDefault(), "error whe connect unlinked blocks %s", new Object[]{StringToolkit.m11523a((Throwable) e4)}));
                            Peer.this.linkedThread = null;
                            if (equals(Peer.this.peerManager.getMainPeer())) {
                                Peer.this.peerManager.checkMainPeerAndAddGetHeaderMessage();
                            }
                            Peer.this.checkAndDisconnectPeer(PeerErrorType.Exception);
                            z = false;
                        }
                    }
                } else {
                    return;
                }
            }
        }

    }

    //mo42765H
    public boolean isSyncing() {
        return false;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Message getNewInvMessage();

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Message getPingMessage(long j);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Message getNewTxMessage(Transaction dhVar);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Message getNewPongMessage(Message kwVar);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Message getVersionMessage(PeerInfo peerInfo);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void mo42726a(String str, int i, String str2);

    /* renamed from: a */
    public abstract void addGetBlockDataMessage(Collection<UInt256> collection) throws IOException;

    //mo42728a
    public abstract void addGetDataMessage(List<Inv> list) throws IOException;

    //910 mo42789a
    public abstract void addGetHeadersMessage(List<byte[]> list, byte[] bArr) throws IOException;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void addRejectMessage(Message kwVar, CValidationState atVar, UInt256 uInt256);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    //910 mo42792a
    public abstract void processOtherMessage(Message kwVar, String str) throws IOException;

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public abstract void addVerackMessage();

    //910 mo42797b
    public void mo42738b(List<UInt256> list) throws IOException {

    }

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public abstract Message getNewNotFoundMessage();

    /* access modifiers changed from: protected */
    /* renamed from: h */
    public void processAddrMessage(Message kwVar) {
    }

    public Peer(Wallet izVar) {
        this.wallet = izVar;
        this.peerInfo = new PeerInfo(atomicLong.addAndGet(1));
        this.byteBuffer = new ByteBuffer(65536);
        this.cachedBlockCreator = getCachedBlockCreator();
        this.cachedBlockList = new CachedBlockList(this.cachedBlockCreator);
    }

    //mo44570d
    public long getConnectingStartTime() {
        return this.connectingStartTime;
    }

    /* access modifiers changed from: protected */
    //mo42741e
    public CachedBlockCreator getCachedBlockCreator() {
        return new CachedBlockCreator();
    }

    //mo44563a
    public synchronized void setPeerEvent(PeerEvent peerEvent) {
        this.peerEvent = peerEvent;
    }

    //mo44564a
    public void setPeerManager(PeerManager peerManager) {
        this.peerManager = peerManager;
    }

    //mo44573f
    //910 mo44656f
    public final PeerInfo getPeerInfo() {
        return this.peerInfo;
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
                addGetBlockDataMessage(new Vector<UInt256>(this.cachedBlockList.getBlockHashSet()));
            }
        } else {
            this.lastBlockHash = null;
            getBlockHashListAndAddGetHeaderMessage();
        }
    }


    //mo44574g
    public void clearCacheBlockAndAdd1000FromDb() {
        this.cachedBlockList.clear();
        this.merkleBlockFragments.clear();
        this.cachedBlockList.setLastBlockHashAndAddToCachedBlockMap(this.wallet.getSelfBlockChainModel().getBlockHeaderFromCurBlockNoAndLimit1000(new int[0]));
    }

    //mo44576h
    //910 mo44659h
    public PeerStatus getPeerStatus() {
        return this.peerStatus;
    }

    //mo42744i
    public void checkAndReConnectingPeer() throws IOException {
        if (this.peerStatus == PeerStatus.Disconnected) {
            reConnectingPeer();
        }
    }
    //mo42705J
    private void reConnectingPeer() throws IOException {
        synchronized (this) {
            this.isAvailable = false;
            this.peerStatus = PeerStatus.Connecting;
            this.connectingStartTime = System.currentTimeMillis();
        }
        notifyConnectStatusChange();
        synchronized (this) {
            if (this.peerStatus == PeerStatus.Connecting) {
                Log.info("重连节点",this.peerInfo.mainAddressInfo.getInetAddress().getHostAddress());
                if (this.socketChannel == null) {
                    this.socketChannel = SocketChannel.open();
                    this.socketChannel.configureBlocking(false);
                }
                Selector selector = this.peerManager.getSelector();
                this.selectionKey = this.socketChannel.register(selector, 8);
                this.selectionKey.attach(this);
                this.socketChannel.connect(new InetSocketAddress("123.133.83.86", 6533));//新增1
                this.socketChannel.connect(new InetSocketAddress(this.peerInfo.mainAddressInfo.getInetAddress(), this.peerInfo.getMainAddressInfo().getPort()));
                selector.wakeup();
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: j */
    //mo44577j
    public void notifyConnectStatusChange() {
        PeerEvent peerEvent = this.peerEvent;
        if (peerEvent != null) {
            peerEvent.onConnectStatusChange(this, this.peerStatus);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: k */
    public void mo44579k() {
        PeerEvent peerEvent = this.peerEvent;
        if (peerEvent != null) {
            peerEvent.mo44595a(this);
        }
    }

    //mo44580l
    public boolean isConnect() {
        if (this.peerStatus == PeerStatus.Disconnected) {
            return false;
        }
        if (this.peerStatus == PeerStatus.Connecting || !this.isAvailable) {
            if (System.currentTimeMillis() - this.connectingStartTime > ((long) this.f13243e)) {
                return true;
            }
        } else if (getDelay() <= ((long) this.f13240b) && System.currentTimeMillis() - this.lastProcessMessageTimeMills <= ((long) this.f13244f)) {
            return false;
        } else {
            return true;
        }
        return false;
    }

    //mo44581m
    public void checkAndNormalDisconnectPeer() {
        checkAndDisconnectPeer(PeerErrorType.NormalDisconnect);
    }

    //mo44562a
    //910 mo44646a
    public void checkAndDisconnectPeer(PeerErrorType peerErrorType) {
        if (this.peerStatus != PeerStatus.Disconnected) {
            disconnectPeer(peerErrorType);
        }
    }

    /* access modifiers changed from: protected */
    //mo44568b
    public void disconnectPeer(PeerErrorType peerErrorType) {
        synchronized (this) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPeerInfoHostAddress());
            sb.append(" disconnected  , reason ");
            sb.append(peerErrorType);
            Log.infoObject((Object) this, sb.toString());
            this.isAvailable = false;
            if (this.socketChannel != null) {
                try {
                    this.socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socketChannel = null;
            }
            if (this.selectionKey != null) {
                this.selectionKey.cancel();
            }
            this.selectionKey = null;
            this.byteBuffer.reset();
            this.cachedBlockList.clear();
            this.lastBlockHash = null;
            this.messageHeader = null;
            this.peerStatus = PeerStatus.Disconnected;
            this.messageByteBufferLinkedList.clear();
            this.cachedBlockList.clear();
            this.f13237G.clear();
            this.f13238H.clear();
            this.connectingStartTime = 0;
            this.lastLiveTime = 0;
            this.lastPongTimeMillis = 0;
            this.f13260w++;
        }
        synchronized (this.merkleBlockFragments) {
            this.merkleBlockFragments.clear();
            this.linkedThread = null;
        }
        if (this.peerManager.getMainPeer() == this) {
            this.peerManager.checkMainPeerAndAddGetHeaderMessage();
        }
        notifyConnectStatusChange();
    }

    /* access modifiers changed from: 0000 */
    //mo44582n
    public void setConnectedAndReset() {
        synchronized (this) {
            this.byteBuffer.reset();
            this.cachedBlockList.clear();
            this.lastBlockHash = null;
            this.peerStatus = PeerStatus.Connected;
            this.lastLiveTime = 0;
            this.lastPongTimeMillis = 0;
            this.f13237G.clear();
            this.f13238H.clear();
            this.messageHeader = null;
            this.f13253p = true;
            this.f13260w = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Peer connected ");
        sb.append(toString());
        Log.m11473a((Object) this, sb.toString());
        notifyConnectStatusChange();
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: o */
    //910 mo44665o
    public int mo44583o() throws IOException, UnSupportedProtocolMagicException, InvalidateProtocolException {
        int length = this.byteBuffer.writeToBytesAndGetLength(this.socketChannel);
        if (length > 0) {
            mo42706K();
            this.byteBuffer.mo43634d();
        }
        return length;
    }

    /* renamed from: K */
    //910 mo42767L自来也
    private void mo42706K() throws IOException, UnSupportedProtocolMagicException, InvalidateProtocolException {
        if (this.messageHeader == null) {
            this.messageHeader = new MessageHeader();
        }
        while (this.peerStatus == PeerStatus.Connected && this.byteBuffer.availReadLength() > 0) {
            if (this.messageHeader.getIsInit() || this.messageHeader.initData(this.byteBuffer)) {
                int payloadLength = (int) this.messageHeader.getPayloadLength();
                if (payloadLength <= 0 || this.byteBuffer.availReadLength() >= payloadLength) {
                    long availReadLength = (long) this.byteBuffer.availReadLength();
                    if (this.messageHeader.getCommand().equals("block")) {
                        this.byteBuffer.addReadPos(payloadLength);
                        getBlockHashListAndAddGetHeaderMessage();
                    } else {
                        this.byteBuffer.computeMaxReadableAndMarketReadLen(payloadLength);
                        if (this.messageHeader.getCommand().equals("tx")) {
                            System.arraycopy(this.byteBuffer.getBytes(), this.byteBuffer.getReadPos(), new byte[payloadLength], 0, payloadLength);
                        }
                        Message message = this.wallet.getSelfWalletHelper().getSelfMessageFactory().getMessage(this.messageHeader, this.byteBuffer);
                        this.byteBuffer.computeMaxReadableAndMarketReadLen(-1);
                        this.messageHeader.clear();
                        if (message != null) {
                            processMessage(message);
                            long a3 = availReadLength - ((long) this.byteBuffer.availReadLength());
                            if (a3 > -1) {
                                long j = (long) payloadLength;
                                if (a3 < j) {
                                    int i = (int) (j - a3);
                                    this.byteBuffer.checkAndReadToBytes(new byte[i], 0, i);
                                }
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    //m13018k
    //910 m13166k
    //自来也发送节点
    private final void processMessage(Message message) throws IOException, UnSupportedProtocolMagicException, InvalidateProtocolException {
        this.lastProcessMessageTimeMills = System.currentTimeMillis();
        ChainParams chainParams = this.wallet.getChainParams();
        if (!message.getTypeString().equalsIgnoreCase("tx")) {
            String str = this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append(getPeerInfoHostAddress());
            sb.append(" process message: ");
            sb.append(message.getTypeString());
            sb.append(" : ");
            sb.append(message.getBytesLength());
            Log.LogDebug(str, sb.toString());
        }
        if (Arrays.equals(message.getMagicBytes(), chainParams.magicBytes)) {
            String typeString = message.getTypeString();
            if (typeString.equals("ping")) {
                addMessageToLinkedList(getNewPongMessage(message));
            } else if (typeString.equals("pong")) {
                this.lastPongTimeMillis = System.currentTimeMillis();
                String str2 = this.canonicalName;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getPeerInfoHostAddress());
                sb2.append(" delay:");
                sb2.append(getDelay());
                sb2.append("ms");
                Log.LogDebug(str2, sb2.toString());
            } else if (typeString.equals("version")) {
                processVersionMessage(message);
            } else if (typeString.equals("verack")) {
                processVerackMessage(message);
            } else if (typeString.equals("headers")) {
                processHeaderMessage((HeaderMessageInterface) message);
            } else if (typeString.equals("inv")) {
                processInvMessage(message);
            } else if (typeString.equals("tx")) {
                processTxMessage(message);
            } else if (typeString.equals("mempool")) {
                checkAndaddInvMessage();
            } else if (typeString.equals("addr")) {
                processAddrMessage(message);
            } else if (typeString.equals("getblocks")) {
                m13019l(message);
            } else if (typeString.equals("getheaders")) {
                m13019l(message);
            } else if (typeString.equals("getaddr")) {
                m13019l(message);
            } else if (typeString.equals("getdata")) {
                processGetDataMessage(message);
                //节点推送有新区块
            } else if (typeString.equals("merkleblock")) {
                processMerkleBlockMessage(message);
            } else if (typeString.equals("reject")) {
                mo44571d(message);
            } else if (typeString.equals("notfound")) {
                mo44572e(message);
            } else {
                processOtherMessage(message, typeString);
            }
        } else {
            String str3 = this.canonicalName;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(getPeerInfoHostAddress());
            sb3.append(" magic ");
            sb3.append(StringToolkit.bytesToString(message.getMagicBytes()));
            sb3.append(" for message ");
            sb3.append(message.getTypeString());
            sb3.append(" doesn't match ");
            sb3.append(StringToolkit.bytesToString(chainParams.magicBytes));
            Log.logWarning(str3, sb3.toString());
            throw new UnSupportedProtocolMagicException(StringToolkit.bytesToString(message.getMagicBytes()));
        }
    }

    //mo44584p
    public long getPeerInfoHeight() {
        return this.peerInfo.getHeight();
    }

    //910 mo44651b
    //mo44567b
    public void processTxMessageOld(Message message) {
        Transaction transaction = ((TxMessageInterface) message).getTransaction();
        UInt256 txId = transaction.getTxId();
        this.f13237G.put(txId, transaction);
        transaction.recomputeByTxs(null);
        boolean isRelatedLocal = transaction.isRelatedToLocalAddress();
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        if (isRelatedLocal) {
            if (!this.cachedBlockList.mo43982a(transaction) && !this.merkleBlockFragments.mo44550b(txId)) {
                if (transaction.getTimeStamp() == 0) {
                    transaction.setTimeStamp(System.currentTimeMillis() / 1000);
                }
                this.wallet.lock();
                transactionModel.processNewTransaction(transaction);
                this.wallet.unLock();
            }
        } else if (transactionModel.isRelayedTransactionConfirmed(transaction)) {
            this.wallet.lock();
            this.f13237G.remove(txId);
            this.wallet.unLock();
        } else {
            this.f13238H.put(transaction.getTxId(), transaction);
        }
    }

    //910 mo44651b
    //mo44567b
    public void processTxMessage(Message message) {
        Transaction transaction = ((TxMessageInterface) message).getTransaction();
        UInt256 txId = transaction.getTxId();
        this.f13237G.put(txId, transaction);
        transaction.recomputeByTxs(null);
        boolean isRelatedLocal = transaction.isRelatedToLocalAddress();
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        if (!isRelatedLocal) {
            this.wallet.startThreadLock();
            if (transaction.getTimeStamp() == 0) {
                Transaction localTransaction = transactionModel.getTransactionFromAllTransactionMap(txId);
                if (localTransaction == null || localTransaction.getTimeStamp() <= 0) {
                    transaction.updateTimeStamp(System.currentTimeMillis() / 1000);
                } else {
                    transaction.updateTimeStamp(localTransaction.getTimeStamp());
                }
            }
            if (transactionModel.isRelayedTransactionConfirmed(transaction)) {
                this.f13237G.remove(txId);
            } else {
                this.f13238H.put(transaction.getTxid(), transaction);
            }
            this.wallet.endThreadLock();
        } else if (!this.cachedBlockList.mo43982a(transaction) && !this.merkleBlockFragments.mo44550b(txId)) {
            this.wallet.startThreadLock();
            if (transaction.getTimeStamp() == 0) {
                Transaction localTransaction = transactionModel.getTransactionFromAllTransactionMap(txId);
                if (localTransaction == null || localTransaction.getTimeStamp() <= 0) {
                    transaction.updateTimeStamp(System.currentTimeMillis() / 1000);
                } else {
                    transaction.updateTimeStamp(localTransaction.getTimeStamp());
                }
            }
            transactionModel.processNewTransaction(transaction);
            this.wallet.endThreadLock();
        }
    }

    //m13014a
    //910 m13162a
    private void addAllGreaterBlocksToList(CachedBlockInfo cachedBlockInfo, Iterator<CachedBlockInfo> iterator, List<CachedBlockInfo> list, boolean checkGreatThanCurBlockNo) {
        BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
        int currentBlockNo = blockChainModel.getCurrentBlockNo();
        list.add(cachedBlockInfo);
        BlockHeader blockHeader = cachedBlockInfo.blockHeader;
        while (iterator.hasNext()) {
            CachedBlockInfo cachedBlockInfo1 = (CachedBlockInfo) iterator.next();
            cachedBlockInfo1.blockHeader.setBlockNo(blockHeader.getBlockNo() + 1);
            blockChainModel.copyFirstBlockHeaderToSecond(blockHeader, cachedBlockInfo1.blockHeader);
            if (cachedBlockInfo1.blockHeader.getBlockNo() > currentBlockNo || !checkGreatThanCurBlockNo) {
                list.add(cachedBlockInfo1);
            }
            blockHeader = cachedBlockInfo1.blockHeader;
        }
    }


    //mo44559a
    //910 mo44642a
    public void connectAndAddBlocks(CachedBlockList cachedBlockList) {
        UInt256 preBlockHash=null;
        String str = this.canonicalName;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" connect block:");
        sb.append(cachedBlockList.cachedBlockInfoList.size());
        sb.append(" ");
        sb.append(cachedBlockList.getFirstBlockHeader().getBlockNo());
        sb.append(" -> ");
        sb.append(cachedBlockList.getLastIndexBlockHeader().getBlockNo());
        sb.append(" tx:");
        sb.append(cachedBlockList.getSumTransactionNumber());
        sb.append(" error tx:");
        sb.append(cachedBlockList.getSumErrorTxNumber());
        Log.info(str, sb.toString());
        BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
        if (cachedBlockList.isBlockHashToCachedBlockMapEmpty() || cachedBlockList.getLastIndexBlockHeader().getBlockNo() <= blockChainModel.getCurrentBlockNo()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getPeerInfoHostAddress());
            sb2.append(" ----- has save -----");
            Log.LogObjError(this.canonicalName, sb2.toString());
            cachedBlockList.clear();
            return;
        }
        ArrayList<CachedBlockInfo> cachedBlockInfos = new ArrayList<CachedBlockInfo>(cachedBlockList.cachedBlockInfoList);
        int currentBlockNo = blockChainModel.getCurrentBlockNo();
        ArrayList<CachedBlockInfo> arrayList2 = new ArrayList<CachedBlockInfo>();
        ArrayList<CachedBlockInfo> addCachedBlockInfoList = new ArrayList<CachedBlockInfo>();
        Iterator<CachedBlockInfo> cachedBlockInfoIterator = cachedBlockInfos.iterator();
        while (true) {
            if (!cachedBlockInfoIterator.hasNext()) {
                break;
            }
            CachedBlockInfo cachedBlockInfo = (CachedBlockInfo) cachedBlockInfoIterator.next();
            BlockHeader preBlockHeader = blockChainModel.getBlockHeaderFromCachedAndDbByHash(cachedBlockInfo.blockHeader.getPreBlockHash());
            if (preBlockHeader == null) {
                addAllGreaterBlocksToList(cachedBlockInfo, cachedBlockInfoIterator, addCachedBlockInfoList, true);
                break;
            }
            blockChainModel.copyFirstBlockHeaderToSecond(preBlockHeader, cachedBlockInfo.blockHeader);
            cachedBlockInfo.blockHeader.setBlockNo(preBlockHeader.getBlockNo() + 1);
            cachedBlockInfo.blockHeader.getNChainWork();
            UInt256 blockHash = blockChainModel.getBlockHashFromDb((long) cachedBlockInfo.blockHeader.getBlockNo());
            if (blockHash == null || blockHash.isNull()) {
                addAllGreaterBlocksToList(cachedBlockInfo, cachedBlockInfoIterator, addCachedBlockInfoList, true);
            } else if (!blockHash.equals(cachedBlockInfo.blockHeader.getBlockHash())) {
                preBlockHash = preBlockHeader.getBlockHash();
                addAllGreaterBlocksToList(cachedBlockInfo, cachedBlockInfoIterator, arrayList2, false);
                break;
            } else if (cachedBlockInfo.blockHeader.getBlockNo() > currentBlockNo) {
                addCachedBlockInfoList.add(cachedBlockInfo);
            }
        }
        //TODO 先注释掉
        //preBlockHash = null;
        if (!cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            int lastCachedBlockNo = (cachedBlockList.cachedBlockInfoList.get(cachedBlockList.cachedBlockInfoList.size() - 1)).blockHeader.getBlockNo();
            this.lastBlockHash = cachedBlockList.lastBlockHash;
            long l_lastBlockNo = (long) lastCachedBlockNo;
            if (l_lastBlockNo >= this.peerInfo.getHeight()) {
                this.peerInfo.setHeight(l_lastBlockNo);
            }
            if (l_lastBlockNo > blockChainModel.getMaxBlockNo()) {
                blockChainModel.setMaxBlockNoNotForce(lastCachedBlockNo);
                this.peerManager.checkMainPeerAndAddGetHeaderMessage();
            }
        }
        cachedBlockList.clear();
        if (!addCachedBlockInfoList.isEmpty()) {
            try {
                blockChainModel.addBlockListToTable((List<CachedBlockInfo>) addCachedBlockInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (preBlockHash != null) {
            try {
                blockChainModel.mo42680c(preBlockHash);
                if (arrayList2.isEmpty() || blockChainModel.getCurBlockInfo().getBlockNo() + 1 == ((CachedBlockInfo) arrayList2.get(0)).blockHeader.getBlockNo()) {
                    blockChainModel.addBlockListToTable((List<CachedBlockInfo>) arrayList2);
                    this.peerManager.checkMainPeerAndAddGetHeaderMessage();
                } else {
                    this.cachedBlockList.clear();
                    this.merkleBlockFragments.clear();
                    getBlockHashListAndAddGetHeaderMessage();
                    return;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Failed to change block chain to forked chain %s\n%s", new Object[]{preBlockHash.toString(), StringToolkit.m11523a((Throwable) e2)}));
                checkAndDisconnectPeer(PeerErrorType.Exception);
                return;
            }
        }
        try {
            mo42717a(preBlockHash);
        } catch (Exception e3) {
            e3.printStackTrace();
            Log.LogObjError((Object) this, StringToolkit.m11523a((Throwable) e3));
            checkAndDisconnectPeer(PeerErrorType.Exception);
        }

    }


    //910 mo42777a
    public void mo42717a(UInt256 uInt256) throws IOException {
        BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
        if (uInt256 != null) {
            getBlockHashListAndAddGetHeaderMessage();
        } else if (blockChainModel.getNewestBlockHeader().getBlockNo() < blockChainModel.getBlockIndex().blockNo) {
            this.wallet.startThreadLock();
            clearCacheBlockAndAdd1000FromDb();
            this.wallet.endThreadLock();
            addGetBlockDataMessage((Collection<UInt256>) new Vector<UInt256>(this.cachedBlockList.getBlockHashSet()));
        } else {
            getBlockHashListAndAddGetHeaderMessage();
        }
    }

    /* access modifiers changed from: protected */
    //mo42740c
    //910 mo42800c
    public void processVersionMessage(Message message) {
        VersionMessageInterface versionMessage = (VersionMessageInterface) message;
        if (!checkPeerVersion(versionMessage)) {
            checkAndDisconnectPeer(PeerErrorType.ErrorService);
            this.peerManager.banPeer(this);
        }
        this.peerInfo.setProtocalVersion(versionMessage.getSelfProtocalVersion());
        this.peerInfo.setHeight(versionMessage.getHeight());
        this.peerInfo.setServiceInt(versionMessage.getServiceInt());
        this.peerInfo.setTime(versionMessage.getTime());
        this.peerInfo.setVersion(versionMessage.getSubVersion());
        AddressMessage addressFrom = versionMessage.getAddressFrom();
        this.peerInfo.mo44609b(new AddressInfo(addressFrom.mo41275a(), addressFrom.mo41278b()));
        this.peerInfo.setNonce(versionMessage.getNonce().longValue());
        mo44579k();
        TimeData.m121a(this.peerInfo.getMainAddressInfo().getInetAddress().getHostAddress(), versionMessage.getTime() - TimeData.getCurTime());
        addVerackMessage();
    }

    /* access modifiers changed from: protected */
    //mo42736a
    public boolean checkPeerVersion(VersionMessageInterface versionMessageInterface) {
        if (versionMessageInterface.getSubVersion().contains("Satoshi") && versionMessageInterface.mo42603i() && versionMessageInterface.getSelfProtocalVersion() >= 70012) {
            return true;
        }
        return false;
    }

    /* renamed from: l */
    private void m13019l(Message kwVar) {
        String j = kwVar.getTypeString();
        StringBuilder sb = new StringBuilder();
        sb.append("Mobile client do not support command ");
        sb.append(kwVar.getTypeString());
        mo42726a(j, 64, sb.toString());
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public void mo44571d(Message kwVar) {
        RejectMessageInterface messageInterface = (RejectMessageInterface) kwVar;
        Log.LogObjError((Object) this, messageInterface.mo42585d());
        if (this == this.wallet.getSelfPeerManager().getMainPeer() && messageInterface.mo42583a().equals("tx")) {
            byte[] e = messageInterface.mo42586e();
            if (e != null) {
                UInt256 uInt256 = new UInt256(e);
                if (this.wallet.getSelfTransactionModel().getTransactionFromAllTransactionMap(uInt256) != null) {
                    this.wallet.getSelfTransactionModel().mo44492a(uInt256, messageInterface);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public void mo44572e(Message message) {
        List<Inv> a = ((NotFoundMessageInteface) message).mo44403a();
        if (a != null && !a.isEmpty()) {
            String str = this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append(getPeerInfoHostAddress());
            sb.append(" Recv not found: ");
            sb.append(a.size());
            Log.LogDebug(str, sb.toString());
            for (Inv yVar : a) {
                String str2 = this.canonicalName;
                Locale locale = Locale.getDefault();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getPeerInfoHostAddress());
                sb2.append(" Not found inv --> %s : %d");
                Log.LogDebug(str2, String.format(locale, sb2.toString(), new Object[]{yVar.getHash().toString(), Long.valueOf(yVar.getType())}));
                if (yVar.getType() == 1 && !this.wallet.getSelfBlockChainModel().isNewestBlock()) {
                    this.f13238H.put(yVar.getHash(), null);
                    String str3 = this.canonicalName;
                    Locale locale2 = Locale.getDefault();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(getPeerInfoHostAddress());
                    sb3.append(" Tx %s not found in server");
                    Log.LogErrorNoThrow(str3, String.format(locale2, sb3.toString(), new Object[]{yVar.getHash().toString()}));
                }
            }
        }
    }

    //910 mo42802f
    //mo42742f
    //915 mo43038f
    public void processMerkleBlockMessage(Message kwVar) throws IOException {
        MerkelBlockMessageInterface kvVar = (MerkelBlockMessageInterface) kwVar;
        BlockHeader a = kvVar.mo42580a();
        if (!this.cachedBlockList.isLinkedMapHasKey(a.getBlockHash())) {
            String str = this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append(getPeerInfoHostAddress());
            sb.append(" recv merkle block not int cache, wallet height:");
            sb.append(this.wallet.getCurrentBlockNo());
            sb.append(" peer height:");
            sb.append(getPeerInfoHeight());
            Log.LogDebug(str, sb.toString());
            CValidationState atVar = new CValidationState();
            if (!a.checkBlockVaild(atVar, true)) {
                mo42726a(kwVar.getTypeString(), 16, atVar.mo41044b());
                return;
            } else if (Math.abs(this.wallet.getCurrentBlockNo() - getPeerInfoHeight()) > 10) {
                return;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getPeerInfoHostAddress());
        sb2.append(" process merkle block: ");
        sb2.append(a.getBlockHash().toString());
        sb2.append(" , ");
        sb2.append(kvVar.mo42581b() == null ? "0" : Integer.valueOf(kvVar.mo42581b().size()));
        StringBuffer stringBuffer = new StringBuffer(sb2.toString());
        List<UInt256> b = kvVar.mo42581b();
        if (b != null && !b.isEmpty()) {
            stringBuffer.append("\n[\n");
            for (UInt256 uInt256 : b) {
                stringBuffer.append(uInt256.toString());
                stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
            }
            stringBuffer.append("\n]");
        }
        if (kvVar.mo42581b().size() != 0) {
            Log.LogDebug(this.canonicalName, stringBuffer.toString());
        }
        mo42730a(a, kvVar.mo42581b());
    }

    //910 mo44658g
    //mo44575g
    public void processGetDataMessage(Message message) throws IOException {
        GetDataMessageInterface getDataMessageInterface = (GetDataMessageInterface) message;
        if (getDataMessageInterface.getInvVectorSize() < this.wallet.getChainParams().getMaxGetDataSize()) {
            NotFoundMessageInteface notFoundMessageInteface = (NotFoundMessageInteface) getNewNotFoundMessage();
            List<Inv> invList = getDataMessageInterface.getInvVector();
            if (!invList.isEmpty()) {
                ArrayList<Message> newTxMessageList = new ArrayList<>();
                for (Inv inv : invList) {
                    if (inv != null) {
                        long type = inv.getType();
                        if (type == 1) {
                            Transaction transaction = this.wallet.getTransactionFromAllMap(inv.getHash());
                            if (transaction == null) {
                                notFoundMessageInteface.mo44404a(inv);
                            } else {
                                newTxMessageList.add(getNewTxMessage(transaction));
                            }
                        } else if (type != 0) {
                            notFoundMessageInteface.mo44404a(inv);
                        }
                    }
                }
                if (!notFoundMessageInteface.mo44405b()) {
                    addMessageToLinkedList((Message) notFoundMessageInteface);
                }
                for (Message j : newTxMessageList) {
                    addMessageToLinkedList(j);
                }
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("message getdata size() ");
        sb.append(getDataMessageInterface.getInvVectorSize());
        throw new IOException(sb.toString());
    }

    //910 mo44667q
    //mo44585q
    public void checkAndaddInvMessage() throws IOException {
        addInvMessage();
    }

    /* access modifiers changed from: protected */
    //mo44586r
    public void addInvMessage() throws IOException {
        this.wallet.lock();
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        List<Transaction> transactionList = transactionModel.getUnConfirmedTransactionList();
        this.wallet.unLock();
        if (!transactionList.isEmpty()) {
            Message invMessage = getNewInvMessage();
            InvMessageInterface invMessageInterface = (InvMessageInterface) invMessage;
            for (Transaction transaction : transactionList) {
                if (!transactionModel.mo44532g(transaction.getTxId()) && !transaction.isDefaultHash()) {
                    invMessageInterface.addInv(new Inv(1, transaction.getTxId()));
                }
            }
            addMessageToLinkedList(invMessage);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: s */
    public void mo44587s() {
        if (this.f13253p) {
            this.f13253p = false;
            try {
                checkTransactionAndAddTxMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //mo42707L
    private void checkTransactionAndAddTxMessage() throws IOException {
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        List<Transaction> transactionList = transactionModel.getUnConfirmedTransactionList();
        if (!transactionList.isEmpty()) {
            for (Transaction transaction : transactionList) {
                if (!transaction.isCoinBaseTransaction() && !transaction.isDefaultHash() && !transaction.isNotConfirmed() && !transactionModel.mo44532g(transaction.getTxId())) {
                    addMessageToLinkedList(new TxMessage(transaction));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    //mo42745i
    //910 mo42805i
    public void processVerackMessage(Message message) throws IOException {
        this.connectingStartTime = System.currentTimeMillis();
        boolean z = this.isAvailable;
        this.isAvailable = true;
        if (!z) {
            sendFilterLoadMessage(true);
        }
        addPingMessage();
        Peer mainPeer = this.peerManager.getMainPeer();
        long height = getPeerInfoHeight();
        this.wallet.lock();
        long maxBlockNo = this.wallet.getMaxBlockNo();
        String str = this.canonicalName;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" height:");
        sb.append(height);
        sb.append(" main height:");
        sb.append(maxBlockNo);
        Log.LogErrorNoThrow(str, sb.toString());
        if (mainPeer == null && maxBlockNo < height) {
            this.wallet.getSelfBlockChainModel().setMaxBlockNoNotForce((int) height);
        }
        if (mainPeer != this && maxBlockNo < height) {
            this.wallet.getSelfBlockChainModel().setMaxBlockNoNotForce((int) height);
            this.peerManager.checkMainPeerAndSetMaxBlockNo(false);
        }
        if (!z) {
            mo42735a(false);
        }
        this.wallet.unLock();
        this.peerManager.checkAndAddToNormalAddressInfoQueue(getPeerInfo().getMainAddressInfo());
    }

    /* access modifiers changed from: protected */
    //mo42731a/
    //910 mo42790a
    //915 mo43026a
    public void processHeaderMessage(HeaderMessageInterface headerMessageInterface) throws InvalidateProtocolException, IOException {
        List<BlockHeader> unSynchedBlockHeaderList;
        ChainParams chainParams = this.wallet.getChainParams();
        headerMessageInterface.logHeadersInfo();
        this.lastProcessHeadsMessageTime = System.currentTimeMillis();
        String str = this.canonicalName;
        StringBuilder sb = new StringBuilder();
        sb.append(getPeerInfoHostAddress());
        sb.append(" recv headers size:");
        sb.append(headerMessageInterface.getBlockHeaderVectorSize());
        Log.info(str, sb.toString());
        int headersSize = headerMessageInterface.getBlockHeaderVectorSize();
       //chainParams.getClass();
        if (headersSize <= 2000) {
            CValidationState cValidationState = new CValidationState();
            UInt256 rejectTxid = new UInt256();
            if (headerMessageInterface.isRejectMessage(cValidationState, rejectTxid)) {
                addRejectMessage((Message) headerMessageInterface, cValidationState, rejectTxid);
                Locale locale = Locale.getDefault();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getPeerInfoHostAddress());
                sb2.append(" ");
                sb2.append(cValidationState.f8249c);
                Log.LogObjError((Object) this, String.format(locale, sb2.toString(), new Object[0]));
                checkAndDisconnectPeer(PeerErrorType.ErrorService);
                this.peerManager.banPeer(this);
                return;
            }
            int size = headerMessageInterface.getBlockHeaderVector().size();
            //chainParams.getClass();
            if (size == 2000) {
                this.wallet.lock();
                if (this.peerManager.getMainPeer() == null) {
                    this.peerManager.checkMainPeerAndAddGetHeaderMessage();
                }
                if (this.peerManager.getMainPeer() == this || this.wallet.getSelfBlockChainModel().isNewestBlock()) {
                    this.wallet.unLock();
                } else {
                    String str2 = this.canonicalName;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(getPeerInfoHostAddress());
                    sb3.append(" is not main,ignore headers");
                    Log.info(str2, sb3.toString());
                    this.wallet.unLock();
                    return;
                }
            }
            List blockHeaderList = headerMessageInterface.getBlockHeaderVector();
            if (blockHeaderList.isEmpty()) {
                this.wallet.lock();
                this.wallet.getSelfBlockChainModel().checkNewestBlockAndChangeStatus();
                if (this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                    clearCacheBlockAndAdd1000FromDb();
                }
                if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                    this.wallet.unLock();
                    addGetBlockDataMessage((Collection<UInt256>) new Vector<UInt256>(this.cachedBlockList.getBlockHashSet()));
                } else {
                    this.wallet.unLock();
                    mo44587s();
                }
                return;
            }
            this.wallet.lock();
            if (this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                unSynchedBlockHeaderList = new ArrayList<>(blockHeaderList.size());
                checkAndAddToUnSynchedBlockHeaderList(blockHeaderList, unSynchedBlockHeaderList);
            } else {
                unSynchedBlockHeaderList = blockHeaderList;
            }
            if (unSynchedBlockHeaderList.size() > 0) {
                String str3 = this.canonicalName;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(getPeerInfoHostAddress());
                sb4.append(" download headers ");
                sb4.append(((BlockHeader) unSynchedBlockHeaderList.get(0)).getBlockNo());
                sb4.append(" -> ");
                sb4.append(((BlockHeader) unSynchedBlockHeaderList.get(unSynchedBlockHeaderList.size() - 1)).getBlockNo());
                Log.info(str3, sb4.toString());
            }
            if (!this.cachedBlockList.mo43984a(this.wallet, unSynchedBlockHeaderList)) {
                this.wallet.unLock();
                addRejectMessage((Message) headerMessageInterface, cValidationState, rejectTxid);
                Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Received invalidate header message from %s to %s", new Object[]{((BlockHeader) blockHeaderList.get(0)).getBlockHash().toString(), ((BlockHeader) blockHeaderList.get(blockHeaderList.size() - 1)).getBlockHash().toString()}));
                this.peerManager.banPeer(this);
            } else if (this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
                this.wallet.unLock();
                getBlockHashListAndAddGetHeaderMessage();
            } else {
                this.wallet.unLock();
                Vector vector = new Vector(unSynchedBlockHeaderList.size());
                for (BlockHeader blockHeader : unSynchedBlockHeaderList) {
                    vector.add(blockHeader.getBlockHash());
                }
                addGetBlockDataMessage((Collection<UInt256>) vector);
            }
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Block count ");
            sb5.append(headersSize);
            sb5.append(" out of range ");
            chainParams.getClass();
            sb5.append(2000);
            sb5.append(", ignore this header message.");
            Log.logObjectWarning((Object) this, sb5.toString());
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Block header's count ");
            sb6.append(headersSize);
            sb6.append(" must smaller than ");
            chainParams.getClass();
            sb6.append(2000);
            throw new InvalidateProtocolException(sb6.toString());
        }
    }

    /* access modifiers changed from: protected */
    //mo44560a
    //mo44643a
    public void checkAndAddToUnSynchedBlockHeaderList(List<BlockHeader> list, List<BlockHeader> unSynchedBlockHeaderList) {
        BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
        Iterator<BlockHeader> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            BlockHeader blockHeader = (BlockHeader) it.next();
            if (!blockChainModel.isBlockSynched(blockHeader.getBlockHash())) {
                unSynchedBlockHeaderList.add(blockHeader);
                break;
            }
        }
        while (it.hasNext()) {
            unSynchedBlockHeaderList.add(it.next());
        }
    }

    //910 m13168m
    //m13020m
    private void processInvMessage(Message message) throws InvalidateProtocolException, IOException {
        ChainParams chainParams = this.wallet.getChainParams();
        List<Inv> invList = ((InvMessageInterface) message).getInvList();
        if (invList != null && !invList.isEmpty()) {
            int size = invList.size();
            //chainParams.getClass();
            if (size <= 50000) {
                StringBuffer stringBuffer = new StringBuffer("==== invs ===\n");
                for (Inv inv : invList) {
                    stringBuffer.append(inv.getType());
                    stringBuffer.append(" --> ");
                    stringBuffer.append(inv.getHash().toString());
                    stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
                }
                Log.m11473a((Object) this, stringBuffer.toString());
                ArrayList<Inv> invArrayList = new ArrayList<>();
                ArrayListMap<UInt256,Inv> receiveTransactionArrayMap = new ArrayListMap<>();
                for (Inv inv : invList) {
                    if (inv.getType() == 1) {
                        if (!receiveTransactionArrayMap.hasKey(inv.getHash())) {
                            receiveTransactionArrayMap.addKeyValueSynchronized(inv.getHash(), inv);
                        }
                    } else if (inv.getType() == 2 && !this.cachedBlockList.isLinkedMapHasKey(inv.getHash())) {
                        inv.setType(3);
                        invArrayList.add(inv);
                    }
                }
                if (this.wallet.getSelfBlockChainModel().isNewestBlock()) {
                    if (!receiveTransactionArrayMap.isValueListEmpty()) {
                        addGetDataMessage(receiveTransactionArrayMap.getValueList());
                    }
                    if (!invArrayList.isEmpty()) {
                        addGetDataMessage((List<Inv>) invArrayList);
                    }
                }
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Inv count ");
            sb.append(invList.size());
            sb.append(" int message out of range ");
            chainParams.getClass();
            sb.append(50000);
            throw new InvalidateProtocolException(sb.toString());
        }
    }

    //910 mo44652b
    //mo44569b
    public void sendFilterLoadMessage(boolean needLock) throws IOException {
        Message message;
        if (needLock) {
            message = (Message) this.peerManager.lockAndGetBloomFilter();
        } else {
            message = (Message) this.peerManager.checkAndGetBloomFilter();
        }
        message.setTypeString("filterload");
        addMessageToLinkedList(message);
    }

    /* renamed from: a */
    public void mo44561a(BloomFilterInterface bloomFilterInterface) throws IOException {
        Message message = (Message) bloomFilterInterface;
        message.setTypeString("filteradd");
        addMessageToLinkedList(message);
    }

    /* access modifiers changed from: protected */
    /* renamed from: t */
    public void mo44588t() throws IOException {
        Message kwVar = (Message) this.wallet.getSelfWalletHelper().getNewBloomFilter(0, 0.0d, BloomUpdate.UPDATE_ALL);
        kwVar.setTypeString("filterclear");
        addMessageToLinkedList(kwVar);
    }

    //mo44589u
    public boolean getIsAvailable() {
        return this.isAvailable;
    }

    //mo44590v
    public String getAddressInfoKeyHex() {
        AddressInfo addressInfo = this.peerInfo.getMainAddressInfo();
        return addressInfo != null ? addressInfo.getKeyHex() : "";
    }

    /* access modifiers changed from: protected */
    /* renamed from: w */
    public void mo44591w() {
        if (getPeerStatus() == PeerStatus.Connected) {
            synchronized (this) {
                try {
                    Iterator it = this.messageByteBufferLinkedList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ByteBuffer byteBuffer = (ByteBuffer) it.next();
                        byteBuffer.receiveData(this.socketChannel);
                        if (!byteBuffer.mo43638h()) {
                            mo42708M();
                            break;
                        }
                        it.remove();
                    }
                    if (this.messageByteBufferLinkedList.isEmpty() && this.selectionKey.isValid()) {
                        this.selectionKey.interestOps(this.selectionKey.interestOps() & -5);
                    }
                } catch (Exception e) {
                    this.messageByteBufferLinkedList.clear();
                    throw e;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    //mo44578j
    //910 mo42806j
    public void addMessageToLinkedList(Message message) throws IOException {
        if (getPeerStatus() == PeerStatus.Connected) {
            synchronized (this) {
                byte[] serialToStream = message.serialToStream();
                String str = this.canonicalName;
                StringBuilder sb = new StringBuilder();
                sb.append(getPeerInfoHostAddress());
                sb.append(" send message : ");
                sb.append(message.getTypeString());
                sb.append(" --> ");
                sb.append(getPeerInfoHeight());
                sb.append(":");
                sb.append(serialToStream.length);
                Log.LogDebug(str, sb.toString());
                this.messageByteBufferLinkedList.offer(ByteBuffer.getByteBufferByByteArr(serialToStream));
            }
            mo42708M();
        }
    }

    /* renamed from: M */
    private void mo42708M() {
        SelectionKey selectionKey = this.selectionKey;
        if (selectionKey != null && selectionKey.isValid() && this.peerStatus == PeerStatus.Connected) {
            SelectionKey selectionKey2 = this.selectionKey;
            selectionKey2.interestOps(selectionKey2.interestOps() | 4);
            this.selectionKey.selector().wakeup();
        }
    }

    /* access modifiers changed from: protected */
    //mo44592x
    public void addPingMessage() throws IOException {
        if (this.peerStatus == PeerStatus.Connected && this.isAvailable) {
            this.lastLiveTime = System.currentTimeMillis();
            addMessageToLinkedList(getPingMessage(this.lastLiveTime));
        }
    }

    /* access modifiers changed from: protected */
    //mo44593y
    public void checkAndAddPingMessage() throws IOException {
        if (System.currentTimeMillis() - this.lastLiveTime >= ((long) this.pingInterval)) {
            addPingMessage();
        }
    }

    /* access modifiers changed from: protected */
    //mo42746z
    public void checkAndAddGetHeaderMessage() throws IOException {
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.lastProcessHeadsMessageTime;
        long j2 = currentTimeMillis - j;
        if (j > this.lastSendHeaderMessageTime && j2 > ((long) this.f13241c) && this.isAvailable && this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            getBlockHashListAndAddGetHeaderMessage();
        }
    }

    //mo44551A
    public long getDelay() {
        long j = this.lastPongTimeMillis;
        long j2 = this.lastLiveTime;
        return j < j2 ? System.currentTimeMillis() - this.lastLiveTime : j - j2;
    }

    //mo44552B
    //910 mo44635B
    public void getBlockHashListAndAddGetHeaderMessage() throws IOException {
        List list=null;
        BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
        if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            list = blockChainModel.mo44496a(this.cachedBlockList.cachedBlockInfoList);
        } else {
            try {
                list = blockChainModel.getLastNumberBlockHashList();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        addGetHeadersMessage(list, (byte[]) null);
        if (!this.cachedBlockList.isBlockHashToCachedBlockMapEmpty()) {
            String str = this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append(getPeerInfoHostAddress());
            sb.append(" send get headers message, block height:");
            sb.append(this.cachedBlockList.getLastIndexBlockHeader().getBlockNo());
            Log.info(str, sb.toString());
        } else {
            String str2 = this.canonicalName;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getPeerInfoHostAddress());
            sb2.append(" send get headers message, block height:");
            sb2.append(blockChainModel.getCurrentBlockNo());
            Log.info(str2, sb2.toString());
        }
        this.lastSendHeaderMessageTime = System.currentTimeMillis();
    }

    //mo44553C
    public void addVersionMessage() throws IOException {
        addMessageToLinkedList(getVersionMessage(this.peerInfo));
    }

    //910 mo44650b
    public void mo44566b(Collection<UInt256> collection) throws IOException {
        ArrayList arrayList = new ArrayList();
        for (UInt256 yVar : collection) {
            arrayList.add(new Inv(1, yVar));
        }
        addGetDataMessage((List<Inv>) arrayList);
    }

    //mo44554D
    //910 mo44637D
    public String getPeerInfoHostAddress() {
        PeerInfo peerInfo = this.peerInfo;
        return (peerInfo == null || peerInfo.mainAddressInfo == null) ? "0.0.0.0" : this.peerInfo.mainAddressInfo.toString();
    }

    //mo44555E
    public String getPeerHostAddress() {
        PeerInfo peerInfo = this.peerInfo;
        if (peerInfo == null || peerInfo.mainAddressInfo == null) {
            return "0.0.0.0:0";
        }
        return String.format(Locale.getDefault(), "%s:%d", new Object[]{this.peerInfo.mainAddressInfo.getInetAddress(), Integer.valueOf(this.peerInfo.getMainAddressInfo().getPort())});
    }

    /* renamed from: b */
    public void mo44565b(UInt256 uInt256) throws IOException {
        Vector vector = new Vector(1);
        vector.add(uInt256);
        addGetBlockDataMessage((Collection<UInt256>) vector);
    }

    //910 mo44644a
    public void mo42730a(BlockHeader jtVar, List<UInt256> list) throws IOException {
        if (this.cachedBlockList.isLinkedMapHasKey(jtVar.getBlockHash())) {
            this.cachedBlockList.mo43985a(this.wallet, jtVar, list);
        } else {
            synchronized (this.merkleBlockFragments) {
                this.merkleBlockFragments.mo44544a(jtVar, list);
                UInt256 g = jtVar.getPreBlockHash();
                mo44566b((Collection<UInt256>) list);
                if (!g.isNull() && !this.merkleBlockFragments.mo44546a(g) && !this.cachedBlockList.isLinkedMapHasKey(g) && !this.wallet.isBlockHashInCacheOrDb(g, new boolean[0])) {
                    mo44565b(g);
                }
            }
        }
        checkAndStartLinkedTread();
    }

    //910 mo44639F
    //mo44556F
    public void checkAndStartLinkedTread() {
        if (this.linkedThread == null) {
            this.linkedThread = new LinkedThread();
            this.linkedThread.start();
        }
    }

    //mo44557G
    public boolean isEqualToPeerManagerPeer() {
        return this.peerManager.getMainPeer() == this;
    }

    /* access modifiers changed from: protected */
    //910 mo44640I
    public void mo42704H() {
        if (this.merkleBlockFragments.isAllPreBlockInCache(this.wallet, this.cachedBlockList)) {
            for (Pair pair : this.merkleBlockFragments.getBlockHeaderToHashListPairList(this.wallet, this.cachedBlockList)) {
                this.cachedBlockList.mo43985a(this.wallet, (BlockHeader) pair.key, (List) pair.value);
            }
        }
    }

    //mo44558I
    public boolean hasProcessHeadsMessage() {
        return this.lastProcessHeadsMessageTime != 0;
    }
}
