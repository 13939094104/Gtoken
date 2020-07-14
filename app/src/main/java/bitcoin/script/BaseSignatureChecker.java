package bitcoin.script;

import vdsMain.transaction.SigVersion;

public class BaseSignatureChecker {
    public boolean CheckLockTime(CScriptNum cScriptNum) {
        return false;
    }

    public boolean CheckSequence(CScriptNum cScriptNum) {
        return false;
    }

    public boolean CheckSig(byte[] bArr, byte[] bArr2, CScript cScript, SigVersion cxVar) {
        return false;
    }

    public final boolean CheckSig(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        return CheckSig(bArr, bArr2, new CScript(bArr3), SigVersion.m10834a(i));
    }

    public final boolean checkLockTime(long j) {
        return CheckLockTime(new CScriptNum(j));
    }

    public final boolean CheckSequence(long j) {
        return CheckSequence(new CScriptNum(j));
    }
}