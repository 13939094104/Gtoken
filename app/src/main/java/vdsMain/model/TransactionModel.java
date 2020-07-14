package vdsMain.model;

import android.database.Cursor;
import androidx.annotation.NonNull;
import android.util.Log;
import bitcoin.CNoDestination;
import bitcoin.UInt256;
import bitcoin.script.CScript;
import com.google.common.collect.HashMultimap;
import com.vc.libcommon.exception.AddressFormatException;
import com.vc.libcommon.exception.TxSizeException;
import com.vc.libcommon.util.UInt64;
import generic.exceptions.*;
import generic.utils.AddressUtils;
import net.bither.bitherj.utils.Utils;
import vdsMain.*;
import vdsMain.db.PersonalDB;
import vdsMain.db.TransactionDB;
import vdsMain.message.RejectMessageInterface;
import vdsMain.table.*;
import vdsMain.transaction.*;
import vdsMain.wallet.CWallet;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;
import vdsMain.wallet.WalletHelper;

import java.io.IOException;
import java.util.*;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class TransactionModel extends Model {

    public static final long[] f13206g = {10000};

    //f13207a
    protected HashMap<UInt256, Transaction> invalidateTransactionMap = new HashMap<>();

    //f13208b
    protected HashMap<UInt256, String> txIdToRemarkMap = new HashMap<>();

    //f13209c
    protected TxInMap txInMap = new TxInMap();

    //f13210d
    protected HashMultimap<CTxDestination, COutPoint> desToCOutPointMultiMap = HashMultimap.create();

    //f13211e
    protected HashMap<UInt256, Transaction> confirmedRelayedTransactionMap = new HashMap<>();

    protected HashMap<UInt256, Transaction> confirmedVxdTransactionMap = new HashMap<>();

    //f13212f
    protected LinkedHashMap<UInt256, Boolean> unConfirmRelayedTxidMap = new LinkedHashMap<>();

    protected LinkedHashMap<UInt256, Boolean> unConfirmVxdTxidMap = new LinkedHashMap<>();

    protected long f13213i;

    /* renamed from: j */
    protected long f13214j;

    /* renamed from: k */
    protected long f13215k;

    //f13216l
    protected TransactionTable transactionTable;

    //f13217m
    protected AddressTable addressTable;

    //f13218n
    protected AddressToTxMap addressToTxMap;

    //f13219o
    protected HashMap<UInt256, Transaction> allTransactionHashMap;

    //f13220p
    protected LinkedHashMap<UInt256, Transaction> confirmedTransactionLinkedMap;

    //f13221q
    protected ArrayListMap<UInt256, Transaction> unConfirmedTransactionArrayMap;

    //f13222r
    protected CWallet cWallet;

    //f13223s
    protected TransactionEvent transactionEvent;

    //f13224t
    private RejectTxTable rejectTxTable;

    //f13225u
    private TxMarkTable txMarkTable;

    //f13226v
    private TransactionTable invaildTxTable;

    //C3889a
    public interface TransactionEvent {
        //mo44057a
        //910 mo44127a
        void onTransactionReceive(Transaction transaction);

        //mo44085b
        void onTransactionAbanded(Transaction dhVar);

        /* renamed from: c */
        void mo44097c(List<Transaction> list);

        //mo44098d
        void onTransactionSend(UInt256 uInt256);
    }

    public TransactionModel(@NonNull Wallet wallet) {
        super(wallet);
        long[] jArr = f13206g;
        this.f13213i = jArr[0];
        this.f13214j = 1;
        this.f13215k = jArr[0];
        this.allTransactionHashMap = new HashMap<>();
        this.confirmedTransactionLinkedMap = new LinkedHashMap<>();
        this.unConfirmedTransactionArrayMap = new ArrayListMap<>();
    }

    public abstract void mo40444a(Collection<UInt256> collection);

    /* access modifiers changed from: protected */
    //mo40475a
    public abstract boolean replaceRelatedTransactionWithAddress(Transaction dhVar, boolean z);

    //mo44496a
    public synchronized void setTransactionEvent(TransactionEvent transactionEvent) {
        this.transactionEvent = transactionEvent;
    }

    //mo42794a
    public void initDbAndTable() {
        this.addressToTxMap = new AddressToTxMap(this.wallet);
        TransactionDB transactionDB = this.wallet.getSelfTransactionDB();
        PersonalDB personalDB = this.wallet.getPersonalDB();
        this.transactionTable = transactionDB.getSelfTransactionTable();
        this.invaildTxTable = transactionDB.getSelfInvaildTxTable();
        this.rejectTxTable = transactionDB.getSelfRejectTxTable();
        this.txMarkTable = transactionDB.getSelfTxMarkTable();
        this.addressTable = personalDB.getSelfAddressTable();
        this.cWallet = this.wallet.getSelfCWallet();
    }

    //mo44533h
    public String getRemarkByTxid(UInt256 txid) {
        return (String) this.txIdToRemarkMap.get(txid);
    }

    public boolean mo44543m(Transaction dhVar) {
        return mo44532g(dhVar.getTxId());
    }

    public boolean mo44532g(UInt256 uInt256) {
        return ((Transaction) this.invalidateTransactionMap.get(uInt256)) != null;
    }

    public void mo40442a(Transaction srcTransaction, List<Transaction> list) {
        Transaction tempTransaction = getTransactionFromAllTransactionMap(srcTransaction.getTxId());
        if (tempTransaction != null) {
            UInt256 tempTxid = tempTransaction.getTxId();
            checkAndRemoveTxInsForTransactionFromTxInMap(tempTransaction);
            this.addressToTxMap.removeFromCTxDesToTxid2BoolByTxid(tempTxid);
            for (TxOut txOut : tempTransaction.getSelfTxOutList()) {
                CTxDestination cTxDestination = txOut.getScriptCTxDestination();
                if (!(cTxDestination instanceof CNoDestination)) {
                    this.desToCOutPointMultiMap.remove(cTxDestination, txOut.getCOutPoint());
                }
            }
            List<Transaction> confirmedTransactionList = findConfirmedTransactionFromAll(tempTransaction);
            list.addAll(confirmedTransactionList);
            for (Transaction transaction : confirmedTransactionList) {
                this.transactionTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) transaction);
                this.allTransactionHashMap.remove(transaction.getTxId());
                checkAndRemoveTxInsForTransactionFromTxInMap(transaction);
                this.addressToTxMap.removeFromCTxDesToTxid2BoolByTxid(transaction.getTxId());
                for (TxOut txOut : transaction.getSelfTxOutList()) {
                    CTxDestination outDes = txOut.getScriptCTxDestination();
                    if (!(outDes instanceof CNoDestination)) {
                        this.desToCOutPointMultiMap.remove(outDes, txOut.getCOutPoint());
                    }
                }
                this.unConfirmedTransactionArrayMap.removeAndGet(transaction.getTxId());
            }
            this.allTransactionHashMap.remove(tempTxid);
            this.invalidateTransactionMap.remove(tempTxid);
            this.confirmedRelayedTransactionMap.remove(tempTxid);
            this.confirmedTransactionLinkedMap.remove(tempTxid);
            this.unConfirmedTransactionArrayMap.removeAndGet(tempTxid);
            searchRelatedAddressAndCalBalance(tempTransaction);
        }
    }

    //mo40460g
    public void searchRelatedAddressAndCalBalance(Transaction transaction) {
        List<TxIn> txInList = transaction.getSelfTxInList();
        List<TxOut> txOutList = transaction.getSelfTxOutList();
        HashMap<String, Address> addressHashMap = new HashMap();
        HashMap<Integer, Account> accountHashMap = new HashMap();
        for (TxIn txIn : txInList) {
            searchRelatedAddressAndAccount(txIn.getAddress(), addressHashMap, accountHashMap);
        }
        for (TxOut txOut : txOutList) {
            searchRelatedAddressAndAccount(txOut.getAddress(), addressHashMap, accountHashMap);
        }
        if (!addressHashMap.isEmpty()) {
            for (Address address : addressHashMap.values()) {
                address.calSubAddressBalanceInfo(this.wallet.getBlockChainType());
            }
        }
        if (!accountHashMap.isEmpty()) {
            for (Account account : accountHashMap.values()) {
                account.calSubAccountBalanceInfo(false, this.wallet.getBlockChainType());
            }
        }
    }

    //mo44494a
    public void searchRelatedAddressAndAccount(String addressString, HashMap<String, Address> addressHashMap, HashMap<Integer, Account> accountHashMap) {
        if (!addressHashMap.containsKey(addressString)) {
            Address address = this.wallet.getAddressFromUsingAddressMap((CharSequence) addressString);
            AccountModel accountModel = this.wallet.getSelfAccountModel();
            if (address != null) {
                addressHashMap.put(addressString, address);
                if (accountHashMap.containsKey(address.getAccount())) {
                    accountHashMap.put(address.getAccount(), accountModel.getAccountFromSparseArr(address.getAccount()));
                }
            }
        }
    }

    public Transaction mo44476a(Address address) {
        Transaction transaction = null;
        if (address == null) {
            return null;
        }
        ChainParams chainParams = getWalllet().getChainParams();
        List utxoList = mo40449b(address.getCTxDestination());
        if (utxoList == null || utxoList.isEmpty()) {
            return null;
        }
        Iterator it = utxoList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Utxo utxo = (Utxo) it.next();
            Transaction transaction1 = (Transaction) this.allTransactionHashMap.get(utxo.getTxOutTxid());
            if (transaction1.checkAndGetBlockNoFromAll() < chainParams.f14913c && utxo.getValue() > 10000000) {
                transaction = transaction1;
                break;
            }
        }
        return transaction;
    }

    //mo40468n
    private List<Transaction> findConfirmedTransactionFromAll(Transaction transaction) {
        ArrayList arrayList = new ArrayList();
        for (Transaction unConfirmedTransaction : this.unConfirmedTransactionArrayMap.getValueList()) {
            Iterator unConfirmTransactionTxInIterator = unConfirmedTransaction.getSelfTxInList().iterator();
            while (true) {
                if (!unConfirmTransactionTxInIterator.hasNext()) {
                    break;
                }
                Transaction tempTransaction = this.wallet.getTransactionFromAllTransactionMap(((TxIn) unConfirmTransactionTxInIterator.next()).getPrevTxOut().txid);
                if (tempTransaction != null && transaction.getTxId().equals(tempTransaction.getTxId())) {
                    arrayList.add(unConfirmedTransaction);
                    break;
                }
            }
        }
        ArrayList<Transaction> arrayList2 = new ArrayList<>();
        arrayList2.addAll(arrayList);
        for (Transaction transaction1 : arrayList2) {
            arrayList.addAll(findConfirmedTransactionFromAll(transaction1));
        }
        return arrayList;
    }

    public void mo44495a(List<Address> list) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        ArrayList arrayList = new ArrayList();
        for (Address jjVar : list) {
            String c = jjVar.getAddressString(this.wallet.getBlockChainType());
            hashMap2.put(c, jjVar);
            for (UInt256 uInt256 : this.addressToTxMap.mo43950a(c)) {
                Transaction b = getTransactionFromAllTransactionMap(uInt256);
                if (!hashMap.containsKey(uInt256) && b.isConfirmed()) {
                    hashMap.put(uInt256, b);
                }
            }
        }
        Set<Map.Entry> entrySet = hashMap.entrySet();
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry value : entrySet) {
            Transaction dhVar = (Transaction) value.getValue();
            dhVar.recomputeByTxs(null);
            if (!dhVar.isRelatedToLocalAddress()) {
                mo44515c(dhVar, (List<Transaction>) arrayList2);
            }
            arrayList.addAll(arrayList2);
        }
    }

    public void mo44515c(Transaction transaction, List<Transaction> list) {
        this.transactionTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) transaction);
        if (list != null) {
            list.add(transaction);
        }
        mo40442a(transaction, list);
    }

    //mo40441a
    //910 mo40479b
    public void checkAndRemoveTxInsForTransactionFromTxInMap(Transaction transaction) {
        if (transaction.getSelfTxInList() != null) {
            this.txInMap.checkAndRemoveTxInForTransaction((TransactionInteface) transaction);
        }
    }

    public long mo44502b() {
        return this.f13213i;
    }

    public long mo44511c() {
        return this.f13214j;
    }

    //mo44520d
    public List<Transaction> getConfirmedAndUnConfirmTransactionList() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        transactions.addAll(this.confirmedTransactionLinkedMap.values());
        transactions.addAll(this.unConfirmedTransactionArrayMap.getValueList());
        return transactions;
    }

    //mo44480a
    public List<Transaction> getConfirmedTransactionListFromAll(UInt256 blockHash) {
        if (blockHash == null) {
            return null;
        }
        List<UInt256> confirmedTxids = this.wallet.getSelfBlockChainModel().getConfirmedTxids(blockHash);
        if (confirmedTxids == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(confirmedTxids.size());
        for (UInt256 txid : confirmedTxids) {
            Transaction transaction = getTransactionFromAllTransactionMap(txid);
            if (transaction != null) {
                arrayList.add(transaction);
            }
        }
        return arrayList;
    }

    public List<Transaction> mo44526e() {
        return new ArrayList(this.confirmedTransactionLinkedMap.values());
    }

    //mo44529f
    //910 mo44609e
    public List<Transaction> getUnConfirmedTransactionList() {
        return this.unConfirmedTransactionArrayMap.getValueList();
    }

    //mo44498a
    public boolean isTxInMapHasKey(COutPoint cOutPoint) {
        return this.txInMap.hasKey(cOutPoint);
    }

    //mo44481a
    public List<Transaction> getUnConfirmTransactionFromTxIn(TxIn txIn) {
        return getUnConfirmTransactionListFromPrevTxOut(txIn.getPrevTxOut());
    }

    //mo44505b
    public List<Transaction> getUnConfirmTransactionListFromPrevTxOut(COutPoint cOutPoint) {
        ArrayList arrayList = new ArrayList();
        List<TxIn> a = this.txInMap.mo43309a(cOutPoint);
        if (a == null || a.isEmpty()) {
            return arrayList;
        }
        HashMap hashMap = new HashMap();
        for (TxIn txIn : a) {
            UInt256 txid = txIn.getMTxid();
            if (!hashMap.containsKey(txid)) {
                Transaction unConfirmedTransaction = (Transaction) this.unConfirmedTransactionArrayMap.getValueSynchronized(txid);
                if (unConfirmedTransaction != null) {
                    hashMap.put(txid, unConfirmedTransaction);
                    arrayList.add(unConfirmedTransaction);
                }
            }
        }
        return arrayList;
    }

    //mo44484a
    public List<Transaction> getUnConfirmedTransactionList(CTxDestination des) {
        return this.addressToTxMap.getTransactionListByConfirmType(des, TransactionConfirmType.UNCONFIRMED);
    }

    /* renamed from: a */
    public Transaction mo44477a(byte[] bArr) {
        return (Transaction) this.allTransactionHashMap.get(new UInt256(bArr));
    }

    //mo44475a
    public Transaction getTransactionByClueTxid(String clueTxid) {
        return getTransactionFromAllTransactionMap(new UInt256(Utils.getReverseStringBytes(clueTxid), true));
    }

    public Transaction mo40454d(UInt256 uInt256) {
        Transaction b = getTransactionFromAllTransactionMap(uInt256);
        if (b == null) {
            return null;
        }
        mo44493a( b);
        List<TxOut> d = b.getSelfTxOutList();
        if (d != null) {
            for (TxOut dnVar : d) {
                CTxDestination l = dnVar.getScriptCTxDestination();
                if (l != null && !(l instanceof CNoDestination)) {
                    Address b2 = this.wallet.getAddressByCTxDestinationFromArrayMap(l);
                    if (b2 != null) {
                        CTxDestination u = b2.getCTxDestination();
                        COutPoint n = dnVar.getCOutPoint();
                        if (!this.desToCOutPointMultiMap.containsEntry(u, n)) {
                            this.desToCOutPointMultiMap.put(b2.getCTxDestination(), n);
                        }
                    }
                }
            }
        }
        return b;
    }

    public void mo44493a(TransactionInteface diVar) {
        this.txInMap.mo43312a(diVar);
        ArrayList arrayList = new ArrayList();
        this.txInMap.mo43313a(diVar, arrayList);
        mo40444a((Collection<UInt256>) arrayList);
    }

    //mo44504b
    //910 mo44588b
    public Transaction getTransactionFromAllTransactionMap(UInt256 txid) {
        Transaction transaction = (Transaction) this.allTransactionHashMap.get(txid);
        if (transaction != null) {
            return transaction;
        }
        Transaction transaction1 = (Transaction) this.confirmedRelayedTransactionMap.get(txid);
        if (transaction1 != null) {
            return transaction1;
        }
        return (Transaction) this.invalidateTransactionMap.get(txid);
    }

    //mo44518c
    public boolean isAllTransactionMapContainsKey(UInt256 txid) {
        return this.allTransactionHashMap.containsKey(txid);
    }

    //mo44485a
    public List<Transaction> getTransactionListByConfirmType(CTxDestination des, TransactionConfirmType confirmType) {
        return this.addressToTxMap.getTransactionListByConfirmType(des, confirmType);
    }

    /* renamed from: a */
    public List<Transaction> mo44479a(int i, TransactionConfirmType ixVar) {
        Account account = this.wallet.getAccountFromSparseArr(i);
        if (account == null) {
            return null;
        }
        return mo44482a(account, ixVar);
    }

    public List<Transaction> mo44482a(Account account, TransactionConfirmType transactionConfirmType) {
        List<Address> addressList = account.getALLAddressList();
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        ArrayList<Transaction> arrayList = new ArrayList();
        for (Address address : addressList) {
            List<Transaction> transactionList = this.addressToTxMap.getTransactionListByConfirmType(address.getCTxDestination(), transactionConfirmType);
            if (transactionList != null && !transactionList.isEmpty()) {
                arrayList.addAll(transactionList);
            }
        }
        return arrayList;
    }

    //mo44522d
    public void addTransactionTxInOutToMap(Transaction transaction, boolean z) {
        List<TxIn> txInList = transaction.getSelfTxInList();
        List<TxOut> txOutList = transaction.getSelfTxOutList();
        UInt256 txId = transaction.getTxId();
        for (TxIn txIn : txInList) {
            if (txIn.getPrevTransactionTxOut() == null && !txIn.isCoinBaseVin()) {
                COutPoint prevTxOut = txIn.getPrevTxOut();
                Transaction preTransaction = getTransactionFromAllTransactionMap(prevTxOut.txid);
                if (preTransaction != null) {
                    txIn.setPreTxOut(preTransaction.getTxOut(prevTxOut.index));
                }
            }
            CTxDestination cTxDestination = txIn.getCTxDestination();
            if (cTxDestination != null && !cTxDestination.isNull()) {
                this.txInMap.checkAndAddPreTxOut2TxInSynchronized(txIn);
                if (this.wallet.getAddressByCTxDestinationFromArrayMap(cTxDestination) != null) {
                    this.addressToTxMap.checkAndCTxDestToTxid2True(cTxDestination, txId);
                }
            }
        }
        int i = 0;
        for (TxOut txOut : txOutList) {
            CTxDestination cTxDestination = txOut.getScriptCTxDestination();
            if (cTxDestination == null || cTxDestination.isNull()) {
                i++;
            } else {
                if (this.wallet.getAddressByCTxDestinationFromArrayMap(cTxDestination) != null) {
                    this.addressToTxMap.checkAndCTxDestToTxid2True(cTxDestination, txId);
                    COutPoint cOutPoint = this.wallet.getSelfWalletHelper().getNewCOutPointByTxidAndIndex(txId, i);
                    if (!this.desToCOutPointMultiMap.containsEntry(cTxDestination, cOutPoint)) {
                        this.desToCOutPointMultiMap.put(cTxDestination, cOutPoint);
                    }
                }
                i++;
            }
        }
    }

    //mo44516c
    public void checkIsConfirmAndAddToMap(Transaction transaction, boolean z) {
        boolean notNull = transaction.isConfirmed();
        this.allTransactionHashMap.put(transaction.getTxId(), transaction);
        if (notNull) {
            this.confirmedTransactionLinkedMap.put(transaction.getTxId(), transaction);
        } else {
            this.unConfirmedTransactionArrayMap.addKeyValueSynchronized(transaction.getTxId(), transaction);
        }
    }

    /* renamed from: b */
    public Transaction mo44503b(int i, TransactionConfirmType ixVar) {
        Account a = this.wallet.getAccountFromSparseArr(i);
        Transaction dhVar = null;
        if (a == null) {
            return null;
        }
        List<Address> f = a.getALLAddressList();
        if (f == null || f.isEmpty()) {
            return null;
        }
        for (Address u : f) {
            List<Transaction> a2 = this.addressToTxMap.getTransactionListByConfirmType(u.getCTxDestination(), ixVar);
            if (a2 != null && !a2.isEmpty()) {
                for (Transaction dhVar2 : a2) {
                    if (dhVar == null) {
                        dhVar = dhVar2;
                    }
                    if (dhVar2.getTimeStamp() > dhVar.getTimeStamp()) {
                        dhVar = dhVar2;
                    }
                }
            }
        }
        return dhVar;
    }


    //mo44525e
    public TxOut getTxOutByCOutPoint(COutPoint cOutPoint) {
        if (cOutPoint == null) {
            return null;
        }
        return mo44478a(cOutPoint.txid.data(), cOutPoint.index);
    }

    public TxOut mo44478a(byte[] bArr, int index) {
        Transaction transaction = getTransactionByTxidBytes(bArr);
        if (transaction == null) {
            return null;
        }
        return transaction.getTxOut(index);
    }

    //mo44537i
    public boolean isTransactionConfirmed(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if (transaction.getConfirms() > 0) {
            return true;
        }
        return !transaction.isDefaultHash() && !transaction.isNotConfirmed();
    }

    //mo40457f
    public boolean isCoutPointHasUsed(COutPoint cOutPoint) {
        ArrayList<UInt256> arrayList = new ArrayList<>();
        this.txInMap.getTxInTxidAndAddToCollectionSynchronized(cOutPoint, arrayList, new HashMap[0]);
        if (arrayList.isEmpty()) {
            return false;
        }
        for (UInt256 txid : arrayList) {
            if (isTransactionConfirmed(getTransactionFromAllTransactionMap(txid))) {
                return true;
            }
        }
        return false;
    }

    public CScript mo44512c(COutPoint outPoint) throws InvalidateUtxoException {
        TxOut e = getTxOutByCOutPoint(outPoint);
        if (e != null) {
            Address address = this.wallet.getAddressByCTxDestinationFromArrayMap(e.getScriptCTxDestination());
            if (address == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Can not fond address ");
                sb.append(e.getAddress());
                sb.append(" for vout ");
                sb.append(outPoint.toString());
                throw new InvalidateUtxoException(sb.toString());
            } else if (!(address instanceof BitcoinMultiSigAddress)) {
                return null;
            } else {
                try {
                    List a = ((BitcoinMultiSigAddress) address).mo40873a(this.wallet.getBlockChainType());
                    if (a != null && !a.isEmpty()) {
                        return ((CMultisigPubkey) address.getSelfPubKey()).mo9451j();
                    }
                } catch (AddressFormatException e2) {
                    e2.printStackTrace();
                }
                return null;
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("UTXO [");
            sb2.append(outPoint);
            sb2.append("] not exists.");
            throw new InvalidateUtxoException(sb2.toString());
        }
    }

    //mo44514c
    public void updateTransactionBlockHashAndNo(Transaction transaction) {
        this.transactionTable.getDbWritableDataHelper().update(this.transactionTable.getTableName(), transaction.getBlockHashAndNoContentValues(), String.format(Locale.US, "%s=?", new Object[]{"txid"}), new String[]{transaction.getTxId().toString()});
    }

    public void mo40452b(Transaction transaction, boolean needUpdate) {
        if (needUpdate) {
            updateTransactionBlockHashAndNo(transaction);
        }
        Transaction localTransaction = getTransactionFromAllTransactionMap(transaction.getTxId());
        UInt256 localTxId = localTransaction.getTxId();
        Transaction unConfirmedTransaction = (Transaction) this.unConfirmedTransactionArrayMap.removeAndGet(localTxId);
        if (unConfirmedTransaction != null && !this.confirmedTransactionLinkedMap.containsKey(unConfirmedTransaction.getTxId())) {
            this.confirmedTransactionLinkedMap.put(unConfirmedTransaction.getTxId(), unConfirmedTransaction);
        }
        if (this.invalidateTransactionMap.containsKey(localTxId)) {
            this.invaildTxTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) localTransaction);
            this.invalidateTransactionMap.remove(localTxId);
        }
        Vector<Transaction> vector = new Vector<>();
        UInt256 txId = transaction.getTxId();
        List<TxIn> txInList = transaction.getSelfTxInList();
        Vector<UInt256> txInTxidVector = new Vector<>();
        if (!txInList.isEmpty()) {
            for (TxIn txIn : txInList) {
                txInTxidVector.clear();
                this.txInMap.getTxInTxidAndAddToCollectionSynchronized(txIn.getPrevTxOut(), txInTxidVector);
                if (!txInTxidVector.isEmpty()) {
                    for (UInt256 txid : txInTxidVector) {
                        if (!txid.equals(txId)) {
                            Transaction tempTrans = getTransactionFromAllTransactionMap(txid);
                            mo44507b(tempTrans, (List<Transaction>) vector);
                            vector.add(tempTrans);
                        }
                    }
                }
            }
        }
        if (this.transactionEvent != null && !vector.isEmpty()) {
            this.transactionEvent.mo44097c(vector);
        }
    }

    //910 mo44591b
    public void mo44507b(Transaction transaction, List<Transaction> list) {
        if (this.allTransactionHashMap.containsKey(transaction.getTxId())) {
            mo44515c(transaction, list);
        }
    }

    //mo44486a
    public List<Utxo> getSpendUtxoList(boolean includeFee, List<Utxo> list, long sendValue, long feeValue) {
        if (list == null) {
            return null;
        }
        TransactionUtils.sortUtxoList(list);
        long sumUtxoValue = 0;
        if (!includeFee) {
            sendValue += feeValue;
        }
        ArrayList<Utxo> arrayList = new ArrayList<Utxo>();
        for (Utxo utxo : list) {
            arrayList.add(utxo);
            sumUtxoValue += utxo.getValue();
            int i = (sumUtxoValue > sendValue ? 1 : (sumUtxoValue == sendValue ? 0 : -1));
            if (i > 0 || (includeFee && i == 0)) {
                break;
            }
        }
        return arrayList;
    }

    public List<Utxo> mo40427a(boolean z) {
        CopyOnWriteArraySet<COutPoint> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
        Set<CTxDestination> keySet = this.desToCOutPointMultiMap.keySet();
        if (keySet == null || keySet.isEmpty()) {
            return new Vector(1);
        }
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        for (CTxDestination oVar : keySet) {
            Address a = addressModel.getAddressByCTxDestinationFromUsingAddressMap(oVar);
            if (a == null || !a.isRecycleFlag()) {
                copyOnWriteArraySet.addAll(this.desToCOutPointMultiMap.get(oVar));
            }
        }
        if (copyOnWriteArraySet.isEmpty()) {
            return null;
        }
        Vector<Utxo> vector = new Vector<>();
        for (COutPoint cOutPoint : copyOnWriteArraySet) {
            Transaction transaction = getTransactionFromAllTransactionMap(cOutPoint.txid);
            if (transaction != null && !isCoutPointHasUsed(cOutPoint)) {
                vector.add(new Utxo(transaction.getTxOut(cOutPoint.index)));
            }
        }
        return vector;
    }

    //mo44490a
    public final void removeTransactionsSinceStartBlockNo(long startBlockNo, boolean deleteUnConfirmedTransaction) {
        if (startBlockNo < 1) {
            startBlockNo = 1;
        }
        ArrayList<Transaction> waitDeleteTransactionList = new ArrayList<>();
        for (Transaction transaction : this.confirmedTransactionLinkedMap.values()) {
            if (transaction.checkAndGetBlockNoFromAll() >= startBlockNo) {
                waitDeleteTransactionList.add(transaction);
            }
        }
        if (deleteUnConfirmedTransaction) {
            waitDeleteTransactionList.addAll(this.unConfirmedTransactionArrayMap.getValueList());
        }
        for (Transaction transaction : this.invalidateTransactionMap.values()) {
            if (transaction.checkAndGetBlockNoFromAll() >= startBlockNo) {
                waitDeleteTransactionList.add(transaction);
            }
        }
        TransactionDB transactionDB = this.wallet.getSelfTransactionDB();
        transactionDB.beginTransaction();
        for (Transaction transaction : waitDeleteTransactionList) {
            this.transactionTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) transaction);
            this.addressToTxMap.removeFromCTxDesToTxid2BoolByTxid(transaction.getTxId());
            this.allTransactionHashMap.remove(transaction.getTxId());
            this.invalidateTransactionMap.remove(transaction.getTxId());
            this.invaildTxTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) transaction);
        }
        transactionDB.endTransaction(true);
    }

    public Transaction createNewContractCallTransaction(CharSequence charSequence, List<COutPoint> cOutPointList, ContractCallInfo contractCallInfo,
                                                        List<AddressMoneyInfo> addressMoneyInfoList, long fee,
                                                        VOutList vOutList, ScriptList scriptList, TxInfo... txInfos) throws InvalidateUtxoException, AddressFormatException, AddressNotFoundException, IOException, SignatureFailedException, EncryptException, NoPrivateKeyException, NotMatchException {
        if (cOutPointList == null || cOutPointList.isEmpty()) {
            throw new IllegalArgumentException("Do not have any utxo info.");
        } else if (addressMoneyInfoList == null || addressMoneyInfoList.isEmpty()) {
            throw new IllegalArgumentException("Do not have any output info.");
        }else if (contractCallInfo == null) {
            throw new IllegalArgumentException("Do not have any contract info.");
        } else {
            List voutList = (vOutList == null ? new VOutList() : vOutList).txOutList;
            if (voutList == null) {
                voutList = new ArrayList();
            }
            String str = null;
            long txOutsValue = 0;
            for (COutPoint cOutPoint : cOutPointList) {
                if (!isCoutPointHasUsed(cOutPoint)) {
                    TxOut txOut = getTxOutByCOutPoint(cOutPoint);
                    if (txOut != null) {
                        Address address = this.wallet.getAddressByCTxDestinationFromArrayMap(txOut.getScriptCTxDestination());
                        if (address != null) {
                            if (str == null) {
                                str = address.getAddressString(this.wallet.getBlockChainType());
                            }
                            txOutsValue += txOut.getSatoshi();
                            if (scriptList != null) {
                                if (address instanceof BitcoinMultiSigAddress) {
                                    scriptList.mo44673a((CMultisigPubkey) address.getSelfPubKey());
                                } else {
                                    scriptList.addToCScriptList((CScript) null);
                                }
                            }
                            voutList.add(txOut);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Can not fond address ");
                            sb.append(txOut.getAddress());
                            sb.append(" for vout ");
                            sb.append(cOutPoint.toString());
                            throw new InvalidateUtxoException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("UTXO [");
                        sb2.append(addressMoneyInfoList.toString());
                        sb2.append("] not exists.");
                        throw new InvalidateUtxoException(sb2.toString());
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("UTXO [");
                    sb3.append(cOutPoint.toString());
                    sb3.append("] was already spent.");
                    throw new InvalidateUtxoException(sb3.toString());
                }
            }
            Transaction newTransaction = this.wallet.getSelfWalletHelper().getNewTransaction();
            ConvertedTxIns convertedTxIns = new ConvertedTxIns(this.wallet);
            convertedTxIns.addContractTxOutList(voutList);
            ConvertedTxOuts convertedTxOuts = new ConvertedTxOuts(this.wallet);
            convertedTxOuts.addAddressMoneyInfo(addressMoneyInfoList, (List<TxOut>[]) new List[0]);
            convertedTxOuts.addContractCallInfo(contractCallInfo);
            newTransaction.setVersion(1);
            newTransaction.checkAndSetLockTime(Math.max(0, this.wallet.getCurrentBlockNo() - 10));
            m12904a(fee, convertedTxIns.sumVinsValue - convertedTxOuts.sumValue, convertedTxIns, convertedTxOuts);
            newTransaction.setTxInAndTxOutAndSetIndex((List<TxIn>) convertedTxIns, (List<TxOut>) convertedTxOuts, false);
            newTransaction.updateTxidByContent();
            newTransaction.recomputeByTxs(null);
            Iterator it = convertedTxOuts.iterator();
            long minerAddressValue = 0;
            long receiveAddressValue = 0;
            while (it.hasNext()) {
                TxOut txOut = (TxOut) it.next();
                if (convertedTxOuts.indexOf(txOut) > 0) {
                    receiveAddressValue += txOut.getSatoshi();
                } else {
                    minerAddressValue += txOut.getSatoshi();
                }
            }
            if (txInfos.length > 0) {
                String str2 = ((AddressMoneyInfo) addressMoneyInfoList.get(0)).receiveAddress;
                TxInfo txInfo = txInfos[0];
                txInfo.setSumVinValue(CAmount.toDecimalSatoshiString(Long.valueOf(txOutsValue)));
                txInfo.mo44675a(str);
                txInfo.setSendSumValue(CAmount.toDecimalSatoshiString(Long.valueOf(minerAddressValue)));
                txInfo.setReceiveAddress(str2);
                txInfo.setSelfAddressValue(CAmount.toDecimalSatoshiString(Long.valueOf(receiveAddressValue)));
                txInfo.mo44679c(str);
                txInfo.setFeeValue(CAmount.toDecimalSatoshiString(Long.valueOf((txOutsValue - minerAddressValue) - receiveAddressValue)));
                txInfo.setSelfPaySumValue(CAmount.toDecimalSatoshiString(Long.valueOf(txOutsValue - receiveAddressValue)));
            }
            return newTransaction.getCloneTransaction();
        }
    }

    //mo40424a
    public Transaction createNewTransaction(CharSequence charSequence, List<COutPoint> cOutPointList, List<AddressMoneyInfo> addressMoneyInfoList, long fee, VOutList vOutList, ScriptList scriptList, TxInfo... txInfos) throws InvalidateUtxoException, AddressFormatException, AddressNotFoundException, IOException, SignatureFailedException, EncryptException, NoPrivateKeyException, NotMatchException {
        if (cOutPointList == null || cOutPointList.isEmpty()) {
            throw new IllegalArgumentException("Do not have any utxo info.");
        } else if (addressMoneyInfoList == null || addressMoneyInfoList.isEmpty()) {
            throw new IllegalArgumentException("Do not have any output info.");
        } else {
            List voutList = (vOutList == null ? new VOutList() : vOutList).txOutList;
            if (voutList == null) {
                voutList = new ArrayList();
            }
            String str = null;
            long txOutsValue = 0;
            for (COutPoint cOutPoint : cOutPointList) {
                if (!isCoutPointHasUsed(cOutPoint)) {
                    TxOut txOut = getTxOutByCOutPoint(cOutPoint);
                    if (txOut != null) {
                        Address address = this.wallet.getAddressByCTxDestinationFromArrayMap(txOut.getScriptCTxDestination());
                        if (address != null) {
                            if (str == null) {
                                str = address.getAddressString(this.wallet.getBlockChainType());
                            }
                            txOutsValue += txOut.getSatoshi();
                            if (scriptList != null) {
                                if (address instanceof BitcoinMultiSigAddress) {
                                    scriptList.mo44673a((CMultisigPubkey) address.getSelfPubKey());
                                } else {
                                    scriptList.addToCScriptList((CScript) null);
                                }
                            }
                            voutList.add(txOut);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Can not fond address ");
                            sb.append(txOut.getAddress());
                            sb.append(" for vout ");
                            sb.append(cOutPoint.toString());
                            throw new InvalidateUtxoException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("UTXO [");
                        sb2.append(addressMoneyInfoList.toString());
                        sb2.append("] not exists.");
                        throw new InvalidateUtxoException(sb2.toString());
                    }
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("UTXO [");
                    sb3.append(cOutPoint.toString());
                    sb3.append("] was already spent.");
                    throw new InvalidateUtxoException(sb3.toString());
                }
            }
            Transaction newTransaction = this.wallet.getSelfWalletHelper().getNewTransaction();
            ConvertedTxIns convertedTxIns = new ConvertedTxIns(this.wallet);
            convertedTxIns.addTxOutList(voutList);
            ConvertedTxOuts convertedTxOuts = new ConvertedTxOuts(this.wallet);
            convertedTxOuts.addAddressMoneyInfo(addressMoneyInfoList, (List<TxOut>[]) new List[0]);
            m12904a(fee, convertedTxIns.sumVinsValue - convertedTxOuts.sumValue, convertedTxIns, convertedTxOuts);
            newTransaction.setTxInAndTxOutAndSetIndex((List<TxIn>) convertedTxIns, (List<TxOut>) convertedTxOuts, false);
            newTransaction.updateTxidByContent();
            newTransaction.recomputeByTxs(null);
            Iterator it = convertedTxOuts.iterator();
            long minerAddressValue = 0;
            long receiveAddressValue = 0;
            while (it.hasNext()) {
                TxOut txOut = (TxOut) it.next();
                if (convertedTxOuts.indexOf(txOut) > 0) {
                    receiveAddressValue += txOut.getSatoshi();
                } else {
                    minerAddressValue += txOut.getSatoshi();
                }
            }
            if (txInfos.length > 0) {
                String str2 = ((AddressMoneyInfo) addressMoneyInfoList.get(0)).receiveAddress;
                TxInfo txInfo = txInfos[0];
                txInfo.setSumVinValue(CAmount.toDecimalSatoshiString(Long.valueOf(txOutsValue)));
                txInfo.mo44675a(str);
                txInfo.setSendSumValue(CAmount.toDecimalSatoshiString(Long.valueOf(minerAddressValue)));
                txInfo.setReceiveAddress(str2);
                txInfo.setSelfAddressValue(CAmount.toDecimalSatoshiString(Long.valueOf(receiveAddressValue)));
                txInfo.mo44679c(str);
                txInfo.setFeeValue(CAmount.toDecimalSatoshiString(Long.valueOf((txOutsValue - minerAddressValue) - receiveAddressValue)));
                txInfo.setSelfPaySumValue(CAmount.toDecimalSatoshiString(Long.valueOf(txOutsValue - receiveAddressValue)));
            }
            return newTransaction.getCloneTransaction();
        }
    }

    private long m12904a(long fee, long computedFee, ConvertedTxIns convertedTxIns, ConvertedTxOuts convertedTxOuts) {
        if (convertedTxIns.isEmpty()) {
            return 0;
        }
        if (fee > 0) {
            checkTransactionFeeVaild(computedFee, TxAddrDirectType.BITCOIN_BITCOIN);
            int i = (fee > computedFee ? 1 : (fee == computedFee ? 0 : -1));
            if (i != 0) {
                if (i > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("The param fee ");
                    sb.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(fee)));
                    sb.append(" was bigger than computed fee ");
                    sb.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(computedFee)));
                    throw new IllegalArgumentException(sb.toString());
                } else if (convertedTxIns.mineAddressString == null || convertedTxIns.mineAddressString.isEmpty()) {
                    throw new IllegalArgumentException("Can not find any mine address in txins");
                }
            }
        } else if (computedFee <= 0) {
            return 0;
        } else {
            checkTransactionFeeVaild(computedFee, TxAddrDirectType.BITCOIN_BITCOIN);
        }
        long j3 = (convertedTxIns.sumVinsValue - convertedTxOuts.sumValue) - fee;
        if (j3 <= 0) {
            return 0;
        }
        SeriableOuts seriableOuts = new SeriableOuts(this.wallet);
        seriableOuts.setTxOutList((List<TxOut>) convertedTxOuts);
        try {
            fee = (long) Math.max((seriableOuts.serialToStream().length + 148) * 3, 30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (j3 >= fee) {
            convertedTxOuts.addNewTxOut(((TxIn) convertedTxIns.get(0)).getCTxDestination(), j3);
        }
        return j3;
    }

    //mo44489a
    public void checkTransactionFeeVaild(long computedFee, TxAddrDirectType txAddrDirectType) {
        if (computedFee > 0) {
            UInt64 uInt64 = new UInt64(computedFee);
            ChainParams chainParams = this.wallet.getChainParams();
            if (txAddrDirectType != TxAddrDirectType.BITCOIN_BITCOIN) {
                return;
            }
            if (computedFee < chainParams.minFee.getValue()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Transaction's fee ");
                sb.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(computedFee)));
                sb.append(" must bigger than ");
                sb.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(chainParams.minFee.getValue())));
                throw new IllegalArgumentException(sb.toString());
            } else if (uInt64.mo18944a((Object) chainParams.maxFee) != -1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Transaction's fee ");
                sb2.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(computedFee)));
                sb2.append(" must smaller than ");
                sb2.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(chainParams.maxFee.getValue())));
                throw new IllegalArgumentException(sb2.toString());
            }
        } else {
            throw new IllegalArgumentException("The transaction must contains fee");
        }
    }

    //mo44473a
    public long calMinFee(boolean checkMaxFee, List<COutPoint> list, List<AddressMoneyInfo> list2, long fee, VOutList mbVar, ScriptList lvVar) throws InvalidateUtxoException, AddressFormatException, SignatureFailedException, EncryptException, NoPrivateKeyException, AddressNotFoundException, IOException, NotMatchException {
        Transaction transaction = createNewTransaction(null, list, list2, fee, mbVar, lvVar, new TxInfo[0]);
        if (checkMaxFee) {
            return Math.max(500000, transaction.calMinFee());
        }
        return transaction.calMinFee();
    }

    public boolean mo44524d(COutPoint cOutPoint) {
        Transaction transaction = this.wallet.getTransactionFromAllMap(cOutPoint.txid);
        if (transaction != null && !isTxInMapHasKey(cOutPoint) && !transaction.isTransactionLock() && transaction.getTxOut(cOutPoint.index).getSatoshi() > 0) {
            return true;
        }
        return false;
    }

    //mo44527e
    public void noticeTransactionAbanded(Transaction transaction) {
        if (this.transactionEvent != null && transaction.isRelatedToLocalAddress()) {
            this.transactionEvent.onTransactionAbanded(transaction);
        }
    }

    public boolean mo44519c(String str) {
        return mo44532g(new UInt256(Utils.getReverseStringBytes(str), true));
    }

    //mo44488a
    public TxSignatureResult signTransactionAndGetResult(List<Utxo> spendUtxoList, List<AddressMoneyInfo> addressMoneyInfoList, long fee, CharSequence pwd) throws TxSizeException, NotMatchException, EncryptException, InvalidateUtxoException, IOException, AddressNotFoundException, AddressFormatException, NoPrivateKeyException, SignatureFailedException {
        this.wallet.checkWalletPassword(pwd);
        TxInfo txInfo = new TxInfo();
        TxSignatureResult txSignatureResult = signTransaction(createNewTransaction(pwd, vdsMain.Utils.getCoutPointUtxoFromUtxoList(spendUtxoList), addressMoneyInfoList, fee, null, null, txInfo), pwd);
        txSignatureResult.setTxInfo(txInfo);
        checkTransactionSize(txSignatureResult.transaction);
        return txSignatureResult;
    }

    public TxSignatureResult signContractCallTransactionAndGetResult(List<Utxo> spendUtxoList,ContractCallInfo contractCallInfo,List<AddressMoneyInfo> addressMoneyInfoList, long fee, CharSequence pwd) throws TxSizeException, NotMatchException, EncryptException, InvalidateUtxoException, IOException, AddressNotFoundException, AddressFormatException, NoPrivateKeyException, SignatureFailedException {
        this.wallet.checkWalletPassword(pwd);
        TxInfo txInfo = new TxInfo();
        TxSignatureResult txSignatureResult = signTransaction(createNewContractCallTransaction(pwd, vdsMain.Utils.getCoutPointUtxoFromUtxoList(spendUtxoList), contractCallInfo, addressMoneyInfoList, fee, null, null, txInfo), pwd);
        txSignatureResult.setTxInfo(txInfo);
        checkTransactionSize(txSignatureResult.transaction);
        return txSignatureResult;
    }

    public void mo44492a(UInt256 uInt256, RejectMessageInterface kzVar) {
        Transaction b = getTransactionFromAllTransactionMap(uInt256);
        if (b != null) {
            b.initDefaultBlockHashIfNull();
            this.transactionTable.updateBlockHash(uInt256, b.getBlockHash());
            RejectTx dgVar = new RejectTx();
            dgVar.setTable(this.rejectTxTable);
            dgVar.mo43225a(uInt256);
            dgVar.mo43226a(kzVar.mo42585d());
            dgVar.mo43224a(kzVar.mo42584b());
            this.rejectTxTable.replace((AbstractTableItem) dgVar);
            try {
                Log.e("TransactionModel", String.format("setTransactionAbanded: %s  reason: %s", new Object[]{uInt256.toString(), kzVar.mo42585d()}));
            } catch (Exception e) {
                e.printStackTrace();
            }
            noticeTransactionAbanded(b);
        }
    }

    public OfflineTransaction mo40428a(CharSequence charSequence, List<COutPoint> list, List<AddressMoneyInfo> list2, long j) throws NotMatchException, EncryptException, InvalidateUtxoException, IOException, AddressNotFoundException, AddressFormatException, NoPrivateKeyException, SignatureFailedException, TxSizeException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Do not have any utxo info.");
        }
        OfflineTransaction e = this.wallet.getSelfWalletHelper().mo42419e();
        e.f13338a = createNewTransaction(charSequence, list, list2, j, e.f13339b, e.f13340c, new TxInfo[0]);
        e.f13343f = e.f13338a.mo43263o();
        e.f13344g = j;
        e.mo44669d();
        List<AddressMoneyInfo> list3 = list2;
        e.mo44666a(AddressUtils.getDesFromAddressString((CharSequence) ((AddressMoneyInfo) list2.get(0)).receiveAddress, this.wallet));
        checkTransactionSize(e.f13338a);
        return e;
    }

    public boolean mo44501a(TxOut dnVar) {
        return mo44524d(dnVar.getCOutPoint());
    }

    public void mo44528e(Transaction transaction, boolean z) throws TxSizeException, UtxoAlreadySpendException, UtxoNotFoundException, InvalidateTransactionException {
        List<TxIn> txInList = transaction.getSelfTxInList();
        List<TxOut> txOutList = transaction.getSelfTxOutList();
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        checkTransactionSize(transaction);
        boolean isSigned = false;
        if (!z || transaction.mo43053a(new VOutList[0])) {
            long spendMoney = 0;
            long ownedMoney = 0;
            boolean z3 = false;
            for (TxIn txIn : txInList) {
                if (!addressModel.isUsingDesAddressMapHasKey(txIn.getCTxDestination())) {
                    z3 = true;
                } else {
                    COutPoint prevTxOut = txIn.getPrevTxOut();
                    if (!isCoutPointHasUsed(prevTxOut)) {
                        TxOut preTxOut = getTxOutByCOutPoint(prevTxOut);
                        if (preTxOut != null) {
                            ownedMoney += preTxOut.getSatoshi();
                            isSigned = true;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("can not find utxo ");
                            sb.append(txIn.getPrevTxOut().toString());
                            sb.append(" for tx ");
                            sb.append(transaction.getTransactionHexString());
                            throw new UtxoNotFoundException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Tx out was already spent: ");
                        sb2.append(txIn.getPrevTxOut().toString());
                        throw new UtxoAlreadySpendException(sb2.toString());
                    }
                }
            }
            if (isSigned) {
                for (TxOut txOut : txOutList) {
                    spendMoney += txOut.getSatoshi();
                }
                int i = (spendMoney > ownedMoney ? 1 : (spendMoney == ownedMoney ? 0 : -1));
                if (i >= 0 && !z3) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Transaction's spent ");
                    sb3.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(spendMoney)));
                    sb3.append(" must smaller than the money ");
                    sb3.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(ownedMoney)));
                    sb3.append(" you owned, tx: ");
                    sb3.append(transaction.getTransactionHexString());
                    throw new InvalidateTransactionException(sb3.toString());
                } else if (i > 0) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Transaction's spent ");
                    sb4.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(spendMoney)));
                    sb4.append(" is bigger than the money ");
                    sb4.append(ownedMoney);
                    sb4.append(" you owned, tx: ");
                    sb4.append(transaction.getTransactionHexString());
                    throw new InvalidateTransactionException(sb4.toString());
                }
            }
        } else {
            throw new IllegalStateException("Tansaction not signed!");
        }
    }

    public void mo44497a(CTxDestination oVar, HashMap<CTxDestination, Address> hashMap, HashMap<Integer, Account> hashMap2) {
        if (!hashMap.containsKey(oVar)) {
            Address b = this.wallet.getAddressByCTxDestinationFromArrayMap(oVar);
            AccountModel E = this.wallet.getSelfAccountModel();
            if (b != null) {
                hashMap.put(oVar, b);
                if (hashMap2.containsKey(Integer.valueOf(b.getAccount()))) {
                    hashMap2.put(Integer.valueOf(b.getAccount()), E.getAccountFromSparseArr(b.getAccount()));
                }
            }
        }
    }

    //mo40469o
    private void addCachedAddressFromTx(Transaction transaction) {
        if (getUseTransaction() && transaction.isRelatedToLocalAddress()) {
            AddressModel addressModel = this.wallet.getSelfAddressModel();
            List<TxOut> txOutList = transaction.getSelfTxOutList();
            if (txOutList != null && !txOutList.isEmpty()) {
                try {
                    for (TxOut txOut : txOutList) {
                        CTxDestination txOutDes = txOut.getScriptCTxDestination();
                        List<Address> addressList = addressModel.mo43101d(txOutDes);
                        if (addressList == null || addressList.isEmpty()) {
                            addressModel.mo43116j(txOutDes);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    vdsMain.Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Failed to add cached address from tx %s \n%s", new Object[]{transaction.getTxidHashString(), StringToolkit.m11523a((Throwable) e)}));
                }
            }
        }
    }

    public void initAllDataFromDb() {
        boolean d = this.transactionTable.isInTransaction();
        if (!d) {
            this.transactionTable.beginTransaction();
        }
        selectAllFromTransactionTable();
        initDataFromRelayedTable();
        initDataFromVxdRelayedTable();
        initDataFromInvaildTxTable();
        selectAllFromTxRemarksTable();
        fillConfirmedBlockHashToTxidsMap();
        if (!d) {
            this.transactionTable.endTransaction(false);
        }
    }

    public void mo40436a(Cursor cursor) {
        Transaction transaction = (Transaction) this.transactionTable.getCompleteTableItem(cursor);
        while (transaction != null) {
            transaction.recomputeByTxs(null);
            replaceRelatedTransactionWithAddress(transaction, true);
            transaction = (Transaction) this.transactionTable.getCompleteTableItem(cursor);
        }
    }

    //mo44513c
    public TxSendResult checkPwdAndSendTransactionToPeer(Transaction transaction, CharSequence charSequence) throws NotMatchException, TxSizeException, UtxoNotFoundException, InvalidateTransactionException, CoolWalletException, UtxoAlreadySpendException {
        this.wallet.checkWalletPassword(charSequence);
        return checkAndSendTransactionToPeers(transaction);
    }

    //mo44536i
    public void selectAllFromTransactionTable() {
        Cursor cursor = this.transactionTable.selectAll();
        try {
            mo40436a(cursor);
            BLOCK_CHAIN_TYPE blockChainType = this.wallet.getBlockChainType();
            Iterator accountIterator = getWalllet().getSelfAccountModel().getAccountVector().iterator();
            while (accountIterator.hasNext()) {
                ((Account) accountIterator.next()).mo44136a(blockChainType);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            vdsMain.Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Error occured when load transaction...", new Object[0]));
        }
        cursor.close();
    }



    //mo44517c
    public void checkAndAddUnConfirmTxid(List<UInt256> txidList) {
        TransactionTable rtxTable = this.wallet.getSelfTransactionDB().getSelfRelayedTxTable();
        for (UInt256 txid : txidList) {
            if (!this.confirmedRelayedTransactionMap.containsKey(txid) && !this.unConfirmRelayedTxidMap.containsKey(txid)) {
                this.unConfirmRelayedTxidMap.put(txid, Boolean.TRUE);
                rtxTable.replaceUnConfirmTransaction(txid);
            }
        }
    }

    public void checkAndAddUnConfirmVxdTxid(UInt256 txid) {
        TransactionTable vxdRtxTable = this.wallet.getSelfTransactionDB().getSelfRelayedVxdTable();
        if (!this.confirmedVxdTransactionMap.containsKey(txid) && !this.unConfirmVxdTxidMap.containsKey(txid)) {
            this.unConfirmVxdTxidMap.put(txid, Boolean.TRUE);
            vxdRtxTable.replaceUnConfirmTransaction(txid);
        }
    }

    //mo44538j
    public void initDataFromRelayedTable() {
        TransactionTable relayedTxTable = this.wallet.getSelfTransactionDB().getSelfRelayedTxTable();
        int version = relayedTxTable.getDbWritableDataHelper().getVersion();
        WalletHelper walletHelper = this.wallet.getSelfWalletHelper();
        CWallet cWallet = this.wallet.getSelfCWallet();
        Cursor cursor = relayedTxTable.selectAll();
        while (cursor.moveToNext()) {
            try {
                UInt256 txid = new UInt256();
                txid.setHash(cursor.getString(0));
                if (cursor.getInt(1) == -1) {
                    this.unConfirmRelayedTxidMap.put(txid, Boolean.valueOf(true));
                } else {
                    Transaction transaction = walletHelper.getNewVCWalletTx(null);
                    transaction.setSelfCWallet(cWallet);
                    transaction.setWallet(this.wallet);
                    transaction.initAllTableItemVariable(cursor, version, 0);
                    this.confirmedRelayedTransactionMap.put(txid, transaction);
                }
            } catch (Exception e) {
                e.printStackTrace();
                vdsMain.Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Error occured when load relayed transaction...", new Object[0]));
            }
        }
        cursor.close();
    }

    public void initDataFromVxdRelayedTable() {
        TransactionTable relayedVxdTxTable = this.wallet.getSelfTransactionDB().getSelfRelayedVxdTable();
        int version = relayedVxdTxTable.getDbWritableDataHelper().getVersion();
        WalletHelper walletHelper = this.wallet.getSelfWalletHelper();
        CWallet cWallet = this.wallet.getSelfCWallet();
        Cursor cursor = relayedVxdTxTable.selectAll();
        while (cursor.moveToNext()) {
            try {
                UInt256 txid = new UInt256();
                txid.setHash(cursor.getString(0));
                if (cursor.getInt(1) == -1) {
                    this.unConfirmVxdTxidMap.put(txid, Boolean.TRUE);
                } else {
                    Transaction transaction = walletHelper.getNewVCWalletTx(null);
                    transaction.setSelfCWallet(cWallet);
                    transaction.setWallet(this.wallet);
                    transaction.initAllTableItemVariable(cursor, version, 0);
                    this.confirmedVxdTransactionMap.put(txid, transaction);
                }
            } catch (Exception e) {
                e.printStackTrace();
                vdsMain.Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Error occured when load relayed vxd transaction...", new Object[0]));
            }
        }
        cursor.close();
    }

    //mo44540k
    public void initDataFromInvaildTxTable() {
        int version = this.invaildTxTable.getDbWritableDataHelper().getVersion();
        WalletHelper walletHelper = this.wallet.getSelfWalletHelper();
        CWallet cWallet = this.wallet.getSelfCWallet();
        Cursor cursor = this.invaildTxTable.selectAll();
        while (cursor.moveToNext()) {
            try {
                UInt256 txid = new UInt256();
                txid.setHash(cursor.getString(0));
                Transaction transaction = walletHelper.getNewVCWalletTx(null);
                transaction.setSelfCWallet(cWallet);
                transaction.setWallet(this.wallet);
                transaction.initAllTableItemVariable(cursor, version, 0);
                List<TxIn> txInList = transaction.getSelfTxInList();
                if (txInList != null && !txInList.isEmpty()) {
                    for (TxIn txIn : txInList) {
                        CTxDestination des = txIn.getCTxDestination();
                        if (des != null) {
                            if (!des.isNull()) {
                                this.addressToTxMap.checkAndCTxDestToTxid2True(des, txid);
                            }
                        }
                    }
                }
                this.invalidateTransactionMap.put(txid, transaction);
            } catch (Exception e3) {
                e3.printStackTrace();
                vdsMain.Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Error occured when load invalidate transaction...", new Object[0]));
            }
        }
        cursor.close();
    }

    //mo44541k
    //910 mo44624l
    public void replaceData(Transaction transaction) {
        this.transactionTable.replaceDataSynchronized((AbstractTableItem) transaction);
    }

    //mo40466m
    private void selectAllFromTxRemarksTable() {
        Cursor e = this.txMarkTable.selectAll();
        while (e.moveToNext()) {
            try {
                UInt256 uInt256 = new UInt256();
                uInt256.setHash(e.getString(0));
                this.txIdToRemarkMap.put(uInt256, e.getString(1));
            } catch (Exception e2) {
                e2.printStackTrace();
                vdsMain.Log.LogObjError((Object) this, String.format(Locale.getDefault(), "Error when load readTxMarks...", new Object[0]));
            }
        }
        e.close();
    }

    //mo44534h
    public void fillConfirmedBlockHashToTxidsMap() {
        this.wallet.getSelfBlockChainModel().fillConfirmedBlockHashToTxidsMap();
    }

    public void mo44531f(UInt256 txid) {
        TransactionTable transactionTable = this.wallet.getSelfTransactionDB().getSelfRelayedTxTable();
        if (!this.confirmedRelayedTransactionMap.containsKey(txid) && !this.unConfirmRelayedTxidMap.containsKey(txid)) {
            this.unConfirmRelayedTxidMap.put(txid, Boolean.TRUE);
            transactionTable.replaceUnConfirmTransaction(txid);
            this.wallet.getSelfPeerManager().mo44621a(txid);
        }
    }

    //mo44521d
    //910 mo44610e
    public void notifyTransactionReceive(Transaction transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("sendTransactionReceivedMessage: ");
        sb.append(transaction.getTxidHashString());
        Log.i("TransactionModel", sb.toString());
        if (this.transactionEvent != null && transaction.isRelatedToLocalAddress()) {
            this.transactionEvent.onTransactionReceive(transaction);
        }
    }

    //910 mo44601c
    //mo44510b
    public boolean processNewTransaction(Transaction transaction) {
        boolean hasRelatedAddress=false;
        List txInList = transaction.getSelfTxInList();
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        if (!txInList.isEmpty()) {
            Iterator txInIterator = txInList.iterator();
            while (true) {
                if (!txInIterator.hasNext()) {
                    break;
                }
                CTxDestination cTxDestination = ((TxIn) txInIterator.next()).getCTxDestination();
                if (cTxDestination != null) {
                    Address address = addressModel.getAddressByCTxDestinationFromUsingAddressMap(cTxDestination);
                    if (address == null) {
                        address = addressModel.getAddressFromUnuseAddressMap(cTxDestination);
                    }
                    if (address == null) {
                        address = addressModel.getAddressFromUsingAddressShadowMap(cTxDestination);
                    }
                    if (address != null && !address.isRecycleFlag()) {
                        hasRelatedAddress = true;
                        break;
                    }
                }
            }
        }
        if (!hasRelatedAddress) {
            List txOutList = transaction.getSelfTxOutList();
            if (txOutList != null && !txOutList.isEmpty()) {
                Iterator it2 = txOutList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    CTxDestination cTxDestination = ((TxOut) it2.next()).getScriptCTxDestination();
                    if (cTxDestination != null) {
                        Address address = addressModel.getAddressByCTxDestinationFromUsingAddressMap(cTxDestination);
                        if (address == null) {
                            address = addressModel.getAddressFromUnuseAddressMap(cTxDestination);
                        }
                        if (address == null) {
                            address = addressModel.getAddressFromUsingAddressShadowMap(cTxDestination);
                        }
                        if (address != null && !address.isRecycleFlag()) {
                            hasRelatedAddress = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!hasRelatedAddress) {
            if (!(transaction instanceof VTransaction)) {
                return false;
            }
            VTransaction vTransaction = (VTransaction) transaction;
            if (!vTransaction.isOutputDescriptionListNotEmpty() && !vTransaction.isSpendDescriptionListNotEmpty()) {
                return false;
            }
        }
        UInt256 txId = transaction.getTxId();
        boolean isHasInLocalTransaction = isAllTransactionMapContainsKey(txId);
        if (this.invalidateTransactionMap.containsKey(txId)) {
            this.invalidateTransactionMap.remove(txId);
            this.invaildTxTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) transaction);
        }
        addCachedAddressFromTx(transaction);
        if (!transaction.isBlockHashNotNull()) {
            transaction.setMBlockNo((int) this.wallet.getCurrentBlockNo());
        }
        boolean isReplaceSuccess = replaceRelatedTransactionWithAddress(transaction, false);
        if (isReplaceSuccess || isHasInLocalTransaction) {
            List<TxIn> txIns = transaction.getSelfTxInList();
            if (txIns != null && !txIns.isEmpty()) {
                for (TxIn txIn : txIns) {
                    COutPoint prevTxOut = txIn.getPrevTxOut();
                    if (prevTxOut != null && !isAllTransactionMapContainsKey(prevTxOut.txid) && !this.unConfirmRelayedTxidMap.containsKey(prevTxOut.txid)) {
                        mo44531f(prevTxOut.txid);
                    }
                }
            }
            if (transaction.isConfirmed()) {
                mo40452b(transaction, false);
            } else if (getUseTransaction()) {
                ArrayList<CTxDestination> arrayList = new ArrayList<>();
                transaction.fillDesListWithRelatedAddress((List<CTxDestination>) arrayList);
                LinkedHashMap<Integer,Account> linkedHashMap = new LinkedHashMap();
                for (CTxDestination b : arrayList) {
                    Address b2 = this.wallet.getAddressByCTxDestinationFromArrayMap(b);
                    if (b2 != null) {
                        b2.calSubAddressBalanceInfo(this.wallet.getBlockChainType());
                        Account a4 = this.wallet.getAccountFromSparseArr(b2.getAccount());
                        if (!linkedHashMap.containsKey(Integer.valueOf(b2.getAccount()))) {
                            linkedHashMap.put(Integer.valueOf(b2.getAccount()), a4);
                        }
                    }
                }
                for (Account account : linkedHashMap.values()) {
                    account.calSubAccountBalanceInfo(false, this.wallet.getBlockChainType());
                }
                if (!isHasInLocalTransaction) {
                    notifyTransactionReceive(transaction);
                }
            }
        }
        return isReplaceSuccess;
    }

    //mo44530f
    public TxSendResult checkAndSendTransactionToPeers(Transaction transaction) throws CoolWalletException, TxSizeException, InvalidateTransactionException, UtxoAlreadySpendException, UtxoNotFoundException {
        this.wallet.mo44060a("Could not send transaction for cool wallet!");
        mo44528e(transaction, true);
        for (TxIn txIn : transaction.getSelfTxInList()) {
            TxOut prevTxOut = txIn.getPrevTransactionTxOut();
            if (prevTxOut == null) {
                UtxoNotFoundException utxoNotFoundException = new UtxoNotFoundException(String.format(Locale.getDefault(), "Utxo not exists: %s", new Object[]{txIn.getPrevTxOut().toString()}));
                utxoNotFoundException.mo18969a(transaction.getTransactionHexString());
                throw utxoNotFoundException;
            } else if (!prevTxOut.mo43324k()) {
                throw new UtxoAlreadySpendException(String.format(Locale.getDefault(), "VOut %s : %d was already spent!", new Object[]{prevTxOut.getTxidString(), Integer.valueOf(prevTxOut.getIndex())}));
            }
        }
        Transaction tempTransaction = transaction.getCloneTransaction();
        this.wallet.lock();
        tempTransaction.setTimeStamp(System.currentTimeMillis() / 1000);
        processNewTransaction(tempTransaction);
        searchRelatedAddressAndCalBalance(tempTransaction);
        this.wallet.unLock();
        TransactionEvent transactionEvent = this.transactionEvent;
        if (transactionEvent != null) {
            transactionEvent.onTransactionSend(tempTransaction.getTxId());
        }
        this.wallet.getSelfPeerManager().addTxMessage(transaction);
        StringBuilder sb = new StringBuilder();
        sb.append("sendTransactionNoCheckPwd: ");
        sb.append(tempTransaction.getTxId().hashString());
        sb.append("\nhex:  ");
        sb.append(tempTransaction.getTransactionHexString());
        Log.e("TXID-------", sb.toString());
        return new TxSendResult(tempTransaction, true);
    }

    //mo44506b
    public TxSignatureResult signTransaction(Transaction transaction, CharSequence pwd) throws NotMatchException {
        Transaction cloneTransaction = transaction.clone();
        return new TxSignatureResult(cloneTransaction, cloneTransaction.signTransaction(pwd));
    }

    public boolean mo44500a(Transaction dhVar, int... iArr) {
        long j;
        BlockChainModel C = this.wallet.getSelfBlockChainModel();
        long k = C.getMaxBlockNo() + 1;
        if ((Math.max(iArr.length > 0 ? iArr[0] : -1, 0) & 2) != 0) {
            j = C.getNewestBlockHeader().mo44302m();
        } else {
            j = TimeData.m122b();
        }
        return mo44499a(dhVar, k, j);
    }

    public boolean mo44499a(Transaction dhVar, long j, long j2) {
        if (dhVar.getLockTime() == 0) {
            return true;
        }
        long g = dhVar.getLockTime();
        if (dhVar.getLockTime() >= 500000000) {
            j = j2;
        }
        if (g < j) {
            return true;
        }
        for (TxIn s : dhVar.getSelfTxInList()) {
            if (!s.mo43305s()) {
                return false;
            }
        }
        return true;
    }

    //mo44491a
    public void replaceToTxMarkTable(UInt256 txid, String reMark) {
        this.txMarkTable.inersetOrUpdateTxMark(txid, reMark);
        this.txIdToRemarkMap.put(txid, reMark);
    }

    public List<Utxo> mo44483a(Account account, boolean z) {
        ArrayList arrayList = new ArrayList();
        if (account == null) {
            return arrayList;
        }
        List<Address> addressList = account.getTxDesAddressList();
        boolean isHdAccount = false;
        if (account instanceof HDAccount) {
            isHdAccount = true;
        }
        for (Address address : addressList) {
            if (!isHdAccount || !address.isFlagIndentity()) {
                List a = mo40426a(address.getCTxDestination(), z);
                if (a != null && !a.isEmpty()) {
                    arrayList.addAll(a);
                }
            }
        }
        return arrayList;
    }

    public List<Utxo> mo40449b(CTxDestination oVar) {
        return mo40426a(oVar, false);
    }

    public List<Utxo> mo40426a(CTxDestination des, boolean z) {
        boolean z2;
        if (des == null) {
            return null;
        }
        LinkedList<COutPoint> linkedList = new LinkedList<>();
        synchronized (this.desToCOutPointMultiMap) {
            linkedList.addAll(this.desToCOutPointMultiMap.get(des));
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        Vector vector = new Vector();
        Vector vector2 = new Vector(3);
        for (COutPoint cOutPoint : linkedList) {
            if (!this.invalidateTransactionMap.containsKey(cOutPoint.txid)) {
                Transaction transaction = getTransactionFromAllTransactionMap(cOutPoint.txid);
                if (transaction != null && !transaction.isDefaultHash()) {
                    long currentBlockNo = this.wallet.getCurrentBlockNo();
                    TxOut txOut = transaction.getTxOut(cOutPoint.index);
                    boolean z3 = transaction.isTransactionLock() || (txOut.getFlag() == 1 && currentBlockNo - transaction.checkAndGetBlockNoFromAll() < this.wallet.getChainParams().getVidLockBlockNumber());
                    vector2.clear();
                    this.txInMap.getTxInTxidAndAddToCollectionSynchronized(cOutPoint, vector2, new HashMap[0]);
                    if (!vector2.isEmpty()) {
                        Iterator it = vector2.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                z2 = false;
                                break;
                            }
                            Transaction transaction1 = getTransactionFromAllTransactionMap((UInt256) it.next());
                            if (transaction.getConfirms() > 0) {
                                z2 = true;
                                break;
                            }
                            z3 |= transaction1.isTransactionLock();
                        }
                    } else {
                        z2 = false;
                    }
                    if (!z2 && (!z3 || z)) {
                        vector.add(new Utxo(txOut, z3));
                    }
                }
            }
        }
        return vector;
    }

    public TxSignatureResult mo44487a(Transaction dhVar, CharSequence charSequence) throws NotMatchException {
        this.wallet.checkWalletPassword(charSequence);
        return new TxSignatureResult(dhVar.clone(), dhVar.signTransaction(charSequence));
    }

    public void checkTransactionSize(Transaction transaction) throws TxSizeException {
        if (transaction != null && transaction.getTransactionBytesLength() > 100000) {
            throw new TxSizeException();
        }
    }

    //mo44477a
    public Transaction getTransactionByTxidBytes(byte[] bArr) {
        return this.allTransactionHashMap.get(new UInt256(bArr));
    }

    public void mo40456e(UInt256 uInt256) {
        Transaction transaction = getTransactionFromAllTransactionMap(uInt256);
        if (!transaction.isCoinBaseTransaction()) {
            mo44493a((TransactionInteface) transaction);
        }
    }

    //910 mo44623k
    //mo44539j
    public boolean isRelayedTransactionConfirmed(Transaction transaction) {
        if (this.confirmedRelayedTransactionMap.containsKey(transaction.getTxId())) {
            return true;
        }
        if (!this.unConfirmRelayedTxidMap.containsKey(transaction.getTxId())) {
            return false;
        }
        transaction.recomputeByTxs(null);
        this.wallet.getSelfTransactionDB().getSelfRelayedTxTable().replaceSynchronized((AbstractTableItem) this.wallet.getSelfWalletHelper().getNewVCWalletTx(transaction));
        this.confirmedRelayedTransactionMap.put(transaction.getTxId(), transaction);
        this.unConfirmRelayedTxidMap.remove(transaction.getTxId());
        return true;
    }

    //910 mo44593b
    public synchronized void mo44509b(List<Transaction> list) {
        list.clear();
        if (!this.unConfirmedTransactionArrayMap.isValueListEmpty()) {
            for (Map.Entry<UInt256, Transaction> entry : this.allTransactionHashMap.entrySet()) {
                if (((Transaction) entry.getValue()).isTransactionLock()) {
                    list.add(entry.getValue());
                }
            }
        }
    }



    //mo40465l
    public void clearAll() {
        this.addressToTxMap.clear();
        this.desToCOutPointMultiMap.clear();
        this.txInMap.clear();
        this.allTransactionHashMap.clear();
        this.confirmedRelayedTransactionMap.clear();
        this.unConfirmRelayedTxidMap.clear();
        this.confirmedTransactionLinkedMap.clear();
        this.unConfirmedTransactionArrayMap.clear();
        this.invalidateTransactionMap.clear();
    }


    public void mo44523d(String str) {
        Transaction transaction = getTransactionByClueTxid(str);
        if (transaction != null) {
            this.invalidateTransactionMap.remove(transaction.getTxId());
            transaction.deleteData(this.transactionTable);
            transaction.deleteData(this.invaildTxTable);
            this.allTransactionHashMap.remove(transaction.getTxId());
            this.rejectTxTable.deleteDataByOtherTableItemSynchronized((AbstractTableItem) transaction);
            this.confirmedTransactionLinkedMap.remove(transaction.getTxId());
            this.unConfirmedTransactionArrayMap.removeAndGet(transaction.getTxId());
        }
    }
}
