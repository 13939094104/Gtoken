package vdsMain;

/* renamed from: iv */
public enum PubkeyType {
    UNKNOWN(0),
    PUBKEY(1),
    MULTISIG(3),
    ANONYMOUS(4),
    CONTRACT(7),
    WITNESS_V0_KEY_HASH(8),
    WITNESS_SCRIPT_HASH(9),
    WITNESS_V0_KEY_HASH_SCRIPT(10),
    WITNESS_SCRIPT_HASH_SCRIPT(11);
    

    /* renamed from: j */
    private int f12991j;

    private PubkeyType(int i) {
        this.f12991j = i;
    }

    /* renamed from: a */
    public int mo44014a() {
        return this.f12991j;
    }

    /* renamed from: a */
    public static PubkeyType m12199a(int i) {
        switch (i) {
            case 0:
                return UNKNOWN;
            case 1:
                return PUBKEY;
            case 3:
                return MULTISIG;
            case 7:
                return CONTRACT;
            case 8:
                return WITNESS_V0_KEY_HASH;
            case 9:
                return WITNESS_SCRIPT_HASH;
            case 10:
                return WITNESS_V0_KEY_HASH_SCRIPT;
            case 11:
                return WITNESS_SCRIPT_HASH_SCRIPT;
            default:
                return UNKNOWN;
        }
    }
}
