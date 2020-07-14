package com.vtoken.application.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.vc.libcommon.exception.AddressFormatException;
import com.vc.libcommon.utils.JNIUtils;
import com.vcwallet.core.part.PartWallet;
import com.vtoken.application.constant.Constant;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.vdsecology.vcash.tools.NativeLibLoader;
import generic.crypto.KeyCryptor;
import generic.exceptions.PasswordException;
import generic.keyid.CTxDestinationFactory;
import generic.utils.AddressUtils;
import org.apache.commons.io.FileUtils;

import vcash.exception.SpaceNotEnoughException;
import vdsMain.*;
import vdsMain.config.WalletConfigure;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.PartAddress;
import vdsMain.wallet.VWallet;
import vdsMain.model.Address;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Boolean.*;

public class PartLoader {

    //915 f13500c
    private static PartLoader loader;

    //915 f13501a
    ArrayList<PartWallet> partWalletArrayList;

    //915 f13502b
    String title;

    //f13503d
    private Context mContext;

    //915 f13504e
    private PartWallet partWallet;

    /* renamed from: hq$2 */
    /* compiled from: PartLoader */
    static /* synthetic */ class C39832 {

        /* renamed from: a */
        static final /* synthetic */ int[] f13506a = new int[BLOCK_CHAIN_TYPE.values().length];

        static  int[] f13507b=new int[BLOCK_CHAIN_TYPE.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(11:0|1|2|3|4|5|6|7|9|10|12) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            f13507b = new int[NETWORK_TYPE.values().length];
            f13507b[NETWORK_TYPE.MAIN.ordinal()] = 1;
            f13507b[NETWORK_TYPE.TEST.ordinal()] = 2;
            try {
                f13507b[NETWORK_TYPE.REGTEST.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f13506a[BLOCK_CHAIN_TYPE.BITCOIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    private PartLoader(Context context) {
        this.mContext = context;
        NativeLibLoader.INSTANCE.loadLibrary();
        JNIUtils.INSTANCE.initJNI();
    }

    //m12633a
    public static PartLoader getSingleInstance(Context context) {
        if (loader == null) {
            synchronized (PartLoader.class) {
                if (loader == null) {
                    loader = new PartLoader(context);
                }
            }
        }
        loader.setContext(context.getApplicationContext());
        return loader;
    }

    //mo44244b
    public void setContext(Context context) {
        this.mContext = context;
    }

    //915 mo44237a
    public void setTitle(String str) {
        this.title = str;
    }

    //915 mo44243b
    public ArrayList<PartWallet> getPartWalletList(String path) {
        List list;
        List<PartAddress> defaultPartAddressList;
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            return null;
        }
        File[] listFiles = pathFile.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append(File.separator);
        sb.append("vcash");
        String vcashPathString = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(path);
        sb3.append(File.separator);
        sb3.append("vds");
        String vdsPathString = sb3.toString();
        File vcashPathFile = new File(vcashPathString);
        File vdsPathFile = new File(vdsPathString);
        if (vcashPathFile.exists() && !vdsPathFile.exists()) {
            vcashPathFile.renameTo(vdsPathFile);
        }
        ArrayList<PartWallet> partWallets = new ArrayList<>();
        if (vdsPathFile.exists()) {
            Log.i("PartLoader", "load: default");
            PartWallet partWallet = new PartWallet(path);
            try {
                defaultPartAddressList = checkAndGetWalletPartAddressList(partWallet, BLOCK_CHAIN_TYPE.VCASH);
            } catch (Exception unused) {
                defaultPartAddressList = null;
            }
            if (defaultPartAddressList != null && !defaultPartAddressList.isEmpty()) {
                StringBuilder confPath = new StringBuilder();
                confPath.append(path);
                confPath.append(File.separator);
                confPath.append("vds");
                confPath.append(File.separator);
                confPath.append("wallet.conf");
                if (!new File(confPath.toString()).exists()) {
                    partWallet.setLabel(this.title);
                }
                partWallets.add(partWallet);
            }
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append(pathFile);
        sb6.append(File.separator);
        sb6.append("separated");
        File separatedDirectory = new File(sb6.toString());
        if (!separatedDirectory.exists()) {
            return partWallets;
        }
        File[] exterWalletList = separatedDirectory.listFiles();
        if (exterWalletList == null || exterWalletList.length == 0) {
            return partWallets;
        }
        for (File exterWallet : exterWalletList) {
            String name = exterWallet.getName();
            if (name.startsWith("wallet_") && !name.equalsIgnoreCase("wallet_")) {
                PartWallet walletPath = new PartWallet(exterWallet.getPath());
                try {
                    list = checkAndGetWalletPartAddressList(walletPath, BLOCK_CHAIN_TYPE.VCASH);
                } catch (Exception unused2) {
                    list = null;
                }
                if (list != null && !list.isEmpty()) {
                    partWallets.add(walletPath);
                }
            }
        }
        return partWallets;
    }

    //915 mo44246c
    public boolean hasVdsWallet(String path) {
        if (!new File(path).exists()) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append(File.separator);
        sb.append("vds");
        File vdsFile = new File(sb.toString());
        if (!vdsFile.exists()) {
            return false;
        }
        File[] listFiles = vdsFile.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return false;
        }
        return true;
    }

    //915 mo44235a
    public ChainParams getChainParams(PartWallet partWallet, BLOCK_CHAIN_TYPE block_chain_type) {
        ChainParams chainParams;
        String walletPath = partWallet.getPath();
        if (!hasVdsWallet(walletPath)) {
            return null;
        }
        chainParams = new VChainParam(new VWallet(this.mContext));
//        if (C39832.f13506a[ihVar.ordinal()] != 1) {
//            uVar = new VChainParam(new VWallet(this.f13503d));
//        } else {
//            uVar = new ChainParams(new BitcoinWallet(this.f13503d));
//        }
        StringBuilder sb = new StringBuilder();
        sb.append(walletPath);
        sb.append(File.separator);
        sb.append("vds");
        sb.append(File.separator);
        sb.append("wallet.conf");
        WalletConfigure walletConfigure = getNewWalletConfigure(chainParams, sb.toString());
        partWallet.setLabel(walletConfigure.configFile.getValueFromMap("label"));
        String balance = walletConfigure.configFile.getValueFromMap("balance");
        if (TextUtils.isEmpty(balance)) {
            balance = "0";
        }
        partWallet.setBalance(balance);
//        String btcBalance = walletConfigure.configFile.getValueFromMap("btcBalance");
//        if (TextUtils.isEmpty(btcBalance)) {
//            btcBalance = "0";
//        }
//        partWallet.setBtcBalance(btcBalance);
        return chainParams;
    }

    //915 mo44242b
    public ArrayList<PartAddress> checkAndGetWalletPartAddressList(PartWallet partWallet, BLOCK_CHAIN_TYPE blockChainType) {
        if (partWallet == null || TextUtils.isEmpty(partWallet.getPath())) {
            return null;
        }
        return getWalletParAddressList(getChainParams(partWallet, blockChainType), partWallet);
    }

    //915 m12634a
    private ArrayList<PartAddress> getWalletParAddressList(ChainParams chainParams, PartWallet partWallet) {
        SQLiteDatabase sQLiteDatabase;
        if (partWallet == null || TextUtils.isEmpty(partWallet.getPath())) {
            return null;
        }
        String walletPath = partWallet.getPath();
        NETWORK_TYPE networkType = chainParams.getSelfNetworkType();
        StringBuilder sb = new StringBuilder();
        sb.append(walletPath);
        sb.append(File.separator);
        sb.append("vds");
        sb.append(File.separator);
        sb.append("db");
        String vdsDbPathString = sb.toString();
        switch (networkType) {
            case MAIN:
                StringBuilder sb3 = new StringBuilder();
                sb3.append(walletPath);
                sb3.append(File.separator);
                sb3.append("vds");
                sb3.append(File.separator);
                sb3.append("db");
                vdsDbPathString = sb3.toString();
                break;
            case TEST:
                StringBuilder sb4 = new StringBuilder();
                sb4.append(walletPath);
                sb4.append(File.separator);
                sb4.append("vds");
                sb4.append(File.separator);
                sb4.append("test");
                sb4.append(File.separator);
                sb4.append("db");
                vdsDbPathString = sb4.toString();
                break;
            case REGTEST:
                StringBuilder sb5 = new StringBuilder();
                sb5.append(walletPath);
                sb5.append(File.separator);
                sb5.append("vds");
                sb5.append(File.separator);
                sb5.append("regtest");
                sb5.append(File.separator);
                sb5.append("db");
                vdsDbPathString = sb5.toString();
                break;
        }
        StringBuilder personalDbPath = new StringBuilder();
        personalDbPath.append(vdsDbPathString);
        personalDbPath.append(File.separator);
        personalDbPath.append("personal");
        int i = 1;
        SQLiteDatabase personalDatabase = SQLiteDatabase.openDatabase(personalDbPath.toString(), null, SQLiteDatabase.OPEN_READONLY);
        Cursor addrCursor = personalDatabase.query("hd_account", new String[]{"addr"}, null, null, null, null, null);
        int i2 = 0;
        int addr = addrCursor.moveToNext() ? addrCursor.getInt(0) : 0;
        addrCursor.close();
        Cursor addressCursor = personalDatabase.query("address", new String[]{"hash", "lable", "address_order_index", "clue_txid", "flag", "is_direct_sign_vid", "priv","applying_vxd"}, null, null, null, null, null);
        ArrayList<PartAddress> partAddresses = new ArrayList<>();
        int generalNumber = 0;
        int vidNumber = 0;
        int anonymousNumber = 0;
        int multsignNumber = 0;
        int vxdNumber=0;
        int i8 = 0;
        while (addressCursor.moveToNext()) {
            PartAddress partAddress = new PartAddress();
            try {
                CTxDestination des = CTxDestinationFactory.getDesByBytes(StringToolkit.getBytes(addressCursor.getString(i2)));
                partAddress.setDes(des);
                partAddress.setAddressString(AddressUtils.getAddressString(des, chainParams));
                partAddress.setLabel(addressCursor.getString(i));
                partAddress.setAddressOrderIndex(addressCursor.getInt(2));
                String clueTxid = addressCursor.getString(3);
                int flag = addressCursor.getInt(4);
                AddressType addressType = Address.getAddressType(flag);
                boolean isDirectSignVid = addressCursor.getInt(5) != 0;
                byte[] privBytes = addressCursor.getBlob(6);
                int vxdFlag = addressCursor.getInt(7);
                sQLiteDatabase = personalDatabase;
                try {
                    if (DataTypeToolkit.bitAndNotZero(flag, 64)) {
                        i = 1;
                        i2 = 0;
                    } else {
                        if(vxdFlag==1){
                            vxdNumber++;
                        }
                        if (addressType != AddressType.IDENTITY && TextUtils.isEmpty(clueTxid)) {
                            if (!isDirectSignVid) {
                                if (addressType == AddressType.ANONYMOUS) {
                                    anonymousNumber++;
                                } else if (addressType == AddressType.MULTISIG && (privBytes == null || privBytes.length == 0)) {
                                    multsignNumber++;
                                } else if (DataTypeToolkit.bitAndNotZero(flag, 256)) {
                                    i8++;
                                } else {
                                    generalNumber++;
                                }
                                partAddress.setAddressType(addressType);
                                partAddresses.add(partAddress);
                                personalDatabase = sQLiteDatabase;
                            }
                        }else{
                            vidNumber++;
                            partAddress.setAddressType(addressType);
                            partAddresses.add(partAddress);
                            personalDatabase = sQLiteDatabase;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    personalDatabase = sQLiteDatabase;
                    i = 1;
                    i2 = 0;
                }
            } catch (Exception e2) {
                sQLiteDatabase = personalDatabase;
                e2.printStackTrace();
                personalDatabase = sQLiteDatabase;
                i = 1;
                i2 = 0;
            }
        }
        SQLiteDatabase sQLiteDatabase2 = personalDatabase;
        addressCursor.close();
        partWallet.setGeneralNumber(generalNumber);
        partWallet.setVidNumber(vidNumber);
        partWallet.setAnonymousNumber(anonymousNumber);
        partWallet.setMutliSignNumber(multsignNumber);
        partWallet.setVxdNumber(vxdNumber);
        partWallet.setAddr(addr);
        partWallet.mo19003e(i8);
        if (!(generalNumber == 0 && vidNumber == 0 && multsignNumber == 0 && addr == 0 && i8 == 0)) {
            Cursor query3 = sQLiteDatabase2.query("settings", new String[]{"value"}, "name=?", new String[]{"HideAllAmount"}, null, null, null, null);
            if (query3.moveToNext()) {
                partWallet.setIsHideAllAmount(!valueOf(query3.getString(0)));
            }
            query3.close();
        }
        sQLiteDatabase2.close();
        return partAddresses;
    }

    /* access modifiers changed from: protected */
    //915 mo44234a
    public WalletConfigure getNewWalletConfigure(ChainParams chainParams, String path) {
        WalletConfigure walletConfigure = new WalletConfigure();
        try {
            walletConfigure.initConfigFile(chainParams, path);
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
            }
            Log.i("PartLoader", "loadConfigureFile: initMain");
            chainParams.initMainParams();
        }
        return walletConfigure;
    }

    //915 mo44233a
    public PartWallet getCurPartWallet() {
        return this.partWallet;
    }

    //915 mo44236a
    public void setCurWallet(PartWallet partWallet) {
        this.partWallet = partWallet;
    }

    //mo44241b
    public String getDefaultWalletPath() {
        StringBuilder defaultPath = new StringBuilder();
        defaultPath.append(FileToolkit.getVaildPath(Environment.getExternalStorageDirectory().getAbsolutePath()));
        defaultPath.append("/");
        //defaultPath.append(this.mContext.getPackageName());
        defaultPath.append(Constant.INSTANCE.getWalletConstPath());
        String defaultPathString = defaultPath.toString();
        try {
            FileToolkit.checkDirectory(defaultPathString, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultPathString;
    }

    //915 mo44245c
    public String getSeparatedPath() {
        String defaultWalletPath = getDefaultWalletPath();
        StringBuilder sb = new StringBuilder();
        sb.append(defaultWalletPath);
        sb.append(File.separator);
        sb.append("separated");
        return sb.toString();
    }

    //915 mo44248d
    public Properties loadPropertiesFromPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream(str), StandardCharsets.UTF_8));
        } catch (Exception unused) {
        }
        return properties;
    }

    //915 mo44247d
    public ArrayList<PartWallet> getPartWalletList() {
        return this.partWalletArrayList;
    }

    //915 mo44238a
    public void setPartWalletList(ArrayList<PartWallet> arrayList) {
        this.partWalletArrayList = arrayList;
    }

    //915 mo44239a
    public boolean checkWalletPwd(String str, String str2) throws PasswordException {
        Cursor rawQuery = SQLiteDatabase.openDatabase(str2, null, 0).rawQuery(String.format(Locale.ENGLISH, "select value from %s where name=? ", new Object[]{"settings"}), new String[]{"pwd"});
        if (rawQuery.moveToNext()) {
            String string = rawQuery.getString(0);
            rawQuery.close();
            KeyCryptor keyCryptor = new KeyCryptor();
            keyCryptor.mo19024a(string);
            return keyCryptor.checkPwd(str);
        }
        rawQuery.close();
        throw new PasswordException("transfer to wallet can not get pwd");
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x017b  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x01a6  */
    /* renamed from: a */
    public boolean mo44240a(String str, String str2, String str3, ArrayList<String> arrayList, boolean z, VCashCore hbVar) throws PasswordException, SpaceNotEnoughException, AddressFormatException {
        boolean z2;
        boolean z3;
        KeyCryptor keyCryptor;
        char c;
        SQLiteDatabase sQLiteDatabase;
        String str4;
        String str5;
        PartLoader hqVar;
        SQLiteDatabase sQLiteDatabase2;
        SQLiteStatement sQLiteStatement;
        PartLoader partLoader = this;
        String str6 = str;
        String str7 = str2;
        String str8 = str3;
        VCashCore vCashCore = hbVar;
        StringBuilder sb = new StringBuilder();
        sb.append("transferAddresses: dbPath->");
        sb.append(str8);
        Log.i("PartLoader", sb.toString());
        int i = 0;
        SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(str8, null, 0);
        int i2 = 1;
        Cursor rawQuery = openDatabase.rawQuery(String.format(Locale.ENGLISH, "select value from %s where name=? ", new Object[]{"settings"}), new String[]{"pwd"});
        if (rawQuery.moveToNext()) {
            String string = rawQuery.getString(0);
            rawQuery.close();
            Cursor rawQuery2 = openDatabase.rawQuery(" PRAGMA table_info('address')", null);
            HashSet hashSet = new HashSet();
            while (rawQuery2.moveToNext()) {
                hashSet.add(rawQuery2.getString(1));
            }
            rawQuery2.close();
            KeyCryptor keyCryptor2 = new KeyCryptor();
            keyCryptor2.mo19024a(string);
            if (keyCryptor2.checkPwd(str7)) {
                String[] l = vCashCore.getAddressFromAddressString((String) arrayList.get(0), new BLOCK_CHAIN_TYPE[0]).getCloumnsStringArr();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("REPLACE INTO ");
                stringBuffer.append("address");
                stringBuffer.append(" ( ");
                for (int i3 = 0; i3 < l.length; i3++) {
                    String str9 = l[i3];
                    if (hashSet.contains(str9)) {
                        if (i3 != 0) {
                            stringBuffer.append(" , ");
                        }
                        stringBuffer.append(str9);
                    }
                }
                stringBuffer.append(" ) VALUES ( ");
                for (int i4 = 0; i4 < l.length; i4++) {
                    if (hashSet.contains(l[i4])) {
                        if (i4 != 0) {
                            stringBuffer.append(" , ");
                        }
                        stringBuffer.append("?");
                    }
                }
                stringBuffer.append(" ) ");
                String stringBuffer2 = stringBuffer.toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("transferAddresses: ");
                sb2.append(stringBuffer2);
                Log.i("PartLoader", sb2.toString());
                openDatabase.beginTransaction();
                SQLiteStatement compileStatement = openDatabase.compileStatement(stringBuffer2);
                KeyCryptor n = vCashCore.getWalletByChainType(BLOCK_CHAIN_TYPE.VCASH).getSelfKeyCryptor();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Address c2 = vCashCore.getAddressFromAddressString((String) it.next(), new BLOCK_CHAIN_TYPE[i]);
                    if (c2 == null) {
                        Log.i("PartLoader", "transfer but address is empty");
                    } else {
                        compileStatement.clearBindings();
                        String[] strArr = {"hash", "priv"};
                        String[] strArr2 = new String[i2];
                        strArr2[i] = c2.getCTxDestination().getHash();
                        Address address = c2;
                        KeyCryptor keyCryptor3 = n;
                        SQLiteStatement sQLiteStatement2 = compileStatement;
                        String[] strArr3 = l;
                        SQLiteDatabase sQLiteDatabase3 = openDatabase;
                        Cursor query = openDatabase.query("address", strArr, "hash=?", strArr2, null, null, null);
                        if (query.moveToNext()) {
                            byte[] blob = query.getBlob(1);
                            if (blob != null && blob.length > 0) {
                                z2 = true;
                                query.close();
                                if (!z2) {
                                    Log.i("PartLoader", "subscribe: has same address in selected wallet");
                                    if (!z) {
                                        Log.i("PartLoader", "transferAddresses: recycle address ");
                                        vCashCore.checkAndRecycleAddress(str6, address);
                                        openDatabase = sQLiteDatabase3;
                                        n = keyCryptor3;
                                        compileStatement = sQLiteStatement2;
                                        l = strArr3;
                                        i = 0;
                                        i2 = 1;
                                    } else {
                                        openDatabase = sQLiteDatabase3;
                                        n = keyCryptor3;
                                        compileStatement = sQLiteStatement2;
                                        l = strArr3;
                                        i = 0;
                                        i2 = 1;
                                    }
                                } else {
                                    boolean G = address.isAccount();
                                    if (address.isAccount()) {
                                        z3 = false;
                                        address.setFlag256(false);
                                        G = true;
                                    } else {
                                        z3 = false;
                                    }
                                    boolean Y = address.isCategoryStatusNotZero();
                                    if (!Y) {
                                        address.updateCategoryStatusByBoolean(true, z3);
                                    }
                                    if (!TextUtils.equals(str, str2)) {
                                        Log.i("PartLoader", "transfer convert once");
                                        keyCryptor = keyCryptor3;
                                        partLoader.m12635a(keyCryptor, str6, str7, address);
                                    } else {
                                        keyCryptor = keyCryptor3;
                                    }
                                    ContentValues c3 = address.getContentValues();
                                    String[] strArr4 = strArr3;
                                    int i5 = 0;
                                    while (i5 < strArr4.length) {
                                        String str10 = strArr4[i5];
                                        if (!hashSet.contains(str10)) {
                                            sQLiteStatement = sQLiteStatement2;
                                        } else {
                                            Object obj = c3.get(str10);
                                            if (obj == null) {
                                                sQLiteStatement = sQLiteStatement2;
                                                sQLiteStatement.bindNull(i5 + 1);
                                            } else {
                                                sQLiteStatement = sQLiteStatement2;
                                                if (obj instanceof byte[]) {
                                                    sQLiteStatement.bindBlob(i5 + 1, (byte[]) obj);
                                                } else if (obj instanceof Integer) {
                                                    sQLiteStatement.bindLong(i5 + 1, (long) ((Integer) obj).intValue());
                                                } else if (obj instanceof Long) {
                                                    sQLiteStatement.bindLong(i5 + 1, ((Long) obj).longValue());
                                                } else if (obj instanceof Double) {
                                                    sQLiteStatement.bindDouble(i5 + 1, ((Double) obj).doubleValue());
                                                } else if (obj instanceof Boolean) {
                                                    sQLiteStatement.bindLong(i5 + 1, ((Boolean) obj).booleanValue() ? 1 : 0);
                                                } else {
                                                    sQLiteStatement.bindString(i5 + 1, (String) obj);
                                                }
                                            }
                                        }
                                        i5++;
                                        sQLiteStatement2 = sQLiteStatement;
                                        String str11 = str;
                                        String str12 = str2;
                                    }
                                    compileStatement = sQLiteStatement2;
                                    compileStatement.execute();
                                    Log.i("PartLoader", "execute: ");
                                    if (G) {
                                        address.setFlag256(true);
                                    }
                                    if (!Y) {
                                        c = 0;
                                        address.updateCategoryStatusByBoolean(false, false);
                                    } else {
                                        c = 0;
                                    }
                                    if (address instanceof BitcoinMultiSigAddress) {
                                        BitcoinMultiSigAddress bitcoinMultiSigAddress = (BitcoinMultiSigAddress) address;
                                        BLOCK_CHAIN_TYPE[] ihVarArr = new BLOCK_CHAIN_TYPE[1];
                                        ihVarArr[c] = BLOCK_CHAIN_TYPE.VCASH;
                                        List<String> a = bitcoinMultiSigAddress.mo40873a(ihVarArr);
                                        if (a == null || a.isEmpty()) {
                                            sQLiteDatabase = sQLiteDatabase3;
                                        } else {
                                            for (String addr : a) {
                                                String label = vCashCore.getLabelByAddressString(addr);
                                                try {
                                                    ContentValues contentValues = new ContentValues();
                                                    contentValues.put("addr", addr);
                                                    contentValues.put("lable", label);
                                                    sQLiteDatabase2 = sQLiteDatabase3;
                                                    try {
                                                        sQLiteDatabase2.insert("sub_address", "addr", c3);
                                                    } catch (Exception e) {
                                                        e = e;
                                                    }
                                                } catch (Exception e2) {
                                                    sQLiteDatabase2 = sQLiteDatabase3;
                                                    e2.printStackTrace();
                                                    sQLiteDatabase3 = sQLiteDatabase2;
                                                }
                                                sQLiteDatabase3 = sQLiteDatabase2;
                                            }
                                            sQLiteDatabase = sQLiteDatabase3;
                                        }
                                    } else {
                                        sQLiteDatabase = sQLiteDatabase3;
                                    }
                                    if (!TextUtils.equals(str, str2)) {
                                        hqVar = this;
                                        str5 = str;
                                        str4 = str2;
                                        hqVar.m12635a(keyCryptor, str4, str5, address);
                                        Log.i("PartLoader", "transfer convert again");
                                    } else {
                                        hqVar = this;
                                        str5 = str;
                                        str4 = str2;
                                    }
                                    if (!z) {
                                        Log.i("PartLoader", "transferAddresses: recycle address ");
                                        vCashCore.checkAndRecycleAddress(str5, address);
                                    }
                                    partLoader = hqVar;
                                    str6 = str5;
                                    n = keyCryptor;
                                    str7 = str4;
                                    l = strArr4;
                                    openDatabase = sQLiteDatabase;
                                    i = 0;
                                    i2 = 1;
                                }
                            }
                        }
                        z2 = false;
                        query.close();
                        if (!z2) {
                        }
                    }
                }
                PartLoader hqVar3 = partLoader;
                SQLiteDatabase sQLiteDatabase4 = openDatabase;
                compileStatement.close();
                sQLiteDatabase4.setTransactionSuccessful();
                sQLiteDatabase4.endTransaction();
                sQLiteDatabase4.close();
                return true;
            }
            PartLoader hqVar4 = partLoader;
            throw new PasswordException("transfer to wallet pwd not equals input pwd");
        }
        PartLoader hqVar5 = partLoader;
        rawQuery.close();
        throw new PasswordException("transfer to wallet can not get pwd");
    }

    /* renamed from: a */
    private void m12635a(KeyCryptor keyCryptor, String str, String str2, Address jkVar) throws SpaceNotEnoughException {
        if (jkVar.getSelfEncryptedPrivateKey() != null) {
            try {
                jkVar.getSelfEncryptedPrivateKey().updateBytesByNewPwd(keyCryptor, str, str2);
            } catch (Exception e) {
                e.printStackTrace();
        }
        }
    }

    //915 mo44249e
    public String getNewWalletDirectory() throws SpaceNotEnoughException {
        String sb;
        File file = new File(getSingleInstance(this.mContext).getDefaultWalletPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.getFreeSpace() >= FileUtils.ONE_GB) {
            File separatedPath = new File(getSingleInstance(this.mContext).getSeparatedPath());
            if (!separatedPath.exists()) {
                separatedPath.mkdirs();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(separatedPath.getPath());
                sb2.append(File.separator);
                sb2.append("wallet_1");
                return sb2.toString();
            }
            File[] listFiles = separatedPath.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().startsWith("wallet_");
                }
            });
            if (listFiles == null || listFiles.length == 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(separatedPath.getPath());
                sb3.append(File.separator);
                sb3.append("wallet_1");
                return sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(separatedPath.getPath());
            sb4.append(File.separator);
            sb4.append("wallet_");
            sb4.append(listFiles.length);
            String sb5 = sb4.toString();
            if (!new File(sb5).exists()) {
                return sb5;
            }
            int i = 0;
            do {
                i++;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(separatedPath.getPath());
                sb6.append(File.separator);
                sb6.append("wallet_");
                sb6.append(i);
                sb = sb6.toString();
            } while (new File(sb).exists());
            return sb;
        }
        throw new SpaceNotEnoughException();
    }
}