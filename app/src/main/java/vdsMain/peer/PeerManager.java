package vdsMain.peer;

import androidx.annotation.NonNull;
import bitcoin.CPubKey;
import bitcoin.UInt256;
import generic.network.AddressInfo;
import generic.utils.AddressUtils;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.message.Message;
import vdsMain.message.TxMessage;
import vdsMain.model.AddressModel;
import vdsMain.model.BlockChainModel;
import vdsMain.model.TransactionModel;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.TxOut;
import vdsMain.transaction.Utxo;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class PeerManager implements Peer.PeerEvent {
    protected final int f13301a = 5;

    /* renamed from: b */
    protected final int f13302b = 16;

    //f13303c
    protected PeerFile peerFile;

    //f13304d
    protected PeerFile banPeerFile;

    //f13305e
    protected PeerFile customPeerFile;

    //f13306f
    protected LinkedBlockingQueue<AddressInfo> banAddressInfoBlockQueue = new LinkedBlockingQueue<>();

    //f13307g
    protected LinkedBlockingQueue<AddressInfo> normalAddressInfoBlockQueue = new LinkedBlockingQueue<>(100);

    //f13308h
    protected LinkedBlockingQueue<AddressInfo> backupAddressInfoQueue = new LinkedBlockingQueue<>(100);

    //f13309i
    protected LinkedBlockingQueue<AddressInfo> testAddressInfoBlockQueue = new LinkedBlockingQueue<>(1500);

    //f13310j
    //910 f13449j
    protected ConcurrentHashMap<String, Peer> normalPeerHashMap = new ConcurrentHashMap<>();

    //f13311k
    protected ConcurrentHashMap<String, Peer> backupPeerHashMap = new ConcurrentHashMap<>();

    //f13312l
    protected LinkedBlockingQueue<Peer> connectPeerBlockQueue = new LinkedBlockingQueue<>();

    //f13313m
    protected BloomFilterInterface bloomFilter;

    //f13314n
    protected Wallet wallet;

    //f13315o
    protected Lock lock = new ReentrantLock();

    //f13316p
    protected PeerManagerEvent peerManagerEvent;

    //f13317q
    protected PeerManagerStatus peerManagerStatus = PeerManagerStatus.STOP;

    //f13318r
    public final String canonicalName = getClass().getCanonicalName();

    //f13319s
    public ConnectThread connectThread;

    //f13320t
    public PeerIOThread peerIOThread;


    //f13321u
    public Selector selector;

    //f13322v
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /* renamed from: w */
    private Timer timer;

    /* renamed from: x */
    private ExecutorService f13324x = Executors.newCachedThreadPool();

    /* renamed from: y */
    private boolean f13325y = false;

    //f13326z
    //910 f13465z
    private Peer mainPeer = null;

    //C3898d
    public enum PeerManagerStatus {
        START,
        STOP
    }

    //C3896b
    public interface PeerManagerEvent {
        //mo44068a
        void notifyMainPeerChange(PeerManager peerManager, Peer peer);

        //mo44069a
        void onMessageReceived(PeerManager peerManager, Peer peer, Message message);

        //mo44070a
        void onConnectStatusChange(PeerManager peerManager, Peer peer, Peer.PeerStatus peerStatus);

        //mo44071a
        void onPeerManagerStatusChange(PeerManager lkVar, PeerManagerStatus dVar);

        /* renamed from: a_ */
        void mo44077a_();
    }

    //m13184a
    public static /* synthetic */ int comparePeer(Peer peer, Peer peer1) {
        if (peer.getPeerInfoHeight() > peer1.getPeerInfoHeight()) {
            return 1;
        }
        if (peer.getPeerInfoHeight() < peer1.getPeerInfoHeight()) {
            return -1;
        }
        if (peer.getDelay() == 0 || peer.getDelay() < peer1.getDelay()) {
            return 1;
        }
        if (peer.getDelay() > peer1.getDelay()) {
            return -1;
        }
        return 0;
    }

    //C3895a
     class ConnectThread extends UserThread {

        /* renamed from: b */
        private boolean f13329b = true;

        public ConnectThread() {

        }

        /* renamed from: a */
        public void threadStartEvent() {
            Peer tempPeer;
            String canonicalName = PeerManager.this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append("Peer manager connect thread start:");
            sb.append(this);
            Log.LogErrorNoThrow(canonicalName, sb.toString());
            while (getAtomicBoolean()) {
                ArrayList<Peer> removePeerList = new ArrayList<>();
                ArrayList<Peer> arrayList2 = new ArrayList();
                ArrayList<Peer> arrayList3 = new ArrayList<>();
                ArrayList<Peer> connectPeerList = new ArrayList<>();
                ArrayList arrayList5 = new ArrayList();
                int connectAndHasHeightPeerNumber = 0;
                int i2 = 0;
                for (Map.Entry<String, Peer> value : PeerManager.this.normalPeerHashMap.entrySet()) {
                    Peer peer = (Peer) value.getValue();
                    if (peer.getPeerStatus() == Peer.PeerStatus.Disconnected) {
                        removePeerList.add(peer);
                    } else if (peer.getPeerStatus() == Peer.PeerStatus.Connected) {
                        if (PeerManager.this.isDefaultRandomAdressMapContains(peer.getAddressInfoKeyHex())) {
                            if (System.currentTimeMillis() - peer.getConnectingStartTime() > 60000 && peer.hasProcessHeadsMessage()) {
                                arrayList2.add(peer);
                            }
                        } else if (System.currentTimeMillis() - peer.getConnectingStartTime() > 60000 && peer.hasProcessHeadsMessage()) {
                            arrayList3.add(peer);
                            i2++;
                        }
                        if (peer.getPeerInfoHeight() > 0) {
                            connectAndHasHeightPeerNumber++;
                        }
                    }
                }
                for (Map.Entry value2 : PeerManager.this.backupPeerHashMap.entrySet()) {
                    Peer peer = (Peer) value2.getValue();
                    if (peer.getPeerStatus() == Peer.PeerStatus.Disconnected) {
                        connectPeerList.add(peer);
                    } else if (peer.getPeerStatus() == Peer.PeerStatus.Connected) {
                        if (PeerManager.this.isDefaultRandomAdressMapContains(peer.getAddressInfoKeyHex())) {
                            if (System.currentTimeMillis() - peer.getConnectingStartTime() > 60000) {
                                arrayList5.add(peer);
                            }
                        } else if (System.currentTimeMillis() - peer.getConnectingStartTime() > 60000) {
                            arrayList5.add(peer);
                        }
                        int i3 = (peer.getPeerInfoHeight() > 0 ? 1 : (peer.getPeerInfoHeight() == 0 ? 0 : -1));
                    }
                }
                if (i2 > 2) {
                    removePeerList.addAll(arrayList2);
                    connectAndHasHeightPeerNumber -= arrayList2.size();
                }
                if (i2 > 4) {
                    Collections.sort(arrayList3, new Comparator<Peer>() {
                        @Override
                        public int compare(Peer o1, Peer o2) {
                            return PeerManager.comparePeer((Peer) o1, (Peer) o2);
                        }
                    });
                    Log.LogErrorNoThrow(PeerManager.this.canonicalName, "-----------------------------");
                    for (Peer peer : arrayList3) {
                        String b2 = PeerManager.this.canonicalName;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(peer.getPeerInfoHostAddress());
                        sb2.append(" height:");
                        sb2.append(peer.getPeerInfoHeight());
                        sb2.append(" delay:");
                        sb2.append(peer.getDelay());
                        Log.LogErrorNoThrow(b2, sb2.toString());
                    }
                    for (Peer peer : arrayList3) {
                        if (!peer.isEqualToPeerManagerPeer()) {
                            peer.checkAndDisconnectPeer(Peer.PeerErrorType.CONNECT_RESET);
                            removePeerList.add(peer);
                            i2--;
                            connectAndHasHeightPeerNumber--;
                            if (i2 <= 4) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                for (Peer peer : removePeerList) {
                    PeerManager.this.normalPeerHashMap.remove(peer.getAddressInfoKeyHex());
                }
                this.f13329b = false;
                PeerManager.this.connectToTestPeer(this.f13329b, connectAndHasHeightPeerNumber);

                for (Peer peer : connectPeerList) {
                    PeerManager.this.connectPeerBlockQueue.add(peer);
                }
                this.f13329b = false;
                if (!PeerManager.this.connectPeerBlockQueue.isEmpty()) {
                    while (PeerManager.this.peerManagerStatus == PeerManagerStatus.START) {
                        synchronized (PeerManager.this) {
                            tempPeer = (Peer) PeerManager.this.connectPeerBlockQueue.poll();
                        }
                        if (tempPeer != null) {
                            try {
                                String canonicalName1 = PeerManager.this.canonicalName;
                                StringBuilder sb5 = new StringBuilder();
//                                sb5.append("123.133.83.86");
                                sb5.append(tempPeer.getPeerInfoHostAddress());
                                sb5.append(" connectting to peer");
                                Log.info(canonicalName1, sb5.toString()+"自来也");
                                PeerManager.this.checkStartAndReconnectingPeer(tempPeer);
                            } catch (IOException unused) {
                                tempPeer.checkAndDisconnectPeer(Peer.PeerErrorType.ConnectionRefused);
                                return;
                            }
                        }
                        else {
                            break;
                        }
                    }
                }
                try {
                    Log.LogDebug(PeerManager.this.canonicalName, " ----- Peer manager connect thread sleep -----");
                    Thread.sleep(10000);
                } catch (InterruptedException unused2) {
                    return;
                }
            }
        }

        /* access modifiers changed from: private */
        /* renamed from: a */

        /* access modifiers changed from: protected */
        /* renamed from: b */
        public void threadEndEvent() {
            String b = PeerManager.this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append("peer connect thread ");
            sb.append(this);
            sb.append(" end...");
            Log.LogErrorNoThrow(b, sb.toString());
            synchronized (PeerManager.this) {
                if (PeerManager.this.connectThread == this) {
                    PeerManager.this.connectThread = null;
                }
            }
        }
    }

    //C3897c
    class PeerIOThread extends UserThread {
        public PeerIOThread() {

        }

        /* renamed from: a */
        public void threadStartEvent() {
            String b = PeerManager.this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append("peer io thread ");
            sb.append(this);
            sb.append(" start");
            Log.LogErrorNoThrow(b, sb.toString());
            while (getAtomicBoolean()) {
                if (PeerManager.this.normalPeerHashMap.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PeerManager.this.selector.select(500);
                        if (PeerManager.this.selector == null) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                        } else {
                            Iterator it = PeerManager.this.selector.selectedKeys().iterator();
                            while (it.hasNext() && getAtomicBoolean()) {
                                SelectionKey selectionKey = (SelectionKey) it.next();
                                it.remove();
                                PeerManager.this.checkSelectionKeyAndExcute(selectionKey);
                            }
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        PeerManager.this.disconnectAllPeerAndResetSelector();
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        /* renamed from: b */
        public void threadEndEvent() {
            String b = PeerManager.this.canonicalName;
            StringBuilder sb = new StringBuilder();
            sb.append("peer io thread ");
            sb.append(this);
            sb.append(" end...");
            Log.LogErrorNoThrow(b, sb.toString());
            synchronized (PeerManager.this) {
                if (PeerManager.this.peerIOThread == this) {
                    PeerManager.this.peerIOThread = null;
                }
            }
        }
    }



    /* renamed from: a */
    public void mo44595a(Peer lhVar) {
    }

    //always false
    public boolean isDefaultRandomAdressMapContains(String str) {
        return false;
    }

    public PeerManager(@NonNull Wallet wallet) {
        this.wallet = wallet;
        disconnectAllPeerAndResetSelector();
    }

    //mo44623a
    public synchronized void setPeerManagerEvent(PeerManagerEvent peerManagerEvent) {
        this.peerManagerEvent = peerManagerEvent;
    }

    //mo44627b
    public Selector getSelector() {
        return this.selector;
    }

    //mo44632c
    public Vector<Peer> getNormalPeerVector() {
        return new Vector<>(this.normalPeerHashMap.values());
    }

    //mo44635d
    public Vector<Peer> getBackupPeerVector() {
        return new Vector<>(this.backupPeerHashMap.values());
    }

    //mo40483e
    public void checkAndStartMainThread() {
        if (this.peerManagerStatus == PeerManagerStatus.START) {
            Log.info(this.canonicalName, "Peer manager was already started.");
        } else {
            checkAndStartThread();
        }
    }

    //mo44637f
    public void checkAndStopPeerManager() {
        if (this.peerManagerStatus == PeerManagerStatus.START) {
            stopPeerManager();
        }
    }

    /* access modifiers changed from: protected */
    //mo44638g
    public void notifyPeerManagerStart() {
        PeerManagerEvent peerManagerEvent = this.peerManagerEvent;
        if (peerManagerEvent != null) {
            peerManagerEvent.onPeerManagerStatusChange(this, this.peerManagerStatus);
        }
    }

    //mo40484r
    private void checkAndStartThread() {
        if (this.peerManagerStatus == PeerManagerStatus.START) {
            Log.info(this.canonicalName, "Peer manager was already started.");
            return;
        }
        synchronized (this) {
            this.peerManagerStatus = PeerManagerStatus.START;
            startConnectThread();
            startPeerIOThread();
        }
        notifyPeerManagerStart();
    }

    //mo44639h
    //910 mo44721h
    public Peer getMainPeer() {
        return this.mainPeer;
    }

    //mo44640i
    //910 mo44722i
    public void checkMainPeerAndAddGetHeaderMessage() {
        checkMainPeerAndSetMaxBlockNo(true);
    }

    //910 mo44706a
    public void checkMainPeerAndSetMaxBlockNo(boolean addGetHeaderMessage) {
        if (getPeerManagerStatus() == PeerManagerStatus.START) {
            BlockChainModel blockChainModel = this.wallet.getSelfBlockChainModel();
            long currentBlockNo = (long) blockChainModel.getCurrentBlockNo();
            Peer newestPeer = null;
            for (Peer peer : this.normalPeerHashMap.values()) {
                Log.info(peer.getPeerHostAddress(), String.valueOf(peer.getPeerInfoHeight()));
                if (peer.getPeerStatus() == Peer.PeerStatus.Connected && peer.getPeerInfoHeight() > currentBlockNo) {
                    currentBlockNo = peer.getPeerInfoHeight();
                    newestPeer = peer;
                }
            }
            for (Peer peer : this.backupPeerHashMap.values()) {
                if (peer.getPeerStatus() == Peer.PeerStatus.Connected && peer.getPeerInfoHeight() > currentBlockNo) {
                    currentBlockNo = peer.getPeerInfoHeight();
                    Log.info(peer.getPeerHostAddress(), String.valueOf(currentBlockNo));
                    newestPeer = peer;
                }
            }
            blockChainModel.setMaxBlockNo((int) currentBlockNo, true);
            if (newestPeer != null && !newestPeer.equals(this.mainPeer)) {
                try {
                    this.mainPeer = newestPeer;
                    String str = this.canonicalName;
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mainPeer.getPeerInfoHostAddress());
                    sb.append(" is main peer, height:");
                    sb.append(this.mainPeer.getPeerInfoHeight());
                    Log.info(str, sb.toString());
                    if (addGetHeaderMessage) {
                        newestPeer.addGetHeadersMessage(blockChainModel.getLastNumberBlockHashList(), (byte[]) null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                this.peerManagerEvent.notifyMainPeerChange(this, this.mainPeer);
            }
        }
    }

    //mo40485s
    private void startPeerIOThread() {
        PeerIOThread peerIOThread = this.peerIOThread;
        if (peerIOThread == null || !peerIOThread.getAtomicBoolean()) {
            this.peerIOThread = new PeerIOThread();
            this.peerIOThread.start();
            this.timer = new Timer();
            this.timer.schedule(new TimerTask() {
                public void run() {
                    PeerManager.this.checkPingMessageAndCheckMaxBlockNo();
                }
            }, 0, 5000);
        }
    }

    //mo40486t
    private void startConnectThread() {
        ConnectThread connectThread = this.connectThread;
        if (connectThread == null || !connectThread.getAtomicBoolean()) {
            this.connectThread = new ConnectThread();
            this.connectThread.start();
        }
    }

    //mo40487u
    private void stopPeerIOThread() {
        PeerIOThread peerIOThread = this.peerIOThread;
        if (peerIOThread != null) {
            peerIOThread.interrupt();
            this.peerIOThread.setAtomicBooleanFalse();
            this.peerIOThread = null;
        }
    }

    //m13139v
    private void stopConnectThread() {
        ConnectThread connectThread = this.connectThread;
        if (connectThread != null) {
            connectThread.interrupt();
            this.connectThread.setAtomicBooleanFalse();
            this.connectThread = null;
        }
    }

    /* access modifiers changed from: private */
    //m13140w
    public void checkPingMessageAndCheckMaxBlockNo() {
        if (this.peerManagerStatus == PeerManagerStatus.START) {
            Log.LogDebug(this.canonicalName, "check time out!");
            boolean z = false;
            Vector<Peer> normalPeerVector = new Vector(this.normalPeerHashMap.values());
            Iterator<Peer> it = new Vector(this.backupPeerHashMap.values()).iterator();
            while (it.hasNext()) {
                Peer peer = it.next();
                if (!peer.isConnect() || !this.wallet.getSelfBlockChainModel().isNewestBlock()) {
                    try {
                        peer.checkAndAddPingMessage();
                    } catch (Exception unused) {
                        peer.checkAndDisconnectPeer(Peer.PeerErrorType.ReadWriteFailed);
                    }
                } else {
                    String str = this.canonicalName;
                    StringBuilder sb = new StringBuilder();
                    sb.append(peer.getPeerInfoHostAddress());
                    sb.append(" time out!");
                    Log.info(str, sb.toString());
                    peer.checkAndDisconnectPeer(Peer.PeerErrorType.TimeOut);
                    z = true;
                }
            }
            Iterator normalPeerIterator = normalPeerVector.iterator();
            while (normalPeerIterator.hasNext()) {
                Peer peer = (Peer) normalPeerIterator.next();
                if (!peer.isConnect() || !this.wallet.getSelfBlockChainModel().isNewestBlock()) {
                    try {
                        peer.checkAndAddPingMessage();
                    } catch (Exception unused2) {
                        peer.checkAndDisconnectPeer(Peer.PeerErrorType.ReadWriteFailed);
                    }
                } else {
                    String str2 = this.canonicalName;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(peer.getPeerInfoHostAddress());
                    sb2.append(" time out!");
                    Log.info(str2, sb2.toString());
                    peer.checkAndDisconnectPeer(Peer.PeerErrorType.TimeOut);
                    z = true;
                }
            }
            Peer mainPeer = getMainPeer();
            Iterator it3 = normalPeerVector.iterator();
            while (it3.hasNext()) {
                Peer peer = (Peer) it3.next();
                if (peer.getPeerStatus() == Peer.PeerStatus.Connected) {
                    try {
                        peer.checkAndAddGetHeaderMessage();
                        z = true;
                    } catch (Exception unused3) {
                        peer.checkAndDisconnectPeer(Peer.PeerErrorType.ReadWriteFailed);
                    }
                }
            }
            if (mainPeer == null || mainPeer.getPeerInfoHeight() < this.wallet.getMaxBlockNo()) {
                checkMainPeerAndAddGetHeaderMessage();
            }
            if (z) {
                this.selector.wakeup();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0087, code lost:
        r0 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x008f, code lost:
        if (r0.hasNext() == false) goto L_0x009b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0091, code lost:
        ((p000.Peer) r0.next()).mo44581m();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009b, code lost:
        mo44638g();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009e, code lost:
        return;
     */
    //m13141x
    private void stopPeerManager() {
        this.atomicInteger.addAndGet(1);
        ArrayList<Peer> arrayList = new ArrayList<>();
        ArrayList<Peer> arrayList2 = new ArrayList<>();
        synchronized (this) {
            if (this.peerManagerStatus != PeerManagerStatus.STOP) {
                this.peerManagerStatus = PeerManagerStatus.STOP;
                if (this.timer != null) {
                    this.timer.cancel();
                    this.timer = null;
                }
                stopConnectThread();
                stopPeerIOThread();
                this.mainPeer = null;
                this.connectPeerBlockQueue.clear();
                arrayList.addAll(this.normalPeerHashMap.values());
                for (Peer peer : arrayList) {
                    this.normalAddressInfoBlockQueue.remove(peer.getAddressInfoKeyHex());
                }
                arrayList2.addAll(this.backupPeerHashMap.values());
                for (Peer peer : arrayList2) {
                    this.backupAddressInfoQueue.remove(peer.getAddressInfoKeyHex());
                }
                this.backupPeerHashMap.clear();
                this.normalPeerHashMap.clear();
            }
        }
    }

    /* renamed from: j */
    public void mo44641j() {
        stopPeerManager();
        checkAndStartThread();
    }

    /* access modifiers changed from: private */
    //m13131c
    public void checkStartAndReconnectingPeer(Peer peer) throws IOException {
        if (this.peerManagerStatus == PeerManagerStatus.START) {
            peer.checkAndReConnectingPeer();
        }
    }

    /* access modifiers changed from: private */
    //m13120a
    public void checkSelectionKeyAndExcute(SelectionKey selectionKey) {
        Peer peer = (Peer) selectionKey.attachment();
        if (peer != null) {
            if (this.peerManagerStatus == PeerManagerStatus.STOP) {
                peer.checkAndDisconnectPeer(Peer.PeerErrorType.NormalDisconnect);
                return;
            }
            try {
                if (!selectionKey.isValid()) {
                    peer.checkAndDisconnectPeer(Peer.PeerErrorType.ConnectionClosed);
                    this.selector.wakeup();
                } else if (selectionKey.isConnectable()) {
                    try {
                        if (((SocketChannel) selectionKey.channel()).finishConnect()) {
                            peer.setConnectedAndReset();
                            StringBuilder sb = new StringBuilder();
                            sb.append("Successfully connected to ");
                            sb.append(peer.getPeerHostAddress());
                            Log.LogObjError((Object) this, sb.toString());
                            selectionKey.interestOps(1).attach(peer);
                            peer.addVersionMessage();
                        }
                    } catch (IOException e) {
                        peer.checkAndDisconnectPeer(Peer.PeerErrorType.ConnectionRefused);
                        e.printStackTrace();
                    } catch (Exception e2) {
                        peer.checkAndDisconnectPeer(Peer.PeerErrorType.ConnectionRefused);
                        e2.printStackTrace();
                    }
                } else if (selectionKey.isReadable()) {
                    executeSelectionKey(selectionKey);
                } else if (selectionKey.isWritable()) {
                    m13130c(selectionKey);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    //m13128b
    private void executeSelectionKey(SelectionKey selectionKey) {
        Peer peer = (Peer) selectionKey.attachment();
        if (peer != null) {
            try {
                if (!selectionKey.isValid()) {
                    peer.checkAndDisconnectPeer(Peer.PeerErrorType.ConnectionClosed);
                    return;
                }
                if (selectionKey.isReadable()) {
                    int o = peer.mo44583o();
                    if (o != 0 && o == -1) {
                        peer.checkAndDisconnectPeer(Peer.PeerErrorType.ConnectionClosed);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                String str = this.canonicalName;
                StringBuilder sb = new StringBuilder();
                sb.append(peer.getPeerInfoHostAddress());
                sb.append(" read failed, connection closed");
                Log.LogError(str, sb.toString(), (Throwable) e);
                peer.checkAndDisconnectPeer(Peer.PeerErrorType.ReadWriteFailed);
            }
        }
    }

    /* renamed from: c */
    private void m13130c(SelectionKey selectionKey) {
        Peer lhVar = (Peer) selectionKey.attachment();
        try {
            lhVar.mo44591w();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("peer ");
            sb.append(lhVar);
            sb.append(" write error ");
            Log.LogObjError((Object) this, sb.toString(), (Throwable) e);
            lhVar.checkAndDisconnectPeer(Peer.PeerErrorType.ReadWriteFailed);
            this.selector.wakeup();
        }
    }

    //m13142y
    private void normalDisconnectAllPeerMap() {
        for (Peer peer : this.normalPeerHashMap.values()) {
            if (peer.getPeerStatus() != Peer.PeerStatus.Disconnected) {
                peer.checkAndNormalDisconnectPeer();
            }
        }
        for (Peer peer : this.backupPeerHashMap.values()) {
            if (peer.getPeerStatus() != Peer.PeerStatus.Disconnected) {
                peer.checkAndNormalDisconnectPeer();
            }
        }
    }

    /* access modifiers changed from: private */
    //m13143z
    public synchronized void disconnectAllPeerAndResetSelector() {
        try {
            normalDisconnectAllPeerMap();
            if (this.selector != null) {
                this.selector.close();
            }
            this.selector = null;
        } catch (Exception e) {
            e.printStackTrace();
            this.selector = null;
        }
        try {
            this.selector = SelectorProvider.provider().openSelector();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return;
    }

    //mo44642k
    public PeerManagerStatus getPeerManagerStatus() {
        return this.peerManagerStatus;
    }

    /* access modifiers changed from: private */
    //m13126a
    public void connectToTestPeer(boolean z, int i) {
        if (this.peerManagerStatus == PeerManagerStatus.START) {
            addGetAddressMsgAndInitNormalAddressInfoQueue(z);
            int peerCount = this.wallet.getChainParams().peerCount;
            if (!this.testAddressInfoBlockQueue.isEmpty() && this.normalAddressInfoBlockQueue.size() < 16) {
                String str = this.canonicalName;
                StringBuilder sb = new StringBuilder();
                sb.append("test peer size:");
                sb.append(this.testAddressInfoBlockQueue.size());
                Log.LogErrorNoThrow(str, sb.toString());
                int size = 8 - this.normalPeerHashMap.size();
                while (size > 0) {
                    AddressInfo addressInfo = (AddressInfo) this.testAddressInfoBlockQueue.poll();
                    if (addressInfo == null) {
                        break;
                    } else if (!this.normalPeerHashMap.containsKey(addressInfo.getKeyHex()) && !this.normalAddressInfoBlockQueue.contains(addressInfo)) {
                        String str2 = this.canonicalName;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(" test peer to connecting ");
                        sb2.append(addressInfo.toString());
                        Log.LogErrorNoThrow(str2, sb2.toString());
                        Peer a = getNewPeerFromAddressInfo(addressInfo);
                        this.connectPeerBlockQueue.offer(a);
                        this.normalPeerHashMap.put(addressInfo.getKeyHex(), a);
                        size--;
                    }
                }
            }
            int needCount = 0;
            int remindNeedPeerCount = this.normalPeerHashMap.size() >= peerCount ? i > 5 ? 0 : 5 - i : peerCount - this.normalPeerHashMap.size();
            while (true) {
                if (remindNeedPeerCount <= 0 && !z) {
                    break;
                }
                AddressInfo addressInfo2 = (AddressInfo) this.normalAddressInfoBlockQueue.poll();
                if (addressInfo2 == null) {
                    break;
                } else if (this.normalPeerHashMap.containsKey(addressInfo2.getKeyHex())) {
                    this.normalAddressInfoBlockQueue.offer(addressInfo2);
                    break;
                } else {
                    String str3 = this.canonicalName;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(" backup peer to connecting ");
                    sb3.append(addressInfo2.toString());
                    sb3.append(" seed:");
                    sb3.append(isDefaultRandomAdressMapContains(addressInfo2.getKeyHex()));
                    Log.LogErrorNoThrow(str3, sb3.toString());
                    Peer peer = getNewPeerFromAddressInfo(addressInfo2);
                    this.connectPeerBlockQueue.offer(peer);
                    this.normalPeerHashMap.put(addressInfo2.getKeyHex(), peer);
                    remindNeedPeerCount--;
                }
            }
            if (this.normalPeerHashMap.size() < peerCount) {
                needCount = peerCount - this.normalPeerHashMap.size();
            } else if (i <= 5) {
                needCount = 5 - i;
            }
            while (true) {
                if (needCount <= 0 && !z) {
                    break;
                }
                AddressInfo addressInfo3 = (AddressInfo) this.backupAddressInfoQueue.poll();
                if (addressInfo3 == null) {
                    break;
                } else if (this.backupPeerHashMap.containsKey(addressInfo3.getKeyHex())) {
                    this.backupAddressInfoQueue.offer(addressInfo3);
                    break;
                } else {
                    String str4 = this.canonicalName;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(" backup peer to connecting ");
                    sb4.append(addressInfo3.toString());
                    sb4.append(" seed:");
                    sb4.append(isDefaultRandomAdressMapContains(addressInfo3.getKeyHex()));
                    Log.LogErrorNoThrow(str4, sb4.toString());
                    Peer peer = getNewPeerFromAddressInfo(addressInfo3);
                    this.connectPeerBlockQueue.offer(peer);
                    this.backupPeerHashMap.put(addressInfo3.getKeyHex(), peer);
                    needCount--;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void addGetAddressMsgAndInitNormalAddressInfoQueue(boolean z) {
        if (this.normalAddressInfoBlockQueue.size() < 16) {
            ChainParams J = this.wallet.getChainParams();
            if (J.getSelfNetworkType() == NETWORK_TYPE.MAIN) {
                AddressInfo[] a = new BitcoinDNSFinder().mo43126a(J.mo42387m(), J.peerPort, null);
                if (a == null) {
                    Log.infoObject((Object) this, "DNS found failed, system will use backup dns list.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("dns founded: ");
                    sb.append(a.length);
                    Log.infoObject((Object) this, sb.toString());
                    synchronized (this) {
                        for (AddressInfo addressInfo : a) {
                            if (addressInfo.mo19006d() && !this.banAddressInfoBlockQueue.contains(addressInfo) && !this.normalAddressInfoBlockQueue.contains(addressInfo) && !this.normalPeerHashMap.containsKey(addressInfo.getKeyHex())) {
                                if (this.normalAddressInfoBlockQueue.size() >= 16) {
                                    break;
                                }
                                this.normalAddressInfoBlockQueue.offer(addressInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    //mo44620a
    public Peer getNewPeerFromAddressInfo(AddressInfo addressInfo) {
        Peer peer = this.wallet.getSelfWalletHelper().getNewPeer(this.wallet);
        peer.getPeerInfo().setMainAddressInfo(addressInfo);
        peer.setPeerManager(this);
        peer.setPeerEvent((Peer.PeerEvent) this);
        return peer;
    }

    //910 mo44725l
    //mo44643l
    public BloomFilterInterface checkAndGetBloomFilter() {
        if (this.bloomFilter == null) {
            this.bloomFilter = getFilterInterface();
        }
        return this.bloomFilter;
    }

    //mo44644m
    //910 mo44726m
    public BloomFilterInterface lockAndGetBloomFilter() {
        this.lock.lock();
        this.bloomFilter = getFilterInterface();
        this.lock.unlock();
        return this.bloomFilter;
    }

    //915 mo45037n
    public void mo44645n() {
        lockAndGetBloomFilter();
        for (Peer lhVar : this.normalPeerHashMap.values()) {
            try {
                if (lhVar.getPeerStatus() == Peer.PeerStatus.Connected) {
                    lhVar.mo44588t();
                    lhVar.sendFilterLoadMessage(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                lhVar.checkAndDisconnectPeer(Peer.PeerErrorType.Exception);
            }
        }
        for (Peer lhVar2 : this.backupPeerHashMap.values()) {
            try {
                if (lhVar2.getPeerStatus() == Peer.PeerStatus.Connected) {
                    lhVar2.mo44588t();
                    lhVar2.sendFilterLoadMessage(false);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                lhVar2.checkAndDisconnectPeer(Peer.PeerErrorType.Exception);
            }
        }
    }

    /* renamed from: a */
    public void mo40474a(List<Address> list) {
        this.lock.lock();
        this.bloomFilter = getFilterInterface();
        ArrayList<byte[]> arrayList = new ArrayList<>();
        for (Address jjVar : list) {
            CTxDestination u = jjVar.getCTxDestination();
            if (!jjVar.isRecycleFlag()) {
                if (AddressUtils.m944a(u, false)) {
                    byte[] data = u.data();
                    if (!this.bloomFilter.contains(data)) {
                        arrayList.add(data);
                    }
                }
            }
        }
        this.lock.unlock();
        BloomFilterInterface a = this.wallet.getSelfWalletHelper().getNewBloomFilter(arrayList.size(), 5.0E-7d, BloomUpdate.UPDATE_P2PUBKEY_ONLY);
        for (byte[] b : arrayList) {
            a.insert(b);
        }
        for (Peer a2 : this.normalPeerHashMap.values()) {
            try {
                a2.mo44561a(a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Peer a3 : this.backupPeerHashMap.values()) {
            try {
                a3.mo44561a(a);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    //mo44703a
    public void mo44621a(UInt256 uInt256) {
        Peer mainPeer = getMainPeer();
        if (mainPeer != null) {
            try {
                if (mainPeer.getIsAvailable()) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(uInt256);
                    mainPeer.mo42738b((List<UInt256>) arrayList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    //mo44626a
    public boolean sendMsgFromMainPeer(Message message) throws IOException {
        Peer mainPeer = getMainPeer();
        if (mainPeer == null || !mainPeer.getIsAvailable()) {
            Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Failed to send message %s, main peer is null or unacceptable", new Object[]{message.getTypeString()}));
            return false;
        }
        mainPeer.addMessageToLinkedList(message);
        return true;
    }

    /* renamed from: b */
    public void mo40478b(List<Address> list) {
        mo44645n();
    }

    /* renamed from: b */
    public void mo44630b(Message kwVar) throws IOException {
        Peer h = getMainPeer();
        if (h != null) {
            h.addMessageToLinkedList(kwVar);
        }
    }

    /* renamed from: a */
    private void m13125a(Utxo utxo, byte[] bArr) {
        if (bArr.length >= 36) {
            System.arraycopy(utxo.getTxOutTxid().data(), 0, bArr, 0, 32);
            Utils.uint32ToByteArrayLE((long) utxo.mo44695d(), bArr, 32);
            return;
        }
        throw new IllegalArgumentException(String.format(Locale.getDefault(), "Output leneght %d must bigger than 36", new Object[]{Integer.valueOf(bArr.length)}));
    }

    //mo44625a
    public boolean addTxMessage(Transaction transaction) {
        return addMessageToAllNormalPeer((Message) new TxMessage(transaction));
    }

    //mo44634c
    public boolean addMessageToAllNormalPeer(Message message) {
        boolean z = false;
        for (Peer peer : this.normalPeerHashMap.values()) {
            try {
                if (peer.getIsAvailable()) {
                    peer.addMessageToLinkedList(message);
                    z = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    //910 mo44728o
    //mo44646o
    public BloomFilterInterface getFilterInterface() {
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        TransactionModel transactionModel = this.wallet.getSelfTransactionModel();
        Collection<CTxDestination> localAddressDesCollection = addressModel.getAllCTxDestination();
        List<Transaction> unConfirmedTransactionList = transactionModel.getUnConfirmedTransactionList();
        ArrayList<Utxo> utxoArrayList = new ArrayList<>();
        for (Transaction transaction : unConfirmedTransactionList) {
            for (TxOut txOut : transaction.getSelfTxOutList()) {
                CTxDestination temDes = txOut.getScriptCTxDestination();
                if (temDes != null && !temDes.isNull()) {
                    Address usingAddress = addressModel.getAddressByCTxDestinationFromUsingAddressMap(temDes);
                    if ((usingAddress == null || !usingAddress.isRecycleFlag())&& addressModel.isUsingDesAddressMapHasKey(temDes)) {
                        utxoArrayList.add(new Utxo(txOut));
                    }
                }
            }
        }
        List<Utxo> utxoList = transactionModel.mo40427a(true);
        if (utxoList != null) {
            for (Utxo utxo : utxoList) {
                utxoArrayList.add(utxo);
            }
        }
        BloomFilterInterface bloomFilterInterface = this.wallet.getSelfWalletHelper().getNewBloomFilter((localAddressDesCollection.size() + utxoArrayList.size() + unConfirmedTransactionList.size()+100) * 2, 5.0E-7d, BloomUpdate.UPDATE_ALL);
        for (CTxDestination loclDes : localAddressDesCollection) {
            Address loclAddress = this.wallet.getAddressByCTxDestinationFromArrayMap(loclDes);
            if (loclAddress == null) {
                loclAddress = this.wallet.getSelfAddressModel().getAddressByCTxDestinationFromUsingAddressMap(loclDes);
            }
            if (loclAddress != null && !loclAddress.isRecycleFlag()) {
                bloomFilterInterface.insert(loclDes.data());
                CPubkeyInterface pubKey = loclAddress.getSelfPubKey();
                if (pubKey != null && (pubKey instanceof CPubKey)) {
                    CPubKey cPubKey = (CPubKey) pubKey;
                    if (cPubKey.getTypeLength() == 33) {
                        bloomFilterInterface.insert(DataTypeToolkit.copyPart(cPubKey.getByteArr(), 0, 33));
                    } else {
                        bloomFilterInterface.insert(loclDes.data());
                    }
                }
            }
        }
        byte[] bArr = new byte[36];
        for (Utxo utxo : utxoArrayList) {
            m13125a(utxo, bArr);
            if (!bloomFilterInterface.contains(bArr)) {
                bloomFilterInterface.insert(bArr);
            }
        }
        for (Transaction transaction : unConfirmedTransactionList) {
            byte[] data = transaction.getTxId().data();
            if (!bloomFilterInterface.contains(data)) {
                bloomFilterInterface.insert(data);
            }
        }
        return bloomFilterInterface;
    }

    /* renamed from: a */
    public void onConnectStatusChange(Peer peer, Peer.PeerStatus peerStatus) {
        PeerManagerEvent peerManagerEvent = this.peerManagerEvent;
        if (peerManagerEvent != null) {
            peerManagerEvent.onConnectStatusChange(this, peer, peerStatus);
        }
    }

    //mo44622a
    public void notifyMessageReceived(Peer peer, Message message) {
        PeerManagerEvent peerManagerEvent = this.peerManagerEvent;
        if (peerManagerEvent != null) {
            peerManagerEvent.onMessageReceived(this, peer, message);
        }
    }

    /* renamed from: a */
    public void mo44594a() {
        PeerManagerEvent peerManagerEvent = this.peerManagerEvent;
        if (peerManagerEvent != null) {
            peerManagerEvent.mo44077a_();
        }
    }

    //mo44647p
    public synchronized void clearAll() {
        this.banAddressInfoBlockQueue.clear();
        this.normalAddressInfoBlockQueue.clear();
        this.backupAddressInfoQueue.clear();
    }

    //mo40473a
    public void initAddressInfoQueueFromFile(String peerPath, String banPath) {
        this.peerFile = new PeerFile(peerPath);
        this.banPeerFile = new PeerFile(banPath);
        StringBuilder sb = new StringBuilder();
        sb.append(peerPath);
        sb.append("_custom");
        this.customPeerFile = new PeerFile(sb.toString());
        for (AddressInfo addressInfo : this.banPeerFile.getAddressInfoList()) {
            if (!this.banAddressInfoBlockQueue.contains(addressInfo)) {
                this.banAddressInfoBlockQueue.offer(addressInfo);
            }
        }
        int peerCount = this.wallet.getChainParams().peerCount;
        for (AddressInfo addressInfo2 : this.peerFile.getAddressInfoList()) {
            if (!this.normalAddressInfoBlockQueue.contains(addressInfo2) && !this.banAddressInfoBlockQueue.contains(addressInfo2) && this.normalAddressInfoBlockQueue.size() < peerCount * 4) {
                this.normalAddressInfoBlockQueue.offer(addressInfo2);
            }
        }
    }

    //mo44628b
    public void checkAndAddToNormalAddressInfoQueue(AddressInfo addressInfo) {
        if (!isDefaultRandomAdressMapContains(addressInfo.getKeyHex())) {
            if (this.normalAddressInfoBlockQueue.size() <= this.wallet.getChainParams().peerCount * 4 && !this.normalAddressInfoBlockQueue.contains(addressInfo) && !this.banAddressInfoBlockQueue.contains(addressInfo)) {
                this.normalAddressInfoBlockQueue.offer(addressInfo);
            }
        }
    }

    /* renamed from: c */
    public void mo44633c(List<AddressInfo> list) {
        if (this.normalAddressInfoBlockQueue.size() <= this.wallet.getChainParams().peerCount * 4) {
            for (AddressInfo addressInfo : list) {
                if (!isDefaultRandomAdressMapContains(addressInfo.getKeyHex()) && !this.normalAddressInfoBlockQueue.contains(addressInfo) && !this.normalAddressInfoBlockQueue.contains(addressInfo) && !this.banAddressInfoBlockQueue.contains(addressInfo) && !this.testAddressInfoBlockQueue.contains(addressInfo) && !this.normalPeerHashMap.containsKey(addressInfo)) {
                    this.testAddressInfoBlockQueue.offer(addressInfo);
                }
            }
        }
    }

    //mo40482c
    //910 mo40510c
    public void checkSizeAndAddAddressInfo(AddressInfo addressInfo) {
        if (this.peerFile.getAddressInfoList().size() < 16) {
            this.peerFile.addAddressInfo(addressInfo);
        }
    }

    //mo44631b
    public void banPeer(Peer peer) {
        AddressInfo addressInfo = peer.getPeerInfo().getMainAddressInfo();
        if (!this.banAddressInfoBlockQueue.contains(addressInfo)) {
            this.banAddressInfoBlockQueue.add(addressInfo);
            this.banPeerFile.addAddressInfo(addressInfo);
            this.normalPeerHashMap.remove(peer);
            this.normalAddressInfoBlockQueue.remove(addressInfo);
        }
    }

    //mo44648q
    public boolean isPeerManagerStart() {
        return this.peerManagerStatus == PeerManagerStatus.START;
    }

    /* renamed from: d */
    public void mo44636d(AddressInfo addressInfo) {
        PeerFile liVar = this.customPeerFile;
        if (liVar != null && !liVar.mo44600a(addressInfo.getKeyHex())) {
            Peer a = getNewPeerFromAddressInfo(addressInfo);
            this.backupPeerHashMap.put(addressInfo.getKeyHex(), a);
            this.backupAddressInfoQueue.offer(addressInfo);
            this.connectPeerBlockQueue.offer(a);
            this.customPeerFile.addAddressInfo(addressInfo);
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
        Iterator it = a.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AddressInfo addressInfo2 = (AddressInfo) it.next();
            if (addressInfo2.getKeyHex().equals(addressInfo.getKeyHex())) {
                a.remove(addressInfo2);
                this.backupPeerHashMap.remove(addressInfo2.getKeyHex());
                this.backupAddressInfoQueue.remove(addressInfo2.getKeyHex());
                break;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("_custom");
        FileUtils.isFileExist(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append("_custom");
        this.customPeerFile = new PeerFile(sb3.toString());
        for (AddressInfo a2 : a) {
            this.customPeerFile.addAddressInfo(a2);
        }
    }

    /* renamed from: b */
    public void mo44629b(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_custom");
        this.customPeerFile = new PeerFile(sb.toString());
        List<AddressInfo> a = this.customPeerFile.getAddressInfoList();
        Iterator it = a.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AddressInfo addressInfo = (AddressInfo) it.next();
            if (addressInfo.getKeyHex().equals(str2)) {
                a.remove(addressInfo);
                this.backupPeerHashMap.remove(str2);
                this.backupAddressInfoQueue.remove(str2);
                break;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append("_custom");
        FileUtils.isFileExist(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append("_custom");
        this.customPeerFile = new PeerFile(sb3.toString());
        for (AddressInfo a2 : a) {
            this.customPeerFile.addAddressInfo(a2);
        }
    }


    //910 mo44731r
    public boolean hasSyncingPeer() {
        for (Peer peer : this.normalPeerHashMap.values()) {
            if (peer.getPeerStatus() == Peer.PeerStatus.Connected && peer.isSyncing()) {
                return true;
            }
        }
        return false;
    }
}