package vdsMain.block;

import androidx.annotation.NonNull;
import bitcoin.BaseBlob;
import bitcoin.UInt256;
import bitcoin.consensus.ArithUint256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vcash.crypt.Script;
import vdsMain.WalletSerializedAbstractTableItem;
import vdsMain.table.AbstractTable;
import vdsMain.transaction.CHashWriter;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.Arrays;

public abstract class BlockHeader extends WalletSerializedAbstractTableItem {

    //f13132a
    //910 f13270a
    protected int blockNo;

    //f13133b
    protected long blockVersion;

    //f13134c
    //910 f13272c
    protected UInt256 blockHash;

    //f13135d
    //910 f13273d
    protected UInt256 preBlockHash;

    //f13136e
    protected UInt256 merkelRootHash;

    //f13137f
    //910 f13275f
    protected ArithUint256 nChainWork;

    //f13138g
    protected long bits;

    //f13139h
    protected long blockTime;

    //f13140i
    protected BlockSyncStatus blockSyncStatus;

    /* renamed from: j */
    protected boolean f13141j;

    /* renamed from: a */
    public String getKey() {
        return "hash";
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) throws IOException {
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {

    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract void mo40990a(StringBuffer stringBuffer);

    /* renamed from: a */
    public abstract boolean checkBlockVaild(CValidationState atVar, boolean z);

    public BlockHeader(@NonNull Wallet izVar, AbstractTable fxVar) {
        super(izVar, fxVar);
        this.blockNo = -1;
        this.blockVersion = 0;
        this.nChainWork = new ArithUint256();
        this.bits = 0;
        this.blockTime = 0;
        this.blockSyncStatus = BlockSyncStatus.UNSYNC;
        this.f13141j = false;
        this.blockHash = new UInt256();
        this.preBlockHash = new UInt256();
        this.merkelRootHash = new UInt256();
    }

    public BlockHeader(@NonNull Wallet izVar) {
        super(izVar);
        this.blockNo = -1;
        this.blockVersion = 0;
        this.nChainWork = new ArithUint256();
        this.bits = 0;
        this.blockTime = 0;
        this.blockSyncStatus = BlockSyncStatus.UNSYNC;
        this.f13141j = false;
        this.blockHash = new UInt256();
        this.preBlockHash = new UInt256();
        this.merkelRootHash = new UInt256();
    }

    public BlockHeader(@NonNull BlockHeader blockHeader) {
        super((WalletSerializedAbstractTableItem) blockHeader);
        this.blockNo = -1;
        this.blockVersion = 0;
        this.nChainWork = new ArithUint256();
        this.bits = 0;
        this.blockTime = 0;
        this.blockSyncStatus = BlockSyncStatus.UNSYNC;
        this.f13141j = false;
        initFromBlockHeader(blockHeader);
    }

    public BlockHeader(@NonNull Wallet izVar, int i, long j, @NonNull UInt256 uInt256, @NonNull UInt256 uInt2562, @NonNull UInt256 uInt2563, long j2) {
        super(izVar);
        this.blockNo = -1;
        this.blockVersion = 0;
        this.nChainWork = new ArithUint256();
        this.bits = 0;
        this.blockTime = 0;
        this.blockSyncStatus = BlockSyncStatus.UNSYNC;
        this.f13141j = false;
        this.blockNo = i;
        this.blockVersion = j;
        this.blockHash = uInt256;
        this.preBlockHash = uInt2562;
        this.merkelRootHash = uInt2563;
        this.bits = j2;
        this.blockTime = System.currentTimeMillis();
    }

    ///mo44289a
    public void initFromBlockHeader(BlockHeader blockHeader) {
        this.blockNo = blockHeader.blockNo;
        this.blockVersion = blockHeader.blockVersion;
        this.blockHash = blockHeader.blockHash;
        this.preBlockHash = blockHeader.preBlockHash;
        this.merkelRootHash = blockHeader.merkelRootHash;
        this.bits = blockHeader.bits;
        this.blockTime = blockHeader.blockTime;
        this.blockSyncStatus = blockHeader.blockSyncStatus;
        this.f13141j = blockHeader.f13141j;
        this.nChainWork.copySetNChainWork(blockHeader.nChainWork);
    }

    //mo44292d
    //910 mo44367d
    public int getBlockNo() {
        return this.blockNo;
    }

    //mo44285a
    //910 mo44360a
    public synchronized void setBlockNo(int i) {
        this.blockNo = i;
    }

    //mo44293e
    //910 mo44368e
    public UInt256 getBlockHash() {
        return this.blockHash;
    }

    /* renamed from: f */
    public UInt256 mo44295f() {
        try {
            byte[] X = serialSelfToBytes();
            UInt256 uInt256 = new UInt256();
            Script.scrypt_1024_1_1_256(X, uInt256.data());
            return uInt256;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //mo44296g
    //910 mo44371g
    public UInt256 getPreBlockHash() {
        return this.preBlockHash;
    }

    //mo44286a
    public synchronized void setPreBlockHash(@NonNull UInt256 uInt256) {
        this.preBlockHash = uInt256;
    }

    //mo44297h
    public UInt256 getMerkelRootHash() {
        return this.merkelRootHash;
    }

    //mo44298i
    public long getBits() {
        return this.bits;
    }

    /* renamed from: j */
    public long mo44299j() {
        return this.blockTime;
    }

    /* renamed from: b */
    public synchronized void mo44291b(long j) {
        this.blockTime = j;
    }

    /* renamed from: e_ */
    public String getValue() {
        return this.blockHash.hashString();
    }

    /* renamed from: a */
    public void mo44290a(boolean z) {
        this.f13141j = z;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("\nblockNo: ");
        sb.append(this.blockNo);
        stringBuffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("\nblockVersion: ");
        sb2.append(this.blockVersion);
        stringBuffer.append(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("\nblockHash: ");
        sb3.append(this.blockHash.hashString());
        stringBuffer.append(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("\nprevBlockHash: ");
        sb4.append(this.preBlockHash.hashString());
        stringBuffer.append(sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append("\nmerkelRootHash: ");
        sb5.append(this.merkelRootHash.hashString());
        stringBuffer.append(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append("\nbits: ");
        sb6.append(Long.toHexString(this.bits));
        stringBuffer.append(sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append("\nblockTime: ");
        sb7.append(this.blockTime);
        stringBuffer.append(sb7.toString());
        StringBuilder sb8 = new StringBuilder();
        sb8.append("\nnChainWork: ");
        sb8.append(this.nChainWork);
        stringBuffer.append(sb8.toString());
        mo40990a(stringBuffer);
        return stringBuffer.toString();
    }

    //mo44300k
    public BlockSyncStatus getBlockSyncStatus() {
        return this.blockSyncStatus;
    }

    //mo44288a
    public void setBlockSyncStatus(BlockSyncStatus ikVar) {
        this.blockSyncStatus = ikVar;
    }

    /* access modifiers changed from: protected */
    /* renamed from: l */
    public void mo44301l() {
        CHashWriter cHashWriter = new CHashWriter();
        mo44659c((StreamWriter) cHashWriter);
        this.blockHash.set((BaseBlob) cHashWriter.GetHash());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BlockHeader)) {
            return false;
        }
        BlockHeader jtVar = (BlockHeader) obj;
        return this.blockHash.equals(jtVar.blockHash) && this.preBlockHash.equals(jtVar.preBlockHash) && this.merkelRootHash.equals(jtVar.merkelRootHash);
    }

    /* renamed from: m */
    public long mo44302m() {
        long[] jArr = new long[11];
        int i = 0;
        int i2 = 10;
        BlockHeader jtVar = this;
        while (i < 11 && jtVar != null) {
            i2--;
            jArr[i2] = jtVar.mo44299j();
            i++;
            jtVar = this.wallet.checkAndGetBlockInfo(this.preBlockHash).mBlockHeader;
        }
        Arrays.sort(jArr);
        return jArr[i2 + ((jArr.length - i2) / 2)];
    }

    //mo44303n
    //910 mo44378n
    public ArithUint256 getNChainWork() {
        return this.nChainWork;
    }

    //mo44287a
    public void setNChainWork(ArithUint256 arithUint256) {
        this.nChainWork.copySetNChainWork(arithUint256);
    }

    //mo44304o
    public boolean isFirstBlock() {
        UInt256 uInt256 = this.blockHash;
        if (uInt256 != null && this.blockNo <= 1) {
            return uInt256.equals(this.wallet.getChainParams().hash);
        }
        return false;
    }
}
