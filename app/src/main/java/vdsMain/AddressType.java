package vdsMain;


public enum AddressType {
    UNKNOWN(0),
    GENERAL(1),
    MULTISIG(2),
    ANONYMOUS(3),
    IDENTITY(4),
    CONTRACT(5),
    WITNESS_V0_KEY_HASH(6),
    WITNESS_V0_SCRIPT_HASH(7),
    WITNESS_UNKNOWN(8),
    MONITORING_OFFLINE_ADDR(9),
    WITNESS_V0_KEY_HASH_SCRIPT(10),
    WITNESS_V0_SCRIPT_HASH_SCRIPT(11);


    /* renamed from: m */
    private int f13093m;

    private AddressType(int i) {
        this.f13093m = i;
    }

    /* renamed from: a */
    public int mo44257a() {
        return this.f13093m;
    }

    /* renamed from: a */
    public static AddressType m12565a(int i) {
        if (i == 17) {
            return MONITORING_OFFLINE_ADDR;
        }
        switch (i) {
            case 0:
                return UNKNOWN;
            case 1:
                return GENERAL;
            case 2:
                return MULTISIG;
            case 3:
                return ANONYMOUS;
            case 4:
                return IDENTITY;
            case 5:
                return CONTRACT;
            case 6:
                return WITNESS_V0_KEY_HASH;
            case 7:
                return WITNESS_V0_SCRIPT_HASH;
            case 8:
                return WITNESS_UNKNOWN;
            default:
                switch (i) {
                    case 10:
                        return WITNESS_V0_KEY_HASH_SCRIPT;
                    case 11:
                        return WITNESS_V0_SCRIPT_HASH_SCRIPT;
                    default:
                        return UNKNOWN;
                }
        }
    }
}
