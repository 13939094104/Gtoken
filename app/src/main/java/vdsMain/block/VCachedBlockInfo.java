package vdsMain.block;

import androidx.annotation.NonNull;
import vdsMain.transaction.SaplingMerkleTree;

//bip
public class VCachedBlockInfo extends CachedBlockInfo {

    //f11663e
    public SaplingMerkleTree mSaplingMerkleTree;

    public VCachedBlockInfo(@NonNull BlockHeader jtVar, boolean... zArr) {
        super(jtVar, zArr);
    }
}
