package vdsMain.model;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.vc.libcommon.exception.AddressFormatException;
import com.vc.libcommon.util.UInt64;

import bitcoin.CKey;
import generic.exceptions.NoPrivateKeyException;
import generic.exceptions.NotMatchException;
import generic.keyid.CTxDestinationFactory;
import org.apache.commons.io.IOUtils;
import org.web3j.crypto.Keys;

import vdsMain.*;
import vdsMain.table.WalletTable;
import vdsMain.table.WalletTableItem;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.TransactionConfirmType;
import vdsMain.transaction.Utxo;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.*;
import java.util.Collection;

/*AddressType
//8192 ->RECYCLE
//64 ->WATCHED
16->INDENTITY
*/
public abstract class Address extends WalletTableItem {

    private int f13047A;

    /* renamed from: B */
    private boolean f13048B;

    /* renamed from: C */
    private long f13049C;

    //f13050D
    private boolean hide;

    /* renamed from: a */
    private long f13051a;

    /* renamed from: b */
    private long f13052b;


    //mo40825d_
    public abstract AddressType getAddressType();

    public abstract String getBase58PrivateKey(byte[] bArr, BLOCK_CHAIN_TYPE... igVarArr) throws AddressFormatException;

    //mo40822a
    public abstract byte[] getSignMessageBytes(CharSequence message, CPrivateKeyInterface privateKey);

    //f13067q
    protected HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> chainTypeToBalanceInfoMap;

    //f13053c
    protected EncryptedPrivateKey mPrivateKey;

    //f13054d
    protected CPubkeyInterface mPubKey;

    protected CPubkeyInterface mFullPubKey;

    //f13055e
    protected String label;

    //f13056f
    protected int account;

    /* renamed from: g */
    protected boolean f13057g;

    //f13058h
    //915 f13725h
    protected int flag;

    //f13059i
    protected int address_group;

   //f13060j
    protected int address_order_index;

    //f13061k
    protected int v_index;

    //f13062l
    //915 f13729l
    protected int category_status;

    //f13063m
    protected boolean is_direct_sign_vid;

    //f13064n
    protected boolean applying_vid;

    //f13065o
    protected String clue_txid;

    protected boolean applying_vxd;

    protected String vxd_txid;

    /* renamed from: p */
    protected int f13066p;

    //f13068r
    //915 f13735r
    protected CTxDestination cTxDestination;

    //f13069t
    private double fission_reward;

    //f13070u
    private double runaway_reward;

    //f13071v
    private int direct_inv_no;

    //f13072w
    private long last_block;

    //f13073x
    private double season_fission_reward;

    //f13074y
    private long season_fission_total;

    //f13075z
    private long fission_total;

    private int f13717E;

    //mo44207c
    public void setAccount(int i) {
        this.account = i;
    }

    //mo44223f
    public void setAddressOrderIndex(int i) {
        this.address_order_index = i;
    }

    //mo44216d
    public void setFlag128(boolean z) {
        setFlag(128, z);
    }

    //mo44188a
    public void setFlag8(boolean z) {
        setFlag(8, z);
    }
    //mo44201b
    public void setFlag64(boolean z) {
        setFlag(64, z);
    }
    //mo44220e
    //915 mo44591e
    public void setFlag256(boolean z) {
        setFlag(256, z);
    }
    //mo44168N
    public int getAddressOrderIndex() {
        return this.address_order_index;
    }

    //mo44171Q
    public String getClueTxid() {
        return this.clue_txid;
    }

    //mo44241n
    public int getFlag() {
        return this.flag;
    }

    //mo44191ab
    public long getLockReceive() {
        return this.f13049C;
    }

    //mo44190aa
    public int getFirstFloor() {
        return this.f13066p;
    }

    //mo44170P
    public boolean isAppingVid() {
        return this.applying_vid && !isFlagIndentity();
    }

    public boolean isAppingVxd(){
        return this.applying_vxd;
    }

    //mo44244q
    public EncryptedPrivateKey getPrivateKeyClone() {
        return this.mPrivateKey.clone();
    }

    //mo44174X
    //915 mo44543Y
    public boolean isCategoryStatusNotZero() {
        return this.category_status != 0;
    }

