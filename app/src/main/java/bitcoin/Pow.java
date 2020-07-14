package bitcoin;

import bitcoin.consensus.ArithUint256;
import vdsMain.block.BlockHeader;

public class Pow {
    private static native void GetBlockProofNative(int[] iArr, long j);

    /* renamed from: a */
    public static ArithUint256 m447a(BlockHeader jtVar) {
        ArithUint256 arithUint256 = new ArithUint256();
        GetBlockProofNative(arithUint256.mo9508d(), jtVar.getBits());
        return arithUint256;
    }
}