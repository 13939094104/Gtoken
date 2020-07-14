package vdsMain.block;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import net.bither.bitherj.utils.Utils;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.ChainParams;
import vdsMain.wallet.Wallet;

import java.io.IOException;

public class BitcoinBlockHeader extends BlockHeader {

    /* renamed from: k */
    private long f7737k;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public boolean mo40991a(CValidationState atVar) {
        return true;
    }

    /* renamed from: g_ */
    public int mo40994g_() {
        return 80;
    }

    public BitcoinBlockHeader(@NonNull Wallet izVar) {
        super(izVar);
        this.f7737k = 0;
    }

    public BitcoinBlockHeader(@NonNull BlockHeader jtVar) {
        super(jtVar);
        this.f7737k = 0;
    }

    public BitcoinBlockHeader(@NonNull Wallet izVar, @NonNull AbstractTable fxVar) {
        super(izVar, fxVar);
        this.f7737k = 0;
    }

    public BitcoinBlockHeader(@NonNull Wallet izVar, int i, long j, @NonNull UInt256 uInt256, @NonNull UInt256 uInt2562, @NonNull UInt256 uInt2563, long j2) {
        super(izVar, i, j, uInt256, uInt2562, uInt2563, j2);
        this.f7737k = 0;
        this.f7737k = this.blockTime;
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ver", Long.valueOf(this.blockVersion));
        contentValues.put("no", Integer.valueOf(this.blockNo));
        contentValues.put("hash", this.blockHash.hashString());
        contentValues.put("prev", this.preBlockHash.hashString());
        contentValues.put("merkle", this.merkelRootHash.hashString());
        contentValues.put("bits", Long.valueOf(this.bits));
        contentValues.put("time", Long.valueOf(this.blockTime));
        contentValues.put("nonce", Long.valueOf(this.f7737k));
        contentValues.put("chain", this.nChainWork.mo9500b());
        contentValues.put("stat", Integer.valueOf(this.blockSyncStatus.getValue()));
        return contentValues;
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
                this.f7737k = cursor.getLong(i3);
                return;
            case 6:
                this.nChainWork.mo9497a(cursor.getString(i3));
                return;
            case 7:
                this.bits = cursor.getLong(i3);
                return;
            case 8:
                this.blockTime = cursor.getLong(i3);
                return;
            case 9:
                this.blockVersion = cursor.getLong(i3);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void mo40990a(StringBuffer stringBuffer) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nnonce: ");
        sb.append(this.f7737k);
        stringBuffer.append(sb.toString());
    }

    /* renamed from: a */
    public synchronized void mo40987a(long j) {
        this.f7737k = j;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void writeSerialData(StreamWriter streamWriter) throws IOException {
        streamWriter.writeUInt32T(this.blockVersion);
        this.preBlockHash.serialToStream(streamWriter);
        this.merkelRootHash.serialToStream(streamWriter);
        streamWriter.writeUInt32T(this.blockTime);
        streamWriter.writeUInt32T(this.bits);
        streamWriter.writeUInt32T(this.f7737k);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void decodeSerialData(SeriableData seriableData) {
        byte[] readBytes = seriableData.readBytes(mo40994g_());
        this.blockVersion = Utils.m3444a(readBytes, 0);
        this.preBlockHash.setData(readBytes, 4);
        this.merkelRootHash.setData(readBytes, 36);
        this.blockTime = Utils.m3444a(readBytes, 68);
        this.bits = Utils.m3444a(readBytes, 72);
        this.f7737k = Utils.m3444a(readBytes, 76);
        mo44301l();
        if (this.f13141j) {
            seriableData.readVariableInt();
        }
    }

    /* renamed from: a */
    public boolean checkBlockVaild(CValidationState atVar, boolean z) {
        if (!mo40991a(atVar) || !mo40993b(atVar)) {
            return false;
        }
        ChainParams J = this.wallet.getChainParams();
        long j = (long) this.blockNo;
        if ((this.blockVersion >= 2 || j < J.f14916f) && ((this.blockVersion >= 3 || j < J.f14918h) && (this.blockVersion >= 4 || j < J.f14917g))) {
            UInt256 uInt256 = (UInt256) J.f14919i.get(j);
            if (uInt256 == null || uInt256.equals(this.blockHash)) {
                return true;
            }
            return atVar.mo41042a(100, false, 16, "block hash is error at check point", false);
        }
        return atVar.mo41042a(100, false, 16, "header version error", false);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public boolean mo40993b(CValidationState atVar) {
        long a = Utils.getTimeStamp() + 7200;
        if (this.blockTime <= a) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("time-too-new (");
        sb.append(this.blockTime);
        sb.append(" - ");
        sb.append(a);
        sb.append(")");
        return atVar.mo41042a(100, false, 16, sb.toString(), false);
    }
}