    //mo44169O
    public boolean getIsDirectSignVid() {
        return this.is_direct_sign_vid;
    }

    public boolean mo44173S() {
        return (this.category_status & 1) != 0;
    }

    //mo44179a
    public void setFissionReward(double d) {
        this.fission_reward = d;
    }

    //mo44181a
    public void setLastBlock(long j) {
        this.last_block = j;
    }

    //mo44214d
    public void setDirectInvNo(int i) {
        this.direct_inv_no = i;
    }

    //mo44208c
    public void setFissionTotal(long j) {
        this.fission_total = j;
    }

    //mo44175Y
    public int getVIndex() {
        return this.v_index;
    }

    //mo44166L
    public int getAddressGroup() {
        return this.address_group;
    }

    //915 mo44564af
    public int getAddressSyncFlag() {
        return this.f13717E;
    }

    //mo44246s
    public boolean privateKeyIsNotEmpty() {
        EncryptedPrivateKey encryptedPrivateKey = this.mPrivateKey;
        return encryptedPrivateKey != null && !encryptedPrivateKey.isBytesEmpty();
    }

    //mo44196b
    public CPrivateKeyInterface getAddressPrivateKey(CharSequence charSequence) throws AddressFormatException, NotMatchException {
        return this.wallet.getAddressPrivateKey(this, charSequence);
    }

