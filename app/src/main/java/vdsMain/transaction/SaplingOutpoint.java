package vdsMain.transaction;

import bitcoin.UInt256;

//brk
public class SaplingOutpoint   extends COutPoint {
    public SaplingOutpoint() {
    }

    public SaplingOutpoint(UInt256 uInt256, int i) {
        super(uInt256, i);
    }
}