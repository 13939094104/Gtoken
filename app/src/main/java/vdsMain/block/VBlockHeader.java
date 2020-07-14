package vdsMain.block;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.BaseBlob;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.*;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.io.IOException;

//bje
public class VBlockHeader extends BlockHeader {

    //f11724k
    protected UInt256 nonce;

    //f11725l
    protected byte[] solutionBytes;

    //f11726m
    protected UInt256 hashFinalSaplingRoot;

    //f11727n
    protected UInt256 hashStateRoot;

    //f11728o
    protected UInt256 hashUTXORoot;

    //f11729p
    protected long mVibPool;

    protected BlockSyncStatus f11843q;

    //mo42502s
    public int getBlockHeaderByteLength() {
        return 212;
    }

    public VBlockHeader(@NonNull Wallet izVar) {
        super(izVar);
        this.nonce = new UInt256();
        this.solutionBytes = null;
        this.f11843q = BlockSyncStatus.UNSYNC;
        this.blockVersion = 4;
    }

    public VBlockHeader(BlockHeader blockHeader) {
        super(blockHeader);
        this.nonce = new UInt256();
        this.solutionBytes = null;
        this.f11843q = BlockSyncStatus.UNSYNC;
        if (blockHeader instanceof VBlockHeader) {
            VBlockHeader vBlockHeader = (VBlockHeader) blockHeader;
            this.nonce = UInt256.replace(this.nonce, vBlockHeader.nonce);
            this.hashFinalSaplingRoot = UInt256.replace(this.hashFinalSaplingRoot, vBlockHeader.hashFinalSaplingRoot);
            this.solutionBytes = DataTypeToolkit.bytesCopy(vBlockHeader.solutionBytes);
            this.hashStateRoot = UInt256.replace(this.hashStateRoot, vBlockHeader.hashStateRoot);
            this.hashUTXORoot = UInt256.replace(this.hashUTXORoot, vBlockHeader.hashUTXORoot);
            this.mVibPool = vBlockHeader.mVibPool;
        }
    }

    public VBlockHeader(@NonNull Wallet izVar, AbstractTable fxVar) {
        super(izVar, fxVar);
        this.nonce = new UInt256();
        this.solutionBytes = null;
        this.f11843q = BlockSyncStatus.UNSYNC;
        this.blockVersion = 4;
    }

    /* renamed from: p */
    public UInt256 mo42499p() {
        return this.nonce;
    }

    /* renamed from: q */
    public byte[] mo42500q() {
        return this.solutionBytes;
    }

    public VBlockHeader(@NonNull Wallet izVar, int i, long j, @NonNull UInt256 uInt256, @NonNull UInt256 uInt2562, @NonNull UInt256 uInt2563, long j2) {
        super(izVar, i, j, uInt256, uInt2562, uInt2563, j2);
        this.nonce = new UInt256();
        this.solutionBytes = null;
    }

    //mo42496b
    public void initHashFinalSaplingRoot(UInt256 uInt256) {
        if (uInt256 == null) {
            this.hashFinalSaplingRoot = null;
        } else {
            this.hashFinalSaplingRoot = UInt256.replace(this.hashFinalSaplingRoot, uInt256);
        }
    }

    /* renamed from: r */
    public long mo42501r() {
        return this.mVibPool;
    }

    /* renamed from: c */
    public void mo42497c(UInt256 uInt256) {
        if (uInt256 == null) {
            this.hashStateRoot = null;
            return;
        }
        UInt256 uInt2562 = this.hashStateRoot;
        if (uInt2562 == null) {
            this.hashStateRoot = new UInt256((BaseBlob) uInt256);
        } else {
            uInt2562.set((BaseBlob) uInt256);
        }
    }

