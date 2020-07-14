package vdsMain;

/* renamed from: iu */
public enum PrivateKeyType {
    UNKNOWN(0),
    BITCOIN(0),
    SAPLING_EXPANDED_SPEND_KEY(1),
    SAPLING_EXTENED_SPEND_KEY(2),
    WITNESS_V0_KEY_HASH(3),
    WITNESS_V0_KEY_HASH_SCRIPT(4);
    

    /* renamed from: g */
    private int f12980g;

    private PrivateKeyType(int i) {
        this.f12980g = i;
    }
}
