package vdsMain;
import vdsMain.model.Address;
import androidx.annotation.NonNull;
import bitcoin.CKey;
import com.vc.libcommon.exception.AddressFormatException;
import generic.utils.AddressUtils;
import vdsMain.table.WalletTable;

import java.util.Locale;

public class WitnessKeyScriptAddress extends Address {
    /* renamed from: a */
    public boolean mo40821a(BLOCK_CHAIN_TYPE igVar) {
        return true;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public byte[] getSignMessageBytes(CharSequence message, CPrivateKeyInterface privateKey) {
        return null;
    }

    public WitnessKeyScriptAddress(@NonNull Address jjVar) {
        super(jjVar);
        setFlag(16384, true);
    }

    public WitnessKeyScriptAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(16384, true);
    }

    public WitnessKeyScriptAddress(@NonNull WalletTable kjVar, byte[] bArr, byte[] bArr2, CharSequence charSequence) {
        super(kjVar, bArr, bArr2, charSequence);
        setFlag(16384, true);
    }

    /* renamed from: d_ */
    public AddressType getAddressType() {
        return AddressType.WITNESS_V0_KEY_HASH_SCRIPT;
    }

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public CPubkeyInterface getPubKey() {
        return new WitnessKey0HashScriptPubkey();
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public EncryptedPrivateKey getEncryptedPrivateKey() {
        return new EncryptedWitnessKey0ScriptPrviKey();
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getPrivateKey() {
        CKey cKey = new CKey();
        cKey.mo9399a(true);
        WitnessScriptPrivKey afVar = new WitnessScriptPrivKey(cKey);
        cKey.clearBytes();
        return afVar;
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
}
