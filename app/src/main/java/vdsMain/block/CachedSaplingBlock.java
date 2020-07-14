package vdsMain.block;

import android.content.ContentValues;
import android.database.Cursor;
import bitcoin.BaseBlob;
import bitcoin.UInt256;
import vdsMain.ByteBuffer;
import vdsMain.DummySeriableData;
import vdsMain.table.AbstractTableItem;
import vdsMain.transaction.SaplingMerkleTree;
import vdsMain.transaction.Transaction;
import vdsMain.transaction.VTransaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//bjr
public class CachedSaplingBlock extends AbstractTableItem {

    /* renamed from: a */
    public UInt256 f11799a;

    /* renamed from: b */
    public SaplingMerkleTree f11800b;

    //f11801c
    public List<CachedTxSaplingOutput> cachedTxSaplingOutputList;

    /* renamed from: a */
    public String getKey() {
        return "block_hash";
    }

    public CachedSaplingBlock() {
        this.f11799a = new UInt256();
    }

    public CachedSaplingBlock(UInt256 uInt256, SaplingMerkleTree bsb, Collection<Transaction> collection) {
        this();
        mo42523a(uInt256, bsb, collection);
    }

    //mo42525d
    public boolean isCachedTxSaplingOutputListNotEmpty() {
        List<CachedTxSaplingOutput> list = this.cachedTxSaplingOutputList;
        return list != null && !list.isEmpty();
    }

    /* renamed from: a */
    public void mo42523a(UInt256 uInt256, SaplingMerkleTree bsb, Collection<Transaction> collection) {
        this.f11799a.set((BaseBlob) uInt256);
        this.f11800b = bsb;
        if (collection == null || collection.isEmpty()) {
            this.cachedTxSaplingOutputList = null;
            return;
        }
        for (Transaction dhVar : collection) {
            this.cachedTxSaplingOutputList = new ArrayList(10);
            if (dhVar instanceof VTransaction) {
                List i = ((VTransaction) dhVar).getOutputDescriptionList();
                if (i != null && !i.isEmpty()) {
                    if (this.cachedTxSaplingOutputList == null) {
                        this.cachedTxSaplingOutputList = new ArrayList(10);
                    }
                    this.cachedTxSaplingOutputList.add(new CachedTxSaplingOutput(dhVar.getTxId(), i));
                }
            }
        }
        if (this.cachedTxSaplingOutputList.isEmpty()) {
            this.cachedTxSaplingOutputList = null;
        }
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3)  {
        switch (i3) {
            case 0:
                this.f11799a.setHex(cursor.getString(i3));
                return;
            case 1:
                mo42522a(cursor, i3);
                return;
            case 2:
                try {
                    mo42524b(cursor, i3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            default:
                return;
        }
    }

    /* renamed from: a */
    public void mo42522a(Cursor cursor, int... iArr) {
        this.f11800b = null;
        int i = 0;
        if (iArr.length > 0) {
            i = iArr[0];
        }
        byte[] blob = cursor.getBlob(i);
        if (blob != null && blob.length != 0) {
            this.f11800b = (SaplingMerkleTree) new DummySeriableData(new ByteBuffer(blob)).readOptionalObject(SaplingMerkleTree.class);
        }
    }

    /* renamed from: b */
    public void mo42524b(Cursor cursor, int... iArr) throws IOException {
        int i = 0;
        if (iArr.length > 0) {
            i = iArr[0];
        }
        byte[] blob = cursor.getBlob(i);
        if (blob == null || blob.length == 0) {
            this.cachedTxSaplingOutputList = null;
            return;
        }
        this.cachedTxSaplingOutputList = new ArrayList();
        new DummySeriableData(new ByteBuffer(blob)).readObjectList(this.cachedTxSaplingOutputList, CachedTxSaplingOutput.class);
        if (this.cachedTxSaplingOutputList.isEmpty()) {
            this.cachedTxSaplingOutputList = null;
        }
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("block_hash", this.f11799a.hexString());
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new DummySeriableData((OutputStream) byteArrayOutputStream).writeOptionalObject(this.f11800b);
            contentValues.put("tree", byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            new DummySeriableData((OutputStream) byteArrayOutputStream2).writeObjectList(this.cachedTxSaplingOutputList);
            contentValues.put("cms", byteArrayOutputStream2.toByteArray());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return contentValues;
    }

    /* renamed from: e_ */
    public String getValue() {
        return this.f11799a.toString();
    }
}
