package bitcoin.script;

import bitcoin.CPubKey;
import bitcoin.UInt256;
import com.vc.libcommon.exception.AddressFormatException;
import vdsMain.DataTypeToolkit;
import vdsMain.transaction.PrecomputedTransactionData;
import vdsMain.transaction.SigVersion;
import vdsMain.transaction.TransactionInteface;
import vdsMain.transaction.TxIn;

import java.io.IOException;

public class GenericTransactionSignatureChecker<E> extends BaseSignatureChecker {
    protected long amount;
    protected int nIn;
    protected E txTo;
    protected PrecomputedTransactionData txdata;

    public GenericTransactionSignatureChecker(E e, int i, long j) {
        this.txTo = e;
        this.nIn = i;
        this.amount = j;
        this.txdata = null;
    }

    public GenericTransactionSignatureChecker(E e, int i, long j, PrecomputedTransactionData cqVar) {
        this.txTo = e;
        this.nIn = i;
        this.amount = j;
        this.txdata = cqVar;
    }

    /* access modifiers changed from: protected */
    public boolean VerifySignature(byte[] bArr, CPubKey cPubKey, UInt256 uInt256) {
        return cPubKey.verfiySignture(uInt256, bArr);
    }

    public boolean CheckSig(byte[] bArr, byte[] bArr2, CScript cScript, SigVersion cxVar) {
        byte[] bArr3 = bArr;
        try {
            CPubKey cPubKey = new CPubKey(bArr2);
            if (!cPubKey.isLengthGreaterZero() || bArr3 == null || bArr3.length == 0) {
                return false;
            }
            byte b = (byte) (bArr3[bArr3.length - 1] & 0xff);
            try {
                if (!VerifySignature(DataTypeToolkit.copyPartBytes(bArr3, 0, bArr3.length - 1), cPubKey, new Interpreter().mo9560a((TransactionInteface) this.txTo, cScript, this.nIn, b, this.amount, cxVar, this.txdata))) {
                    return false;
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (AddressFormatException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public boolean CheckLockTime(CScriptNum cScriptNum) {
        TransactionInteface diVar = (TransactionInteface) this.txTo;
        if (((diVar.getLockTime() >= 500000000 || cScriptNum.mo9555a(500000000) >= 0) && (diVar.getLockTime() < 500000000 || cScriptNum.mo9555a(500000000) < 0)) || cScriptNum.mo9555a(diVar.getLockTime()) > 0 || ((TxIn) diVar.getSelfTxInList().get(this.nIn)).mo43305s()) {
            return false;
        }
        return true;
    }

    public boolean CheckSequence(CScriptNum cScriptNum) {
        TransactionInteface diVar = (TransactionInteface) this.txTo;
        long n = ((TxIn) diVar.getSelfTxInList().get(this.nIn)).getSequence();
        if (diVar.getVersion() < 2 || (-2147483648L & n) != 0) {
            return false;
        }
        long j = n & 4259839;
        long a = (long) cScriptNum.mo9556b(4259839).mo9554a();
        int i = (j > 4194304 ? 1 : (j == 4194304 ? 0 : -1));
        return ((i < 0 && a < 4194304) || (i >= 0 && a >= 4194304)) && a <= j;
    }
}
