package vdsMain.block;

import androidx.annotation.NonNull;
import bitcoin.UInt256;

import java.util.Vector;

//bjf
public class VBlockInfo extends BlockInfo {

    /* renamed from: c */
    protected Vector<UInt256> f11730c = new Vector<>();

    public VBlockInfo(@NonNull BlockHeader blockHeader) {
        super(blockHeader);
        if (blockHeader instanceof VBlockHeader) {
            VBlockHeader vBlockHeader = (VBlockHeader) blockHeader;
            initHashFinalSaplingRoot(vBlockHeader.hashFinalSaplingRoot);
            mo42503a(vBlockHeader.hashStateRoot);
            mo42504b(vBlockHeader.hashUTXORoot);
        }
    }

    /* renamed from: a */
    public void mo42503a(UInt256 uInt256) {
        ((VBlockHeader) this.mBlockHeader).mo42497c(uInt256);
    }

    /* renamed from: b */
    public void mo42504b(UInt256 uInt256) {
        ((VBlockHeader) this.mBlockHeader).mo42498d(uInt256);
    }

    //mo42505c
    public void initHashFinalSaplingRoot(UInt256 uInt256) {
        ((VBlockHeader) this.mBlockHeader).initHashFinalSaplingRoot(uInt256);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo41009a(BlockHeader blockHeader) {
        if (!(blockHeader instanceof VBlockHeader)) {
            StringBuilder sb = new StringBuilder();
            sb.append("BlockHeader type ");
            sb.append(blockHeader.getClass().getSimpleName());
            sb.append(" not VBlockHeader");
            throw new IllegalArgumentException(sb.toString());
        }
    }
}