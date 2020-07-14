package vcash.script;

import bitcoin.CPubKey;
import bitcoin.UInt256;
import bitcoin.script.BaseSignatureChecker;
import bitcoin.script.CScript;
import bitcoin.script.CScriptNum;
import com.vc.libcommon.exception.AddressFormatException;
import vdsMain.DataTypeToolkit;
import vdsMain.transaction.*;

public class VTransactionSignatureChecker extends BaseSignatureChecker {
    protected int nIn;
    protected VPrecomputedTransactionData txData;
    //txTo
    protected TransactionInteface vTransaction;

    /* access modifiers changed from: protected */
    public boolean VerifySignature(byte[] bArr, CPubKey cPubKey, UInt256 uInt256) {
        return cPubKey.verfiySignture(uInt256, bArr);
    }

    public VTransactionSignatureChecker(TransactionInteface transactionInteface, int i) {
        this.vTransaction = transactionInteface;
        this.nIn = 65535 & i;
    }

    public VTransactionSignatureChecker(TransactionInteface diVar, int i, VPrecomputedTransactionData txData) {
        this.vTransaction = diVar;
        this.nIn = 65535 & i;
        this.txData = txData;
    }

    public boolean CheckSig(byte[] bArr, byte[] bArr2, CScript cScript, SigVersion sigVersion) {
        try {
            CPubKey cPubKey = new CPubKey(bArr2);
            if (!cPubKey.isLengthGreaterZero() || bArr == null || bArr.length == 0) {
                return false;
            }
            short lastByte = (short) (bArr[bArr.length - 1] & 0xff);
            try {
                if (!VerifySignature(DataTypeToolkit.copyPartBytes(bArr, 0, bArr.length - 1), cPubKey, new VInterpreter().SignatureHash(cScript, (VTransactionInterface) this.vTransaction, (long) this.nIn, (int) lastByte, sigVersion, this.txData))) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (AddressFormatException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public boolean CheckLockTime(CScriptNum cScriptNum) {
        TransactionInteface diVar = this.vTransaction;
        if (((diVar.getLockTime() >= 500000000 || cScriptNum.mo9555a(500000000) >= 0) && (diVar.getLockTime() < 500000000 || cScriptNum.mo9555a(500000000) < 0)) || cScriptNum.mo9555a(diVar.getLockTime()) > 0 || ((TxIn) diVar.getSelfTxInList().get(this.nIn)).mo43305s()) {
            return false;
        }
        return true;
    }

    public boolean CheckSequence(CScriptNum cScriptNum) {
        TransactionInteface diVar = this.vTransaction;
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