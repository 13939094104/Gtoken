package vdsMain;

import androidx.annotation.NonNull;
import bitcoin.CPubKey;
import vdsMain.table.WalletTable;
import vdsMain.model.Address;
import vdsMain.wallet.Wallet;

import java.util.HashMap;

public class WitnessScriptHashScriptAddress extends BitcoinMultiSigAddress {
    public WitnessScriptHashScriptAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(32768, true);
    }

    public WitnessScriptHashScriptAddress(@NonNull Address jjVar) {
        super(jjVar);
        setFlag(32768, true);
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

    /* renamed from: d_ */
    public AddressType mo40825d_() {
        return AddressType.WITNESS_V0_SCRIPT_HASH_SCRIPT;
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public CPubKey mo40827f() {
        return new WitnessScriptHashScriptPubkey();
    }
}
