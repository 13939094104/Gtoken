package bitcoin.script;

import bitcoin.CPubKey;
import vdsMain.DataTypeToolkit;
import vdsMain.transaction.ScriptChunk;
import com.vc.libcommon.util.Integer;
import vdsMain.transaction.SolveResult;
import vdsMain.transaction.TxnOutType;

import java.util.List;

public class Standard {

    private static native int Solver(byte[] bArr, List<byte[]> list);

    /* renamed from: a */
    public static boolean m532a(int i) {
        int i2 = i & 255;
        return i2 >= 81 && i2 <= 96;
    }

    //m534a
    public static boolean setResultTxOutType(CScript cScript, SolveResult solveResult) {
        int Solver = Solver(cScript.copyToNewBytes(), solveResult.vSolutions);
        solveResult.txnOutType = TxnOutType.Companion.getTxOutType(Solver);
        return Solver != TxnOutType.TX_NONSTANDARD.getValue();
    }

    public static boolean m533a(CScript cScript, Integer integer, List<byte[]> list) {
        ScriptChunk scriptChunk = new ScriptChunk();
        CScript.ScriptByteIterator d = cScript.getNewScriptByteIterator();
        if (cScript.getArrayBufferWritePos() < 1 || !DataTypeToolkit.m11505b((int) cScript.mo9519a(), 174) || !cScript.GetOp(d, scriptChunk, true) || !m532a(scriptChunk.opWord)) {
            return false;
        }
        integer.set(CScript.checkIsOP_N((byte) scriptChunk.opWord));
        while (cScript.GetOp(d, scriptChunk, true) && CPubKey.m418d(scriptChunk.bytes)) {
            list.add(scriptChunk.bytes);
        }
        if (!m532a(scriptChunk.opWord)) {
            return false;
        }
        int a = CScript.checkIsOP_N((byte) scriptChunk.opWord);
        return list.size() == a && a >= integer.getValue();
    }
}
