package vdsMain;

public enum HeaderType {
    UNKNOWN(0),
    PUBKEY(1),
    PUBKEY_P2SH(2),
    SAPLING_PAY_ADDR(3),
    SAPLING_FULL_VIEW_KEY(4),
    SAPLING_INCOMMING_VIEWING_KEY(5),
    PRIVATE_KEY(6),
    PRIVATE_KEY_ANONYMOUS(7),
    CONTRACT(8),
    WITNESS_V0_KEY_HASH(9),
    WITNESS_V0_SCRIPT_HASH(10),
    WITNESS_UNKNOWN(11);


    /* renamed from: m */
    private int f13112m;

    private HeaderType(int i) {
        this.f13112m = i;
    }
}
