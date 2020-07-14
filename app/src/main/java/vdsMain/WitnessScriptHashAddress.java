package vdsMain;

import androidx.annotation.NonNull;
import bitcoin.CPubKey;
import com.vc.libcommon.exception.AddressFormatException;
import vdsMain.table.WalletTable;

import java.util.HashMap;
import java.util.List;
import vdsMain.model.Address;
import vdsMain.wallet.Wallet;

public class WitnessScriptHashAddress extends BitcoinMultiSigAddress {
    public WitnessScriptHashAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(5632, true);
    }

    public WitnessScriptHashAddress(@NonNull WalletTable kjVar, @NonNull CTxDestination oVar) {
        super(kjVar, oVar);
        setFlag(5632, true);
    }

    public WitnessScriptHashAddress(@NonNull Address jjVar) {
        super(jjVar);
        setFlag(5632, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo40876a(boolean... zArr) {
        Wallet ae = getWallet();
        if (this.chainTypeToBalanceInfoMap == null) {
            this.chainTypeToBalanceInfoMap = new HashMap();
            SubAddressInfo jpVar = new SubAddressInfo(ae);
            this.chainTypeToBalanceInfoMap.put(jpVar.getBlockChainType(), jpVar);
        }
    }

    /* renamed from: b */
    public static WitnessScriptHashAddress m6175b(@NonNull WalletTable kjVar, int i, List<byte[]> list) throws AddressFormatException {
        WitnessScriptHashAddress apVar = new WitnessScriptHashAddress(kjVar);
        apVar.mo40874a(i, list);
        return apVar;
    }

    /* renamed from: d_ */
    public AddressType mo40825d_() {
        return AddressType.WITNESS_V0_SCRIPT_HASH;
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public CPubKey mo40827f() {
        return new WitnessScriptHashPubKey();
    }
}