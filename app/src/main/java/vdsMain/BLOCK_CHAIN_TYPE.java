package vdsMain;

public enum BLOCK_CHAIN_TYPE {
    BITCOIN(0),
    VCASH(1),
    ETH(2),
    UNKNOWN(-1);


    //f12887d
    private int chainType;

    private BLOCK_CHAIN_TYPE(int i) {
        this.chainType = i;
    }

    //mo43956a
    public int getChainType() {
        return this.chainType;
    }

    //m12139a
    public static BLOCK_CHAIN_TYPE getChainType(int i) {
        switch (i) {
            case 0:
                return BITCOIN;
            case 1:
                return VCASH;
            default:
                return UNKNOWN;
        }
    }
}