    //mo44185a
    public void updateClueTxidByDesHash(String txid) {
        this.clue_txid = txid;
        ContentValues contentValues = new ContentValues();
        contentValues.put("clue_txid", txid);
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44232i
    public void updateAppingVid(boolean applyingVid) {
        this.applying_vid = applyingVid;
        ContentValues contentValues = new ContentValues();
        contentValues.put("applying_vid", Boolean.valueOf(applyingVid));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    public void updateVxdTxidByDesHash(String txid) {
        this.vxd_txid = txid;
        ContentValues contentValues = new ContentValues();
        contentValues.put("vxd_txid", txid);
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44232i
    public void updateAppingVxd(boolean applyingVxd) {
        this.applying_vxd = applyingVxd;
        ContentValues contentValues = new ContentValues();
        contentValues.put("applying_vxd", Boolean.valueOf(applyingVxd));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }


    //mo44227g
    public void updateIsDirectSignVid(boolean z) {
        this.is_direct_sign_vid = z;
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_direct_sign_vid", Boolean.valueOf(z));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44557a
    public void updateCategoryStatusByBoolean(boolean z, boolean z2) {
        if (z) {
            this.category_status = 1;
        } else {
            this.category_status = 0;
        }
        if (z2) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotificationCompat.CATEGORY_STATUS, Integer.valueOf(this.category_status));
            updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
        }
    }

    //mo44198b
    public void updateCategoryStatus(int i, boolean z) {
        this.category_status = DataTypeToolkit.m11489a(i, i, z);
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotificationCompat.CATEGORY_STATUS, Integer.valueOf(i));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44226g
    public void updateAddressOrderIndex(int i) {
        setAddressOrderIndex(i);
        ContentValues contentValues = new ContentValues();
        contentValues.put("address_order_index", Integer.valueOf(i));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    public void updateAddressFullPub() {
        if(mFullPubKey==null)
            return;
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_pub", mFullPubKey.getByteArr());
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    public String getOrginPrivateKey(CharSequence pwd) throws AddressFormatException, NotMatchException {
        byte[] bytes=getAddressPrivateKey(pwd).getCopyBytes();
        byte[] tempBytes=new byte[32];
        System.arraycopy(bytes,0,tempBytes,0,32);
        return StringToolkit.bytesToString(tempBytes);
    }

    //mo44178a
    public String getBase58PrivateKey(CharSequence pwd, BLOCK_CHAIN_TYPE... igVarArr) throws AddressFormatException, NotMatchException {
        return getBase58PrivateKey(getAddressPrivateKey(pwd).getCopyBytes(), igVarArr);
    }


    //mo44217e
    public long getAvailableBalance(BLOCK_CHAIN_TYPE blockChainType) {
        SubAddressInfo subAddressInfo = getSubAddressInfo(blockChainType);
        if (subAddressInfo == null) {
            return 0;
        }

        return subAddressInfo.availBalance;
    }

    //mo44212d
    public long getSumBalance(BLOCK_CHAIN_TYPE block_chain_type) {
        SubAddressInfo subAddressInfo = getSubAddressInfo(block_chain_type);
        if (subAddressInfo == null) {
            return 0;
        }
        return subAddressInfo.sumBalance;
    }

    //mo44228h
    public void setAndUpdateFlag(int i) {
        setFlag(i, false);
        ContentValues contentValues = new ContentValues();
        contentValues.put("flag", Integer.valueOf(getFlag()));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44224f
    public void setAndUpdateFlag16(boolean z) {
        setFlag(16, z);
        ContentValues contentValues = new ContentValues();
        contentValues.put("flag", Integer.valueOf(getFlag()));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44163I
    public Collection<SubAddressInfo> getSubAddressInfoCollecetion() {
        HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> hashMap = this.chainTypeToBalanceInfoMap;
        if (hashMap == null) {
            return null;
        }
        return hashMap.values();
    }



    //mo44247t
    public CPubkeyInterface getSelfPubKey() {
        return this.mPubKey;
    }

    public CPubkeyInterface getSelfFullPubKey() {
        return this.mFullPubKey;
    }

    //mo44193ad
    public boolean getHide() {
        return this.hide;
    }

    public void mo40875a(byte[] bArr) throws AddressFormatException {
        this.mPubKey.Set(bArr);
        mo40876a(new boolean[0]);
        initDestFromPubKey();
    }

    public boolean mo40923f_() {
        int i = this.flag;
        return i == 1 || i == 16385 || i == 32769;
    }

    //mo44162H
    public boolean isFlagIndentity() {
        return (this.flag & 16) != 0;
    }

    //mo44239l
    public boolean isUnlessAddress() {
        return (this.flag & 8) != 0;
    }

    //mo44161G
    public boolean isWatchedFlag() {
        return (this.flag & 64) != 0;
    }

    //mo44160F
    //915 mo44529G
    public boolean isAccount() {
        return (this.flag & 256) != 0;
    }

    //mo44176Z
    public boolean isRecycleFlag() {
        return (this.flag & 8192) != 0;
    }

    //mo44202b
    //915 mo44573b
    public boolean isFlagEqual(int i) {
        return (i & this.flag) != 0;
    }

    //mo44165K
    public String getChainToAddressString() {
        if (this.chainTypeToBalanceInfoMap == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (SubAddressInfo subAddressInfo : this.chainTypeToBalanceInfoMap.values()) {
            stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
            stringBuffer.append(subAddressInfo.getBlockChainType());
            stringBuffer.append(" : ");
            stringBuffer.append(subAddressInfo.getAddressString(this.cTxDestination));
        }
        return stringBuffer.toString();
    }

    //mo44184a
    public void setLabel(CharSequence charSequence) {
        if (charSequence != null) {
            this.label = charSequence.toString();
        } else {
            this.label = "";
        }
    }

    //mo44218e
    public Wallet getWalletByChainType(BLOCK_CHAIN_TYPE... blockChainTypes) {
        return getSubAddressInfo(blockChainTypes).wallet;
    }

    //mo44225g
    public Transaction getNewestTransaction(BLOCK_CHAIN_TYPE... chainTypes) {
        List transactionList = getWalletByChainType(chainTypes).getSelfTransactionModel().getTransactionListByConfirmType(getCTxDestination(), TransactionConfirmType.ALL);
        if (transactionList == null || transactionList.isEmpty()) {
            return null;
        }
        for (int size = transactionList.size() - 1; size >= 0; size--) {
            Transaction transaction = (Transaction) transactionList.get(size);
            if (!transaction.isDefaultHash()) {
                return transaction;
            }
        }
        return null;
    }

    public UInt64 mo44222f(BLOCK_CHAIN_TYPE... blockChainTypes) {
        Wallet wallet = getWalletByChainType(blockChainTypes);
        UInt64 uInt64 = new UInt64();
        List<Transaction> a = wallet.getSelfTransactionModel().getUnConfirmedTransactionList(getCTxDestination());
        if (a != null) {
            for (Transaction a2 : a) {
                uInt64.add(a2.getAddressVoutSumSubVinSum(this));
            }
        }
        return uInt64;
    }

    //mo44164J
    public BLOCK_CHAIN_TYPE[] getBlockChainTypeArr() {
        HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> hashMap = this.chainTypeToBalanceInfoMap;
        int i = 0;
        if (hashMap == null) {
            return new BLOCK_CHAIN_TYPE[0];
        }
        BLOCK_CHAIN_TYPE[] blockChainTypeArr = new BLOCK_CHAIN_TYPE[hashMap.size()];
        for (BLOCK_CHAIN_TYPE blockChainType : this.chainTypeToBalanceInfoMap.keySet()) {
            blockChainTypeArr[i] = blockChainType;
            i++;
        }
        return blockChainTypeArr;
    }

    //mo44209c
    public void calSubAddressBalanceInfo(BLOCK_CHAIN_TYPE block_chain_type) {
        SubAddressInfo subAddressInfo = getSubAddressInfo(block_chain_type);
        if (subAddressInfo != null) {
            subAddressInfo.sumBalance = 0;
            subAddressInfo.availBalance = 0;
            List<Utxo> utxoList = subAddressInfo.wallet.getSelfTransactionModel().mo40426a(this.cTxDestination, true);
            if (utxoList != null && !utxoList.isEmpty()) {
                for (Utxo utxo : utxoList) {
                    subAddressInfo.sumBalance += utxo.getValue();
                    if (!utxo.getIsLock()) {
                        subAddressInfo.availBalance += utxo.getValue();
                    }
                }
            }
        }
    }


    //mo44213d
    public SubAddressInfo getSubAddressInfo(BLOCK_CHAIN_TYPE... blockChainTypes) {
        HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> hashMap = this.chainTypeToBalanceInfoMap;
        if (hashMap == null) {
            return null;
        }
        if (blockChainTypes.length == 0) {
            return (SubAddressInfo) hashMap.get(this.wallet.getBlockChainType());
        }
        return (SubAddressInfo) hashMap.get(blockChainTypes[0]);
    }

    //mo44205c
    public String getAddressString(BLOCK_CHAIN_TYPE... blockChainTypes) {
        SubAddressInfo subAddressInfo;
        if (blockChainTypes.length == 0 || blockChainTypes[0] == null) {
            subAddressInfo = getSubAddressInfo(this.wallet.getBlockChainType());
        }
        else {
            switch (blockChainTypes[0]){
                case ETH:
                    if (mFullPubKey==null)
                        return null;
                    else {
                        byte[] tempArr=new byte[64];
                        System.arraycopy(mFullPubKey.getByteArr(),1,tempArr,0,64);
                        return "0x"+Keys.getAddress(StringToolkit.bytesToString(tempArr));
                    }
            }
            subAddressInfo = getSubAddressInfo(blockChainTypes[0]);
        }
        if (subAddressInfo == null) {
            return null;
        }
        return subAddressInfo.getAddressString(this.cTxDestination);
    }

    public void mo44229h(boolean z) {
        this.f13048B = z;
    }

    public String getKey() {
        return "hash";
    }

    public String getValue() {
        return this.mPubKey.getCKeyID().getHash();
    }
    //mo44245r
    //915 mo44618s
    public EncryptedPrivateKey getSelfEncryptedPrivateKey() {
        return this.mPrivateKey;
    }

    //mo40827f
    public abstract CPubkeyInterface getPubKey();

    //mo40823b
    public abstract CPrivateKeyInterface getPrivateKey();

    //mo40826e
    public abstract EncryptedPrivateKey getEncryptedPrivateKey();

    ////mo40820a
    public abstract void encryptPrivateKeyByPwd(byte[] bArr, CharSequence charSequence) throws AddressFormatException;

    //mo44248u
    //915 mo44621v
    public CTxDestination getCTxDestination() {
        return this.cTxDestination;
    }

    public Address(@NonNull WalletTable walletTable, byte[] bArr, int i, CharSequence charSequence) throws AddressFormatException {
        super(walletTable);
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        this.flag = getFlagByAddressType(getAddressType());
        this.mPubKey = getPubKey();
        this.mPrivateKey = getEncryptedPrivateKey();
        encryptPrivateKeyByPwd(bArr, charSequence);
        initDestFromPubKey();
        addSubAddressInfoFromWallet();
    }

    public Address(@NonNull WalletTable walletTable) {
        super(walletTable);
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        this.flag = getFlagByAddressType(getAddressType());
        this.mPubKey = getPubKey();
        this.mPrivateKey = getEncryptedPrivateKey();
        initDestFromPubKey();
    }

    public Address(@NonNull WalletTable walletTable, byte[] pubKeyBytes, byte[] privateKeyBytes, CharSequence charSequence) {
        super(walletTable);
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        this.flag = getFlagByAddressType(getAddressType());
        this.mPubKey = getPubKey();
        try {
            this.mPubKey.Set(pubKeyBytes);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        this.mPrivateKey = getEncryptedPrivateKey();
        this.wallet.encryptPrivateKeyByPwd(privateKeyBytes, this.mPrivateKey, charSequence.toString());
        initDestFromPubKey();
        addSubAddressInfoFromWallet();
    }

    public Address(@NonNull WalletTable walletTable, byte[] pubKeyBytes,byte[] fullPubKeyBytes, byte[] privateKeyBytes,CharSequence charSequence) {
        super(walletTable);
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        this.flag = getFlagByAddressType(getAddressType());
        this.mPubKey = getPubKey();
        try {
            this.mPubKey.Set(pubKeyBytes);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        this.mFullPubKey = getPubKey();
        try {
            this.mFullPubKey.Set(fullPubKeyBytes);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        this.mPrivateKey = getEncryptedPrivateKey();
        this.wallet.encryptPrivateKeyByPwd(privateKeyBytes, this.mPrivateKey, charSequence.toString());
        initDestFromPubKey();
        addSubAddressInfoFromWallet();
    }

    public Address(@NonNull WalletTable walletTable, String str) {
        super(walletTable);
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        if (StringToolkit.getCharSequenceLength((CharSequence) str) != 0) {
            this.flag = getFlagByAddressType(getAddressType());
            this.mPubKey = getPubKey();
            CPrivateKeyInterface privateKey = getPrivateKey();
            this.mPubKey = privateKey.getPubKey();
            if(privateKey instanceof CKey){
                this.mFullPubKey=((CKey) privateKey).getUnCompressedPubKey();
            }
            this.mPrivateKey = getEncryptedPrivateKey();
            this.wallet.encryptPrivateKeyByPwd(privateKey.getCopyBytes(), this.mPrivateKey, str);
            privateKey.clearBytes();
            initDestFromPubKey();
            addSubAddressInfoFromWallet();
            return;
        }
        throw new NullPointerException("password is empty");
    }

    public Address(@NonNull WalletTable walletTable, @NonNull CTxDestination cTxDestination) {
        super(walletTable);
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        this.cTxDestination = cTxDestination;
        addSubAddressInfoFromWallet();
    }

    public Address(@NonNull Address address) {
        super((WalletTable) address.getTable());
        this.label = "";
        this.account = Integer.MIN_VALUE;
        this.address_group = -1;
        this.f13066p = 0;
        this.f13048B = false;
        this.cTxDestination = null;
        mo44187a(address);
    }
    //mo44252y
    public double getFissionReward() {
        return this.fission_reward;
    }
    //mo44253z
    public double getRunawayReward() {
        return this.runaway_reward;
    }

    //mo44155A
    public int getDirectInvNo() {
        return this.direct_inv_no;
    }

    //mo44251x
    public long getLastBlock() {
        return this.last_block;
    }

    //mo44157C
    public double getSeasonFissionReward() {
        return this.season_fission_reward;
    }
    //mo44159E
    public long getFissionTotal() {
        return this.fission_total;
    }
    //mo44158D
    public long getSeasonFissionTotal() {
        return this.season_fission_total;
    }

    //mo44243p
    public int getAccount() {
        return this.account;
    }

    public void mo44187a(Address address) {
        initFromOtherAddress(address);
        initDestFromPubKey();
        mo44210c(address);
    }

    public void mo44210c(Address address) {
        HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> hashMap = address.chainTypeToBalanceInfoMap;
        if (hashMap != null && !hashMap.isEmpty()) {
            for (Map.Entry entry : address.chainTypeToBalanceInfoMap.entrySet()) {
                addSubAddressInfo(new SubAddressInfo(((SubAddressInfo) entry.getValue()).wallet));
            }
        }
    }

    //mo44219e
    public void updateAddressGroup(int i) {
        this.address_group = i;
        ContentValues contentValues = new ContentValues();
        contentValues.put("address_group", Integer.valueOf(i));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44167M
    public void updateNullAddressGroup() {
        this.address_group = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put("address_group", Integer.valueOf(this.address_group));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //mo44200b
    public void initFromOtherAddress(Address address) {
        this.label = address.label;
        this.account = address.account;
        this.f13051a = address.f13051a;
        this.f13052b = address.f13052b;
        this.f13057g = address.f13057g;
        this.flag = address.flag;
        this.address_group = address.address_group;
        this.address_order_index = address.address_order_index;
        this.v_index = address.v_index;
        this.mPubKey = getPubKey();
        try {
            this.mPubKey.initFromOtherPubKey(address.mPubKey);
        } catch (AddressFormatException e) {
            e.printStackTrace();
        }
        this.mPrivateKey = getEncryptedPrivateKey();
        this.mPrivateKey.initFromOtherBytes(address.mPrivateKey.getBytes());
    }

    //mo44233j
    public void initDestFromPubKey() {
        CPubkeyInterface mPubKey = this.mPubKey;
        if (mPubKey != null) {
            this.cTxDestination = mPubKey.getCKeyID();
        } else if (!isWatchedAddress()) {
            this.cTxDestination = null;
        }
    }

    //mo44242o
    public boolean isWatchedAddress() {
        return (this.flag & 64) != 0;
    }

    public String getOrginLabel() {
        if(this.label==null||this.label.equals(""))
            return "";
        return this.label;
    }

    //mo44240m
    public String getLabel() {
        if(this.label==null||this.label.equals(""))
            return "";
        return "["+this.label+"]";
    }

    public boolean mo44156B() {
        return this.f13057g;
    }

    //mo44186a
    public void setLabel(String str, boolean needUpdateDb) {
        this.label = str;
        if (needUpdateDb) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("lable", str);
            updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
        }
    }

    //m12448a
    public static int getFlagByAddressType(AddressType addressType) {
        switch (addressType) {
            case GENERAL:
                return 1;
            case WITNESS_V0_KEY_HASH:
                return 2048;
            case WITNESS_V0_SCRIPT_HASH:
                return 5632;
            case MULTISIG:
                return 4;
            case ANONYMOUS:
                return 2;
            case IDENTITY:
                return 16;
            case UNKNOWN:
                return 0;
            case WITNESS_V0_KEY_HASH_SCRIPT:
                return 16384;
            case WITNESS_V0_SCRIPT_HASH_SCRIPT:
                return 32768;
            default:
                return 0;
        }
    }
    //m12449a
    //915 m13071a
    public static AddressType getAddressType(int i) {
        if (DataTypeToolkit.bitAndNotZero(i, 16384)) {
            return AddressType.WITNESS_V0_KEY_HASH_SCRIPT;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 32768)) {
            return AddressType.WITNESS_V0_SCRIPT_HASH_SCRIPT;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 2)) {
            return AddressType.ANONYMOUS;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 2048)) {
            return AddressType.WITNESS_V0_KEY_HASH;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 5632)) {
            return AddressType.WITNESS_V0_SCRIPT_HASH;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 4)) {
            return AddressType.MULTISIG;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 16)) {
            return AddressType.IDENTITY;
        }
        if (DataTypeToolkit.bitAndNotZero(i, 1)) {
            return AddressType.GENERAL;
        }
        return AddressType.UNKNOWN;
    }

    //mo44180a
    //915 mo44548a
    public void setFlag(int i, boolean calOr) {
        this.flag = DataTypeToolkit.m11489a(this.flag, i, calOr);
    }

    //mo44203b
    public boolean hasBlockChainTypeInSubAddressInfoMap(BLOCK_CHAIN_TYPE blockChainType) {
        HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> hashMap = this.chainTypeToBalanceInfoMap;
        if (hashMap == null) {
            return false;
        }
        return hashMap.containsKey(blockChainType);
    }

    //mo44236k
    public void addSubAddressInfoFromWallet() {
        if (this.cTxDestination == null) {
            this.chainTypeToBalanceInfoMap = null;
        } else {
            addSubAddressInfo(new SubAddressInfo(this.wallet));
        }
    }

    //mo44189a
    public boolean addSubAddressInfo(SubAddressInfo subAddressInfo) {
        if (subAddressInfo == null) {
            return false;
        }
        if (subAddressInfo.wallet != null) {
            HashMap<BLOCK_CHAIN_TYPE, SubAddressInfo> hashMap = this.chainTypeToBalanceInfoMap;
            if (hashMap == null) {
                this.chainTypeToBalanceInfoMap = new HashMap<>();
                this.chainTypeToBalanceInfoMap.put(subAddressInfo.getBlockChainType(), subAddressInfo);
                return true;
            } else if (!hashMap.containsKey(subAddressInfo.getBlockChainType())) {
                this.chainTypeToBalanceInfoMap.put(subAddressInfo.getBlockChainType(), subAddressInfo);
                return true;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("SubAddressInfo already registed type for ");
                sb.append(subAddressInfo.getBlockChainType());
                throw new IllegalArgumentException(sb.toString());
            }
        } else {
            throw new IllegalArgumentException("SubAddressInfo must contains type.");
        }
    }

    public String getSignMessageBase64(CharSequence message, CharSequence pwd) throws NotMatchException, AddressFormatException, NoPrivateKeyException {
        this.wallet.checkWalletPassword(pwd);
        CPrivateKeyInterface privateKey = this.wallet.getAddressPrivateKey(this, pwd);
        if (privateKey != null) {
            try {
                String signString = getSignMessageBase64String(message, privateKey);
                privateKey.clearBytes();
                return signString;
            } catch (UnsupportedOperationException e) {
                privateKey.clearBytes();
                throw e;
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(getAddressString(new BLOCK_CHAIN_TYPE[0]));
            sb.append(" didn't hava a private key.");
            throw new NoPrivateKeyException(sb.toString());
        }
    }

    //mo44195b
    public final String getSignMessageBase64String(CharSequence charSequence, CPrivateKeyInterface privateKey) {
        return StringToolkit.getBase64FromBytes(getSignMessageBytes(charSequence, privateKey));
    }

    public void mo40876a(boolean... zArr) {
    }

    //mo40785a
    public void initTableItemVariable(Cursor cursor, int i, int i2, int columnIndex) {
        boolean z = false;
        byte[] blob=null;
        switch (columnIndex) {
            case 0:
                try {
                    this.cTxDestination = CTxDestinationFactory.getDesByBytes(StringToolkit.getBytes(cursor.getString(columnIndex)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            case 1:
                this.flag = cursor.getInt(columnIndex);
                return;
            case 2:
                this.account = cursor.getInt(columnIndex);
                return;
            case 3:
                this.label = cursor.getString(columnIndex);
                return;
            case 4:
                this.fission_reward = cursor.getDouble(columnIndex);
                return;
            case 5:
                this.runaway_reward = cursor.getDouble(columnIndex);
                return;
            case 6:
                this.direct_inv_no = cursor.getInt(columnIndex);
                return;
            case 7:
                this.last_block = cursor.getLong(columnIndex);
                return;
            case 8:
                blob = cursor.getBlob(columnIndex);
                if (blob == null || blob.length == 0) {
                    this.mPubKey = null;
                } else {
                    if (this.mPubKey == null) {
                        this.mPubKey = getPubKey();
                    }
                    try {
                        this.mPubKey.Set(blob);
                    } catch (AddressFormatException e) {
                        e.printStackTrace();
                    }
                }
                mo40876a(true);
                return;
            case 9:
                this.mPrivateKey.initFromOtherBytes(cursor.getBlob(columnIndex));
                return;
            case 10:
                this.season_fission_reward = (double) cursor.getLong(columnIndex);
                return;
            case 11:
                this.season_fission_total = cursor.getLong(columnIndex);
                return;
            case 12:
                this.fission_total = cursor.getLong(columnIndex);
                return;
            case 13:
                this.address_group = cursor.getInt(columnIndex);
                return;
            case 14:
                this.address_order_index = cursor.getInt(columnIndex);
                return;
            case 15:
                if (cursor.getInt(columnIndex) != 0) {
                    z = true;
                }
                this.is_direct_sign_vid = z;
                return;
            case 16:
                if (cursor.getInt(columnIndex) != 0) {
                    z = true;
                }
                this.applying_vid = z;
                return;
            case 17:
                this.clue_txid = cursor.getString(columnIndex);
                return;
            case 18:
                this.category_status = cursor.getInt(columnIndex);
                return;
            case 19:
                this.v_index = cursor.getInt(columnIndex);
                return;
            case 20:
                if (cursor.getInt(columnIndex) != 0) {
                    z = true;
                }
                this.hide = z;
                return;
            case 21:
                if (cursor.getInt(columnIndex) != 0) {
                    z = true;
                }
                this.applying_vxd = z;
                return;
            case 22:
                this.vxd_txid = cursor.getString(columnIndex);
                return;
            case 23:
                blob = cursor.getBlob(columnIndex);
                if (blob == null || blob.length == 0) {
                    this.mFullPubKey = null;
                } else {
                    if (this.mFullPubKey == null) {
                        this.mFullPubKey = getPubKey();
                    }
                    try {
                        this.mFullPubKey.Set(blob);
                    } catch (AddressFormatException e) {
                        e.printStackTrace();
                    }
                }
                mo40876a(true);
                return;
            default:
                return;
        }
    }


    //915 mo44611l
    public String[] getCloumnsStringArr() {
        return new String[]{"hash", "account", "flag", "lable", "fission_reward", "runaway_reward", "direct_inv_no", "last_block", "pub", "priv", "address_group", "address_order_index", "is_direct_sign_vid", "applying_vid", "clue_txid", NotificationCompat.CATEGORY_STATUS, "hide", "address_sync_flag"};
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("hash", getCTxDestination().getHash());
        CPubkeyInterface pubKey = this.mPubKey;
        if (pubKey != null) {
            contentValues.put("pub", pubKey.getByteArr());
        }
        EncryptedPrivateKey privateKey = this.mPrivateKey;
        if (privateKey != null) {
            contentValues.put("priv", privateKey.getBytes());
        }
        String str = this.label;
        if (str != null) {
            contentValues.put("lable", str);
        }
        contentValues.put("account", Integer.valueOf(this.account));
        contentValues.put("flag", Integer.valueOf(this.flag));
        contentValues.put("fission_reward", Double.valueOf(this.fission_reward));
        contentValues.put("runaway_reward", Double.valueOf(this.runaway_reward));
        contentValues.put("direct_inv_no", Integer.valueOf(this.direct_inv_no));
        contentValues.put("last_block", Long.valueOf(this.last_block));
        contentValues.put("season_fission_reward", Double.valueOf(this.season_fission_reward));
        contentValues.put("season_fission_total", Long.valueOf(this.season_fission_total));
        contentValues.put("fission_total", Long.valueOf(this.fission_total));
        contentValues.put("address_group", Integer.valueOf(this.address_group));
        contentValues.put("address_order_index", Integer.valueOf(this.address_order_index));
        contentValues.put("is_direct_sign_vid", Boolean.valueOf(this.is_direct_sign_vid));
        contentValues.put("applying_vid", Boolean.valueOf(this.applying_vid));
        contentValues.put("clue_txid", this.clue_txid);
        contentValues.put(NotificationCompat.CATEGORY_STATUS, Integer.valueOf(this.category_status));
        contentValues.put("v_index", Integer.valueOf(this.v_index));
        contentValues.put("hide", Boolean.valueOf(this.hide));
        contentValues.put("applying_vxd", Boolean.valueOf(this.applying_vxd));
        contentValues.put("vxd_txid", this.vxd_txid);
        CPubkeyInterface fullPubKey = this.mFullPubKey;
        if (fullPubKey != null) {
            contentValues.put("full_pub", fullPubKey.getByteArr());
        }
        return contentValues;
    }

    //mo44238k
    public void updateIsHide(boolean isHide) {
        this.hide = isHide;
        ContentValues contentValues = new ContentValues();
        contentValues.put("hide", Boolean.valueOf(isHide));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }

    //915 mo44606j
    public void updateRecycleFlag(boolean z) {
        setFlag(8192, z);
        ContentValues contentValues = new ContentValues();
        contentValues.put("flag", Integer.valueOf(this.flag));
        updateData(contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{this.cTxDestination.getHash()});
    }
}