    /* renamed from: d */
    public void mo42498d(UInt256 uInt256) {
        if (uInt256 == null) {
            this.hashUTXORoot = null;
            return;
        }
        UInt256 uInt2562 = this.hashUTXORoot;
        if (uInt2562 == null) {
            this.hashUTXORoot = new UInt256((BaseBlob) uInt256);
        } else {
            uInt2562.set((BaseBlob) uInt256);
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.blockVersion);
        streamWriter.writeUInt256(this.preBlockHash);
        streamWriter.writeUInt256(this.merkelRootHash);
        streamWriter.writeUInt256(this.hashFinalSaplingRoot);
        streamWriter.writeUInt64(this.mVibPool);
        streamWriter.writeUInt32T(this.blockTime);
        streamWriter.writeUInt32T(this.bits);
        streamWriter.writeUInt256(this.hashStateRoot);
        streamWriter.writeUInt256(this.hashUTXORoot);
        streamWriter.writeUInt256(this.nonce);
        streamWriter.writeVariableBytes(this.solutionBytes);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {
        byte[] bytes=seriableData.readBytes(getBlockHeaderByteLength());
        DummySeriableData dummySeriableData = new DummySeriableData(new ByteBuffer(bytes));;
        this.hashFinalSaplingRoot = new UInt256();
        this.hashStateRoot = new UInt256();
        this.hashUTXORoot = new UInt256();
        this.blockVersion = dummySeriableData.readUInt32();
        this.preBlockHash.decodeSerialStream((SeriableData) dummySeriableData);
        this.merkelRootHash.decodeSerialStream((SeriableData) dummySeriableData);
        this.hashFinalSaplingRoot.decodeSerialStream((SeriableData) dummySeriableData);
        this.mVibPool = dummySeriableData.readUInt64().longValue();
        this.blockTime = dummySeriableData.readUInt32();
        this.bits = dummySeriableData.readUInt32();
        this.hashStateRoot.decodeSerialStream((SeriableData) dummySeriableData);
        this.hashUTXORoot.decodeSerialStream((SeriableData) dummySeriableData);
        this.nonce.decodeSerialStream((SeriableData) dummySeriableData);
        this.solutionBytes = seriableData.readVariableBytes();
        mo44301l();
        if (this.f13141j) {
            seriableData.readVariableInt();
        }
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
        switch (i3) {
            case 0:
                this.blockHash.setHash(cursor.getString(i3));
                return;
            case 1:
                this.blockNo = cursor.getInt(i3);
                return;
            case 2:
                this.preBlockHash.setHash(cursor.getString(i3));
                return;
            case 3:
                this.blockSyncStatus = BlockSyncStatus.m12158a(cursor.getInt(i3));
                return;
            case 4:
                this.merkelRootHash.setHash(cursor.getString(i3));
                return;
            case 5:
                this.hashFinalSaplingRoot = Utils.m13304a(cursor, i3, false);
                return;
            case 6:
                this.hashStateRoot = Utils.m13304a(cursor, i3, false);
                return;
            case 7:
                this.hashUTXORoot = Utils.m13304a(cursor, i3, false);
                return;
            case 8:
                this.mVibPool = cursor.getLong(i3);
                return;
            case 9:
                this.nonce.setHash(cursor.getString(i3));
                break;
            case 10:
                break;
            case 11:
                this.bits = cursor.getLong(i3);
                return;
            case 12:
                this.blockTime = cursor.getLong(i3);
                return;
            case 13:
                this.solutionBytes = cursor.getBlob(i3);
                return;
            case 14:
                this.blockVersion = cursor.getLong(i3);
                return;
            case 15:
                this.f11843q = BlockSyncStatus.m12158a(cursor.getInt(i3));
                return;
            default:
                return;
        }
        this.nChainWork.mo9497a(cursor.getString(i3));
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ver", Long.valueOf(this.blockVersion));
        contentValues.put("no", Integer.valueOf(this.blockNo));
        contentValues.put("hash", this.blockHash.hashString());
        contentValues.put("prev", this.preBlockHash.hashString());
        contentValues.put("merkle", this.merkelRootHash.hashString());
        contentValues.put("stat", Integer.valueOf(this.blockSyncStatus.getValue()));
        Utils.m13307a(contentValues, "nonce", this.nonce, "");
        contentValues.put("chain", this.nChainWork.mo9500b());
        contentValues.put("bits", Long.valueOf(this.bits));
        contentValues.put("time", Long.valueOf(this.blockTime));
        byte[] bArr = this.solutionBytes;
        if (bArr != null) {
            contentValues.put("sol", bArr);
        }
        Utils.m13307a(contentValues, "sapling", this.hashFinalSaplingRoot, "");
        Utils.m13307a(contentValues, "statroot", this.hashStateRoot, "");
        Utils.m13307a(contentValues, "utxoroot", this.hashUTXORoot, "");
        contentValues.put("vibpool", Long.valueOf(this.mVibPool));
        contentValues.put("anon_stat", Integer.valueOf(this.f11843q.getValue()));
        return contentValues;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo40990a(StringBuffer stringBuffer) {
        if (this.hashFinalSaplingRoot != null) {
            stringBuffer.append("\nhashFinalSaplingRoot: ");
            stringBuffer.append(this.hashFinalSaplingRoot.toString());
        }
        if (this.nonce != null) {
            stringBuffer.append("\nnonce: ");
            stringBuffer.append(this.nonce.toString());
        }
        if (this.hashStateRoot != null) {
            stringBuffer.append("\nhashStateRoot: ");
            stringBuffer.append(this.hashStateRoot.toString());
        }
        if (this.hashUTXORoot != null) {
            stringBuffer.append("\nhashUTXORoot: ");
            stringBuffer.append(this.hashUTXORoot.toString());
        }
        if (this.solutionBytes != null) {
            stringBuffer.append("\nsolution: ");
            stringBuffer.append(StringToolkit.bytesToString(this.solutionBytes));
        }
        stringBuffer.append("\nvibpool: ");
        stringBuffer.append(this.mVibPool);
    }

    //mo40992a
    public boolean checkBlockVaild(CValidationState cValidationState, boolean z) {
        ChainParams chainParams = this.wallet.getChainParams();
        if (this.blockVersion < 4) {
            return cValidationState.mo41041a(100, false, 16, "version-too-low");
        }
        if (z && !Pow.m9542a(this, chainParams)) {
            return cValidationState.mo41041a(100, false, 16, "invalid-solution");
        }
        if (z && !Pow.m9541a(mo44295f(), this.bits, (VParams) chainParams.mo43961n())) {
            return cValidationState.mo41041a(50, false, 16, "high-hash");
        }
        if (this.mVibPool < 0) {
            return cValidationState.mo41041a(100, false, 16, "invalid-vibpool");
        }
        if (this.blockTime > TimeData.m122b() + 7200) {
            return cValidationState.mo41043a(false, 16, "time-too-new");
        }
        return true;
    }
}
