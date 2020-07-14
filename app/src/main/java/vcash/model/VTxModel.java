package vcash.model;

import android.database.Cursor;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import bitcoin.BaseBlob;
import bitcoin.CNoDestination;
import bitcoin.Random;
import bitcoin.UInt256;
import bitcoin.script.CScript;
import com.google.common.collect.HashMultimap;
import com.vc.libcommon.exception.AddressFormatException;
import com.vc.libcommon.exception.TxSizeException;
import generic.exceptions.*;
import generic.utils.AddressUtils;
import net.bither.bitherj.exception.ScriptException;
import qtum.VersionVM;
import vcash.script.VInterpreter;
import vdsMain.*;
import vdsMain.block.CachedSaplingBlock;
import vdsMain.block.CachedTxSaplingOutput;
import vdsMain.block.VCachedBlockInfo;
import vdsMain.db.VTransactionDB;
import vdsMain.message.CluePreCreateTreeMssage;
import vdsMain.model.*;
import vdsMain.table.AbstractTableItem;
import vdsMain.table.CachedSaplingBlockTable;
import vdsMain.table.TransactionTable;
import vdsMain.table.VTxTable;
import vdsMain.transaction.*;
import vdsMain.wallet.VCWallet;
import vdsMain.wallet.VWallet;
import vdsMain.wallet.Wallet;
import zcash.RustZCash;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.Collection;

public class VTxModel extends TransactionModel {
    protected HashMultimap<UInt256, UInt256> f4715t = HashMultimap.create();

    /* renamed from: u */
    protected HashMap<UInt256, COutPoint> f4716u = new HashMap<>();

    /* renamed from: v */
    protected HashMultimap<CTxDestination, UInt256> f4717v = HashMultimap.create();

    //f4718w
    protected HashMap<UInt256, VCWalletTx> txidToVCWalletTxMap = new HashMap<>();

    //f4719x
    protected HashMap<CTxDestination, HashMap<UInt256, VAddressWieght>> cTxDestToTxid2AddressWeightMap = new HashMap<>();

    private void m4754a(long j, long j2) {
    }

    private native void createSaplingOutput(long j, byte[] bArr, byte[] bArr2, byte[] bArr3, long j2, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7, byte[] bArr8, byte[] bArr9);

    public VTxModel(@NonNull Wallet izVar) {
        super(izVar);
    }

    //mo40448a
    //TODO 插入事务检查可以先注释掉
    public boolean replaceRelatedTransactionWithAddress(Transaction transaction, boolean isForce) {
        if (!transaction.isRelatedToLocalAddress()) {
            return false;
        }
        if (isForce) {
            return mo40458f(transaction, true);
        }
        Transaction tempTransaction = getTransactionFromAllTransactionMap(transaction.getTxId());
        if (tempTransaction == null) {
            return mo40458f(this.wallet.getSelfWalletHelper().getNewVCWalletTx(transaction), false);
        }
        if (transaction == tempTransaction) {
            this.transactionTable.replaceDataSynchronized((AbstractTableItem) transaction);
        } else if (!transaction.equals(tempTransaction)) {
            StringBuilder sb = new StringBuilder();
            sb.append("old transaction already exist : ");
            sb.append(tempTransaction.getTxidHashString());
            sb.append(", can not update to new transaction with diffent content: [\n");
            sb.append(transaction.toString());
            sb.append("\n]");
            Log.LogObjError((Object) this, sb.toString());
            return false;
        }
        return true;
    }

    //910 mo40482d
    public Transaction mo40454d(UInt256 uInt256) {
        VTransaction vTransaction = (VTransaction) super.mo40454d(uInt256);
        if (vTransaction == null) {
            return null;
        }
        mo40453c(vTransaction);
        vTransaction.checkAndAddSaplingUtxoValue();
        MapSaplingNoteDataT ah = vTransaction.getMapSaplingNoteDataT();
        if (ah == null || ah.isEmpty()) {
            return vTransaction;
        }
        for (Map.Entry entry : ah.entrySet()) {
            SaplingOutpoint brk = (SaplingOutpoint) entry.getKey();
            SaplingNoteData bsd = (SaplingNoteData) entry.getValue();
            if (bsd.nullifier != null && !this.f4716u.containsKey(bsd.nullifier)) {
                SaplingUtxoValue e = vTransaction.mo43063e(brk.index);
                this.f4716u.put(bsd.nullifier, brk);
                this.f4717v.put(e.cTxDestination, bsd.nullifier);
            }
        }
        List<SaplingUtxoValue> aj = vTransaction.getSaplingUtxoValueList();
        if (aj != null && !aj.isEmpty()) {
            for (SaplingUtxoValue bpn : aj) {
                this.addressToTxMap.checkAndCTxDestToTxid2True(bpn.cTxDestination, uInt256);
            }
        }
        return vTransaction;
    }

    //m4756a
    private void checkAndAddTransationNodeDataToOtherNoteData(VTransaction vTransaction, MapSaplingNoteDataT mapSaplingNoteDataT) {
        mapSaplingNoteDataT.clear();
        if (vTransaction.isOutputDescriptionListNotEmpty()) {
            MapSaplingNoteDataT tempMapSaplingNoteDataT = vTransaction.getMapSaplingNoteDataT();
            if (tempMapSaplingNoteDataT != null) {
                mapSaplingNoteDataT.putAll(tempMapSaplingNoteDataT);
            }
        }
    }

    //m4758b
    private boolean isMapSaplingNoteDataContainTransactionSaplingOutPoint(VTransaction vTransaction, MapSaplingNoteDataT mapSaplingNoteDataT) {
        MapSaplingNoteDataT transactionMapData = vTransaction.getMapSaplingNoteDataT();
        if (transactionMapData == null || transactionMapData.isEmpty()) {
            if (mapSaplingNoteDataT.isEmpty()) {
                return false;
            }
            return true;
        } else if (mapSaplingNoteDataT.isEmpty()) {
            return true;
        } else {
            for (Map.Entry key : transactionMapData.entrySet()) {
                mapSaplingNoteDataT.remove(key.getKey());
            }
            return !mapSaplingNoteDataT.isEmpty();
        }
    }

    /* renamed from: a */
    public void mo40436a(Cursor cursor) {
        VCWalletTx vcWalletTx1 = (VCWalletTx) this.transactionTable.getCompleteTableItem(cursor);
        ArrayList<VCWalletTx> arrayList = new ArrayList<>();
        MapSaplingNoteDataT mapSaplingNoteDataT = new MapSaplingNoteDataT();
        while (vcWalletTx1 != null) {
            checkAndAddTransationNodeDataToOtherNoteData((VTransaction) vcWalletTx1, mapSaplingNoteDataT);
            vcWalletTx1.recomputeByTxs((List) null);
            if (isMapSaplingNoteDataContainTransactionSaplingOutPoint((VTransaction) vcWalletTx1, mapSaplingNoteDataT)) {
                arrayList.add(vcWalletTx1);
            }
            replaceRelatedTransactionWithAddress((Transaction) vcWalletTx1, true);
            vcWalletTx1 = (VCWalletTx) this.transactionTable.getCompleteTableItem(cursor);
        }
        if (!arrayList.isEmpty()) {
            VTxTable vTxTable = (VTxTable) this.wallet.getSelfTransactionDB().getSelfTransactionTable();
            for (VCWalletTx vcWalletTx : arrayList) {
                vTxTable.updateVTxSapn((VTransaction) vcWalletTx);
            }
        }
    }

    //mo40438a
    public void findRelatedSaplingTransactionToMap(VCachedBlockInfo vCachedBlockInfo, int blockNo, HashMap<UInt256, VTransaction> vTransactionHashMap) {
        if (!(vCachedBlockInfo == null || vCachedBlockInfo.mSaplingMerkleTree == null)) {
            fillSaplingVTransactionBelowBlockNo(vCachedBlockInfo.blockHeader.getBlockNo(), vTransactionHashMap);
        }
        if (!(vCachedBlockInfo == null || vCachedBlockInfo.txidToTransactionLinkedMap == null || vCachedBlockInfo.txidToTransactionLinkedMap.isEmpty())) {
            Collection<Transaction> transactions = vCachedBlockInfo.txidToTransactionLinkedMap.values();
            if (transactions != null && !transactions.isEmpty()) {
                SaplingOutpoint saplingOutpoint = new SaplingOutpoint();
                for (Transaction transaction : transactions) {
                    List<OutputDescription> outputDescriptionList = ((VTransaction) transaction).getOutputDescriptionList();
                    if (!(vCachedBlockInfo.mSaplingMerkleTree == null || outputDescriptionList == null || outputDescriptionList.isEmpty())) {
                        saplingOutpoint.initTxidAndIndex(transaction.getTxId(), 0);
                        for (OutputDescription outputDescription : outputDescriptionList) {
                            vCachedBlockInfo.mSaplingMerkleTree.mo42894a(new PedersenHash(outputDescription.cm));
                            mo40430a(blockNo, outputDescription.cm, vTransactionHashMap);
                            if (transaction.getTxId().hexString().equalsIgnoreCase("c36597dfde992d59d58d420cb85add4e05fa366a8df29eb999e578707908a848")) {
                                Log.infoObject((Object) this, "find tx");
                            }
                            if (((VCWalletTx) this.txidToVCWalletTxMap.get(transaction.getTxId())) != null) {
                                mo40433a(blockNo, saplingOutpoint, (SaplingWitness) vCachedBlockInfo.mSaplingMerkleTree.mo43005f(), vTransactionHashMap);
                            }
                            saplingOutpoint.mo43128a(saplingOutpoint.index + 1);
                        }
                    }
                }
            }
        }
        updateSaplingNoteDataWitnessHeight(blockNo);
    }

    /* renamed from: a */
    public void mo40431a(int i, CachedSaplingBlock bjr, HashMap<UInt256, VTransaction> hashMap) {
        fillSaplingVTransactionBelowBlockNo(i, hashMap);
        if (!(bjr == null || bjr.cachedTxSaplingOutputList == null || bjr.cachedTxSaplingOutputList.isEmpty())) {
            SaplingOutpoint brk = new SaplingOutpoint();
            for (CachedTxSaplingOutput bjs : bjr.cachedTxSaplingOutputList) {
                brk.initTxidAndIndex(bjs.f11802a, 0);
                for (UInt256 uInt256 : bjs.f11803b) {
                    bjr.f11800b.mo42894a(new PedersenHash(uInt256));
                    mo40430a(i, uInt256, hashMap);
                    if (((VCWalletTx) this.txidToVCWalletTxMap.get(bjs.f11802a)) != null) {
                        mo40433a(i, brk, (SaplingWitness) bjr.f11800b.mo43005f(), hashMap);
                    }
                    brk.mo43128a(brk.index + 1);
                }
            }
        }
        updateSaplingNoteDataWitnessHeight(i);
    }

