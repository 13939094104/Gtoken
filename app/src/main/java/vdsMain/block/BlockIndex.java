package vdsMain.block;

import bitcoin.BaseBlob;
import bitcoin.UInt256;

public class BlockIndex {

    //f12934a
    public UInt256 blockHash;

    //f12935b
    public UInt256 preBlockHash;

    //f12936c
    public int blockNo;

    //f12937d
    public BlockSyncStatus blockSyncStatus;

    public BlockIndex() {
        this.blockHash = new UInt256();
        this.preBlockHash = new UInt256();
        this.blockNo = 0;
        this.blockSyncStatus = BlockSyncStatus.UNSYNC;
    }

    public BlockIndex(BlockHeader jtVar) {
        this();
        initDataFromBlockHeader(jtVar);
    }

    //mo43969a
    public void initDataFromBlockHeader(BlockHeader blockHeader) {
        this.blockHash.set((BaseBlob) blockHeader.getBlockHash());
        this.preBlockHash.set((BaseBlob) blockHeader.getPreBlockHash());
        this.blockNo = blockHeader.getBlockNo();
        this.blockSyncStatus = blockHeader.getBlockSyncStatus();
    }

    //mo43968a
    public void clear() {
        this.blockHash.setNull();
        this.preBlockHash.setNull();
        this.blockNo = 0;
        this.blockSyncStatus = BlockSyncStatus.UNSYNC;
    }
}
