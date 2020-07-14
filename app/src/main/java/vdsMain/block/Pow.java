package vdsMain.block;

import bitcoin.UInt256;
import bitcoin.consensus.ArithUint256;
import sodium.crypto.generichash.Black2BState;
import vdsMain.CEquihashInput;
import vdsMain.Log;
import vdsMain.UnsafeByteArrayOutputStream;
import vdsMain.VParams;
import vdsMain.wallet.ChainParams;
import zcash.crypto.EquiHash;

import java.io.OutputStream;

//bim
public class Pow {
    /* renamed from: a */
    public static boolean m9541a(UInt256 uInt256, long j, VParams bjh) {
        ArithUint256 arithUint256 = new ArithUint256();
        boolean[] a = arithUint256.mo9498a(j);
        if (a[0] || arithUint256.equals(Integer.valueOf(0)) || a[1] || arithUint256.mo9511f(ArithUint256.m463a(bjh.f8386g)) || ArithUint256.m463a(uInt256).mo9511f(arithUint256)) {
            return false;
        }
        return true;
    }

    /* renamed from: a */
    public static boolean m9542a(VBlockHeader vBlockHeader, ChainParams uVar) {
        int o = uVar.mo43962o();
        int p = uVar.mo43963p();
        Black2BState black2BState = new Black2BState();
        EquiHash.m4842a(o, p, black2BState);
        CEquihashInput bpm = new CEquihashInput(vBlockHeader);
        try {
            UnsafeByteArrayOutputStream abp = new UnsafeByteArrayOutputStream();
            bpm.mo44656a((OutputStream) abp);
            UInt256.serialUInt256(bpm.mo42499p(), (OutputStream) abp);
            try {
                black2BState.mo38395a(abp.toByteArray());
                if (EquiHash.m4843a(o, p, black2BState, vBlockHeader.mo42500q())) {
                    return true;
                }
                Log.LogErrorNoThrow("Pow", "CheckEquihashSolution(): invalid solution");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
}