    /* renamed from: a */
    public boolean mo40446a(int i) {
        boolean z;
        if (this.txidToVCWalletTxMap.isEmpty()) {
            return false;
        }
        VTxTable vTxTable = (VTxTable) this.wallet.getSelfTransactionDB().getSelfTransactionTable();
        ArrayList<VTransaction> arrayList = new ArrayList<>();
        for (Map.Entry value : this.txidToVCWalletTxMap.entrySet()) {
            VCWalletTx bqq = (VCWalletTx) value.getValue();
            MapSaplingNoteDataT ah = bqq.getMapSaplingNoteDataT();
            if (ah != null && !ah.isEmpty()) {
                for (Map.Entry value2 : ah.entrySet()) {
                    SaplingNoteData bsd = (SaplingNoteData) value2.getValue();
                    if (bsd.witnessHeight <= i) {
                        if (!bsd.witnesses.isEmpty()) {
                            if (((SaplingWitness) bsd.witnesses.get(0)).mo43029h() == i) {
                                bsd.witnesses.remove(0);
                            }
                        }
                        bsd.witnessHeight = i - 1;
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        arrayList.add(bqq);
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            return false;
        }
        for (VTransaction a : arrayList) {
            vTxTable.updateVTxSapn(a);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    //mo40450b
    public void updateSaplingNoteDataWitnessHeight(int blockNo) {
        for (Map.Entry entry : this.txidToVCWalletTxMap.entrySet()) {
            Transaction transaction = (Transaction) entry.getValue();
            if (transaction.isConfirmed()) {
                MapSaplingNoteDataT mapSaplingNoteDataT = ((VTransaction) transaction).getMapSaplingNoteDataT();
                if (mapSaplingNoteDataT != null && !mapSaplingNoteDataT.isEmpty()) {
                    for (Map.Entry entry1 : mapSaplingNoteDataT.entrySet()) {
                        SaplingNoteData saplingNoteData = (SaplingNoteData) entry1.getValue();
                        if (saplingNoteData.witnessHeight < blockNo) {
                            saplingNoteData.witnessHeight = blockNo;
                        }
                    }
                }
            }
        }
    }

    //添加到本地内存集合
    /* renamed from: a */
    public void mo40432a(int i, VCWalletTx vcWalletTx) {
        MapSaplingNoteDataT mapSaplingNoteDataT = vcWalletTx.getMapSaplingNoteDataT();
        if (mapSaplingNoteDataT != null && !mapSaplingNoteDataT.isEmpty()) {
            VCCryptoKeyStore cKeyStore = (VCCryptoKeyStore) this.wallet.getSelfCWallet().getCKeyStore();
            for (Map.Entry entry : mapSaplingNoteDataT.entrySet()) {
                SaplingOutpoint saplingOutpoint = (SaplingOutpoint) entry.getKey();
                SaplingNoteData saplingNoteData = (SaplingNoteData) entry.getValue();
                SaplingFullViewingKey saplingFullViewingKey = (SaplingFullViewingKey) cKeyStore.getSaplingFullViewingKeyMap().get(saplingNoteData.incomingViewingKey);
                Address address = cKeyStore.mo42932b(saplingFullViewingKey);
                if (address != null) {
                    CTxDestination des = address.getCTxDestination();
                    if (saplingNoteData.witnessHeight <= i) {
                        saplingNoteData.witnessHeight = i;
                        if (saplingNoteData.witnesses == null || saplingNoteData.witnesses.isEmpty()) {
                            if (saplingNoteData.witnesses == null) {
                                saplingNoteData.witnesses = new Vector();
                            }
                            if (saplingNoteData.nullifier != null) {
                                this.f4716u.remove(saplingNoteData.nullifier);
                                this.f4717v.remove(des, saplingNoteData.nullifier);
                                saplingNoteData.nullifier = null;
                            }
                        } else {
                            long b2 = ((SaplingWitness) saplingNoteData.witnesses.get(0)).mo42907b();
                            OutputDescription outputDescription = (OutputDescription) vcWalletTx.getOutputDescriptionList().get(saplingOutpoint.index);
                            SaplingNotePlaintext a = SaplingNotePlaintext.m10498a(outputDescription.encCiphertext, saplingNoteData.incomingViewingKey, outputDescription.ephemeralKey, outputDescription.cm);
                            if (a != null) {
                                SaplingNote a2 = a.mo43011a(saplingNoteData.incomingViewingKey);
                                if (a2 != null) {
                                    saplingNoteData.nullifier = a2.mo43006a(saplingFullViewingKey, b2);
                                    if (saplingNoteData.nullifier != null) {
                                        this.f4716u.put(saplingNoteData.nullifier, saplingOutpoint);
                                        this.f4717v.put(des, saplingNoteData.nullifier);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public void mo40433a(int blockNo, SaplingOutpoint saplingOutpoint, SaplingWitness saplingWitness, HashMap<UInt256, VTransaction> vTransactionHashMap) {
        VTransaction vTransaction = (VTransaction) getTransactionFromAllTransactionMap(saplingOutpoint.txid);
        if (vTransaction != null) {
            MapSaplingNoteDataT mapSaplingNoteDataT = vTransaction.getMapSaplingNoteDataT();
            if (mapSaplingNoteDataT != null && !mapSaplingNoteDataT.isEmpty()) {
                SaplingNoteData saplingNoteData = (SaplingNoteData) mapSaplingNoteDataT.get(saplingOutpoint);
                if (!(saplingNoteData == null || saplingNoteData == null || saplingNoteData.witnessHeight >= blockNo)) {
                    saplingNoteData.witnessHeight = blockNo - 1;
                    if (saplingNoteData.witnesses == null) {
                        saplingNoteData.witnesses = new ArrayList();
                    } else {
                        saplingNoteData.witnesses.clear();
                    }
                    saplingWitness.setWitnessHeight(blockNo);
                    saplingNoteData.witnesses.add(saplingWitness);
                    if (!vTransactionHashMap.containsKey(saplingOutpoint.txid)) {
                        vTransactionHashMap.put(saplingOutpoint.txid, vTransaction);
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public void mo40430a(int blockNo, UInt256 uInt256, HashMap<UInt256, VTransaction> transactionHashMap) {
        for (Map.Entry entry : this.txidToVCWalletTxMap.entrySet()) {
            VCWalletTx vcWalletTx = (VCWalletTx) entry.getValue();
            MapSaplingNoteDataT mapSaplingNoteDataT = vcWalletTx.getMapSaplingNoteDataT();
            if (mapSaplingNoteDataT != null && !mapSaplingNoteDataT.isEmpty()) {
                for (Map.Entry entry1 : mapSaplingNoteDataT.entrySet()) {
                    SaplingNoteData saplingNoteData = (SaplingNoteData) entry1.getValue();
                    if (saplingNoteData.witnessHeight < blockNo && saplingNoteData.witnesses != null && !saplingNoteData.witnesses.isEmpty()) {
                        ((SaplingWitness) saplingNoteData.witnesses.get(0)).mo42906a(new PedersenHash(uInt256));
                        if (!transactionHashMap.containsKey(vcWalletTx.getTxId())) {
                            transactionHashMap.put(vcWalletTx.getTxId(), vcWalletTx);
                        }
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public void mo40437a(VCachedBlockInfo vCachedBlockInfo) {
        if (vCachedBlockInfo != null && vCachedBlockInfo.txidToTransactionLinkedMap != null && !vCachedBlockInfo.txidToTransactionLinkedMap.isEmpty()) {
            Collection<Transaction> transactionCollection = vCachedBlockInfo.txidToTransactionLinkedMap.values();
            if (transactionCollection != null && !transactionCollection.isEmpty()) {
                int blockNo = vCachedBlockInfo.blockHeader.getBlockNo();
                for (Transaction transaction : transactionCollection) {
                    VCWalletTx vcWalletTx = (VCWalletTx) this.txidToVCWalletTxMap.get(transaction.getTxId());
                    if (vcWalletTx != null) {
                        mo40432a(blockNo, vcWalletTx);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public boolean mo40458f(Transaction transaction, boolean notReplace) {
        VCWalletTx vcWalletTx;
        UInt256 txId = transaction.getTxId();
        boolean containsKey = this.allTransactionHashMap.containsKey(txId);
        try {
            if (transaction instanceof VCWalletTx) {
                vcWalletTx = (VCWalletTx) transaction;
            } else {
                vcWalletTx = (VCWalletTx) getVCWalletTxFromTransaction(transaction);
            }
            if (vcWalletTx.isSpendDescriptionListNotEmpty() || vcWalletTx.isOutputDescriptionListNotEmpty()) {
                this.txidToVCWalletTxMap.put(txId, vcWalletTx);
            }
            if (!transaction.isConfirmed()) {
                vcWalletTx.recomputeByTxs((List) null);
            }
            addTransactionTxInOutToMap(vcWalletTx, notReplace);
            checkIsConfirmAndAddToMap((Transaction) vcWalletTx, notReplace);
            m4759g(vcWalletTx, notReplace);
            if (!notReplace) {
                this.transactionTable.replace((AbstractTableItem) transaction);
            }
            if (transaction.isConfirmed()) {
                calAddressWeightAndPutMap(transaction);
            }
            return true;
        } catch (Exception e) {
            if (!notReplace && containsKey) {
                try {
                    this.confirmedTransactionLinkedMap.remove((Transaction) this.allTransactionHashMap.remove(txId));
                    this.unConfirmedTransactionArrayMap.removeAndGet(txId);
                    this.txInMap.checkAndRemoveTxInForTransaction((TransactionInteface) transaction);
                    mo40439a((VTransaction) transaction);
                } catch (Exception unused) {
                }
            }
            e.printStackTrace();
            return false;
        }
    }

    //mo40468n
    //910 mo40496o
    public void calAddressWeightAndPutMap(Transaction transaction) {
        if (transaction.getFlag() == 1) {
            AddressModel addressModel = this.wallet.getSelfAddressModel();
            for (int i = 0; i < transaction.getSelfTxOutList().size(); i++) {
                TxOut txOut = (TxOut) transaction.getSelfTxOutList().get(i);
                if (txOut.getScriptCTxDestination() != null) {
                    Address address = addressModel.getAddressByCTxDestinationFromUsingAddressMap(txOut.getScriptCTxDestination());
                    if (address != null && address.isFlagEqual(16) && address.getDirectInvNo() >= i && !txOut.getAddress().equals(((TxIn) transaction.getSelfTxInList().get(0)).getAddress())) {
                        double weight = VConsensus.weightArr[i];
                        VAddressWieght vAddressWieght = new VAddressWieght();
                        vAddressWieght.setTxid(transaction.getTxId());
                        vAddressWieght.setBlockNo(transaction.checkAndGetBlockNoFromAll());
                        vAddressWieght.setWeight(weight);
                        if (this.cTxDestToTxid2AddressWeightMap.get(txOut.getScriptCTxDestination()) == null) {
                            this.cTxDestToTxid2AddressWeightMap.put(txOut.getScriptCTxDestination(), new HashMap());
                        }
                        HashMap hashMap = (HashMap) this.cTxDestToTxid2AddressWeightMap.get(txOut.getScriptCTxDestination());
                        hashMap.put(transaction.getTxId(), vAddressWieght);
                        this.cTxDestToTxid2AddressWeightMap.put(txOut.getScriptCTxDestination(), hashMap);
                    }
                }
            }
        }
    }

    /* renamed from: m */
    public HashMap<CTxDestination, HashMap<UInt256, VAddressWieght>> mo40466m() {
        return this.cTxDestToTxid2AddressWeightMap;
    }

    /* renamed from: g */
    private void m4759g(Transaction transaction, boolean z) {
        VTransaction vTransaction = (VTransaction) transaction;
        mo40456e(vTransaction.getTxId());
        UInt256 h_ = transaction.getTxId();
        List<SaplingUtxoValue> aj = vTransaction.getSaplingUtxoValueList();
        if (aj != null && !aj.isEmpty()) {
            for (SaplingUtxoValue bpn : aj) {
                if (this.wallet.isUsingDesAddressMapHasKey(bpn.cTxDestination)) {
                    this.addressToTxMap.checkAndCTxDestToTxid2True(bpn.cTxDestination, h_);
                }
            }
            MapSaplingNoteDataT ah = vTransaction.getMapSaplingNoteDataT();
            if (ah != null && !ah.isEmpty()) {
                for (Map.Entry entry : ah.entrySet()) {
                    SaplingOutpoint brk = (SaplingOutpoint) entry.getKey();
                    SaplingNoteData bsd = (SaplingNoteData) entry.getValue();
                    if (bsd.nullifier != null && !this.f4716u.containsKey(bsd.nullifier)) {
                        this.f4716u.put(bsd.nullifier, brk);
                        this.f4717v.put(vTransaction.mo43063e(brk.index).cTxDestination, bsd.nullifier);
                    }
                }
            }
        }
        mo40453c(vTransaction);
    }

    /* renamed from: a */
    public void mo40442a(Transaction srcTransaction, List<Transaction> list) {
        super.mo40442a(srcTransaction, list);
        mo40469o(srcTransaction);
        this.txidToVCWalletTxMap.remove(srcTransaction.getTxId());
        for (TxOut dnVar : srcTransaction.getSelfTxOutList()) {
            HashMap hashMap = (HashMap) this.cTxDestToTxid2AddressWeightMap.get(dnVar.getScriptCTxDestination());
            if (hashMap != null) {
                hashMap.remove(srcTransaction.getTxId());
                this.cTxDestToTxid2AddressWeightMap.put(dnVar.getScriptCTxDestination(), hashMap);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: o */
    public void mo40469o(Transaction transaction) {
        MapSaplingNoteDataT saplingNoteDataT = ((VTransaction) transaction).getMapSaplingNoteDataT();
        if (saplingNoteDataT != null && !saplingNoteDataT.isEmpty()) {
            for (Map.Entry entry : saplingNoteDataT.entrySet()) {
                if (((SaplingNoteData) entry.getValue()).nullifier != null) {
                    this.f4716u.remove(((SaplingNoteData) entry.getValue()).nullifier);
                }
            }
        }
        checkAndRemoveTxInsForTransactionFromTxInMap(transaction);
    }

    /* renamed from: a */
    public void checkAndRemoveTxInsForTransactionFromTxInMap(Transaction transaction) {
        VTransaction bqf = (VTransaction) transaction;
        mo40455d(bqf);
        mo40439a(bqf);
        super.checkAndRemoveTxInsForTransactionFromTxInMap(transaction);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo40439a(VTransaction bqf) {
        MapSaplingNoteDataT ah = bqf.getMapSaplingNoteDataT();
        if (ah != null && !ah.isEmpty()) {
            for (Map.Entry value : ah.entrySet()) {
                this.f4716u.remove(((SaplingNoteData) value.getValue()).nullifier);
            }
        }
    }

    /* renamed from: a */
    public void mo40444a(Collection<UInt256> collection) {
        VCWalletTx bqq = null;
        for (UInt256 b : collection) {
            Transaction b2 = getTransactionFromAllTransactionMap(b);
            if (b2 != null && (b2 instanceof VCWalletTx)) {
                bqq = (VCWalletTx) b2;
            }
        }
        for (UInt256 b3 : collection) {
            Transaction b4 = getTransactionFromAllTransactionMap(b3);
            if (!(bqq == null || b4 == null || !(b4 instanceof VCWalletTx))) {
                VCWalletTx bqq2 = (VCWalletTx) b4;
                if (!bqq.equals(bqq2)) {
                    bqq2.f12113t.clear();
                    bqq2.f12113t.putAll(bqq.f12113t);
                    bqq2.f12114u.clear();
                    bqq2.f12114u.addAll(bqq.f12114u);
                    bqq2.f12117x = bqq.f12117x;
                    bqq2.f12118y = bqq.f12118y;
                    bqq2.f12119z = bqq.f12119z;
                }
            }
        }
    }

    /* renamed from: i */
    public boolean mo40461i(UInt256 uInt256) {
        Set set = this.f4715t.get(uInt256);
        if (set == null) {
            return false;
        }
        return set.contains(uInt256);
    }

    /* renamed from: e */
    public void mo40456e(UInt256 uInt256) {
        super.mo40456e(uInt256);
        VTransaction vTransaction = (VTransaction) getTransactionFromAllTransactionMap(uInt256);
        if (vTransaction != null) {
            mo40453c(vTransaction);
        }
    }

    //mo40435a
    public void checkTransactionAndFee(long fee, Transaction transaction) {
        VTransaction vTransaction = (VTransaction) transaction;
        long length = (long) transaction.getTransactionBytesLength();
        if (!vTransaction.isSpendDescriptionListNotEmpty()) {
            boolean B_ = vTransaction.isOutputDescriptionListNotEmpty();
        }
        if (length >= 4000000) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Transaction size %d out of range %d", new Object[]{Long.valueOf(length), Integer.valueOf(4000000)}));
        } else if (fee < 10000) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Invalidate tx fee %d", new Object[]{Long.valueOf(fee)}));
        } else if (vTransaction.getVBalance() > 2100000000000000L || vTransaction.getVBalance() < -2100000000000000L) {
            throw new IllegalArgumentException(String.format(Locale.getDefault(), "Invalidate value balance %d", new Object[]{Long.valueOf(vTransaction.getVBalance())}));
        }
    }

    /* access modifiers changed from: protected */
    //mo40470p
    public Transaction getVCWalletTxFromTransaction(Transaction transaction) {
        if (transaction instanceof VCWalletTx) {
            return transaction;
        }
        return this.wallet.getSelfWalletHelper().getNewVCWalletTx(transaction);
    }

    /* renamed from: a */
    public ContractTransaction mo40422a(@NonNull CharSequence charSequence, long j, @NonNull ContractParam bjo, @NonNull List<TxOut> list, @NonNull List<AddressMoneyInfo> list2) throws ScriptException, AddressFormatException, NotMatchException, InvalidateTransactionException {
        CScript cScript;
        ContractParam bjo2 = bjo;
        m4754a(bjo2.f11786b, bjo2.f11787c);
        if (!list.isEmpty()) {
            Vector vector = new Vector();
            CScript cScript2 = new CScript();
            switch (bjo2.f11788d) {
                case UNKNOWN:
                    throw new IllegalArgumentException("Contract param type can not be unknown.");
                case CREATE:
                    cScript2.mo9528b((long) VersionVM.GetEVMDefault().toRaw());
                    cScript2.mo9528b(bjo2.f11786b);
                    cScript2.mo9528b(bjo2.f11787c);
                    cScript2.writeAllDataBytes(bjo2.f11785a);
                    cScript2.checkAndAddOpCode(193);
                    break;
                case CALL:
                    cScript = new CScript(bjo2.f11785a);
                    break;
            }
            cScript = cScript2;
            CRecipient bjl = new CRecipient(cScript, (short) 0, j, new UInt256(), false);
            vector.add(bjl);
            Transaction a = mo40425a(charSequence, vector, list, list2, bjo2.f11787c, 0, 0, 0);
            ContractTransaction bjp = new ContractTransaction(this.wallet);
            bjp.mo42520a(a, true);
            return bjp;
        }
        throw new IllegalArgumentException("Empty vins.");
    }

    /* JADX WARNING: Removed duplicated region for block: B:110:0x02c9  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x02ee A[SYNTHETIC] */
    /* renamed from: a */
    public Transaction mo40425a(@NonNull CharSequence charSequence, @NonNull Vector<CRecipient> vector, @NonNull List<TxOut> list, List<AddressMoneyInfo> list2, long j, int i, int i2, int i3) throws ScriptException, AddressFormatException, NotMatchException, InvalidateTransactionException {
        long j2=0;
        CTxDestination oVar;
        VTxModel vTxModel;
        CharSequence charSequence2;
        long z;
        VTxModel vTxModel2 = this;
        List<TxOut> list3 = list;
        List<AddressMoneyInfo> list4 = list2;
        int i4 = i;
        if (!list.isEmpty()) {
            CTxDestination l = ((TxOut) list3.get(0)).getScriptCTxDestination();
            if (l == null || (l instanceof CNoDestination)) {
                VTxModel vTxModel3 = vTxModel2;
                throw new IllegalArgumentException("Invalidate vin address id.");
            }
            Iterator it = vector.iterator();
            long j3 = 0;
            long j4 = 0;
            while (it.hasNext()) {
                CRecipient bjl = (CRecipient) it.next();
                if (j2 < 0 || bjl.f11779c < 0) {
                    throw new IllegalArgumentException("Transaction amounts must be positive");
                }
                j3 = j2 + bjl.f11779c;
                if (bjl.f11781e) {
                    j4++;
                }
            }
            if (vector.isEmpty() || j2 < 0) {
                VTxModel vTxModel4 = vTxModel2;
                throw new IllegalArgumentException("Transaction amounts must be positive");
            }
            VTransaction bqf = (VTransaction) vTxModel2.wallet.getSelfWalletHelper().getNewTransaction();
            bqf.setFlag(i4);
            bqf.checkAndSetLockTime(Math.max(0, vTxModel2.wallet.getCurrentBlockNo() - 10));
            if (bitcoin.Random.m448a(10) == 0) {
                bqf.checkAndSetLockTime((long) Math.max(0, (int) (bqf.getLockTime() - bitcoin.Random.m448a(100))));
            }
            if (bqf.getLockTime() <= vTxModel2.wallet.getCurrentBlockNo()) {
                VChainParam biq = (VChainParam) vTxModel2.wallet.getChainParams();
                if (bqf.getLockTime() < 500000000) {
                    if (i4 == 8) {
                        oVar = l;
                        bqf.checkAndSetLockTime((long) Validation.m9603a(biq, vTxModel2.wallet.getCurrentBlockNo(), (long) i3));
                    } else {
                        oVar = l;
                    }
                    long j5 = 0;
                    while (true) {
                        bqf.getSelfTxInList().clear();
                        bqf.getSelfTxOutList().clear();
                        Iterator it2 = vector.iterator();
                        boolean z2 = true;
                        while (it2.hasNext()) {
                            CRecipient bjl2 = (CRecipient) it2.next();
                            long j6 = j2;
                            Iterator it3 = it2;
                            VTxOut bqi = new VTxOut(vTxModel2.wallet, bjl2.f11779c, bjl2.f11778b, bjl2.f11777a, bjl2.f11780d);
                            if (bjl2.f11781e) {
                                bqi.mo43284c(j5 / j4);
                                if (z2) {
                                    bqi.mo43284c(j5 % j4);
                                    z2 = false;
                                }
                            }
                            if (!bqi.mo43321a(biq.f11672ab) || bqf.getFlag() == 5) {
                                bqf.mo43248a((TxOut) bqi, false);
                                it2 = it3;
                                j2 = j6;
                                vTxModel2 = this;
                                List<TxOut> list5 = list;
                                list4 = list2;
                            } else if (!bjl2.f11781e || j5 <= 0) {
                                throw new IllegalArgumentException("Transaction amount too small");
                            } else if (bqi.getSatoshi() < 0) {
                                throw new IllegalArgumentException("The transaction amount is too small to pay the fee");
                            } else {
                                throw new IllegalArgumentException("The transaction amount is too small to send after the fee has been deducted");
                            }
                        }
                        List<AddressMoneyInfo> list6 = list4;
                        long j7 = j2;
                        if (list6 != null) {
                            vTxModel = this;
                            ConvertedTxOuts iqVar = new ConvertedTxOuts(vTxModel.wallet);
                            iqVar.addAddressMoneyInfo(list6, (List<TxOut>[]) new List[0]);
                            Iterator it4 = iqVar.iterator();
                            while (it4.hasNext()) {
                                TxOut dnVar = (TxOut) it4.next();
                                if (dnVar.mo43321a(biq.f11672ab)) {
                                    if (bqf.getFlag() != 5) {
                                        throw new IllegalArgumentException("Transaction amount too small");
                                    }
                                }
                                j7 += dnVar.getSatoshi();
                                bqf.mo43248a(dnVar, false);
                            }
                            j2 = j7;
                        } else {
                            vTxModel = this;
                            j2 = j7;
                        }
                        ConvertedTxIns ipVar = new ConvertedTxIns(vTxModel.wallet);
                        ipVar.addTxOutList(list);
                        Iterator it5 = ipVar.iterator();
                        while (it5.hasNext()) {
                            TxIn dlVar = (TxIn) it5.next();
                            Transaction b = vTxModel.getTransactionFromAllTransactionMap(dlVar.getPrevTxOut().txid);
                            if (b != null) {
                                dlVar.getSatoshi();
                                if (b.mo43242N() != 0) {
                                }
                                bqf.addTxIn(dlVar, false);
                            } else {
                                throw new IllegalArgumentException(String.format(Locale.getDefault(), "Transaction %s for utxo %d was not exists.", new Object[]{dlVar.txid.toString(), Integer.valueOf(dlVar.getIndex())}));
                            }
                        }
                        long j8 = 0 - j2;
                        int i5 = (j4 > 0 ? 1 : (j4 == 0 ? 0 : -1));
                        if (i5 == 0) {
                            j8 -= j5;
                        }
                        if (j8 > 0) {
                            VTxOut bqi2 = new VTxOut(vTxModel.wallet, j8, i2, CScript.m484a(oVar), new UInt256[0]);
                            if (i5 > 0 && bqi2.mo43321a(biq.f11672ab)) {
                                long b2 = bqi2.mo43322b(biq.f11672ab) - bqi2.getSatoshi();
                                bqi2.mo43282b(b2);
                                int i6 = 0;
                                while (true) {
                                    if (i6 >= vector.size()) {
                                        break;
                                    } else if (((CRecipient) vector.get(i6)).f11781e) {
                                        TxOut d = bqf.getTxOut(i6);
                                        d.mo43284c(b2);
                                        if (d.mo43321a(biq.f11672ab)) {
                                            throw new IllegalArgumentException("The transaction amount is too small to send after the fee has been deducted");
                                        }
                                    } else {
                                        i6++;
                                    }
                                }
                            }
                            if (bqi2.mo43321a(biq.f11672ab)) {
                                j5 += j8;
                                charSequence2 = charSequence;
                                bqf = (VTransaction) vTxModel.mo44487a((Transaction) bqf, charSequence2).transaction;
                                z = (long) bqf.getTransactionBytesLength();
                                if (z >= 4000000) {
                                    long a = Validation.m9604a(biq, z) + j;
                                    if (a < biq.f11672ab.mo43715a(z)) {
                                        throw new InvalidateTransactionException("Transaction too large for fee policy");
                                    } else if (j5 >= a) {
                                        return bqf;
                                    } else {
                                        vTxModel2 = vTxModel;
                                        j5 = a;
                                        List<TxOut> list7 = list;
                                        list4 = list2;
                                    }
                                } else {
                                    throw new InvalidateTransactionException("Transaction too large");
                                }
                            } else {
                                bqf.getSelfTxOutList().add((int) Random.m448a((long) (bqf.getSelfTxOutList().size() + 1)), bqi2);
                            }
                        }
                        charSequence2 = charSequence;
                        bqf = (VTransaction) vTxModel.mo44487a((Transaction) bqf, charSequence2).transaction;
                        z = (long) bqf.getTransactionBytesLength();
                        if (z >= 4000000) {
                        }
                    }
                } else {
                    VTxModel vTxModel5 = vTxModel2;
                    throw new IllegalArgumentException(String.format(Locale.getDefault(), "Locktime %d must smallet than %d", new Object[]{Long.valueOf(bqf.getLockTime()), Long.valueOf(500000000)}));
                }
            } else {
                VTxModel vTxModel6 = vTxModel2;
                throw new IllegalArgumentException(String.format(Locale.getDefault(), "Locktime %d must smallet than block height %d", new Object[]{Long.valueOf(bqf.getLockTime()), Long.valueOf(vTxModel6.wallet.getCurrentBlockNo())}));
            }
        } else {
            VTxModel vTxModel7 = vTxModel2;
            throw new IllegalArgumentException("vin is empty.");
        }
    }

    /* renamed from: a */
    public OfflineTransaction mo40428a(CharSequence charSequence, List<COutPoint> list, List<AddressMoneyInfo> list2, long j) throws TxSizeException, InvalidateUtxoException, AddressFormatException, IOException, SignatureFailedException, AddressNotFoundException, EncryptException, NoPrivateKeyException, NotMatchException {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Do not have any utxo info.");
        }
        VOfflineTransaction maVar = (VOfflineTransaction) this.wallet.getSelfWalletHelper().mo42419e();
        maVar.f13338a = createNewTransaction(charSequence, list, list2, j, maVar.f13339b, maVar.f13340c, new TxInfo[0]);
        maVar.f13343f = maVar.f13338a.mo43263o();
        maVar.f13344g = j;
        maVar.mo44669d();
        List<AddressMoneyInfo> list3 = list2;
        maVar.mo44666a(AddressUtils.getDesFromAddressString((CharSequence) ((AddressMoneyInfo) list2.get(0)).receiveAddress, this.wallet));
        checkTransactionSize(maVar.f13338a);
        return maVar;
    }

    /* renamed from: a */
    public Transaction createNewTransaction(CharSequence charSequence, List<COutPoint> cOutPointList, List<AddressMoneyInfo> addressMoneyInfoList, long fee, VOutList vOutList, ScriptList scriptList, TxInfo... txInfos) throws InvalidateUtxoException, AddressFormatException, AddressNotFoundException, IOException, SignatureFailedException, EncryptException, NoPrivateKeyException, NotMatchException {
        long sumVinValue;
        long overVinValue;
        String txOutAddress;
        SaplingPaymentAddress saplingPaymentAddress;
        long actualFee;
        boolean needComputeBytes;
        long j5;
        VTransaction tempVTransaction;
        int i;
        String str2;
        long proving_ctx_init;
        List<SpendDescription> m;
        long txOutsLength;
        VOutList tempVoutList = vOutList == null ? new VOutList(this.wallet, cOutPointList.size()) : vOutList;
        VTransaction vTransaction = (VTransaction) this.wallet.getSelfWalletHelper().getNewTransaction();
        ArrayList<COutPoint> tmpCOutPoints = new ArrayList<>();
        ArrayList<SaplingOutpoint> saplingOutpoints = new ArrayList<>();
        ArrayList<SpendDescriptionInfo> arrayList3 = new ArrayList<>();
        Iterator cOutPointIterator = cOutPointList.iterator();
        boolean z3 = false;
        boolean hasCOutPoints = false;
        boolean hasSapingOutPoint = false;
        while (true) {
            int i2 = 1;
            //如果有下一条，一直找把未花费的UTXO加入列表
            if (cOutPointIterator.hasNext()) {
                COutPoint outPoint = (COutPoint) cOutPointIterator.next();
                if (isCoutPointHasUsed(outPoint)) {
                    //UTXO已经花费
                    throw new InvalidateUtxoException(String.format(Locale.getDefault(), "UTXO %s was already spent.", new Object[]{outPoint.toString()}));
                } else if (outPoint instanceof SaplingOutpoint) {
                    saplingOutpoints.add((SaplingOutpoint) outPoint);
                    hasSapingOutPoint = true;
                } else {
                    tmpCOutPoints.add(outPoint);
                    hasCOutPoints = true;
                }
            }//没有同时有coutPoint和sapingOutPoint走这里
            else if (!hasCOutPoints || !hasSapingOutPoint) {
                //有匿名utxo
                if (!saplingOutpoints.isEmpty()) {
                    UInt256 uInt256 = new UInt256();
                    Vector<SaplingWitness> SaplingWitnessVector = new Vector<>(saplingOutpoints.size());
                    mo40445a(saplingOutpoints, SaplingWitnessVector, uInt256);
                    Iterator saplingWitnessVectorIterator = SaplingWitnessVector.iterator();
                    overVinValue = 0;
                    sumVinValue = 0;
                    saplingPaymentAddress = null;
                    txOutAddress = null;
                    for (SaplingOutpoint saplingOutpoint : saplingOutpoints) {
                        SaplingWitness saplingWitness = (SaplingWitness) saplingWitnessVectorIterator.next();
                        if (saplingWitness != null) {
                            SaplingNote saplingNote = mo40423a(saplingOutpoint, i2, z3);
                            if (saplingNote != null) {
                                SaplingUtxoValue saplingUtxoValue = ((VTransaction) getTransactionFromAllTransactionMap(saplingOutpoint.txid)).mo43046a(saplingOutpoint);
                                if (saplingPaymentAddress == null) {
                                    saplingPaymentAddress = (SaplingPaymentAddress) saplingUtxoValue.cTxDestination;
                                } else if (!saplingPaymentAddress.equals(saplingUtxoValue.cTxDestination)) {
                                    throw new IllegalArgumentException("Anonymous transaction could not contains more than 1 input address.");
                                }
                                boolean z6 = hasSapingOutPoint;
                                Iterator it4 = saplingWitnessVectorIterator;
                                SpendDescriptionInfo spendDescriptionInfo = new SpendDescriptionInfo((SaplingPaymentAddress) saplingUtxoValue.cTxDestination, saplingUtxoValue.saplingOutpoint, saplingUtxoValue.value, saplingNote, uInt256, saplingWitness);
                                arrayList3.add(spendDescriptionInfo);
                                overVinValue += saplingUtxoValue.value;
                                sumVinValue += saplingUtxoValue.value;
                                txOutAddress = AddressUtils.getAddressString(saplingUtxoValue.cTxDestination, this.wallet.getChainParams());
                                hasSapingOutPoint = z6;
                                saplingWitnessVectorIterator = it4;
                                i2 = 1;
                                z3 = false;
                            } else {
                                throw new IllegalArgumentException(String.format(Locale.getDefault(), "Can not find sapling note for utxo %s", new Object[]{saplingOutpoint.toString()}));
                            }
                        } else {
                            throw new InvalidateUtxoException(String.format(Locale.getDefault(), "Can not find sapling utxo witness %s", new Object[]{saplingOutpoint.toString()}));
                        }
                    }
                }
                else {
                    overVinValue = 0;
                    sumVinValue = 0;
                    saplingPaymentAddress = null;
                    txOutAddress = null;
                }
                //String str3 = txOutAddress;
                int voutIndex = 0;
                CTxDestination vinDes = null;
                for (COutPoint cOutPoint : tmpCOutPoints) {
                    TxOut txOut = getTxOutByCOutPoint(cOutPoint);
                    if (txOut == null) {
                        txOut = tempVoutList.getTxOutByIndex(voutIndex);
                    }
                    if (cOutPoint != null) {
                        if (vinDes == null) {
                            vinDes = txOut.getScriptCTxDestination();
                        }
                        tempVoutList.addTxOutToList(txOut.clone());
                        overVinValue += txOut.getSatoshi();
                        sumVinValue += txOut.getSatoshi();
                        if (scriptList != null) {
                            scriptList.addToCScriptList(mo44512c(cOutPoint));
                        }
                        vTransaction.addTxIn(new TxIn(txOut), false);
                        voutIndex++;
                        if (txOutAddress == null) {
                            String tempTxOutAddress = txOut.getAddress();
                            txOutAddress = tempTxOutAddress == null ? AddressUtils.getAddressString(txOut.getScriptCTxDestination(), this.wallet.getChainParams()) : tempTxOutAddress;
                        }
                    } else {
                        throw new IllegalArgumentException(String.format(Locale.getDefault(), "Can not found address for utxo %s", new Object[]{cOutPoint.toString()}));
                    }
                }
                Vector<AddressMoneyInfo> commonAddressMoneyList = new Vector<>();
                Vector<Pair<CTxDestination,Long>> saplingAddressPayList = new Vector<>(8);
                Iterator addressMoneyInfoIterator = addressMoneyInfoList.iterator();
                long sendSumValue = 0;
                long selfAddressValue = 0;
                boolean hasSaplingOutput = false;
                while (addressMoneyInfoIterator.hasNext()) {
                    AddressMoneyInfo addressMoneyInfo = (AddressMoneyInfo) addressMoneyInfoIterator.next();
                    CTxDestination voutDes = AddressUtils.getDesFromAddressString((CharSequence) addressMoneyInfo.receiveAddress, this.wallet);
                    if (voutDes == null || (voutDes instanceof CNoDestination)) {
                        //竞拍
                        if (addressMoneyInfo.getFlag() == 8) {
                            commonAddressMoneyList.add(addressMoneyInfo);
                        } else {
                            throw new AddressFormatException(String.format(Locale.getDefault(), "Invalidate output address %s", new Object[]{addressMoneyInfo.receiveAddress}));
                        }
                    } else {
                        if (voutDes instanceof SaplingPaymentAddress) {
                            saplingAddressPayList.add(new Pair(voutDes, Long.valueOf(addressMoneyInfo.sendValue)));
                            hasSaplingOutput = true;
                        } else {
                            commonAddressMoneyList.add(addressMoneyInfo);
                        }
                        overVinValue = overVinValue - addressMoneyInfo.sendValue;
                        if(vinDes.equals(voutDes)){
                            selfAddressValue += addressMoneyInfo.sendValue;
                        }else {
                            sendSumValue += addressMoneyInfo.sendValue;
                        }
//                        if (addressMoneyInfoList.indexOf(addressMoneyInfo) > 0) {
//                            receiveAddressValue += addressMoneyInfo.sendValue;
//                        } else {
//                            selfAddressValue += addressMoneyInfo.sendValue;
//                        }
                    }
                }
                if (overVinValue >= 0) {
                    ConvertedTxOuts convertedTxOuts = new ConvertedTxOuts(this.wallet);
                    convertedTxOuts.addAddressMoneyInfo(commonAddressMoneyList);
                    if (fee <= 0 || overVinValue <= fee) {
                        actualFee = overVinValue;
                    } else {
                        long overVinValueWithOutFee = overVinValue - fee;
                        SeriableOuts seriableOuts = new SeriableOuts(this.wallet);
                        seriableOuts.setTxOutList((List<TxOut>) convertedTxOuts);
                        try {
                            txOutsLength = (long) Math.max((seriableOuts.serialToStream().length + 148) * 3, 30000);
                        } catch (Exception unused) {
                            unused.printStackTrace();
                            txOutsLength = fee;
                        }
                        if (overVinValueWithOutFee >= txOutsLength) {
                            selfAddressValue += overVinValueWithOutFee;
                            //多余的钱返回给txin出钱的人
                            if (hasSapingOutPoint) {
                                saplingAddressPayList.add(new Pair(saplingPaymentAddress, Long.valueOf(overVinValueWithOutFee)));
                            } else {
                                convertedTxOuts.addNewTxOut(vinDes, overVinValueWithOutFee);
                            }
                            actualFee = fee;
                        } else {
                            actualFee = fee;
                            needComputeBytes = false;
                            vTransaction.clearAndSetTxOutList(convertedTxOuts, needComputeBytes);
                            if (!hasSapingOutPoint || hasSaplingOutput) {
                                proving_ctx_init = RustZCash.proving_ctx_init();
                                UInt256 a4 = arrayList3.isEmpty() ? m4753a(proving_ctx_init, (List<SpendDescriptionInfo>) arrayList3, (ZTxInterface) vTransaction, charSequence) : null;
                                UInt256 uInt2562 = a4 != null ? new UInt256() : a4;
                                for (Pair gsVar : saplingAddressPayList) {
                                    m4755a(uInt2562, (SaplingPaymentAddress) gsVar.key, ((Long) gsVar.value).longValue(), (ZTxInterface) vTransaction, proving_ctx_init);
                                }
                                j5 = actualFee;
                                CScript cScript = new CScript();
                                str2 = txOutAddress;
                                i = 0;
                                tempVTransaction = vTransaction;
                                UInt256 a5 = new VInterpreter().SignatureHash(cScript, (VTransactionInterface) vTransaction, (long) Transaction.uIntMax, 1, SigVersion.BASE, (VPrecomputedTransactionData) null);
                                m = tempVTransaction.getSpendDescriptionList();
                                if (m != null && !m.isEmpty()) {
                                    Iterator it7 = ((ArrayList) arrayList3).iterator();
                                    for (SpendDescription brl : m) {
                                        SpendDescriptionInfo bsi2 = (SpendDescriptionInfo) it7.next();
                                        RustZCash.sapling_spend_sig(bsi2.f12232h.begin(), bsi2.f12227c.data(), a5.data(), brl.f12175f);
                                    }
                                }
                                byte[] bArr = new byte[64];
                                RustZCash.sapling_binding_sig(proving_ctx_init, tempVTransaction.getVBalance(), a5.begin(), bArr);
                                tempVTransaction.setSigt(bArr, false);
                                RustZCash.proving_ctx_free(proving_ctx_init);
                                mo40440a(tempVTransaction, a5);
                            } else {
                                j5 = actualFee;
                                str2 = txOutAddress;
                                tempVTransaction = vTransaction;
                                i = 0;
                            }
                            checkTransactionAndFee(j5, (Transaction) tempVTransaction);
                            if (tempVTransaction.isOutputDescriptionListNotEmpty()) {
                                tempVTransaction.checkAndAddSaplingUtxoValue();
                            }
                            if (txInfos.length > 0) {
                                String receiveAddress = ((AddressMoneyInfo) addressMoneyInfoList.get(i)).receiveAddress;
                                TxInfo txInfo = txInfos[i];
                                txInfo.setSumVinValue(CAmount.toDecimalSatoshiString(Long.valueOf(sumVinValue)));
                                txInfo.mo44675a(str2);
                                txInfo.setSendSumValue(CAmount.toDecimalSatoshiString(Long.valueOf(sendSumValue)));
                                txInfo.setReceiveAddress(receiveAddress);
                                txInfo.setSelfAddressValue(CAmount.toDecimalSatoshiString(Long.valueOf(selfAddressValue)));
                                txInfo.mo44679c(str2);
                                txInfo.setFeeValue(CAmount.toDecimalSatoshiString(Long.valueOf((sumVinValue - sendSumValue) - selfAddressValue)));
                                txInfo.setSelfPaySumValue(CAmount.toDecimalSatoshiString(Long.valueOf(sumVinValue - selfAddressValue)));
                            }
                            return tempVTransaction;
                        }
                    }
                    needComputeBytes = false;
                    vTransaction.clearAndSetTxOutList(convertedTxOuts, needComputeBytes);
//                    if (!z) {
//                    }
                    proving_ctx_init = RustZCash.proving_ctx_init();
//                    if (arrayList3.isEmpty()) {
//                    }
//                    if (a4 != null) {
//                    }
//                    for (Pair gsVar2 : vector3) {
//                    }
                    try {
                        j5 = actualFee;
                        CScript cScript2 = new CScript();
                        str2 = txOutAddress;
                        i = 0;
                        tempVTransaction = vTransaction;
                        UInt256 a52 = new VInterpreter().SignatureHash(cScript2, (VTransactionInterface) vTransaction, (long) Transaction.uIntMax, 1, SigVersion.BASE, (VPrecomputedTransactionData) null);
                        m = tempVTransaction.getSpendDescriptionList();
                        Iterator it72 = ((ArrayList) arrayList3).iterator();
//                        while (r4.hasNext()) {
//                        }
                        byte[] bArr2 = new byte[64];
                        RustZCash.sapling_binding_sig(proving_ctx_init, tempVTransaction.getVBalance(), a52.begin(), bArr2);
                        tempVTransaction.setSigt(bArr2, false);
                        RustZCash.proving_ctx_free(proving_ctx_init);
                        mo40440a(tempVTransaction, a52);
                        checkTransactionAndFee(j5, (Transaction) tempVTransaction);
                        if (tempVTransaction.isOutputDescriptionListNotEmpty()) {
                            tempVTransaction.checkAndAddSaplingUtxoValue();
                        }
                        if (txInfos.length > 0) {
                            String receiveAddress = ((AddressMoneyInfo) addressMoneyInfoList.get(i)).receiveAddress;
                            TxInfo txInfo = txInfos[i];
                            txInfo.setSumVinValue(CAmount.toDecimalSatoshiString(Long.valueOf(sumVinValue)));
                            txInfo.mo44675a(str2);
                            txInfo.setSendSumValue(CAmount.toDecimalSatoshiString(Long.valueOf(sendSumValue)));
                            txInfo.setReceiveAddress(receiveAddress);
                            txInfo.setSelfAddressValue(CAmount.toDecimalSatoshiString(Long.valueOf(selfAddressValue)));
                            txInfo.mo44679c(str2);
                            txInfo.setFeeValue(CAmount.toDecimalSatoshiString(Long.valueOf((sumVinValue - sendSumValue) - selfAddressValue)));
                            txInfo.setSelfPaySumValue(CAmount.toDecimalSatoshiString(Long.valueOf(sumVinValue - selfAddressValue)));
                        }
                        return tempVTransaction;
                    } catch (IOException e2) {
                        RustZCash.proving_ctx_free(proving_ctx_init);
                        throw new EncryptException((Throwable) e2);
                    }
                } else {
                    throw new IllegalArgumentException(String.format(Locale.getDefault(), "Total ouput value must smaller than input.", new Object[0]));
                }
            } else {
                throw new IllegalArgumentException("Can not create transaction which contains general and anonymous input address at same time.");
            }
        }
    }

    /* renamed from: a */
    public void mo40440a(VTransaction bqf, UInt256 uInt256) throws IOException, SignatureFailedException {
        boolean A_ = bqf.isSpendDescriptionListNotEmpty();
        boolean B_ = bqf.isOutputDescriptionListNotEmpty();
        if (A_ || B_) {
            CScript cScript = new CScript();
            if (uInt256 == null) {
                uInt256 = new VInterpreter().SignatureHash(cScript, (VTransactionInterface) bqf, (long) Transaction.uIntMax, 1, SigVersion.BASE, (VPrecomputedTransactionData) null);
            }
            long verification_ctx_init = RustZCash.verification_ctx_init();
            if (A_) {
                List<SpendDescription> m = bqf.getSpendDescriptionList();
                if (m != null && !m.isEmpty()) {
                    for (SpendDescription brl : m) {
                        if (!RustZCash.sapling_check_spend(verification_ctx_init, brl.cv.begin(), brl.anchor.begin(), brl.nullifier.begin(), brl.rk.begin(), brl.proof.mo42939b(), brl.f12175f, uInt256.begin())) {
                            RustZCash.verification_ctx_free(verification_ctx_init);
                            throw new IllegalArgumentException("Sapling spend description invalid");
                        }
                    }
                }
            }
            if (B_) {
                for (OutputDescription outputDescription : bqf.getOutputDescriptionList()) {
                    if (!RustZCash.sapling_check_output(verification_ctx_init, outputDescription.cv.begin(), outputDescription.cm.begin(), outputDescription.ephemeralKey.begin(), outputDescription.zkproof.mo42939b())) {
                        RustZCash.verification_ctx_free(verification_ctx_init);
                        throw new IllegalArgumentException("bad-txns-sapling-output-description-invalid");
                    }
                }
            }
            if (bqf.getBsign() != null) {
                if (!RustZCash.sapling_final_check(verification_ctx_init, bqf.getVBalance(), bqf.getBsign(), uInt256.begin())) {
                    RustZCash.verification_ctx_free(verification_ctx_init);
                    throw new IllegalArgumentException("bad-txns-sapling-binding-signature-invalid");
                }
            }
            RustZCash.verification_ctx_free(verification_ctx_init);
        }
    }

    /* renamed from: a */
    private UInt256 m4753a(long j, List<SpendDescriptionInfo> list, ZTxInterface bsl, CharSequence charSequence) throws AddressNotFoundException, EncryptException, NoPrivateKeyException, AddressFormatException, NotMatchException {
        Object obj;
        UInt256 b;
        ZTxInterface bsl2 = bsl;
        SaplingExtendedSpendingKey brw = null;
        bsl2.setSpendDescriptionList(null, false);
        UInt256 uInt256 = null;
        Object obj2 = null;
        for (SpendDescriptionInfo bsi : list) {
            Address b2 = this.wallet.getAddressByCTxDestinationFromArrayMap((CTxDestination) bsi.f12225a);
            if (b2 == null) {
                RustZCash.proving_ctx_free(j);
                throw new AddressNotFoundException(String.format(Locale.getDefault(), "Could not found address %s ", new Object[]{AddressUtils.getAddressString((CTxDestination) bsi.f12225a, this.wallet.getChainParams())}));
            } else if (b2.privateKeyIsNotEmpty()) {
                SaplingNote bsc = bsi.f12226b;
                if (obj2 != null) {
                    try {
                        if (obj2.equals(bsi.f12225a)) {
                            obj = obj2;
                            CharSequence charSequence2 = charSequence;
                            SaplingFullViewingKey a = brw.f12197e.mo42966a();
                            UInt256 uInt2562 = new UInt256((BaseBlob) a.f12202c);
                            b = bsc.mo43007b();
                            UInt256 a2 = bsc.mo43006a(a, bsi.f12229e.mo42907b());
                            if (b != null || b.isNull() || a2 == null || a2.isNull()) {
                                brw.m10460a(brw);
                                RustZCash.proving_ctx_free(j);
                                throw new EncryptException(String.format(Locale.getDefault(), "Could not create nullifier for address %s", new Object[]{AddressUtils.getAddressString((CTxDestination) bsi.f12225a, this.wallet.getChainParams())}));
                            }
                            try {
                                byte[] serialToStream = bsi.f12229e.mo42905a().serialToStream();
                                bsi.f12232h.set((BaseBlob) brw.f12197e.f12182a);
                                SpendDescription brl = new SpendDescription();
                                if (RustZCash.sapling_spend_proof(j, a.f12200a.begin(), brw.f12197e.f12183b.begin(), bsi.f12226b.f12209b.mo42955b(), bsi.f12226b.f12211d.begin(), bsi.f12227c.begin(), bsi.f12226b.mo42950a(), bsi.f12228d.begin(), serialToStream, brl.cv.begin(), brl.rk.begin(), brl.proof.mo42938a())) {
                                    brl.anchor.set((BaseBlob) bsi.f12228d);
                                    brl.nullifier.set((BaseBlob) a2);
                                    bsl2.mo43034a(brl);
                                    bsl2.mo43039d(bsi.f12231g);
                                    obj2 = obj;
                                    uInt256 = uInt2562;
                                } else {
                                    brw.m10460a(brw);
                                    RustZCash.proving_ctx_free(j);
                                    throw new SignatureFailedException(String.format(Locale.getDefault(), "Could not sign anonymous vin [ %s ] for address [ %s ]", new Object[]{bsi.f12230f.toString(), AddressUtils.getAddressString((CTxDestination) bsi.f12225a, this.wallet.getChainParams())}));
                                }
                            } catch (Exception e) {
                                brw.m10460a(brw);
                                RustZCash.proving_ctx_free(j);
                                throw new EncryptException((Throwable) e);
                            }
                        }
                    } catch (Exception e2) {
                        RustZCash.proving_ctx_free(j);
                        throw e2;
                    }
                }
                brw.m10460a(brw);
                SaplingExtendedSpendingKey brw2 = (SaplingExtendedSpendingKey) b2.getAddressPrivateKey(charSequence);
                obj = bsi.f12225a;
                brw = brw2;
                SaplingFullViewingKey a3 = brw.f12197e.mo42966a();
                UInt256 uInt25622 = new UInt256((BaseBlob) a3.f12202c);
                b = bsc.mo43007b();
                UInt256 a22 = bsc.mo43006a(a3, bsi.f12229e.mo42907b());
                if (b != null) {
                }
                brw.m10460a(brw);
                RustZCash.proving_ctx_free(j);
                throw new EncryptException(String.format(Locale.getDefault(), "Could not create nullifier for address %s", new Object[]{AddressUtils.getAddressString((CTxDestination) bsi.f12225a, this.wallet.getChainParams())}));
            } else {
                RustZCash.proving_ctx_free(j);
                throw new NoPrivateKeyException(String.format(Locale.getDefault(), "Address doesn't have private key %s", new Object[]{AddressUtils.getAddressString((CTxDestination) bsi.f12225a, this.wallet.getChainParams())}));
            }
        }
        brw.m10460a(brw);
        return uInt256;
    }

    /* renamed from: a */
    private void m4755a(UInt256 uInt256, SaplingPaymentAddress bsf, long j, ZTxInterface bsl, long j2) {
        SaplingPaymentAddress bsf2 = bsf;
        ZTxInterface bsl2 = bsl;
        OutputDescription outputDescription = new OutputDescription();
        createSaplingOutput(j2, uInt256.data(), bsf2.f12218a.mo42955b(), bsf2.f12219b.data(), j, outputDescription.cv.data(), outputDescription.cm.data(), outputDescription.ephemeralKey.data(), outputDescription.encCiphertext, outputDescription.outCiphertext, outputDescription.zkproof.mo42938a());
        outputDescription.ephemeralKey.updateHash();
        outputDescription.cm.updateHash();
        outputDescription.cv.updateHash();
        bsl2.mo43033a(outputDescription);
        bsl2.mo43039d(-j);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public SaplingNote mo40423a(SaplingOutpoint saplingOutpoint, int i, boolean z) {
        VTransaction vTransaction = (VTransaction) getTransactionFromAllTransactionMap(saplingOutpoint.txid);
        if (vTransaction == null) {
            return null;
        }
        MapSaplingNoteDataT ah = vTransaction.getMapSaplingNoteDataT();
        if (ah == null) {
            return null;
        }
        SaplingNoteData bsd = (SaplingNoteData) ah.get(saplingOutpoint);
        if (bsd == null) {
            return null;
        }
        if ((z && this.f4715t.containsKey(bsd.nullifier)) || !mo44500a((Transaction) vTransaction, new int[0]) || vTransaction.mo43243O() > 0 || vTransaction.mo43242N() < i) {
            return null;
        }
        OutputDescription brj = (OutputDescription) vTransaction.getOutputDescriptionList().get(saplingOutpoint.index);
        SaplingNotePlaintext a = SaplingNotePlaintext.m10498a(brj.encCiphertext, bsd.incomingViewingKey, brj.ephemeralKey, brj.cm);
        if (a == null) {
            return null;
        }
        return a.mo43011a(bsd.incomingViewingKey);
    }

    /* renamed from: f */
    public boolean isCoutPointHasUsed(COutPoint cOutPoint) {
        if (cOutPoint instanceof SaplingOutpoint) {
            return mo40447a((SaplingOutpoint) cOutPoint);
        }
        return super.isCoutPointHasUsed(cOutPoint);
    }

    //910 mo40480b
    public void mo40452b(Transaction transaction, boolean needUpdate) {
        super.mo40452b(transaction, needUpdate);
        AddressModel addressModel = this.wallet.getSelfAddressModel();
        if (transaction.getFlag() == 1) {
            Address createVidAddress = addressModel.getAddressByCTxDestinationFromUsingAddressMap(((TxIn) transaction.getSelfTxInList().get(0)).getCTxDestination());
            if (createVidAddress != null && !createVidAddress.isFlagIndentity()) {
                addressModel.updateAddressFlagAsVid(createVidAddress.getCTxDestination());
                createVidAddress.updateClueTxidByDesHash(transaction.getTxidHashString());
                checkAndSetNewAddressIndex(createVidAddress);
            }
            TxOut txOut = (TxOut) transaction.getSelfTxOutList().get(0);
            if (txOut.getFlag() == 1) {
                Address directVidParentAddress = addressModel.getAddressByCTxDestinationFromUsingAddressMap(txOut.getScriptCTxDestination());
                if (directVidParentAddress != null && !directVidParentAddress.isFlagIndentity() && txOut.getSatoshi() == CAmount.toSatoshiLong(4)) {
                    addressModel.updateDirectSignVid(directVidParentAddress, transaction);
                    checkAndSetNewAddressIndex(directVidParentAddress);
                }
            }
        }
    }

    //m4757b
    private void checkAndSetNewAddressIndex(Address address) {
        if (address.isAccount()) {
            Account account = this.wallet.getSelfAccountModel().getAccountFromSparseArr(address.getAccount());
            if (account instanceof HDAccount) {
                HDAccount hdAccount = (HDAccount) account;
                int addrIndex = hdAccount.getAddrIndex();
                int size = hdAccount.getAddressSize(address.getCTxDestination());
                //TODO 200个待用hd账号 改成20个
                if (size >= addrIndex && size <= 20) {
                    hdAccount.mo40784a((size - addrIndex) + 1, (List<Address>) new ArrayList<Address>());
                }
            }
        }
    }

    //mo40451b
    public void updateVTxSapn(VTransaction vTransaction) {
        ((VTxTable) this.wallet.getSelfTransactionDB().getSelfTransactionTable()).updateVTxSapn(vTransaction);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0163, code lost:
        r5 = r16;
        r0 = false;
     */
    /* renamed from: a */
    public void mo40443a(Transaction transaction, List<SaplingUtxoValue> list, MapSaplingNoteDataT mapSaplingNoteDataT) {
        UInt256 uInt256;
        VTransaction vTransaction=null;
        Set set;
        int i;
        int i2=0;
        Set set2=null;
        SaplingUtxoValue saplingUtxoValue=null;
        boolean z=false;
        int i3;
        SaplingUtxoValue temSaplingUtxoValue;
        VTxModel vTxModel = this;
        if (transaction instanceof VTransaction) {
            VWallet vWallet = (VWallet) vTxModel.wallet;
            VTransaction tmpVTransaction = (VTransaction) transaction;
            UInt256 txid = transaction.getTxid();
            VCCryptoKeyStore vcCryptoKeyStore = (VCCryptoKeyStore) ((VCWallet) vWallet.getSelfCWallet()).getCKeyStore();
            List<OutputDescription> outputDescriptionList = tmpVTransaction.getOutputDescriptionList();
            if (outputDescriptionList == null || outputDescriptionList.isEmpty()) {
                mapSaplingNoteDataT.clear();
                return;
            }
            boolean isListAndNoteDataEmpty = list.isEmpty() && mapSaplingNoteDataT.isEmpty();
            ArrayList<SaplingUtxoValue> arrayList = new ArrayList<SaplingUtxoValue>();
            MapSaplingNoteDataT brr3 = new MapSaplingNoteDataT();
            Set<Map.Entry<SaplingIncomingViewingKey, SaplingFullViewingKey>> entrySet = vcCryptoKeyStore.getSaplingFullViewingKeyMap().entrySet();
            int i5 = 0;
            for (OutputDescription brj : outputDescriptionList) {
                SaplingOutpoint brk = new SaplingOutpoint(txid, i5);
                SaplingUtxoValue bpn3 = null;
                if (isListAndNoteDataEmpty) {
                    uInt256 = txid;
                } else if (mapSaplingNoteDataT == null || mapSaplingNoteDataT.isEmpty()) {
                    uInt256 = txid;
                } else {
                    SaplingNoteData saplingNoteData = (SaplingNoteData) mapSaplingNoteDataT.get(brk);
                    if (saplingNoteData != null) {
                        Iterator it = list.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                uInt256 = txid;
                                temSaplingUtxoValue = null;
                                break;
                            }
                            temSaplingUtxoValue = (SaplingUtxoValue) it.next();
                            uInt256 = txid;
                            if (temSaplingUtxoValue.saplingOutpoint.index == i5) {
                                break;
                            }
                            txid = uInt256;
                        }
                        if (vTxModel.wallet.getAddressByCTxDestinationFromArrayMap(temSaplingUtxoValue.cTxDestination) == null) {
                            list.remove(temSaplingUtxoValue);
                            mapSaplingNoteDataT.remove(temSaplingUtxoValue.saplingOutpoint);
                            i5++;
                            txid = uInt256;
                            vTxModel = this;
                        } else if (saplingNoteData.nullifier != null) {
                            arrayList.add(temSaplingUtxoValue);
                            brr3.put(temSaplingUtxoValue.saplingOutpoint, saplingNoteData);
                            i5++;
                            txid = uInt256;
                            vTxModel = this;
                        } else {
                            bpn3 = tmpVTransaction.mo43063e(i5);
                        }
                    } else {
                        uInt256 = txid;
                    }
                }
                if(entrySet!=null){
                    Iterator it2 = entrySet.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            vTransaction = tmpVTransaction;
                            set = entrySet;
                            i = i5;
                            break;
                        }
                        Map.Entry entry = (Map.Entry) it2.next();
                        if (!vcCryptoKeyStore.mo42935c((SaplingFullViewingKey) entry.getValue())) {
                            SaplingIncomingViewingKey brz = (SaplingIncomingViewingKey) entry.getKey();
                            Iterator it3 = it2;
                            vTransaction = tmpVTransaction;
                            SaplingNotePlaintext a = SaplingNotePlaintext.m10498a(brj.encCiphertext, brz, brj.ephemeralKey, brj.cm);
                            if (a == null) {
                                it2 = it3;
                                tmpVTransaction = vTransaction;
                            } else if (brz.mo42994a(a.f12216c) == null) {
                                set = entrySet;
                                i = i5;
                            } else {
                                if (bpn3 == null) {
                                    saplingUtxoValue = new SaplingUtxoValue();
                                    set2 = entrySet;
                                    i2 = i5;
                                    saplingUtxoValue.value = a.f12177a;
                                    saplingUtxoValue.cTxDestination = vcCryptoKeyStore.mo42932b((SaplingFullViewingKey) entry.getValue()).getCTxDestination();
                                    saplingUtxoValue.saplingOutpoint = brk;
                                } else {
                                    set2 = entrySet;
                                    i2 = i5;
                                    saplingUtxoValue = bpn3;
                                }
                                SaplingNoteData bsd2 = (SaplingNoteData) mapSaplingNoteDataT.get(saplingUtxoValue.saplingOutpoint);
                                if (bsd2 == null) {
                                    bsd2 = new SaplingNoteData();
                                    bsd2.incomingViewingKey = brz;
                                }
                                arrayList.add(saplingUtxoValue);
                                brr3.put(saplingUtxoValue.saplingOutpoint, bsd2);
                                z = true;
                            }
                        }
                    }
                }
                if (z || isListAndNoteDataEmpty) {
                    i3 = i2;
                } else {
                    if (saplingUtxoValue == null) {
                        Iterator it4 = arrayList.iterator();
                        while (true) {
                            if (!it4.hasNext()) {
                                i3 = i2;
                                break;
                            }
                            i3 = i2;
                            if (((SaplingUtxoValue) it4.next()).saplingOutpoint.index == i3) {
                                it4.remove();
                                break;
                            }
                            i2 = i3;
                        }
                    } else {
                        arrayList.remove(saplingUtxoValue);
                        i3 = i2;
                    }
                    brr3.remove(brk);
                }
                i5 = i3 + 1;
                entrySet = set2;
                txid = uInt256;
                tmpVTransaction = vTransaction;
                vTxModel = this;
            }
            list.clear();
            mapSaplingNoteDataT.clear();
            list.addAll(arrayList);
            mapSaplingNoteDataT.putAll(brr3);
        }
    }

    //mo40434a
    public void fillSaplingVTransactionBelowBlockNo(int blockNo, HashMap<UInt256, VTransaction> vTransactionHashMap) {
        for (Map.Entry entry : this.txidToVCWalletTxMap.entrySet()) {
            VTransaction vTransaction = (VTransaction) entry.getValue();
            UInt256 txId = vTransaction.getTxId();
            MapSaplingNoteDataT saplingNoteDataT = vTransaction.getMapSaplingNoteDataT();
            if (saplingNoteDataT != null && !saplingNoteDataT.isEmpty()) {
                for (Map.Entry entry1 : saplingNoteDataT.entrySet()) {
                    SaplingNoteData saplingNoteData = (SaplingNoteData) entry1.getValue();
                    if (saplingNoteData.witnessHeight < blockNo && saplingNoteData.witnesses != null && !saplingNoteData.witnesses.isEmpty()) {
                        SaplingWitness saplingWitness = (SaplingWitness) ((SaplingWitness) saplingNoteData.witnesses.get(0)).clone();
                        saplingWitness.setWitnessHeight(blockNo);
                        saplingNoteData.witnesses.add(0, saplingWitness);
                        if (saplingNoteData.witnesses.size() > 100) {
                            saplingNoteData.witnesses.remove(saplingNoteData.witnesses.size() - 1);
                        }
                        if (!vTransactionHashMap.containsKey(txId)) {
                            vTransactionHashMap.put(txId, vTransaction);
                        }
                    }
                }
            }
        }
    }

    /* renamed from: j */
    public boolean mo40462j(UInt256 uInt256) {
        COutPoint cOutPoint = (COutPoint) this.f4716u.get(uInt256);
        if (cOutPoint == null) {
            return false;
        }
        return this.allTransactionHashMap.containsKey(cOutPoint.txid);
    }

    /* renamed from: a */
    public void mo40445a(List<SaplingOutpoint> list, List<SaplingWitness> list2, UInt256 uInt256) {
        list2.clear();
        UInt256 uInt2562 = null;
        for (SaplingOutpoint brk : list) {
            VCWalletTx bqq = (VCWalletTx) getTransactionFromAllTransactionMap(brk.txid);
            if (bqq != null) {
                MapSaplingNoteDataT ah = bqq.getMapSaplingNoteDataT();
                if (ah == null || ah.isEmpty()) {
                    list2.add(null);
                } else {
                    SaplingNoteData bsd = (SaplingNoteData) ah.get(brk);
                    if (bsd == null || bsd.witnesses.isEmpty()) {
                        list2.add(null);
                    } else {
                        SaplingWitness bsh = (SaplingWitness) ((SaplingWitness) bsd.witnesses.get(0)).clone();
                        list2.add(bsh);
                        if (uInt2562 == null) {
                            uInt2562 = (UInt256) bsh.mo42908c();
                        } else if (!uInt2562.equals(bsh.mo42908c())) {
                            throw new IllegalArgumentException("Invalidate sapling witness.");
                        }
                    }
                }
            } else {
                list2.add(null);
            }
        }
        if (uInt2562 != null) {
            uInt256.set((BaseBlob) uInt2562);
        }
    }

    /* renamed from: a */
    public boolean mo40447a(SaplingOutpoint brk) {
        if (brk == null) {
            return false;
        }
        VTransaction bqf = (VTransaction) getTransactionFromAllTransactionMap(brk.txid);
        if (bqf == null) {
            return false;
        }
        MapSaplingNoteDataT ah = bqf.getMapSaplingNoteDataT();
        if (ah == null) {
            return false;
        }
        SaplingNoteData bsd = (SaplingNoteData) ah.get(brk);
        if (bsd == null || bsd.nullifier == null || !this.f4715t.containsKey(bsd.nullifier) || bqf.mo43242N() < 0) {
            return false;
        }
        return true;
    }

    /* renamed from: k */
    public boolean mo40463k(UInt256 uInt256) {
        if (uInt256 == null) {
            return false;
        }
        Set<UInt256> set = this.f4715t.get(uInt256);
        if (set == null || set.isEmpty()) {
            return false;
        }
        for (UInt256 b : set) {
            Transaction b2 = getTransactionFromAllTransactionMap(b);
            if (b2 != null && b2.mo43242N() >= 0) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: l */
    public SaplingUtxoValue mo40464l(UInt256 uInt256) {
        COutPoint cOutPoint = (COutPoint) this.f4716u.get(uInt256);
        if (cOutPoint == null) {
            return null;
        }
        VTransaction bqf = (VTransaction) getTransactionFromAllTransactionMap(cOutPoint.txid);
        if (bqf == null) {
            return null;
        }
        return bqf.mo43063e(cOutPoint.index);
    }

    /* renamed from: c */
    public void mo40453c(VTransaction bqf) {
        List<SpendDescription> m = bqf.getSpendDescriptionList();
        if (m != null && !m.isEmpty()) {
            UInt256 h_ = bqf.getTxId();
            for (SpendDescription a : m) {
                UInt256 a2 = a.mo42947a();
                if (a2 != null) {
                    this.f4715t.put(a2, h_);
                    COutPoint ciVar = (COutPoint) this.f4716u.get(a2);
                    if (ciVar != null) {
                        VTransaction bqf2 = (VTransaction) getTransactionFromAllTransactionMap(ciVar.txid);
                        if (bqf2 != null) {
                            List aj = bqf2.getSaplingUtxoValueList();
                            if (aj != null) {
                                Iterator it = aj.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    SaplingUtxoValue bpn = (SaplingUtxoValue) it.next();
                                    if (bpn.saplingOutpoint.index == ciVar.index) {
                                        this.addressToTxMap.checkAndCTxDestToTxid2True(bpn.cTxDestination, h_);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* renamed from: d */
    public void mo40455d(VTransaction vTransaction) {
        List<SpendDescription> m = vTransaction.getSpendDescriptionList();
        if (m != null && !m.isEmpty()) {
            UInt256 h_ = vTransaction.getTxId();
            for (SpendDescription a : m) {
                UInt256 a2 = a.mo42947a();
                if (a2 != null) {
                    this.f4715t.remove(a2, h_);
                }
            }
        }
    }

    public Transaction getVxdTransferInTransaction(List<Utxo> utxoList, List<Utxo> spendUtxoList, ArrayList<AddressMoneyInfo> addressMoneyInfos,CharSequence pwd,Boolean includeFee) throws NotMatchException {
        this.wallet.checkWalletPassword(pwd);
        Transaction transaction = null;
        try {
            transaction = TransactionUtils.getNewTransaction(this, false, utxoList, pwd, spendUtxoList, addressMoneyInfos, CAmount.toSatoshiLong(Double.valueOf("0.0001")), includeFee, null, null);
        } catch (EncryptException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AddressNotFoundException e) {
            e.printStackTrace();
        } catch (NoPrivateKeyException e) {
            e.printStackTrace();
        } catch (SignatureFailedException e) {
            e.printStackTrace();
        } catch (AddressFormatException e) {
            e.printStackTrace();
        } catch (InvalidateUtxoException e) {
            e.printStackTrace();
        }
        transaction.setFlag(0);
        transaction.checkAndSetLockTime(this.wallet.getCurrentBlockNo());
        transaction.setTimeStamp(System.currentTimeMillis() / 1000);
        signTransaction(transaction, pwd);
        return transaction;
    }


    //mo40429a
    public TxSendResult createClueTransactionAndSend(List<Utxo> utxoList, List<Utxo> spendUtxoList, CluePreCreateTreeMssage cluePreCreateTreeMssage, String firstParent, CharSequence pwd) throws NotMatchException, InvalidateUtxoException, AddressFormatException, TxSizeException, CoolWalletException, InvalidateTransactionException {
        BigDecimal parentValue;
        this.wallet.checkWalletPassword(pwd);
        BigDecimal sumValue = new BigDecimal(0);
        ArrayList<AddressMoneyInfo> addressMoneyInfos = new ArrayList<>();
        Vector<CluePreCreateTreeMssage.ClueParent> clueParentVector = cluePreCreateTreeMssage.clueParentVector;
        int size = clueParentVector.size();
        StringBuilder sb = new StringBuilder();
        sb.append("parent ");
        sb.append(firstParent);
        android.util.Log.i("createV", sb.toString());
        if (12 < size) {
            return null;
        }
        for (int i = size - 1; i >= 0; i--) {
            CluePreCreateTreeMssage.ClueParent clueParent = clueParentVector.get(i);
            String parentAddressString = AddressUtils.getAddressString(clueParent.des, getWalllet().getChainParams());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("parent ");
            sb2.append(parentAddressString);
            sb2.append("   ");
            sb2.append(clueParent.directClueCount);
            android.util.Log.i("createV", sb2.toString());
            if (TextUtils.equals(parentAddressString, firstParent)) {
                parentValue = new BigDecimal("4");
            } else {
                long directClueCount = clueParent.directClueCount;
                long indexAddOne = (long) (i + 1);
                if (i == 0 || directClueCount >= indexAddOne) {
                    parentValue = new BigDecimal("0.5");
                } else {
                    parentValue = BigDecimal.ZERO;
                }
            }
            addressMoneyInfos.add(0, new AddressMoneyInfo(parentAddressString, CAmount.toSatoshiLong(Double.valueOf(parentValue.doubleValue())), 1));
            sumValue = sumValue.add(parentValue);
        }
        Transaction transaction = null;
        try {
            transaction = TransactionUtils.getNewTransaction(this, false, utxoList, pwd, spendUtxoList, addressMoneyInfos, CAmount.toSatoshiLong(Double.valueOf(new BigDecimal("10").subtract(sumValue).doubleValue())), true, null, null, new List[0]);
        } catch (EncryptException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AddressNotFoundException e) {
            e.printStackTrace();
        } catch (NoPrivateKeyException e) {
            e.printStackTrace();
        } catch (SignatureFailedException e) {
            e.printStackTrace();
        }
        transaction.setFlag(1);
        transaction.checkAndSetLockTime(this.wallet.getCurrentBlockNo());
        transaction.setTimeStamp(System.currentTimeMillis() / 1000);
        signTransaction(transaction, pwd);
        return checkAndSendTransactionToPeers(transaction);
    }

    /* renamed from: b */
    public List<Utxo> mo40449b(CTxDestination oVar) {
        return mo40426a(oVar, false);
    }

    /* renamed from: a */
    public List<Utxo> mo40426a(CTxDestination des, boolean z) {
        boolean z2;
        boolean z3;
        if (!(des instanceof SaplingPaymentAddress)) {
            return super.mo40426a(des, z);
        }
        ArrayList arrayList = new ArrayList();
        Set<UInt256> set = this.f4717v.get(des);
        if (set != null) {
            for (UInt256 uInt256 : set) {
                COutPoint ciVar = (COutPoint) this.f4716u.get(uInt256);
                if (ciVar != null) {
                    VTransaction bqf = (VTransaction) getTransactionFromAllTransactionMap(ciVar.txid);
                    if (bqf != null && !bqf.isDefaultHash()) {
                        Set<UInt256> set2 = this.f4715t.get(uInt256);
                        if (set2 == null || set2.isEmpty()) {
                            z3 = false;
                            z2 = false;
                        } else {
                            z3 = false;
                            z2 = false;
                            for (UInt256 b : set2) {
                                Transaction b2 = getTransactionFromAllTransactionMap(b);
                                if (b2 != null) {
                                    if (b2.getConfirms() < 1) {
                                        z3 = true;
                                    } else {
                                        z2 = true;
                                    }
                                }
                            }
                        }
                        if (!z2 && (!z3 || z)) {
                            arrayList.add(new Utxo(this.wallet, bqf.mo43063e(ciVar.index), z3));
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    /* renamed from: a */
    public List<Utxo> mo40427a(boolean z) {
        List<Utxo> a = super.mo40427a(z);
        if (this.f4717v != null) {
            if (a == null) {
                a = new ArrayList<>();
            }
            Set<Map.Entry<CTxDestination, UInt256>> entries = this.f4717v.entries();
            AddressModel D = this.wallet.getSelfAddressModel();
            for (Map.Entry entry : entries) {
                Address a2 = D.getAddressByCTxDestinationFromUsingAddressMap((CTxDestination) entry.getKey());
                if (a2 == null || !a2.isRecycleFlag()) {
                    UInt256 uInt256 = (UInt256) entry.getValue();
                    if (!mo40463k(uInt256)) {
                        COutPoint ciVar = (COutPoint) this.f4716u.get(uInt256);
                        if (ciVar != null) {
                            VTransaction bqf = (VTransaction) getTransactionFromAllTransactionMap(ciVar.txid);
                            if (bqf != null && (bqf.isConfirmed() || z)) {
                                a.add(new Utxo(this.wallet, bqf.mo43063e(ciVar.index), new boolean[0]));
                            }
                        }
                    }
                }
            }
        }
        return a;
    }

    /* renamed from: g */
    public void initAllDataFromDb() {
        super.initAllDataFromDb();
    }

    /* renamed from: l */
    public void clearAll() {
        super.clearAll();
        this.f4716u.clear();
        this.f4717v.clear();
        this.f4715t.clear();
        this.f4716u.clear();
        this.cTxDestToTxid2AddressWeightMap.clear();
    }

    /* renamed from: n */
    public int mo40467n() {
        HashMap hashMap = new HashMap();
        int i = -1;
        for (Map.Entry value : this.txidToVCWalletTxMap.entrySet()) {
            VCWalletTx bqq = (VCWalletTx) value.getValue();
            if (!bqq.isDefaultHash() && bqq.isConfirmed()) {
                int k = bqq.getBlockNo();
                MapSaplingNoteDataT ah = bqq.getMapSaplingNoteDataT();
                if (ah != null && !ah.isEmpty()) {
                    for (SaplingNoteData bsd : ah.values()) {
                        if (bsd.witnesses == null || bsd.witnesses.isEmpty()) {
                            bsd.witnessHeight = -1;
                            if (i == -1 || i > k) {
                                i = k;
                            }
                            if (!hashMap.containsKey(Integer.valueOf(k))) {
                                hashMap.put(Integer.valueOf(k), bqq.getBlockHash());
                            }
                        }
                    }
                }
            }
        }
        if (i == -1) {
            return i;
        }
        BlockChainModel C = this.wallet.getSelfBlockChainModel();
        CachedSaplingBlockTable v2 = ((VTransactionDB) this.wallet.getSelfTransactionDB()).getCachedSaplingBlockTable();
        int d = C.getNewestBlockHeader().getBlockNo();
        TransactionTable transactionTable = this.wallet.getSelfTransactionDB().getSelfTransactionTable();
        boolean d2 = transactionTable.isInTransaction();
        if (!d2) {
            transactionTable.beginTransaction();
        }
        HashMap<UInt256, VTransaction> hashMap2 = new HashMap();
        while (i <= d) {
            UInt256 uInt256 = (UInt256) hashMap.get(Integer.valueOf(i));
            if (uInt256 == null) {
                uInt256 = C.getBlockHashFromDb((long) i);
            }
            CachedSaplingBlock a = v2.mo42529a(uInt256);
            if (a == null || a.f11800b != null || !hashMap.containsKey(Integer.valueOf(i))) {
                mo40431a(i, a, hashMap2);
                if (!(a == null || a.cachedTxSaplingOutputList == null)) {
                    for (CachedTxSaplingOutput bjs : a.cachedTxSaplingOutputList) {
                        VCWalletTx bqq2 = (VCWalletTx) this.txidToVCWalletTxMap.get(bjs.f11802a);
                        if (bqq2 != null) {
                            mo40432a(i, bqq2);
                        }
                    }
                }
                i++;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Could not find sapling block info for ");
                sb.append(uInt256.hexString());
                Log.logObjectWarning((Object) this, sb.toString());
                return i - 1;
            }
        }
        if (!hashMap2.isEmpty()) {
            for (VTransaction b : hashMap2.values()) {
                updateVTxSapn(b);
            }
        }
        if (d2) {
            transactionTable.endTransaction(true);
        }
        return -1;
    }

    /* renamed from: g */
    public void searchRelatedAddressAndCalBalance(Transaction transaction) {
        super.searchRelatedAddressAndCalBalance(transaction);
        if (transaction instanceof VTransaction) {
            VTransaction vTransaction = (VTransaction) transaction;
            HashMap<CTxDestination,Address> addressHashMap = new HashMap();
            HashMap<Integer, Account> accountHashMap = new HashMap();
            List<SpendDescription> spendDescriptionList = vTransaction.getSpendDescriptionList();
            if (spendDescriptionList != null && !spendDescriptionList.isEmpty()) {
                for (SpendDescription spendDescription : spendDescriptionList) {
                    SaplingUtxoValue saplingUtxoValue = mo40464l(spendDescription.nullifier);
                    if (!(saplingUtxoValue == null || saplingUtxoValue.cTxDestination == null)) {
                        Address address = this.wallet.getAddressByCTxDestinationFromArrayMap(saplingUtxoValue.cTxDestination);
                        if (address != null) {
                            addressHashMap.put(saplingUtxoValue.cTxDestination, address);
                        }
                    }
                }
            }
            List<SaplingUtxoValue> saplingUtxoValueList = vTransaction.getSaplingUtxoValueList();
            if (saplingUtxoValueList != null && !saplingUtxoValueList.isEmpty()) {
                for (SaplingUtxoValue saplingUtxoValue : saplingUtxoValueList) {
                    mo44497a(saplingUtxoValue.cTxDestination, addressHashMap, accountHashMap);
                }
            }
            if (!addressHashMap.isEmpty()) {
                for (Address address : addressHashMap.values()) {
                    address.calSubAddressBalanceInfo(this.wallet.getBlockChainType());
                }
            }
            if (!accountHashMap.isEmpty()) {
                for (Account a : accountHashMap.values()) {
                    a.calSubAccountBalanceInfo(false, this.wallet.getBlockChainType());
                }
            }
        }
    }

}
