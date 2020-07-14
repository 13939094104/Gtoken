package vdsMain.transaction;

import bitcoin.UInt256;

//bpo
public class VCOutPoint extends COutPoint {
    public VCOutPoint() {
    }

    public VCOutPoint(UInt256 uInt256, int i) {
        super(uInt256, i);
    }
}