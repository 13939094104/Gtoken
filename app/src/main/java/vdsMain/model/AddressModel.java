package vdsMain.model;

import android.database.Cursor;
import androidx.annotation.NonNull;

import bitcoin.CKey;
import bitcoin.script.CScript;
import bitcoin.script.WitnessV0KeyHash;
import bitcoin.script.WitnessV0ScriptHash;
import com.vc.libcommon.exception.AddressFormatException;
import generic.crypto.KeyCryptor;
import generic.exceptions.EncryptException;
import generic.exceptions.NotMatchException;
import generic.keyid.CTxDestinationFactory;
import generic.utils.AddressUtils;
import vdsMain.*;
import vdsMain.table.AddressTable;
import vdsMain.table.OriginNodeTable;
import vdsMain.table.SubAddressTable;
import vdsMain.transaction.CScriptID;
import vdsMain.transaction.Transaction;
import vdsMain.wallet.Wallet;
import vdsMain.SparseArray;

import java.io.IOException;
import java.util.*;
import java.util.Collection;

public class AddressModel extends Model {

    public AddressModel(@NonNull Wallet wallet) {
        super(wallet);
    }

    //f12261a
    private AddressTable addressTable;

    //f12262b
    private OriginNodeTable originNodeTable;

    //f12263c
    //915 f12911c
    private Vector<Address> addressVector = new Vector<>();

    //f12264d
    private int addressAmount = 0;

    //f12265e
    //915 f12913e
    private ArrayListMap<CTxDestination, Address> usingTxDesAddressMap = new ArrayListMap<>();

    //f12266f
    //915 f12914f
    private LinkedHashMap<CTxDestination, Address> unUseCTxDestinationAddressMap = new LinkedHashMap<>();

    //f12269j
    //915 f12917j
    private SparseArray<ArrayListMap<CTxDestination, Address>> txDesAddressMapSparseArr = new SparseArray<>();

    //f12267g
    private TxDestinationShadowMap usingTxDestinationShadowMap = new TxDestinationShadowMap();

    //f12268i
    private TxDestinationShadowMap unUseCTxDestShadowMap = new TxDestinationShadowMap();

    //f12270k
    //915 f12918k
    private SubAddressTable subAddressTable;

    //mo43077a
    public void InitAddressTable() {
        if (this.addressTable == null) {
            this.addressTable = getPersonalDB().getSelfAddressTable();
        }
        if (this.originNodeTable == null) {
            this.originNodeTable = getPersonalDB().getOriginNodeTable();
        }
        if (this.subAddressTable == null) {
            this.subAddressTable = getPersonalDB().getSubAddressTable();
        }
    }



    //mo42670b
    public void initAddressSparseArr() {
        addAddressToSparseArrByFlag(64);
        addAddressToSparseArrByFlag(16);
        addAddressToSparseArrByFlag(4);
        addAddressToSparseArrByFlag(1089);
        addAddressToSparseArrByFlag(8192);
        addAddressToSparseArrByFlag(2);
    }

    //mo43110h
    public void updateAddressInfo(Address address) {
        this.addressTable.updateAddressInfo(address);
    }



