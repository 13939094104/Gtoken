package zcash;

public class RustZCash {
    public static native void ask_to_ak(byte[] bArr, byte[] bArr2);

    public static native boolean check_diversifier(byte[] bArr);

    public static native void crh_ivk(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native boolean ivk_to_pkd(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native void merkle_hash(long j, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public static native void nsk_to_nk(byte[] bArr, byte[] bArr2);

    public static native void proving_ctx_free(long j);

    public static native long proving_ctx_init();

    public static native boolean sapling_binding_sig(long j, long j2, byte[] bArr, byte[] bArr2);

    public static native boolean sapling_check_output(long j, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native boolean sapling_check_spend(long j, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7);

    public static native boolean sapling_compute_cm(byte[] bArr, byte[] bArr2, long j, byte[] bArr3, byte[] bArr4);

    public static native boolean sapling_compute_nf(byte[] bArr, byte[] bArr2, long j, byte[] bArr3, byte[] bArr4, byte[] bArr5, long j2, byte[] bArr6);

    public static native boolean sapling_final_check(long j, long j2, byte[] bArr, byte[] bArr2);

    public static native void sapling_generate_r(byte[] bArr);

    public static native boolean sapling_spend_proof(long j, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, long j2, byte[] bArr6, byte[] bArr7, byte[] bArr8, byte[] bArr9, byte[] bArr10);

    public static native boolean sapling_spend_sig(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native void tree_uncommitted(byte[] bArr);

    public static native void verification_ctx_free(long j);

    public static native long verification_ctx_init();

    public static native boolean zip32_xfvk_address(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public static native void zip32_xsk_derive(byte[] bArr, long j, byte[] bArr2);

    public static native void zip32_xsk_master(byte[] bArr, long j, byte[] bArr2);
}
