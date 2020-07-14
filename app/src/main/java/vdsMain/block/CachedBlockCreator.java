package vdsMain.block;

public class CachedBlockCreator {
    //mo42376a
    public CachedBlockInfo getNewCachedBlockInfo(BlockHeader blockHeader, boolean... zArr) {
        return new CachedBlockInfo(blockHeader, zArr);
    }
}