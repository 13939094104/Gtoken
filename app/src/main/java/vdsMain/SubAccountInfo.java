package vdsMain;

public class SubAccountInfo {

    //f13043a
    public BLOCK_CHAIN_TYPE blockChainType;

    //f13044b
    /* renamed from: b */
    public long balance;

    public SubAccountInfo() {
    }

    public SubAccountInfo(BLOCK_CHAIN_TYPE blockChainType, long balance) {
        this.blockChainType = blockChainType;
        this.balance = balance;
    }
}