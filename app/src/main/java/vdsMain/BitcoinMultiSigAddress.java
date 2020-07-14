package vdsMain;

import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.CPubKey;
import bitcoin.script.CScript;
import com.vc.libcommon.exception.AddressFormatException;
import generic.utils.AddressUtils;
import net.bither.bitherj.exception.ScriptException;
import org.apache.commons.io.IOUtils;
import vdsMain.model.BitCoinAddress;
import vdsMain.table.WalletTable;
import vdsMain.model.Address;
import vdsMain.transaction.Script;
import vdsMain.transaction.TxnOutType;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BitcoinMultiSigAddress extends BitCoinAddress {

    /* renamed from: a */
    protected BytesArrayMapList f6396a = new BytesArrayMapList();

    /* renamed from: b */
    protected int f6397b = 0;

    public BitcoinMultiSigAddress(@NonNull WalletTable kjVar) {
        super(kjVar);
        setFlag(4, true);
    }

    public BitcoinMultiSigAddress(@NonNull WalletTable kjVar, @NonNull CTxDestination oVar) {
        super(kjVar, oVar);
        setFlag(4, true);
    }

    public BitcoinMultiSigAddress(@NonNull Address jjVar) {
        super(jjVar);
        if (jjVar instanceof BitcoinMultiSigAddress) {
            BitcoinMultiSigAddress alVar = (BitcoinMultiSigAddress) jjVar;
            this.f6397b = alVar.f6397b;
            this.f6396a.mo43669a(alVar.f6396a);
        }
    }

    /* renamed from: g */
    public void mo40877g() throws AddressFormatException {
        this.mPubKey.Set(mo40879i().copyToNewBytes());
        initDestFromPubKey();
        mo40876a(true);
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
        super.initTableItemVariable(cursor, i, i2, i3);
        if (i3 == 8) {
            try {
                m5751af();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: d_ */
    public AddressType mo40825d_() {
        return AddressType.MULTISIG;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo40876a(boolean... zArr) {
        Wallet wallet = getWallet();
        if (this.chainTypeToBalanceInfoMap == null) {
            this.chainTypeToBalanceInfoMap = new HashMap();
            SubAddressInfo subAddressInfo = new SubAddressInfo(wallet);
            this.chainTypeToBalanceInfoMap.put(subAddressInfo.getBlockChainType(), subAddressInfo);
        }
    }

    /* renamed from: af */
    private void m5751af() throws AddressFormatException, ScriptException {
        Script crVar = new Script(this.mPubKey.getByteArr());
        if (crVar.getTxnOutType() == TxnOutType.TX_MULTISIG) {
            this.f6396a.mo43673c();
            for (CPubKey m : crVar.mo43168i()) {
                this.f6396a.mo43671a(m.getByteArr());
            }
            this.f6397b = crVar.mo43174o();
            mo40876a(true);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Pubkey is not a MuiltisigScript lock script: ");
        sb.append(this.mPubKey.toString());
        throw new AddressFormatException(sb.toString());
    }

    /* renamed from: a */
    public void mo40875a(byte[] bArr) throws AddressFormatException {
        super.mo40875a(bArr);
        try {
            m5751af();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: a */
    public void mo40874a(int i, List<byte[]> list) throws AddressFormatException {
        if (i <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Threshold ");
            sb.append(i);
            sb.append(" must bigger than 0.");
            throw new AddressFormatException(sb.toString());
        } else if (list == null || list.isEmpty()) {
            throw new AddressFormatException("Pubkeys is empty");
        } else if (i <= list.size()) {
            this.f6397b = i;
            this.f6396a.mo43673c();
            this.f6396a.mo43670a(list);
            mo40877g();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Threshold ");
            sb2.append(i);
            sb2.append("must smaller than pubkey size ");
            sb2.append(list.size());
            throw new AddressFormatException(sb2.toString());
        }
    }

    /* renamed from: h */
    public int mo40878h() {
        return this.f6397b;
    }

    /* renamed from: i */
    public CScript mo40879i() {
        return CScript.m482a(this.f6397b, this.f6396a.mo43668a());
    }

    /* renamed from: a */
    public static BitcoinMultiSigAddress m5749a(@NonNull WalletTable kjVar, int i, List<byte[]> list) throws AddressFormatException {
        BitcoinMultiSigAddress alVar = new BitcoinMultiSigAddress(kjVar);
        alVar.mo40874a(i, list);
        return alVar;
    }

    //915 mo41005a
    public List<String> mo40873a(BLOCK_CHAIN_TYPE... igVarArr) throws AddressFormatException {
        List<byte[]> a = this.f6396a.mo43668a();
        ArrayList arrayList = new ArrayList();
        SubAddressInfo d = getSubAddressInfo(igVarArr);
        if (d == null) {
            return null;
        }
        ChainParams J = d.wallet.getChainParams();
        for (byte[] cPubKey : a) {
            arrayList.add(AddressUtils.m929a((CPubkeyInterface) new CPubKey(cPubKey), J));
        }
        return arrayList;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(super.toString());
        if (this.f6397b > 0) {
            BytesArrayMapList goVar = this.f6396a;
            if (goVar != null && goVar.mo43672b() >= this.f6397b) {
                stringBuffer.append("\nredeemScript: ");
                stringBuffer.append(StringToolkit.bytesToString(mo40879i().copyToNewBytes()));
            }
        }
        stringBuffer.append("{\n address: [");
        stringBuffer.append(getChainToAddressString());
        stringBuffer.append("\n]");
        stringBuffer.append("\naddresses: \n[\n");
        for (BLOCK_CHAIN_TYPE a : getBlockChainTypeArr()) {
            m5750a(stringBuffer, a);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /* renamed from: a */
    private void m5750a(StringBuffer stringBuffer, BLOCK_CHAIN_TYPE igVar) {
        stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
        stringBuffer.append(igVar.name());
        stringBuffer.append("\n[");
        try {
            List<String> a = mo40873a(igVar);
            if (a != null) {
                for (String append : a) {
                    stringBuffer.append(append);
                    stringBuffer.append(IOUtils.LINE_SEPARATOR_UNIX);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stringBuffer.append("\n]");
    }

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public CPubKey mo40827f() {
        return new CMultisigPubkey();
    }
}
