package vdsMain.model;

import androidx.annotation.NonNull;
import bitcoin.CKey;
import bitcoin.CPubKey;
import generic.utils.AddressUtils;
import vdsMain.*;
import vdsMain.table.WalletTable;
import vdsMain.transaction.CHashWriter;

import java.util.HashMap;
import java.util.Locale;

public class BitCoinAddress extends Address {

    public boolean mo40821a(BLOCK_CHAIN_TYPE igVar) {
        return true;
    }

    public BitCoinAddress(@NonNull Address address) {
        super(address);
    }

    public BitCoinAddress(@NonNull WalletTable kjVar, @NonNull CTxDestination oVar) {
        super(kjVar, oVar);
        setFlag(1, true);
    }

    public BitCoinAddress(@NonNull WalletTable walletTable) {
        super(walletTable);
    }

    public BitCoinAddress(@NonNull WalletTable walletTable, byte[] bArr, byte[] bArr2, CharSequence charSequence) {
        super(walletTable, bArr, bArr2, charSequence);
    }

    public BitCoinAddress(@NonNull WalletTable walletTable, byte[] pubKeyBytes,byte[] fullPubKeyBytes, byte[] privateKeyBytes, CharSequence charSequence) {
        super(walletTable, pubKeyBytes, fullPubKeyBytes,privateKeyBytes,charSequence);
    }

    public BitCoinAddress(@NonNull WalletTable walletTable, String pwd) {
        super(walletTable, pwd);
    }

    /* renamed from: d_ */
    public AddressType getAddressType() {
        return AddressType.GENERAL;
    }

    /* access modifiers changed from: protected */
    //mo40820a
    public void encryptPrivateKeyByPwd(byte[] bArr, CharSequence pwd) {
        CKey cKey = CKey.getCKeyFromBytes(bArr);
        this.mPubKey = cKey.getPubKey();
        this.wallet.encryptPrivateKeyByPwd(cKey.getPrivateKeyBytes(), this.mPrivateKey, pwd.toString());
        cKey.clearBytes();
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getPrivateKey() {
        CKey cKey = new CKey();
        cKey.mo9399a(true);
        return cKey;
    }

    //
    public byte[] getSignMessageBytes(CharSequence message, CPrivateKeyInterface privateKey) {
        try {
            CKey cKey = (CKey) privateKey;
            CHashWriter hashWriter = new CHashWriter();
            hashWriter.writeString("Vds Signed Message:\n");
            //hashWriter.writeString("Bitcoin Signed Message:\n");
            hashWriter.writeString(message.toString());
            byte[] bytes = cKey.signCompactNativeHash(hashWriter.GetHash());
            privateKey.clearBytes();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//
//    /* renamed from: a */
//    public static boolean m5482a(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, Wallet izVar) {
//        try {
//            CTxDestination a = CTxDestinationFactory.m912a(charSequence.toString(), izVar.getChainParams());
//            CHashWriter kVar = new CHashWriter();
//            kVar.mo44359a("Vds Signed Message:\n");
//            kVar.mo44359a(charSequence3.toString());
//            byte[] a2 = Base64.m11571a(charSequence2.toString(), 0);
//            CPubKey cPubKey = new CPubKey();
//            if (!cPubKey.mo9441b(kVar.mo44357a(), a2)) {
//                return false;
//            }
//            return cPubKey.mo211h().equals(a);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    //mo40819a
    public String getBase58PrivateKey(byte[] bArr, BLOCK_CHAIN_TYPE... igVarArr) throws com.vc.libcommon.exception.AddressFormatException {
        if (igVarArr.length == 0 || igVarArr[0] == null) {
            return AddressUtils.getBase58String(getWallet().getChainParams(), HeaderType.PRIVATE_KEY, bArr);
        }
        SubAddressInfo d = getSubAddressInfo(igVarArr[0]);
        if (d != null) {
            return AddressUtils.getBase58String(d.wallet.getChainParams(), HeaderType.PRIVATE_KEY, bArr);
        }
        throw new com.vc.libcommon.exception.AddressFormatException(String.format(Locale.getDefault(), "Can not export private key for %s, did you regist sub addr info?", new Object[]{igVarArr[0].name()}));
    }

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

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public CPubKey getPubKey() {
        return new CPubKey();
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public EncryptedPrivateKey getEncryptedPrivateKey() {
        return new BitcoinEncryptedPrivateKey();
    }
}
