package vcash.script;

import bitcoin.script.CScript;
import vdsMain.transaction.SolveResult;
import vdsMain.transaction.TxnOutType;

import java.util.List;

public class VStandard {
    private static native int solver_native(byte[] bArr, List<byte[]> list, boolean z);

    //m4833a
    public static boolean setSloveResultTypeAndCheckIsNotTxNonStandard(CScript cScript, SolveResult solveResult, boolean... zArr) {
        solveResult.txnOutType = TxnOutType.Companion.getTxOutType(solver_native(cScript.copyToNewBytes(), solveResult.vSolutions, zArr.length == 0 ? false : zArr[0]));
        if (solveResult.txnOutType != TxnOutType.TX_NONSTANDARD) {
            return true;
        }
        return false;
    }
}