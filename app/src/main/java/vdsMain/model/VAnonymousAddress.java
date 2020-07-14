package vdsMain.model;

import androidx.annotation.NonNull;
import com.vc.libcommon.exception.AddressFormatException;
import generic.utils.AddressUtils;
import vdsMain.*;
import vdsMain.table.AddressTable;
import vdsMain.table.WalletTable;

import java.util.Locale;

//bjb
public class VAnonymousAddress extends Address {
    public VAnonymousAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(2, true);
    }

    public VAnonymousAddress(@NonNull WalletTable kjVar, @NonNull CTxDestination oVar) {
        super(kjVar, oVar);
        setFlag(2, true);
    }

    public VAnonymousAddress(@NonNull WalletTable kjVar, byte[] bArr, int i, CharSequence charSequence) throws AddressFormatException {
        super(kjVar, bArr, i, charSequence);
    }

    public VAnonymousAddress(@NonNull AddressTable addressTable, SaplingExtendedSpendingKey brw, CharSequence charSequence) {
        super((WalletTable) addressTable);
        setFlag(2, true);
        this.mPubKey = brw.getPubKey();
        this.mPrivateKey = brw.mo42978a(this.wallet, charSequence.toString());
        initDestFromPubKey();
        addSubAddressInfoFromWallet();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void encryptPrivateKeyByPwd(byte[] bArr, CharSequence charSequence) throws AddressFormatException {
        HDSeed hdSeed = null;
        if (bArr.length == 32) {
            if (bArr == null || DataTypeToolkit.isZeroBytes(bArr)) {
                hdSeed = hdSeed.m10442a(32);
            } else {
                hdSeed = new HDSeed(bArr, new boolean[0]);
            }
            SaplingExtendedSpendingKey a = SaplingExtendedSpendingKey.m10459a(hdSeed, this.wallet);
            this.mPubKey.initFromOtherPubKey(a.getPubKey());
            this.wallet.encryptPrivateKeyByPwd(a.getCopyBytes(), this.mPrivateKey, charSequence.toString());
            a.clearBytes();
            return;
        }
        throw new AddressFormatException("Seed length must be 32");
    }

    /* access modifiers changed from: protected */
    /* renamed from: f */
    public CPubkeyInterface getPubKey() {
        return new SaplingPubKey();
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public CPrivateKeyInterface getPrivateKey() {
        try {
            return SaplingExtendedSpendingKey.m10459a(HDSeed.m10442a(32), this.wallet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* renamed from: d_ */
    public AddressType getAddressType() {
        return AddressType.ANONYMOUS;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public byte[] getSignMessageBytes(CharSequence message, CPrivateKeyInterface privateKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("ZCash address: ");
        sb.append(getAddressString(new BLOCK_CHAIN_TYPE[0]));
        sb.append(" was can not sign message, please use a bitcoin address!");
        throw new UnsupportedOperationException(sb.toString());
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public String getBase58PrivateKey(byte[] bArr, BLOCK_CHAIN_TYPE... igVarArr) throws AddressFormatException {
        if (igVarArr.length == 0 || igVarArr[0] == null) {
            return AddressUtils.getBase58String(getWallet().getChainParams(), HeaderType.PRIVATE_KEY_ANONYMOUS, bArr);
        }
        SubAddressInfo d = getSubAddressInfo(igVarArr[0]);
        if (d == null) {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Can not export private key for %s, did you regist sub addr info?", new Object[]{igVarArr[0].name()}));
        } else if (d.getBlockChainType() == BLOCK_CHAIN_TYPE.VCASH) {
            return AddressUtils.getBase58String(d.wallet.getChainParams(), HeaderType.PRIVATE_KEY_ANONYMOUS, bArr);
        } else {
            throw new AddressFormatException(String.format(Locale.getDefault(), "Can not export private key for %s, make sure chain param was zcash chain param type!", new Object[]{igVarArr[0].name()}));
        }
    }

    /* renamed from: a */
    public boolean mo40821a(BLOCK_CHAIN_TYPE igVar) {
        return igVar != BLOCK_CHAIN_TYPE.BITCOIN;
    }

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public EncryptedPrivateKey getEncryptedPrivateKey() {
        return new SaplingEncryptedKey();
    }
}

