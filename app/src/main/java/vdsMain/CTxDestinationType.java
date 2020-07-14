package vdsMain;

public enum CTxDestinationType {
    UNKNOWN(-1),
    SCRIPTID(0),
    KEYID(1),
    CNODESTINATION(2),
    WITNESS_V0_SCRIPT_HASH(3),
    WITNESS_V0_KEY_HASH(4),
    WITNESS_UNKNOWN(5),
    ANONYMOUST_KEY(6),
    CONTRACT(99);


    //f13177j
    private int value;

    private CTxDestinationType(int i) {
        this.value = i;
    }

    //mo44389a
    public int getValue() {
        return this.value;
    }

    //m12759a
    public static CTxDestinationType getDesType(int i) {
        if (i == 99) {
            return CONTRACT;
        }
        switch (i) {
            case -1:
                return UNKNOWN;
            case 0:
                return SCRIPTID;
            case 1:
                return KEYID;
            case 2:
                return CNODESTINATION;
            case 3:
                return WITNESS_V0_SCRIPT_HASH;
            case 4:
                return WITNESS_V0_KEY_HASH;
            case 5:
                return WITNESS_UNKNOWN;
            case 6:
                return ANONYMOUST_KEY;
            default:
                return UNKNOWN;
        }
    }
}
