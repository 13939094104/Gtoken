package vdsMain.transaction;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.ByteBuffer;
import vdsMain.DumpedStreamWriter;
import vdsMain.table.AbstractTable;
import vdsMain.wallet.Wallet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

//bqo
public class VCMerkleTx extends VTransaction {

    /* renamed from: s */
    public int f12093s = -1;

    public VCMerkleTx(@NonNull Wallet izVar) {
        super(izVar);
    }

    public VCMerkleTx(@NonNull Wallet izVar, AbstractTable fxVar) {
        super(izVar, fxVar);
    }

    public VCMerkleTx(Transaction dhVar) {
        super(dhVar);
    }

    public VCMerkleTx(VCMerkleTx bqo) {
        super((Transaction) bqo);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42876b(StreamWriter streamWriter) throws IOException {
        streamWriter.writeVariableInt(0);
        streamWriter.writeUInt32T((long) this.f12093s);
    }

    /* renamed from: a */
    public final long mo42875a(byte[] bArr, int i) throws IOException {
        C3899a aVar = new C3899a(ByteBuffer.getNewByteBuffer(bArr, i));
        long leftReadableLength = aVar.getLeftReadableLength();
        mo42877b((SeriableData) aVar);
        return leftReadableLength - aVar.getLeftReadableLength();
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void mo42877b(SeriableData seriableData) throws IOException {
        UInt256.readList(new Vector(), seriableData);
        this.f12093s = seriableData.readInt32();
    }

    /* renamed from: a */
    public static final void m10340a(Wallet izVar, SeriableData seriableData, List<VCMerkleTx> list) {
        int b = seriableData.readVariableInt().getIntValue();
        for (int i = 0; i < b; i++) {
            new VCWalletTx(izVar).decodeSerialItem(seriableData.getTempInput());
        }
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues c = super.getContentValues();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            mo42876b((StreamWriter) new DumpedStreamWriter(byteArrayOutputStream));
            c.put("ext", byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            c.put("ext", new byte[0]);
        }
        return c;
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3)  {
        if (i3 == cursor.getColumnCount() - 1) {
            byte[] blob = cursor.getBlob(i3);
            if (blob != null && blob.length != 0) {
                try {
                    mo42875a(blob, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            return;
        }
        super.initTableItemVariable(cursor, i, i2, i3);
    }

    /* renamed from: E */
    public Transaction clone() {
        return new VCMerkleTx(this);
    }
}

