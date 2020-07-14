package vdsMain;
import com.vc.libcommon.exception.AddressFormatException;
import vdsMain.model.Address;
import androidx.annotation.NonNull;
import bitcoin.CKey;
import generic.utils.AddressUtils;
import vdsMain.model.BitCoinAddress;
import vdsMain.table.WalletTable;

import java.util.Locale;

public class WitnessKeyHashAddress extends Address {
    /* renamed from: a */
    public boolean mo40821a(BLOCK_CHAIN_TYPE igVar) {
        return true;
    }

    public WitnessKeyHashAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(2048, true);
    }

    public WitnessKeyHashAddress(@NonNull WalletTable kjVar, @NonNull CTxDestination oVar) {
        super(kjVar, oVar);
        setFlag(2048, true);
    }

    public WitnessKeyHashAddress(@NonNull WalletTable kjVar, byte[] bArr, byte[] bArr2, CharSequence charSequence) {
        super(kjVar, bArr, bArr2, charSequence);
        setFlag(2048, true);
    }

    public WitnessKeyHashAddress(Address jjVar) throws AddressFormatException {
        super(jjVar);
        setFlag(1, false);
        setFlag(2048, true);
        if (!(jjVar instanceof BitCoinAddress) && !(jjVar instanceof WitnessKeyHashAddress)) {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Can not convert %s to %s", new Object[]{jjVar.getClass().getName(), getClass().getName()}));
        }
    }

    /* renamed from: f_ */
    public boolean mo40923f_() {
        return this.flag == 2048;
    }

    /* renamed from: d_ */
    public AddressType getAddressType() {
        return AddressType.WITNESS_V0_KEY_HASH;
    }

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public CPubkeyInterface getPubKey() {
        return new CWitnessKey0HashPubkey();
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public EncryptedPrivateKey getEncryptedPrivateKey() {
        return new EncryptedWitnessKey0PrivKey();
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getPrivateKey() {
        CKey cKey = new CKey();
        cKey.mo9399a(true);
        CWitnessPrivKey sVar = new CWitnessPrivKey(cKey);
        cKey.clearBytes();
        return sVar;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void encryptPrivateKeyByPwd(byte[] bArr, CharSequence charSequence) {
        CKey b = CKey.getCKeyFromBytes(bArr);
        this.mPubKey = b.getPubKey();
        this.wallet.encryptPrivateKeyByPwd(b.getPrivateKeyBytes(), this.mPrivateKey, charSequence.toString());
        b.clearBytes();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public String getBase58PrivateKey(byte[] bArr, BLOCK_CHAIN_TYPE... igVarArr) throws AddressFormatException {
        if (igVarArr.length == 0 || igVarArr[0] == null) {
            return AddressUtils.getBase58String(getWallet().getChainParams(), HeaderType.PRIVATE_KEY, bArr);
        }
        SubAddressInfo subAddressInfo = getSubAddressInfo(igVarArr[0]);
        if (subAddressInfo != null) {
            return AddressUtils.getBase58String(subAddressInfo.wallet.getChainParams(), HeaderType.PRIVATE_KEY, bArr);
        }
        throw new AddressFormatException(String.format(Locale.getDefault(), "Can not export private key for %s, did you regist sub addr info?", new Object[]{igVarArr[0].name()}));
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public byte[] getSignMessageBytes(CharSequence message, CPrivateKeyInterface privateKey) {
        throw new UnsupportedOperationException("Not implement yet.");
    }
}
