package zcash.crypto;

import sodium.crypto.generichash.Black2BState;

public class EquiHash {
    private static native void EhInitialiseState(int i, int i2, byte[] bArr);

    private static native boolean EhIsValidSolution(int i, int i2, byte[] bArr, byte[] bArr2);

    /* renamed from: a */
    public static void m4842a(int i, int i2, Black2BState black2BState) {
        EhInitialiseState(i, i2, black2BState.mo38396a());
    }

    /* renamed from: a */
    public static boolean m4843a(int i, int i2, Black2BState black2BState, byte[] bArr) {
        return EhIsValidSolution(i, i2, black2BState.mo38396a(), bArr);
    }
}
