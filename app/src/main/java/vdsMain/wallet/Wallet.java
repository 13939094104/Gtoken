package vdsMain.wallet;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import bitcoin.CKey;
import bitcoin.CKeyStore;
import bitcoin.CPubKey;
import bitcoin.UInt256;
import bitcoin.account.hd.HDSeed;
import bitcoin.script.CScript;
import com.vc.libcommon.exception.AddressFormatException;
import com.vcwallet.encrypt.BIP38Key;
import generic.crypto.KeyCryptor;
import generic.exceptions.*;
import generic.utils.AddressUtils;
import vdsMain.*;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.ChainSyncStatus;
import vdsMain.config.WalletConfigure;
import vdsMain.db.PersonalDB;
import vdsMain.db.TransactionDB;
import vdsMain.message.Message;
import vdsMain.model.*;
import vdsMain.observer.WalletObserver;
import vdsMain.peer.Peer;
import vdsMain.peer.PeerManager;
import vdsMain.table.WalletTable;
import vdsMain.tool.ScriptOpCodes;
import vdsMain.transaction.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Wallet implements SigningProvider,AccountModel.AccountEvent,TransactionModel.TransactionEvent,PeerManager.PeerManagerEvent,BlockChainModel.BlockChainModelEvent {

    //f13003a
    protected WalletType walletType = WalletType.UNKNOWN;

    //915 f13694y
    private String label = "";

    //f13004b
    protected boolean updateSettings = false;

    //f13015m
    protected CWallet cWallet;

    //f13012j
    protected ChainParams chainParams;

    //f13016n
    protected WalletHelper walletHelper;

    //f13017o
    protected TransactionModel transactionModel;


    //f13019q
    protected PeerManager peerManager;

    //f13013k
    protected AddressModel addressModel;

    //f13010h
    //915 f13677h
    protected PersonalDB personalDB;

    //f13011i
    protected TransactionDB transactionDB;


    //f13014l
    protected AccountModel accountModel;

    //f13018p
    protected BlockChainModel blockChainModel;

    //mo41192c
    public abstract BLOCK_CHAIN_TYPE getBlockChainType();

    //f13024v
    //秘钥密码
    protected KeyCryptor keyCryptor;

    //f13005c
    protected Context context;

    //f13023u
    protected AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    //f13026x
    private ReentrantLock lock = new ReentrantLock();

    //910 f13163y
    private ReentrantLock peerLock = new ReentrantLock();

    //f13025w
    private LinkedHashMap<WalletObserver, WalletObserver> walletObserverMap = new LinkedHashMap<>();

    //f13008f
    protected String fileRootDir;

    //f13009g
    protected String dbPath;

    protected String f13007e;

    //f13021s
    protected String peersPath;

    //f13022t
    protected String banListPath;

    //f13020r
    protected String walletConfigPath;

    //f13006d
    protected String pathPrefix;

    public Wallet(@NonNull Context context) {
        if (context != null) {
            this.context = context;
            if (!(context instanceof Application)) {
                this.context = context.getApplicationContext();
            }
        }
    }

    //mo44091c
    public Transaction getTransactionFromAllTransactionMap(UInt256 txid) {
        return this.transactionModel.getTransactionFromAllTransactionMap(txid);
    }

    //910 mo44103R
    public long getCurrentBlockNo() {
        return (long) this.blockChainModel.getCurrentBlockNo();
    }

    //mo41194e
    public abstract CWallet getCWallet();

    //mo41191b
    public abstract ChainParams getWalletChainParams();

    //mo41186a
    public abstract WalletHelper getWalletHelper();

    //mo41189a
    public abstract void addTestAddressList(ChainParams chainParams, WalletConfigure walletConfigure);

    //mo44031P
    //910 mo44101P
    public TransactionDB getSelfTransactionDB() {
        return this.transactionDB;
    }


    //mo41188a
    public abstract PersonalDB getPersonalDB(String str, String str2);
    //mo41193d
    public abstract AddressModel getAddressModel();

    //mo41197h
    public abstract AccountModel getAccountModel();

    //mo41196g
    public abstract TransactionModel getTransactionModel();

    //mo41195f
    public abstract BlockChainModel getBlockChainModel();

    //mo41198i
    public abstract PeerManager getPeerManager();

    public abstract Account mo41187a(Address jjVar, CharSequence charSequence) throws AddressExistsException, NotMatchException;

    //mo44020C
    //910 mo44090C
    //915 mo44368C
    public BlockChainModel getSelfBlockChainModel() {
        return this.blockChainModel;
    }

    //mo44125z
    //915 mo44496z
    public PeerManager getSelfPeerManager() {
        return this.peerManager;
    }

    //mo44047a
    public Account getAccountFromSparseArr(int i) {
        return this.accountModel.getAccountFromSparseArr(i);
    }

    //mo44050a
    public Address getAddressFromUsingAddressMap(CharSequence addressString) {
        return this.addressModel.getAddressFromUsingAddressMapByAddressString(addressString);
    }

    //mo44084b
    public CPrivateKeyInterface getAddressPrivateKey(Address address, CharSequence charSequence) throws AddressFormatException, NotMatchException {
        if (!address.privateKeyIsNotEmpty()) {
            return null;
        }
        checkWalletPassword(charSequence);
        return address.getPrivateKeyClone().getNewCKeyWithKeyCryptor(getSelfKeyCryptor(), charSequence.toString());
    }

    //mo44075a
    public boolean isBlockHashInCacheOrDb(UInt256 uInt256, boolean... ingoreBlockTable) {
        return this.blockChainModel.isBlockHashInCacheOrDb(uInt256, ingoreBlockTable);
    }

    //mo44034S
    public long getMaxBlockNo() {
        return this.blockChainModel.getMaxBlockNo();
    }

    public void mo44060a(String str) throws CoolWalletException {
        if (this.walletType == WalletType.COOL) {
            throw new CoolWalletException(str);
        }
    }

    public void mo42779b(CScript cScript) {
        this.cWallet.getCKeyStore().mo9418a(new CScriptID(cScript));
    }

    //mo44082b
    public Address getAddressByCTxDestinationFromArrayMap(CTxDestination cTxDestination) {
        return this.addressModel.getAddressByCTxDestinationFromUsingAddressMap(cTxDestination);
    }

    //mo44093c
    public Address getAddressFromShadowMap(CTxDestination oVar) {
        return this.addressModel.getAddressFromUsingAddressShadowMap(oVar);
    }

    public boolean mo44089b(CharSequence charSequence) {
        return this.addressModel.isArrayMapOrShadowMapHasKeyByCharSeq(charSequence);
    }

    //mo44053a
    public BlockInfo checkAndGetBlockInfo(UInt256 uInt256) {
        return this.blockChainModel.checkAndGetBlockInfoByBlockHash(uInt256, new boolean[0]);
    }

    //mo44080b
    public Transaction getTransactionFromAllMap(UInt256 uInt256) {
        return this.transactionModel.getTransactionFromAllTransactionMap(uInt256);
    }

    //mo44019B
    //910 mo44089B
    public CWallet getSelfCWallet() {
        return this.cWallet;
    }

    //915 mo44486k
    public void setLabel(String str) {
        this.label = str;
    }

    //915 mo44436ai
    public String getLabel() {
        return this.label;
    }

    //915 mo44435ah
    public boolean getIsHideTotalAmount() {
        return this.personalDB.getSetting().getBoolValue("HideTotalAmount", false);
    }

    //mo44104d
    public boolean isUsingDesAddressMapHasKey(CTxDestination des) {
        return this.addressModel.isUsingDesAddressMapHasKey(des);
    }

    public String mo44044a(Address address, String str, String str2) throws AddressFormatException, NotMatchException, InvalidatePasswordException, EncryptException {
        return BIP38Key.getBIP38Key(getChainParams(), address.getAddressPrivateKey((CharSequence) str), str2);
    }

    public String getAddressPrivateKey(CTxDestination cTxDestination, String str, String str2) throws AddressNotFoundException, AddressFormatException, NotMatchException, InvalidatePasswordException, EncryptException {
        Address address = getAddressByCTxDestinationFromArrayMap(cTxDestination);
        if (address != null) {
            return mo44044a(address, str, str2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Can not found address for key: ");
        sb.append(cTxDestination.toString());
        throw new AddressNotFoundException(sb.toString());
    }

    //mo44109e
    public boolean isUnuseDesAddressMapHasKey(CTxDestination des) {
        return this.addressModel.isUnuseDesAddressMapHasKey(des);
    }

    //mo44055a
    public void reSyncWallet(long startBlockNo, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("wallet:");
        sb.append(getClass().getSimpleName());
        sb.append(" removeSyncedBlocks:");
        sb.append(z);
        vdsMain.Log.m11473a((Object) this, sb.toString());
        lock();
        if (startBlockNo < 1) {
            startBlockNo = 1;
        }
        this.transactionModel.removeTransactionsSinceStartBlockNo(startBlockNo, true);
        this.blockChainModel.deleteBlocksSinceStartBlockNo(startBlockNo, z);
        unLock();
        notifyWalletResync(z);
    }

    //mo44088b
    public void reInitWallet(boolean z) {
        this.updateSettings = z;
        clearWallet();
        initWalletAndData();
        this.updateSettings = false;
    }

    //mo42393ac
    private void clearWallet() {
        this.accountModel.clearAccountVector();
        this.addressModel.ClearAll();
        this.blockChainModel.clearAll();
        this.transactionModel.clearAll();
        this.cWallet.getCKeyStore().clearNative();
    }

    //mo44076a
    public boolean checkAndUpdateWallType(WalletType walletType, boolean... zArr) {
        lock();
        WalletType walletType1 = this.walletType;
        if (walletType1 == null || walletType1 == WalletType.UNKNOWN) {
            if (walletType == WalletType.UNKNOWN) {
                unLock();
                return true;
            }
            checkAndStopPeer();
            this.personalDB.getSetting().insertOrUpdateData("walletType", walletType.name());
            this.walletType = walletType;
            unLock();
            if (zArr.length == 0 || zArr[0]) {
                noticeUpdateWallTypeSuccess();
            }
            return true;
        } else if (walletType == this.walletType) {
            unLock();
            return true;
        } else {
            unLock();
            return false;
        }
    }

    //mo42763a
    public void addScriptToKeyStore(CScript cScript) {
        this.cWallet.getCKeyStore().addScriptToKeyStore(new CScriptID(cScript), cScript);
    }

    //mo44039Y
    public void noticeUpdateWallTypeSuccess() {
        WalletType walletType = this.walletType;
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.OnUpdateWallTypeSuccess(this, walletType);
            }
        }
    }


    //mo44030O
    public WalletType getSelfWalletType() {
        return this.walletType;
    }

    public void intiSplitParamManager() {
    }

    //mo42788m
    public void initPwdFromDb() {
        this.keyCryptor.initPwdBytes();
    }

    //915 mo44372I
    //mo44024I
    public void updateWalletVid() {
        ArrayList<Address> vidAddressList = new ArrayList<>(this.addressModel.getVidAddress(true));
        AddressModel selfAddressModel = getSelfAddressModel();
        if (!vidAddressList.isEmpty()) {
            for (Address vidAddress : vidAddressList) {
                if (vidAddress != null) {
                    String clueTxid = vidAddress.getClueTxid();
                    if (TextUtils.isEmpty(clueTxid) || TextUtils.equals("0", clueTxid)) {
                        List<Transaction> transactionList = this.transactionModel.getTransactionListByConfirmType(vidAddress.getCTxDestination(), TransactionConfirmType.ALL);
                        if (transactionList == null || transactionList.isEmpty()) {
                            vidAddress.setAndUpdateFlag16(false);
                            selfAddressModel.removeFromSparseArrAndAddToAddressVector(16, vidAddress);
                            vidAddress.updateAppingVid(false);
                        } else {
                            for (Transaction transaction : transactionList) {
                                if (transaction.getFlag() == 1) {
                                    List txInList = transaction.getSelfTxInList();
                                    if (!txInList.isEmpty() && (((TxIn) txInList.get(0)).getAddress()).equals(vidAddress.getAddressString())) {
                                        if (transaction.isConfirmed()) {
                                            vidAddress.setAndUpdateFlag16(true);
                                            vidAddress.updateClueTxidByDesHash(transaction.getTxidHashString());
                                        } else {
                                            vidAddress.updateAppingVid(true);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Transaction clueTransaction = this.transactionModel.getTransactionByClueTxid(clueTxid);
                        if (vidAddress.getIsDirectSignVid()) {
                            if (clueTransaction == null) {
                                vidAddress.setAndUpdateFlag16(false);
                                selfAddressModel.removeFromSparseArrAndAddToAddressVector(16, vidAddress);
                                vidAddress.updateAppingVid(false);
                                vidAddress.updateIsDirectSignVid(false);
                                vidAddress.setDirectInvNo(0);
                            }
                        } else if (clueTransaction == null || clueTransaction.getFlag() != 1 || this.transactionModel.mo44543m(clueTransaction) || clueTransaction.isDefaultHash()) {
                            vidAddress.setAndUpdateFlag16(false);
                            selfAddressModel.removeFromSparseArrAndAddToAddressVector(16, vidAddress);
                            vidAddress.updateAppingVid(false);
                        }
                    }
                }
            }
        }
        ArrayList<Address> arrayList2 = new ArrayList<>(this.addressModel.getAddressVector());
        if (!arrayList2.isEmpty()) {
            for (Address address1 : arrayList2) {
                if (address1.isCategoryStatusNotZero()) {
                    List a3 = this.transactionModel.getTransactionListByConfirmType(address1.getCTxDestination(), TransactionConfirmType.CONFIRMED);
                    if (a3 != null && !a3.isEmpty()) {
                        Iterator it = a3.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Transaction transaction = (Transaction) it.next();
                            if (transaction.getFlag() == 1 && !this.transactionModel.mo44543m(transaction) && !transaction.isDefaultHash()) {
                                Address address = selfAddressModel.getAddressByCTxDestinationFromUsingAddressMap(((TxIn) transaction.getSelfTxInList().get(0)).getCTxDestination());
                                if (address != null && !address.isFlagIndentity()) {
                                    selfAddressModel.updateAddressFlagAsVid(address.getCTxDestination());
                                    address.updateClueTxidByDesHash(transaction.getTxidHashString());
                                }
                                TxOut txOut = (TxOut) transaction.getSelfTxOutList().get(0);
                                if (txOut.getFlag() == 1 && txOut.getSatoshi() == CAmount.toSatoshiLong(4)) {
                                    Address tempAddress = selfAddressModel.getAddressByCTxDestinationFromUsingAddressMap(txOut.getScriptCTxDestination());
                                    if (tempAddress != null && !tempAddress.isFlagIndentity()) {
                                        selfAddressModel.updateDirectSignVid(tempAddress, transaction);
                                    }
                                }
                            }
                        }
                    } else {
                        address1.updateAppingVid(false);
                        address1.updateIsDirectSignVid(false);
                        address1.setDirectInvNo(0);
                        return;
                    }
                }
            }
        }
    }


    //mo44123v
    public void InitSettingTable() {
        Settings setting = this.personalDB.getSetting();
        setting.selectAllDataAndInit();
        String a = setting.getMapValue("walletType", WalletType.UNKNOWN.name());
        if (this.updateSettings) {
            WalletType walletType = this.walletType;
            if (walletType != null) {
                setting.insertOrUpdateData("walletType", walletType.name());
            } else {
                setting.insertOrUpdateData("walletType", null);
            }
        } else {
            this.walletType = WalletType.valueOf(a);
        }
    }

    //mo44037W
    public void startPeerManagerNetwork() {
        if (this.walletType == WalletType.HOT) {
            if (this.peerManager.isPeerManagerStart()) {
                Log.w("Wallet", "Peer manager was already started.");
                return;
            }
            Log.i("Wallet", "startNetwork: start");
            this.peerManager.initAddressInfoQueueFromFile(this.peersPath, this.banListPath);
            this.peerManager.checkAndStartMainThread();
            this.blockChainModel.checkNewestBlockAndChangeStatus();
            Log.i("Wallet", "startNetwork: end");
        }
    }

    //mo44064a
    public void addWalletObserver(WalletObserver walletObserver) {
        if (walletObserver != null) {
            this.lock.lock();
            if (!this.walletObserverMap.containsKey(walletObserver)) {
                this.walletObserverMap.put(walletObserver, walletObserver);
            }
            this.lock.unlock();
            return;
        }
        throw new NullPointerException("Oobserver can not null!");
    }

    //startPeerManagerNetwork
    public void changePwdFailCallback(CharSequence charSequence) {
        this.personalDB.endTransaction(false);
        getSelfKeyCryptor().setPwd(charSequence.toString());
        unLock();
        startPeerManagerNetwork();
    }


    //mo42776a
    public void changeWalletPwd(CharSequence oldPwd, CharSequence newPwd) {
        if (hasSetPwd()) {
            checkAndStopPeer();
            lock();
            KeyCryptor keyCryptor = getSelfKeyCryptor();
            this.personalDB.beginTransaction();
            try {
                keyCryptor.setPwd(newPwd);
                this.addressModel.updateAddressPrivkey(keyCryptor, oldPwd.toString(), newPwd.toString());
                this.accountModel.updateHDAccountListSeed(keyCryptor, oldPwd.toString(), newPwd.toString());
                this.personalDB.endTransaction(true);
                startPeerManagerNetwork();
                unLock();
            }  catch (Exception e3) {
                e3.printStackTrace();
                changePwdFailCallback(oldPwd);
            }
        } else {
            lock();
            try {
                this.keyCryptor.setPwd(newPwd);
                unLock();
            } catch (Throwable th) {
                unLock();
                throw th;
            }
        }
    }

    public Address mo44051a(String str, CharSequence charSequence) throws NotMatchException, AddressFormatException, AddressExistsException {
        checkWalletPassword(charSequence);
        Address a = this.walletHelper.mo42408a(DumpedPrivateKeyFactory.checkAndGetDecodedPrivateKeyInfo(this.chainParams, str).getPrivateKey(), charSequence);
        if (a != null) {
            mo41187a(a, charSequence);
            return a;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UnSupported privatekey ");
        sb.append(a);
        throw new AddressFormatException(sb.toString());
    }

    public RejectTx mo44110f(UInt256 uInt256) {
        return this.transactionDB.getSelfRejectTxTable().getRejectTx(uInt256);
    }

    public void mo42392a(ChainParams chainParams) {
        if (!hasSetPwd() && chainParams.getSelfWalletPwd() != null && !chainParams.getSelfWalletPwd().isEmpty()) {
            changeWalletPwd((CharSequence) "", (CharSequence) chainParams.getSelfWalletPwd());
        }
        if (chainParams.getTestAddressList() != null) {
            for (String[] strArr : chainParams.getTestAddressList()) {
                try {
                    if (strArr.length != 1) {
                        if (strArr.length <= 1 || !StringToolkit.isNull((CharSequence) strArr[1])) {
                            if (!mo44089b((CharSequence) strArr[0])) {
                                mo44051a(strArr[1], (CharSequence) this.chainParams.getSelfWalletPwd());
                            }
                        }
                    }
                    GeneralAccount generalAccount = this.accountModel.getSelfGeneralAccount();
                    Address a = this.walletHelper.getAddress((WalletTable) this.personalDB.getSelfAddressTable(), new AddressType[0]);
                    a.setFlag64(true);
                    a.mo40875a(AddressUtils.m948a(this.chainParams, strArr[0]));
                    generalAccount.mo44139a(a, true);
                } catch (AddressFormatException e) {
                    e.printStackTrace();
                } catch (NotMatchException e) {
                    e.printStackTrace();
                } catch (AddressExistsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //mo42759H
    public void addTestAddress() {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            mo42392a(this.chainParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("add test address used: ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        Log.i("wallet", sb.toString());
    }

    //mo44124y
    public final void initWalletAndData() {
        this.atomicBoolean.set(false);
        //TODO 去掉匿名参数初始
        //intiSplitParamManager();
        InitSettingTable();
        initMainModel();
        selectAllAndInitData();
        addTestAddress();
        initPwdFromDb();
        this.atomicBoolean.set(true);
    }

    //mo44027L
    public String getFileRootDir() {
        return this.fileRootDir;
    }

    //mo44028M
    public String getDbPath() {
        return this.dbPath;
    }

    //mo44025J
    //910 mo44095J
    public final ChainParams getChainParams() {
        return this.chainParams;
    }

    //mo44018A
    public WalletHelper getSelfWalletHelper() {
        return this.walletHelper;
    }

    public WalletType getWalletType() {
        return this.walletType;
    }

    //mo44022E
    public AccountModel getSelfAccountModel() {
        return this.accountModel;
    }

    //mo44021D
    //910 mo44091D
    //915 mo44369D
    public AddressModel getSelfAddressModel() {
        return this.addressModel;
    }

    //mo44023F
    //910 mo44093F
    public TransactionModel getSelfTransactionModel() {
        return this.transactionModel;
    }


    //mo44029N
    public Context getContext() {
        return this.context;
    }

    //mo44032Q
    //915 mo44380Q
    public PersonalDB getPersonalDB() {
        return this.personalDB;
    }


    //mo41190b
    public abstract TransactionDB getTransactionDB(String str, String str2);


    //mo42787l
    public KeyCryptor getNewKeyCryptor() {
        return new KeyCryptor(this);
    }

    // mo44117n
    //915 mo44488n
    public KeyCryptor getSelfKeyCryptor() {
        return this.keyCryptor;
    }

    //mo42760T
    public boolean hasSetPwd() {
        return getSelfKeyCryptor().hasSetPwd();
    }

    //mo44090b
    public boolean isHasWalletObserver(WalletObserver walletObserver) {
        return this.walletObserverMap.containsKey(walletObserver);
    }

    public boolean mo44038X() {
        return this.atomicBoolean.get();
    }

    //mo44115j
    public void lock() {
        this.lock.lock();
    }

    //mo44116k
    public void unLock() {
        this.lock.unlock();
    }

    //mo44043a
    public HDSeed decryptToHDSeed(EncryptedHDSeed encryptedHDSeed, CharSequence charSequence) throws NotMatchException {
        checkWalletPassword(charSequence);
        return encryptedHDSeed.decryptBytesToHDSeed(getSelfKeyCryptor(), charSequence.toString());
    }

    //mo42784c
    //915 mo43092c
    public boolean checkMatchPassword(CharSequence charSequence) {
        if (hasSetPwd()) {
            return getSelfKeyCryptor().checkPwd(charSequence);
        }
        return true;
    }
    //mo42786d
    //915 mo43094d
    public void checkWalletPassword(CharSequence charSequence) throws NotMatchException {
        if (!checkMatchPassword(charSequence)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Wrong password ");
            sb.append(charSequence);
            throw new NotMatchException(sb.toString());
        }
    }

    //mo44036V
    public ChainSyncStatus getChainSyncStatus() {
        BlockChainModel blockChainModel = this.blockChainModel;
        if (blockChainModel == null) {
            return ChainSyncStatus.UNSYNC;
        }
        return blockChainModel.getChainSyncStatus();
    }

    //mo44073a
    public void encryptPrivateKeyByPwd(byte[] bArr, EncryptedPrivateKey privateKey, String str) {
        privateKey.encryptBytesByPwd(bArr, getSelfKeyCryptor(), str);
    }

    //mo44042a
    public EncryptedHDSeed getEncryptedHDSeed(HDSeed hDSeed, CharSequence charSequence) {
        return new EncryptedHDSeed(hDSeed, getSelfKeyCryptor(), charSequence.toString());
    }

    //mo44041a
    public long getTimeStampByBlockNo(long blockNo) {
        return this.blockChainModel.getTimeStampByBlockNo(blockNo);
    }

    //mo44033R
    public long getCurBlockNo() {
        return (long) this.blockChainModel.getCurrentBlockNo();
    }

    //mo44074a
    public void addWalletObservers(WalletObserver[] walletObservers) {
        if (walletObservers == null) {
            throw new NullPointerException("Oobserver can not null!");
        } else if (walletObservers.length != 0) {
            this.lock.lock();
            for (WalletObserver walletObserver : walletObservers) {
                if (!this.walletObserverMap.containsKey(walletObserver)) {
                    this.walletObserverMap.put(walletObserver, walletObserver);
                }
            }
            this.lock.unlock();
        }
    }

    //m12214d
    private String getWalletFileName(String str, String str2) {
        String sb2 = str + str2;
        if (!str2.equals("vcash")) {
            return sb2;
        }
        String vdsPath = str + "vds";
        try {
            File file = new File(sb2);
            if (!file.exists()) {
                return vdsPath;
            }
            file.renameTo(new File(vdsPath));
            return vdsPath;
        } catch (Exception e) {
            e.printStackTrace();
            return vdsPath;
        }
    }

    //mo42789t
    public void InitChainParamsAndWalletConfigure() {
        try {
            WalletConfigure walletConfigure = new WalletConfigure();
            walletConfigure.initConfigFile(this.chainParams, this.walletConfigPath);
            addTestAddressList(this.chainParams, walletConfigure);
            this.label = walletConfigure.configFile.getValueFromMap("label");
        } catch (Exception unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("config file load failed: ");
            sb.append(this.walletConfigPath);
            vdsMain.Log.logObjectWarning((Object) this, sb.toString());
            this.chainParams = getWalletChainParams();
            this.chainParams.initMainParams();
        }
    }

    //mo44118o
    public void InitPersonalAndTransactionDb() {
        this.personalDB = getPersonalDB(this.dbPath, "personal");
        this.transactionDB = getTransactionDB(this.dbPath, "transaction");
    }

    //mo44119p
    public void InitDbAndAddToVector() {
        this.personalDB.InitAndAddtoDbVector();
        this.transactionDB.InitAndAddtoDbVector();
    }

    //mo44120q
    public void getDbWriteableSqlDatabase() {
        this.personalDB.getWriteableSqlDatabase();
        this.transactionDB.getWriteableSqlDatabase();
    }

    //mo42399s
    public void initModelAndManager() {
        if (this.addressModel == null) {
            this.addressModel = getAddressModel();
        }
        if (this.accountModel == null) {
            this.accountModel = getAccountModel();
            this.accountModel.setAccountEvent(this);
        }
        if (this.transactionModel == null) {
            this.transactionModel = getTransactionModel();
            this.transactionModel.setTransactionEvent((TransactionModel.TransactionEvent) this);
        }
        if (this.blockChainModel == null) {
            this.blockChainModel = getBlockChainModel();
            this.blockChainModel.setBlockChainModelEvent((BlockChainModel.BlockChainModelEvent) this);
        }
        this.cWallet = getCWallet();
        this.peerManager = getPeerManager();
        this.peerManager.initAddressInfoQueueFromFile(this.peersPath, this.banListPath);
        this.peerManager.setPeerManagerEvent((PeerManager.PeerManagerEvent) this);
    }

    //mo44061a
    public void initAppFileDirectory(@NonNull String str, WalletObserver... walletObservers) throws IOException {
        ScriptOpCodes.Init();
        this.f13007e = str;
        this.pathPrefix = str;
        addWalletObservers(walletObservers);
        if (str.lastIndexOf("/") != str.length() - 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.pathPrefix);
            sb.append("/");
            this.pathPrefix = sb.toString();
        }
        this.pathPrefix = getWalletFileName(this.pathPrefix, getBlockChainType().name().toLowerCase(Locale.getDefault()));
        this.chainParams = getWalletChainParams();
        this.walletHelper = getWalletHelper();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.pathPrefix);
        sb2.append("/wallet.conf");
        this.walletConfigPath = sb2.toString();
        InitChainParamsAndWalletConfigure();
        NETWORK_TYPE networkType = this.chainParams.getNetworkType();
        if (this.chainParams.getNetworkType() != NETWORK_TYPE.MAIN) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.pathPrefix);
            sb3.append("/");
            sb3.append(networkType.name().toLowerCase(Locale.getDefault()));
            this.pathPrefix = sb3.toString();
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.pathPrefix);
        sb4.append("/peers");
        this.peersPath = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(this.pathPrefix);
        sb5.append("/banlist");
        this.banListPath = sb5.toString();
        if (FileToolkit.checkDirectory(this.pathPrefix, false)) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append(this.pathPrefix);
            sb6.append("/data");
            this.fileRootDir = sb6.toString();
            if (FileToolkit.checkDirectory(this.fileRootDir, false)) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(this.pathPrefix);
                sb7.append("/db");
                this.dbPath = sb7.toString();
                if (FileToolkit.checkDirectory(this.dbPath, false)) {
                    try {
                        ApplicationInfo applicationInfo = this.context.getApplicationInfo();
                        PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
                        ChainParams chainParams = this.chainParams;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(this.context.getString(applicationInfo.labelRes));
                        sb8.append(" ");
                        sb8.append(packageInfo.versionName);
                        chainParams.labelResAndVersionName=sb8.toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    this.keyCryptor = getNewKeyCryptor();
                    InitPersonalAndTransactionDb();
                    InitDbAndAddToVector();
                    getDbWriteableSqlDatabase();
                    initModelAndManager();
                    return;
                }
                StringBuilder sb9 = new StringBuilder();
                sb9.append("can not create folder ");
                sb9.append(this.dbPath);
                throw new IOException(sb9.toString());
            }
            StringBuilder sb10 = new StringBuilder();
            sb10.append("can not create folder ");
            sb10.append(this.fileRootDir);
            throw new IOException(sb10.toString());
        }
        StringBuilder sb11 = new StringBuilder();
        sb11.append("can not create folder ");
        sb11.append(this.pathPrefix);
        throw new IOException(sb11.toString());
    }

    //mo44035U
    //915 mo44383U
    public void checkAndStopPeer() {
        Log.i("Wallet", "stopNetwork: start");
        this.peerManager.checkAndStopPeerManager();
        Log.i("Wallet", "stopNetwork: end");
    }

    //mo42397c
    public void clearWalletSetting(boolean z) {
        this.accountModel.clearAccountVector();
        this.addressModel.ClearAll();
        this.blockChainModel.clearAll();
        this.transactionModel.clearAll();
        this.peerManager.clearAll();
        this.cWallet.getCKeyStore().clearNative();
    }


    //mo42400w
    public void initMainModel() {
        this.accountModel.initAccountTableAndData();
        this.addressModel.InitAddressTable();
        this.blockChainModel.initDbAndTable();
        this.transactionModel.initDbAndTable();
    }

    //mo42401x
    public void selectAllAndInitData() {
        this.accountModel.selectAllAndInitData();
        this.addressModel.selectAllAndInitData();
        this.blockChainModel.selectAllAndInitData();
        this.transactionModel.selectAllAndInitData();
    }

    public List<Address> mo44046a(String str, String str2, String pwd) throws AddressFormatException, NotMatchException, AddressExistsException, DecryptException {
        checkWalletPassword((CharSequence) pwd);
        byte[] DecryptBIP38Key = BIP38Key.DecryptBIP38Key(str, str2);
        if (DecryptBIP38Key != null) {
            CKey cKey = new CKey(DecryptBIP38Key);
            List<Address> addressList = this.walletHelper.getAddressListFromPrivateKey((CPrivateKeyInterface) cKey, pwd);
            if (addressList == null || addressList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("UnSupported privatekey ");
                sb.append(cKey);
                throw new AddressFormatException(sb.toString());
            }
            for (Address address : addressList) {
                address.updateCategoryStatus(1, true);
                if (addressList.indexOf(address) == 0) {
                    address.updateIsHide(false);
                } else {
                    address.updateIsHide(true);
                }
            }
            mo44048a(addressList, pwd, new boolean[0]);
            return addressList;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Decode bit38 key failed: ");
        sb2.append(str);
        throw new DecryptException(sb2.toString());
    }

    public Account mo44048a(List<Address> list, String str, boolean... zArr) throws AddressExistsException {
        Account account;
        lock();
        boolean z = zArr != null && zArr.length > 0 && zArr[0];
        ArrayList arrayList = new ArrayList();
        Account tempAccount = null;
        for (Address address : list) {
            if (address instanceof VAnonymousAddress) {
                account = ((VAccountModel) this.accountModel).mo42668i();
            } else {
                account = this.accountModel.getSelfGeneralAccount();
            }
            if (!this.addressModel.txDesAddressArrayListHasKey(address.getCTxDestination())) {
                arrayList.add(address);
            } else if (!z) {
                throw new AddressExistsException();
            }
            tempAccount = account;
        }
        tempAccount.checkAndAddToBitCoinCtxDestAddressMap((List<Address>) arrayList, true);
        unLock();
        return tempAccount;
    }

    //mo44072a
    public void clearWallet(boolean insertOrUpdateCurrentWalletType) {
        WalletType walletType = getWalletType();
        checkAndStopPeer();
        clearWalletSetting(insertOrUpdateCurrentWalletType);
        this.personalDB.clearTables();
        this.transactionDB.clearTables();
        KeyCryptor keyCryptor = this.keyCryptor;
        if (keyCryptor != null) {
            keyCryptor.setPwd("");
        }
        if (insertOrUpdateCurrentWalletType) {
            this.personalDB.getSetting().insertOrUpdateData("walletType", walletType.name());
            this.walletType = walletType;
        } else {
            this.walletType = WalletType.UNKNOWN;
            this.personalDB.getSetting().insertOrUpdateData("walletType", WalletType.UNKNOWN.name());
        }
        try {
            initMainModel();
            selectAllAndInitData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mo44122u
    public List<WalletObserver> getWalletObserverList() {
        this.lock.lock();
        ArrayList arrayList = new ArrayList(this.walletObserverMap.values());
        this.lock.unlock();
        return arrayList;
    }

    public void mo44065a(Account account) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.mo42770a(this, account);
            }
        }
        if (!mo44038X()) {
            return;
        }
        if (account instanceof HDAccount) {
            this.peerManager.mo40474a(account.getTxDesAddressList());
        } else {
            this.peerManager.mo40474a(account.getALLAddressList());
        }
    }

    public void mo42391a(Account account, List<Address> list) {
        this.peerManager.lockAndGetBloomFilter();
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.mo42767a(this, list);
            }
        }
    }

    public void mo42782b(Account jfVar, List<Address> list) {
        this.peerManager.mo40478b(list);
        CKeyStore a = this.cWallet.getCKeyStore();
        for (Address address : list) {
            if (address.getAddressType() == AddressType.GENERAL && (address instanceof BitcoinMultiSigAddress)) {
                CScript i = ((CPubKey) address.getSelfPubKey()).mo9450i();
                if (i.IsPayToScriptHash()) {
                    a.mo9418a(new CScriptID(i));
                }
            }
        }
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.mo42781b(this, list);
            }
        }
    }

    public void mo44094c(Transaction transaction) {
        for (WalletObserver jcVar : getWalletObserverList()) {
            if (isHasWalletObserver(jcVar)) {
                jcVar.mo42766a(this, transaction);
            }
        }
    }

    public void mo44099d(Transaction dhVar) {
        for (WalletObserver jcVar : getWalletObserverList()) {
            if (isHasWalletObserver(jcVar)) {
                jcVar.mo42780b(this, dhVar);
            }
        }
    }

    public void mo44108e(List<Transaction> list) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.mo42785d(this, list);
            }
        }
    }

    public void mo44105e(UInt256 uInt256) {
        for (WalletObserver jcVar : getWalletObserverList()) {
            if (isHasWalletObserver(jcVar)) {
                jcVar.mo42765a(this, uInt256);
            }
        }
    }


    public void onTransactionReceive(Transaction transaction) {
        mo44094c(transaction);
    }

    public void onTransactionAbanded(Transaction dhVar) {
        mo44099d(dhVar);
    }

    public void mo44097c(List<Transaction> list) {
        mo44108e(list);
    }

    public void onTransactionSend(UInt256 uInt256) {
        mo44105e(uInt256);
    }

    public void notifyMainPeerChange(PeerManager peerManager, Peer peer) {
        for (WalletObserver observer : getWalletObserverList()) {
            if (isHasWalletObserver(observer)) {
                observer.onMainPeerChange(peerManager, peer);
            }
        }
    }

    //mo44069a
    public void onMessageReceived(PeerManager peerManager, Peer peer, Message message) {
        for (WalletObserver observer : getWalletObserverList()) {
            if (isHasWalletObserver(observer)) {
                observer.onMessageReceived(this, peer, message);
            }
        }
    }

    /* renamed from: a */
    public void onConnectStatusChange(PeerManager peerManager, Peer peer, Peer.PeerStatus peerStatus) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.onConnectStatusChange(this, peer, peerStatus);
            }
        }
    }

    public void onPeerManagerStatusChange(PeerManager peerManager, PeerManager.PeerManagerStatus dVar) {
        for (WalletObserver observer : getWalletObserverList()) {
            if (isHasWalletObserver(observer)) {
                observer.mo42774a(this, dVar);
            }
        }
    }

    //mo44103d
    public void notifyWalletResync(boolean z) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.onWalletResync(this, z);
            }
        }
    }

    public void mo44077a_() {
        for (WalletObserver observer : getWalletObserverList()) {
            if (isHasWalletObserver(observer)) {
                observer.mo43914a_();
            }
        }
    }

    public void onCurBlockNoChange(int pre, int cur, int max) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.onCurBlockNoChange(this, pre, cur, max);
            }
        }
    }

    public void mo44063a(List<BlockHeader> list, List<Transaction> list2, HashMap<CTxDestination, Address> hashMap) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.mo42768a(this, list, list2, hashMap);
            }
        }
    }

    public void onBlockNoUpdate(BlockHeader jtVar, int i, ChainSyncStatus ioVar) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.onBlockNoUpdate(this, ioVar, jtVar, i, ioVar);
            }
        }
    }

    public void onUpdateBlock(BlockInfo juVar, List<Transaction> list, HashMap<CTxDestination, Address> hashMap) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.onUpdateBlock(this, juVar, list, hashMap);
            }
        }
    }

    //mo44101d
    public void onTransactionsConfirmed(List<Transaction> list) {
        for (WalletObserver walletObserver : getWalletObserverList()) {
            if (isHasWalletObserver(walletObserver)) {
                walletObserver.onTransactionsConfirmed(this, list);
            }
        }
    }

    //mo44087b
    public void notifyOnTransactionsConfirmed(List<Transaction> list) {
        onTransactionsConfirmed(list);
    }

    public String mo44079ab() {
        return this.personalDB.getSetting().getMapValue("networksynchronization", "1");
    }

    public CPrivateKeyInterface mo42761a(CTxDestination oVar, CharSequence charSequence) {
        Address a = this.addressModel.getAddressByCTxDestinationFromUsingAddressMap(oVar);
        if (a == null || !a.privateKeyIsNotEmpty()) {
            return null;
        }
        try {
            return a.getPrivateKeyClone().getNewCKeyWithKeyCryptor(getSelfKeyCryptor(), charSequence.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CPubkeyInterface mo42762a(CTxDestination oVar) {
        Address a = this.addressModel.getAddressByCTxDestinationFromUsingAddressMap(oVar);
        if (a == null) {
            return null;
        }
        CPubkeyInterface t = a.getSelfPubKey();
        if (t.mo210g() == PubkeyType.PUBKEY) {
            return t;
        }
        return null;
    }

    public boolean mo42778a(CScriptID ckVar, CScript cScript) {
        return this.cWallet.getCKeyStore().getScriptAndWriteToCSript(ckVar, cScript);
    }

    //910 mo44188j
    public void startThreadLock() {
        this.peerLock.lock();
        StringToolkit.m11650a(String.format(Locale.getDefault(), "wallet [ %s ] start locked by thread [ %s ]", new Object[]{toString(), Thread.currentThread()}));
    }

    //mo44189k
    public void endThreadLock() {
        if (this.peerLock.isLocked()) {
            StringToolkit.m11650a(String.format(Locale.getDefault(), "wallet [ %s ] start unlock by thread [ %s ]", new Object[]{toString(), Thread.currentThread()}));
        }
        this.peerLock.unlock();
    }

    //915 mo44437aj
    public String getSettingExpandList() {
        return this.personalDB.getSetting().getMapValue("expandList", "");
    }

    //915 mo44487l
    public void setSettingExpandList(String str) {
        this.personalDB.getSetting().insertOrUpdateData("expandList", str);
    }

    //915 mo44410a
    public void setSettingBoolValue(String str, boolean z) {
        this.personalDB.getSetting().setBoolValue(str, z);
    }

    //915 mo44438ak
    public boolean getSettingConfigBoolean() {
        return this.personalDB.getSetting().getBoolValue("config", false);
    }

    //915 mo44482i
    public void setSettingConfigBoolean(boolean z) {
        this.personalDB.getSetting().setBoolValue("config", z);
    }
}
