package vdsMain.block;

//bio
public class VCachedBlockCreator extends CachedBlockCreator {
    /* renamed from: a */
    public CachedBlockInfo getNewCachedBlockInfo(BlockHeader blockInfo, boolean... zArr) {
        return new VCachedBlockInfo(blockInfo, zArr);
    }
}