    public synchronized void checkAndFixFullPubKey(String pwd){
        ArrayList<Address> allAddressList=new ArrayList<>();
        allAddressList.addAll(usingTxDesAddressMap.getValueList());
        allAddressList.addAll(unUseCTxDestinationAddressMap.values());
        KeyCryptor cryptor=this.wallet.getSelfKeyCryptor();
        for(Address address: allAddressList){
            if(address instanceof BitCoinAddress && address.mFullPubKey==null){
                try {
                    CPrivateKeyInterface privateKey=address.getPrivateKeyClone().getNewCKeyWithKeyCryptor(cryptor,pwd);
                    if(privateKey instanceof CKey){
                        address.mFullPubKey=((CKey) privateKey).getUnCompressedPubKey();
                        address.updateAddressFullPub();
                    }
                } catch (AddressFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //mo43079a
    public void removeFromSparseArrAndAddToAddressVector(int i, Address address) {
        if (this.txDesAddressMapSparseArr.isNotNull(i)) {
            ((ArrayListMap) this.txDesAddressMapSparseArr.get(i)).removeAndGet(address.getCTxDestination());
        }
        if (!address.isAccount() && !this.addressVector.contains(address) && !address.getHide()) {
            this.addressVector.add(address);
        }
    }



    //915 mo43372a
    public String getLabelByAddressString(String str) {
        return this.subAddressTable.getLabelByAddressString(str);
    }

    public boolean mo43123m(CTxDestination oVar) {
        //TODO 地址备份表
//        AddressBakRecordTable l = mo44470p().mo44362l();
//        if (l != null) {
//            return l.mo44328a(oVar);
//        }
        return false;
    }

    //mo43078a
    public void addAddressToSparseArrByFlag(int flag) {
        if (flag != 0 && !this.txDesAddressMapSparseArr.isNotNull(flag)) {
            ArrayListMap arrayListMap = new ArrayListMap();
            this.txDesAddressMapSparseArr.put(flag, arrayListMap);
            for (Address address : this.usingTxDesAddressMap.getValueList()) {
                if (address.isFlagEqual(flag)) {
                    CTxDestination cTxDestination = address.getCTxDestination();
                    if (address.isRecycleFlag()) {
                        if (8192 == flag && !arrayListMap.hasKey(cTxDestination)) {
                            arrayListMap.addKeyValueSynchronized(cTxDestination, address);
                        }
                    } else if (address.isWatchedFlag()) {
                        if (flag == 64 && !arrayListMap.hasKey(cTxDestination)) {
                            arrayListMap.addKeyValueSynchronized(cTxDestination, address);
                        }
                    } else if (!arrayListMap.hasKey(cTxDestination)) {
                        arrayListMap.addKeyValueSynchronized(cTxDestination, address);
                    }
                }
            }
        }
    }

    //mo43087a
    public void updateAddressLabel(Address address, String label) {
        this.addressTable.updateLabelByDesHash(address.getCTxDestination(), label);
        address.setLabel((CharSequence) label);
    }

    //mo43075a
    public Address getAddressFromUsingAddressMapByAddressString(CharSequence charSequence) {
        return getAddressByCTxDestinationFromUsingAddressMap(AddressUtils.getDesFromAddressString(charSequence, this.wallet));
    }

    //910 mo43139a
    //mo43076a
    public Address getAddressByCTxDestinationFromUsingAddressMap(CTxDestination cTxDestination) {
        return (Address) this.usingTxDesAddressMap.getValueSynchronized(cTxDestination);
    }

    //mo43094b
    public Address getAddressFromUsingAddressShadowMap(CTxDestination cTxDestination) {
        CTxDestination c = this.usingTxDestinationShadowMap.getCTxDesHashMapValue(cTxDestination);
        if (c != null) {
            return (Address) this.usingTxDesAddressMap.getValueSynchronized(c);
        }
        return null;
    }

    public Address mo43093b(CharSequence charSequence) {
        return getAddressFromUnuseAddressMap(AddressUtils.getDesFromAddressString(charSequence, this.wallet));
    }

    //mo43096c
    public Address getAddressFromUnuseAddressMap(CTxDestination cTxDestination) {
        if (cTxDestination == null) {
            return null;
        }
        Address address = this.unUseCTxDestinationAddressMap.get(cTxDestination);
        if (address == null && ((cTxDestination instanceof WitnessV0KeyHash) || (cTxDestination instanceof WitnessV0ScriptHash))) {
            CTxDestination c = this.unUseCTxDestShadowMap.getCTxDesHashMapValue(cTxDestination);
            if (c != null) {
                address = (Address) this.unUseCTxDestinationAddressMap.get(c);
            }
        }
        return address;
    }

    //mo43088a
    public void updateAddressIsHideAndOperateSpareArr(Address address, boolean isHide) {
        address.updateIsHide(isHide);
        if (isHide) {
            if (mo43102d(address)) {
                this.addressVector.remove(address);
            }
            if (address.isFlagIndentity() && this.txDesAddressMapSparseArr.isNotNull(16)) {
                ((ArrayListMap) this.txDesAddressMapSparseArr.get(16)).removeAndGet(address.getCTxDestination());
                return;
            }
            return;
        }
        if (mo43102d(address) && !this.addressVector.contains(address)) {
            this.addressVector.add(address);
        }
        if (address.isFlagIndentity() && this.txDesAddressMapSparseArr.isNotNull(16)) {
            ((ArrayListMap) this.txDesAddressMapSparseArr.get(16)).addKeyValue(address.getCTxDestination(), address);
        }
    }

    public synchronized Address mo43116j(CTxDestination des) {
        Address address;
        Address usingAddress = getAddressFomUsingTxDesAddressMap(des);
        if (usingAddress == null) {
            return null;
        }
        GeneralAccount generalAccount = this.wallet.getSelfAccountModel().getSelfGeneralAccount();
        if ((des instanceof WitnessV0KeyHash) || (des instanceof WitnessV0ScriptHash) || (des instanceof CScriptID)) {
            try {
                if (des instanceof WitnessV0KeyHash) {
                    address = new WitnessKeyHashAddress(usingAddress);
                } else if (des instanceof WitnessV0ScriptHash) {
                    address = new WitnessScriptHashAddress(usingAddress);
                } else {
                    if (des instanceof CScriptID) {
                        if (usingAddress instanceof BitcoinMultiSigAddress) {
                            address = new WitnessScriptHashScriptAddress(usingAddress);
                        } else if (usingAddress instanceof BitCoinAddress) {
                            address = new WitnessKeyScriptAddress(usingAddress);
                        }
                    }
                    address = null;
                }
                if (address != null) {
                    generalAccount.mo44139a(address, new boolean[0]);
                    if (usingAddress.isCategoryStatusNotZero()) {
                        address.updateCategoryStatus(1, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //mo43091b
    public List<Address> getAddressListByFlag(int i) {
        if (i == 1) {
            return this.addressVector;
        }
        ArrayListMap arrayListMap = (ArrayListMap) this.txDesAddressMapSparseArr.get(i);
        if (arrayListMap == null) {
            return new Vector(1);
        }
        return arrayListMap.getValueList();
    }

    //m10645a
    private void addCTxDestinationToShadowMap(CTxDestination cTxDestination, Address address, TxDestinationShadowMap shadowMap) {
        addScriptByAddressCTxDestination(address);
        if (cTxDestination instanceof CKeyID) {
            shadowMap.addCKeyIDToMap((CKeyID) cTxDestination);
        } else if (cTxDestination instanceof CScriptID) {
            CPubkeyInterface pubKey = address.getSelfPubKey();
            if (pubKey instanceof CMultisigPubkey) {
                shadowMap.addCScriptToMap(((CMultisigPubkey) pubKey).mo9451j());
            }
        }
    }

    //m10646a
    private void removeAllFromShadowMap(CTxDestination cTxDestination, TxDestinationShadowMap shadowMap) {
        shadowMap.removeAllByCTxDestination(cTxDestination);
    }

    //mo43095c
    public List<CTxDestination> getTxShadowMapMutiKeyList() {
        return new Vector(this.usingTxDestinationShadowMap.getMutiMapKeyCollection());
    }

    //mo43100d
    //915 mo43397c
    public List<Address> getAllUsingTxDesMapAddress() {
        return this.usingTxDesAddressMap.getValueList();
    }

    //mo43103e
    //910 mo43166e
    public synchronized Collection<CTxDestination> getAllCTxDestination() {
        LinkedHashMap linkedHashMap;
        linkedHashMap = new LinkedHashMap(this.usingTxDesAddressMap.getValueSize() + this.usingTxDestinationShadowMap.getTxDesHashMapSize()
                + this.unUseCTxDestinationAddressMap.size() + this.unUseCTxDestShadowMap.getTxDesHashMapSize());
        m10644a(linkedHashMap, (Collection<CTxDestination>) this.usingTxDesAddressMap.getKeyList(), false);
        this.usingTxDestinationShadowMap.mo44264a(linkedHashMap);
        this.unUseCTxDestShadowMap.mo44264a(linkedHashMap);
        m10644a(linkedHashMap, (Collection<CTxDestination>) this.unUseCTxDestinationAddressMap.keySet(), false);
        return linkedHashMap.keySet();
    }

    private void m10644a(LinkedHashMap<CTxDestination, Boolean> linkedHashMap, Collection<CTxDestination> collection, boolean z) {
        for (CTxDestination cTxDestination : collection) {
            if (!(cTxDestination instanceof SaplingPaymentAddress) && (!z || !(cTxDestination instanceof WitnessV0KeyHash))) {
                Address address = getAddressByCTxDestinationFromUsingAddressMap(cTxDestination);
                if (address == null || !address.isRecycleFlag()) {
                    linkedHashMap.put(cTxDestination, Boolean.valueOf(true));
                }
            }
        }
    }

    public synchronized List<Address> mo43101d(CTxDestination des) throws AddressFormatException {
        return mo43104e(des);
    }

    public List<Address> mo43104e(CTxDestination des) throws AddressFormatException {
        int i;
        Address tmpAddress;
        Address removeAddress = (Address) this.unUseCTxDestinationAddressMap.remove(des);
        Vector vector = null;
        if (removeAddress == null) {
            CTxDestination txDestination = this.unUseCTxDestShadowMap.removeAndGetValueFromHashMap(des);
            if (txDestination != null) {
                removeAddress = (Address) this.unUseCTxDestinationAddressMap.get(txDestination);
                if (removeAddress != null) {
                    if (des instanceof WitnessV0KeyHash) {
                        tmpAddress = new WitnessKeyHashAddress(removeAddress);
                    } else if (des instanceof WitnessV0ScriptHash) {
                        tmpAddress = new WitnessKeyScriptAddress(removeAddress);
                    } else {
                        if (!(des instanceof CScriptID)) {
                            Log.infoObject((Object) this, "P2SH_SIGWIG address not support yet!");
                        } else if (txDestination instanceof CKeyID) {
                            tmpAddress = new WitnessKeyScriptAddress(removeAddress);
                        }
                        tmpAddress = null;
                    }
                    Account account = this.wallet.getSelfAccountModel().getAccountFromSparseArr(removeAddress.getAccount());
                    if (account instanceof HDAccount) {
                        account = this.wallet.getSelfAccountModel().getSelfGeneralAccount();
                        i = ((HDAccount) account).getAddrIndex();
                    } else {
                        i = 0;
                    }
                    if (tmpAddress != null) {
                        tmpAddress.setFlag8(false);
                        tmpAddress.setAccount(account.getHashCode());
                        account.mo44139a(tmpAddress, false);
                        addAddressToUsingTxDesMap(tmpAddress);
                        vector = new Vector(2);
                        vector.add(tmpAddress);
                    }
                } else {
                    i = 0;
                }
                if (removeAddress != null) {
                    if (!this.addressTable.isInTransaction()) {
                        this.addressTable.beginTransaction();
                    }
                    Account account = this.wallet.getSelfAccountModel().getAccountFromSparseArr(removeAddress.getAccount());
                    account.mo44139a(removeAddress, false);
                    removeAddress.setFlag8(false);
                    this.addressTable.updateFlagByCTxDesHash(removeAddress.getCTxDestination(), removeAddress.getFlag());
                    addAddressToUsingTxDesMap(removeAddress);
                    if (vector == null) {
                        vector = new Vector(1);
                    }
                    vector.add(removeAddress);
                    if (account instanceof HDAccount) {
                        HDAccount hdAccount = (HDAccount) account;
                        int e = hdAccount.getAddrIndex();
                        ListIterator listIterator = hdAccount.getTxDesAddressList().listIterator(i);
                        while (listIterator.hasNext() && i < e) {
                            Address address = (Address) listIterator.next();
                            CTxDestination u = address.getCTxDestination();
                            if (address.isUnlessAddress() && this.unUseCTxDestinationAddressMap.remove(u) != null) {
                                this.unUseCTxDestShadowMap.removeAllByCTxDestination(u);
                                address.setFlag8(false);
                                this.addressTable.updateFlagByCTxDesHash(address.getCTxDestination(), address.getFlag());
                                addAddressToUsingTxDesMap(address);
                            }
                            i++;
                        }
                    }
                    if (this.addressTable.isInTransaction()) {
                        this.addressTable.endTransaction(true);
                    }
                }
                return vector;
            }
        }
        i = 0;
        if (removeAddress != null) {
        }
        return vector;
    }

    public synchronized void mo43082a(List<Address> list) {
        for (Address address : list) {
            CTxDestination cTxDestination = address.getCTxDestination();
            Address address1 = (Address) this.unUseCTxDestinationAddressMap.remove(cTxDestination);
            if (address1 != null) {
                this.unUseCTxDestShadowMap.removeAndGetValueFromHashMap(cTxDestination);
                if (!this.addressTable.isInTransaction()) {
                    this.addressTable.beginTransaction();
                }
                address1.setFlag8(false);
                this.addressTable.updateFlagByCTxDesHash(address1.getCTxDestination(), address1.getFlag());
                addAddressToUsingTxDesMap(address1);
                if (this.addressTable.isInTransaction()) {
                    this.addressTable.endTransaction(true);
                }
            }
        }
    }

    //mo43106f
    public List<Address> getAddressVector() {
        return this.addressVector;
    }


    //915 mo43409f
    public boolean isAddressSyncFinish() {
        for (Map.Entry value : this.usingTxDesAddressMap.getEntrySet()) {
            if (((Address) value.getValue()).getAddressSyncFlag() == 1) {
                return true;
            }
        }
        for (Map.Entry value2 : this.unUseCTxDestinationAddressMap.entrySet()) {
            if (((Address) value2.getValue()).getAddressSyncFlag() == 1) {
                return true;
            }
        }
        return false;
    }

    //mo43108f
    public boolean isUsingDesAddressMapHasKey(CTxDestination cTxDestination) {
        boolean z = true;
        if (this.usingTxDesAddressMap.hasKey(cTxDestination)) {
            return true;
        }
        if (!this.usingTxDestinationShadowMap.cTxDesMutimapContainKey(cTxDestination) && !this.usingTxDestinationShadowMap.cTxDesHashMapContainKey(cTxDestination)) {
            z = false;
        }
        return z;
    }

    //mo43109g
    public boolean txDesAddressArrayListHasKey(CTxDestination oVar) {
        return this.usingTxDesAddressMap.hasKey(oVar);
    }

    //mo43111h
    public boolean isUnuseDesAddressMapHasKey(CTxDestination des) {
        boolean z = true;
        if (this.unUseCTxDestinationAddressMap.containsKey(des)) {
            return true;
        }
        if (!this.unUseCTxDestShadowMap.cTxDesMutimapContainKey(des) && !this.unUseCTxDestShadowMap.cTxDesHashMapContainKey(des)) {
            z = false;
        }
        return z;
    }

    //mo43099c
    public boolean isArrayMapOrShadowMapHasKeyByCharSeq(CharSequence charSequence) {
        CTxDestination a = AddressUtils.getDesFromAddressString(charSequence, getWalllet());
        if (a == null) {
            return false;
        }
        return isUsingDesAddressMapHasKey(a);
    }

    //mo43085a
    public void addScriptByAddressCTxDestination(Address address) {
        CTxDestination cTxDestination = address.getCTxDestination();
        if (cTxDestination instanceof CKeyID) {
            this.wallet.addScriptToKeyStore(CScript.m484a(cTxDestination));
            this.wallet.addScriptToKeyStore(CScript.m484a((CTxDestination) new WitnessV0KeyHash(cTxDestination)));
        } else if (cTxDestination instanceof WitnessV0ScriptHash) {
            CPubkeyInterface t = address.getSelfPubKey();
            if (t != null && (t instanceof CMultisigPubkey)) {
                this.wallet.addScriptToKeyStore(((CMultisigPubkey) t).mo9451j());
            }
            this.wallet.addScriptToKeyStore(CScript.m484a(cTxDestination));
        } else if (cTxDestination instanceof WitnessV0KeyHash) {
            this.wallet.addScriptToKeyStore(CScript.m484a(cTxDestination));
        } else if (cTxDestination instanceof CScriptID) {
            CPubkeyInterface t2 = address.getSelfPubKey();
            if (t2 instanceof CMultisigPubkey) {
                try {
                    CScript j = ((CMultisigPubkey) t2).mo9451j();
                    if (j.mo9540f()) {
                        this.wallet.addScriptToKeyStore(j);
                        this.wallet.addScriptToKeyStore(CScript.m484a((CTxDestination) new WitnessV0ScriptHash(j)));
                        return;
                    }
                    this.wallet.addScriptToKeyStore(CScript.m484a(cTxDestination));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    throw e;
                }
            } else {
                this.wallet.addScriptToKeyStore(CScript.m484a(cTxDestination));
            }
        }
    }

    //mo42672b
    public void addAddressToUsingTxDesMap(Address address) {
        CTxDestination cTxDestination = address.getCTxDestination();
        this.usingTxDesAddressMap.addKeyValueSynchronized(cTxDestination, address);
        mo43089a(cTxDestination, address);
        AddressType addressType = address.getAddressType();
        if (addressType == AddressType.WITNESS_V0_KEY_HASH || addressType == AddressType.WITNESS_V0_KEY_HASH_SCRIPT || addressType == AddressType.WITNESS_V0_SCRIPT_HASH || addressType == AddressType.WITNESS_V0_SCRIPT_HASH_SCRIPT) {
            addScriptByAddressCTxDestination(address);
            this.usingTxDestinationShadowMap.removeAndGetValueFromHashMap(cTxDestination);
            return;
        }
        addCTxDestinationToShadowMap(cTxDestination, address, this.usingTxDestinationShadowMap);
    }

    //mo43098c
    public void addAddressToUnuseCTxDesAddressMap(Address address) {
        CTxDestination cTxDestination = address.getCTxDestination();
        if (!this.unUseCTxDestinationAddressMap.containsKey(cTxDestination)) {
            this.unUseCTxDestinationAddressMap.put(cTxDestination, address);
            addCTxDestinationToShadowMap(cTxDestination, address, this.unUseCTxDestShadowMap);
        }
    }

    //mo43113i
    public Address getAddressFomUsingTxDesAddressMap(CTxDestination key) {
        CTxDestination des = this.usingTxDestinationShadowMap.getCTxDesHashMapValue(key);
        if (des == null) {
            return null;
        }
        return (Address) this.usingTxDesAddressMap.getValueSynchronized(des);
    }

    //915 mo43404d
    public boolean mo43102d(Address address) {
        boolean z = true;
        if (address.mo40923f_()) {
            return true;
        }
        if (address.isFlagIndentity() || address.isWatchedFlag()) {
            return false;
        }
        if (!address.isFlagEqual(2048) && !address.isFlagEqual(16384)) {
            z = false;
        }
        return z;
    }

    public void mo43089a(CTxDestination cTxDestination, Address address) {
        if (!address.isUnlessAddress()) {
            if (!mo43102d(address)) {
                this.addressVector.remove(address);
                for (int i = 0; i < this.txDesAddressMapSparseArr.size(); i++) {
                    int keyAt = this.txDesAddressMapSparseArr.keyAt(i);
                    ArrayListMap arrayListMap = (ArrayListMap) this.txDesAddressMapSparseArr.valueAt(i);
                    if (arrayListMap != null) {
                        if (!address.isFlagEqual(keyAt)) {
                            arrayListMap.removeAndGet(cTxDestination);
                        } else if (!arrayListMap.hasKey(cTxDestination) && !address.getHide() && ((keyAt == 64 || !address.isWatchedAddress()) && (keyAt == 8192 || !address.isRecycleFlag()))) {
                            arrayListMap.addKeyValueSynchronized(cTxDestination, address);
                        }
                    }
                }
            } else if (!address.isRecycleFlag() && !address.getHide() && !this.addressVector.contains(address)) {
                this.addressVector.add(address);
            }
        }
    }

    //m10648o
    private void removeByCTxDestination(CTxDestination cTxDestination) {
        for (int i = 0; i < this.txDesAddressMapSparseArr.size(); i++) {
            ArrayListMap gmVar = (ArrayListMap) this.txDesAddressMapSparseArr.valueAt(i);
            if (gmVar != null) {
                gmVar.removeAndGet(cTxDestination);
            }
        }
    }



    @Override
    public void initAllDataFromDb(){
        this.addressTable.beginTransaction();
        Cursor cursor = this.addressTable.selectAll();
        LinkedHashMap<Integer,List<Address>> accountId2AddressListMap = new LinkedHashMap<>();
        BLOCK_CHAIN_TYPE blockChainType = this.wallet.getBlockChainType();
        ArrayList<Address> witnessAddressList = new ArrayList<>();
        Exception exception=null;
        try {
            Address address = (Address) this.addressTable.getCompleteTableItem(cursor);
            while (address != null) {
                CPubkeyInterface pubKey = address.getSelfPubKey();
                if (pubKey == null || pubKey.checkLength() || (!(address instanceof WitnessKeyHashAddress) && !(address instanceof WitnessKeyScriptAddress)))
                {
                    if (!address.hasBlockChainTypeInSubAddressInfoMap(blockChainType)) {
                        address.addSubAddressInfo(new SubAddressInfo(this.wallet));
                    }
                    List<Address> list = accountId2AddressListMap.get(address.getAccount());
                    if (list == null) {
                        list = new ArrayList<Address>();
                        accountId2AddressListMap.put(address.getAccount(), list);
                    }
                    list.add(address);
                    this.addressAmount = Math.min(this.addressAmount, address.getAddressOrderIndex());
                    address = (Address) this.addressTable.getCompleteTableItem(cursor);
                } else {
                    witnessAddressList.add(address);
                    address = (Address) this.addressTable.getCompleteTableItem(cursor);
                }
            }
        } catch (Exception e) {
            exception=e;
            e.printStackTrace();
        }
        cursor.close();
        if (exception == null) {
            AccountModel accountModel = this.wallet.getSelfAccountModel();
            for (Map.Entry<Integer,List<Address>> entry : accountId2AddressListMap.entrySet()) {
                List<Address> tempAddressList = entry.getValue();
                Account account = accountModel.getAccountFromSparseArr(entry.getKey());
                if (account != null) {
                    account.checkAndAddToBitCoinCtxDestAddressMap(tempAddressList, false);
                    checkAndAddAddressToDes2AddressMap(tempAddressList, false);
                }
                tempAddressList.clear();
            }
            for (HDAccount hdAccount : accountModel.getHDAccountVector()) {
                for (Address address : hdAccount.getAddressVector()) {
                    try {
                        mo43101d(address.getCTxDestination());
                    } catch (AddressFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.addressTable.endTransaction(true);
            initAddressSparseArr();
            mo42671b(witnessAddressList);
            return;
        }
        this.addressTable.endTransaction(false);
        try {
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //mo43074a
    public List<Address> getVidAddress(boolean getFromArrayList) {
        if (getFromArrayList) {
            ArrayListMap<CTxDestination, Address> usingTxDesAddressMap = this.usingTxDesAddressMap;
            if (usingTxDesAddressMap == null || usingTxDesAddressMap.isValueListEmpty()) {
                return new ArrayList(1);
            }
            List<Address> addressList = this.usingTxDesAddressMap.getValueList();
            ArrayList arrayList = new ArrayList();
            for (Address address : addressList) {
                if (address.isAppingVid()) {
                    arrayList.add(0, address);
                }
                if (address.isFlagIndentity() && !address.isWatchedFlag() && !address.isRecycleFlag()) {
                    arrayList.add(address);
                }
            }
            return arrayList;
        }
        ArrayListMap arrayListMap = (ArrayListMap) this.txDesAddressMapSparseArr.get(16);
        if (arrayListMap == null) {
            return new Vector(1);
        }
        return arrayListMap.getValueList();
    }

    public final synchronized void mo43105e(Address address) {
        if (address.isUnlessAddress()) {
            addAddressToUnuseCTxDesAddressMap(address);
        } else {
            addAddressToUsingTxDesMap(address);
        }
    }

    private void mo42671b(List<Address> list) {
        if (list != null && !list.isEmpty() && (this instanceof VAddressModel)) {
            VAddressModel vAddressModel = (VAddressModel) this;
            for (Address tempAddress : list) {
                ComplexBitcoinAddress m2 = vAddressModel.mo42675m(tempAddress);
                if (m2 != null) {
                    List<Address> c = m2.mo42493c();
                    if (c != null && !c.isEmpty()) {
                        for (Address address : c) {
                            if (address != null && (address.getSelfPubKey().checkLength() || (!(address instanceof WitnessKeyHashAddress) && !(address instanceof WitnessKeyScriptAddress)))) {
                                address.updateIsHide(false);
                                mo43105e(address);
                            }
                        }
                    }
                }
            }
            list.clear();
        }
    }



    //mo43115j
    //915 mo43420k
    public Collection<? extends Address> getSelfLinkedMapValueList() {
        return this.unUseCTxDestinationAddressMap.values();
    }


    public List<Address> mo43112i() {
        CTxDestination cTxDestination;
        List<String> i = this.originNodeTable.mo44356i();
        if (i == null || i.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String a : i) {
            try {
                cTxDestination = CTxDestinationFactory.m911a(a);
            } catch (IOException e) {
                e.printStackTrace();
                cTxDestination = null;
            }
            Address a2 = getAddressByCTxDestinationFromUsingAddressMap(cTxDestination);
            if (a2 != null) {
                arrayList.add(a2);
            }
        }
        return arrayList;
    }

    //mo43073a
    public List<Address> createAddress(CharSequence charSequence, int i, CharSequence... charSequenceArr) throws NotMatchException, EncryptException {
        return this.wallet.getSelfAccountModel().getSelfGeneralAccount().createAddress(charSequence, i, charSequenceArr);
    }




    //mo43080a
    public synchronized void updateAddressPrivkey(KeyCryptor keyCryptor, String oldPwd, String newPwd) {
        List<Address> addressList = getAllUsingTxDesMapAddress();
        if (addressList != null) {
            addressList.addAll(getSelfLinkedMapValueList());
        } else {
            addressList = new ArrayList<>();
            addressList.addAll(getSelfLinkedMapValueList());
        }
        for (Address address : addressList) {
            if (!address.isWatchedFlag()) {
//                if (address instanceof BitcoinMultiSigAddress) {
//                    try {
//                        List a = ((BitcoinMultiSigAddress) address).mo40873a(new BLOCK_CHAIN_TYPE[0]);
//                        if (a != null && !a.isEmpty()) {
//                        }
//                    } catch (AddressFormatException e) {
//                        e.printStackTrace();
//                    }
//                }
                address.getSelfEncryptedPrivateKey().updateBytesByNewPwd(keyCryptor, oldPwd, newPwd);
            }
        }
        this.addressTable.updateAddressPrivKey(addressList);
    }

    public void mo43114i(Address address) {
        this.originNodeTable.deleteDataByAddressPubKey(address);
    }

    //mo43086a
    public void updateDirectSignVid(Address address, Transaction transaction) {
        updateAddressFlagAsVid(address.getCTxDestination());
        address.updateIsDirectSignVid(true);
        address.updateClueTxidByDesHash(transaction.getTxidHashString());
        address.updateCategoryStatus(2, true);
    }

    //mo43122l
    public void updateAddressFlagAsVid(CTxDestination des) {
        android.util.Log.e("AddressModel", "updateAddressFlagAsVid: ");
        Address address = getAddressByCTxDestinationFromUsingAddressMap(des);
        if (!address.isWatchedFlag()) {
            this.addressVector.remove(address);
            address.setAndUpdateFlag16(true);
            address.updateAppingVid(true);
            ((ArrayListMap) this.txDesAddressMapSparseArr.get(16)).addKeyValue(des, address);
        }
    }


    //mo42674h
    public void ClearAll() {
        this.addressVector.clear();
        this.usingTxDesAddressMap.clear();
        this.usingTxDestinationShadowMap.mo44268b();
        this.unUseCTxDestinationAddressMap.clear();
        this.unUseCTxDestShadowMap.mo44268b();
        this.txDesAddressMapSparseArr.clear();
    }


    public void mo42673g(Address address) {
        CTxDestination u = address.getCTxDestination();
        Address address1 = (Address) this.usingTxDesAddressMap.removeAndGet(u);
        if (address1 != null) {
            removeAllFromShadowMap(u, this.usingTxDestinationShadowMap);
            if (address1.isUnlessAddress()) {
                removeAllFromShadowMap(u, this.unUseCTxDestShadowMap);
            }
            removeByCTxDestination(u);
            this.addressVector.remove(address1);
            this.unUseCTxDestinationAddressMap.remove(address1);
        }
    }

    public synchronized void mo43084a(List<Address> list, boolean... zArr) {
        if (zArr.length > 0 && zArr[0]) {
            this.addressTable.beginTransaction();
        }
        for (Address address : list) {
            Address address1 = (Address) this.usingTxDesAddressMap.removeAndGet(address.getCTxDestination());
            if (address1 != null) {
                if (address.isFlagEqual(4)) {
                    CPubkeyInterface t = address.getSelfPubKey();
                    if (t instanceof CMultisigPubkey) {
                        CScript j = ((CMultisigPubkey) t).mo9451j();
                        if (j.mo9540f()) {
                            this.wallet.mo42779b(j);
                        }
                    }
                }
                this.addressTable.mo44335c(address1);
                mo42673g(address1);
            }
        }
        if (zArr.length > 0 && zArr[0]) {
            this.addressTable.endTransaction(true);
        }
    }

    //mo43083a
    public final synchronized void checkAndAddAddressToDes2AddressMap(List<Address> list, boolean isUpdate, boolean... zArr) {
        if (list != null) {
            if (!list.isEmpty()) {
                boolean z2 = false;
                if (zArr.length > 0 && zArr[0]) {
                    z2 = true;
                }
                boolean inTransaction = this.addressTable.isInTransaction();
                if (z2 && isUpdate) {
                    this.addressTable.beginTransaction();
                }
                try {
                    for (Address address : list) {
                        if (isUpdate) {
                            this.addressTable.replace(address);
                        }
                        if (address.isUnlessAddress()) {
                            addAddressToUnuseCTxDesAddressMap(address);
                        } else {
                            addAddressToUsingTxDesMap(address);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (z2 && isUpdate && !inTransaction) {
                    this.addressTable.endTransaction(true);
                }
            }
        }
    }

    //915 mo43419j
    public void checkAndRecycleAddress(Address address) {
        CTxDestination des = address.getCTxDestination();
        if (mo43102d(address)) {
            this.addressVector.remove(address);
        } else {
            if (address.isFlagEqual(16) && this.txDesAddressMapSparseArr.isNotNull(16)) {
                ((ArrayListMap) this.txDesAddressMapSparseArr.get(16)).removeAndGet(des);
            }
            if (address.isFlagEqual(4) && this.txDesAddressMapSparseArr.isNotNull(4)) {
                ((ArrayListMap) this.txDesAddressMapSparseArr.get(4)).removeAndGet(des);
            }
            if (address.isFlagEqual(2) && this.txDesAddressMapSparseArr.isNotNull(2)) {
                ((ArrayListMap) this.txDesAddressMapSparseArr.get(2)).removeAndGet(des);
            }
        }
        address.updateRecycleFlag(true);
        if (this.txDesAddressMapSparseArr.isNotNull(8192)) {
            ArrayListMap gmVar = (ArrayListMap) this.txDesAddressMapSparseArr.get(8192);
            if (gmVar != null) {
                gmVar.addKeyValueSynchronized(des, address);
            }
        } else {
            ArrayListMap gmVar2 = new ArrayListMap();
            this.txDesAddressMapSparseArr.put(8192, gmVar2);
            gmVar2.addKeyValueSynchronized(des, address);
        }
        this.wallet.getSelfPeerManager().mo44645n();
    }

    //mo43121l
    //917 mo43456m
    public int getAddressAmount() {
        return this.addressAmount;
    }
}
