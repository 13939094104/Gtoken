package vdsMain.model;

import android.content.ContentValues;
import android.database.Cursor;
import bitcoin.UInt256;
import vdsMain.table.AbstractTableItem;

public class RejectTx extends AbstractTableItem {

    //f12402a
    protected UInt256 mTxid = new UInt256();

    //f12403b
    protected int errorCode;

    //f12404c
    protected String errorReason;

    /* renamed from: a */
    public String getKey() {
        return "txid";
    }

    /* renamed from: a */
    public void initTableItemVariable(Cursor cursor, int i, int i2, int i3) {
        switch (i3) {
            case 0:
                this.mTxid.setHash(cursor.getString(i3));
                return;
            case 1:
                this.errorCode = cursor.getInt(i3);
                return;
            case 2:
                this.errorReason = cursor.getString(i3);
                return;
            default:
                return;
        }
    }

    /* renamed from: c */
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("txid", this.mTxid.hashString());
        contentValues.put("e_code", Integer.valueOf(this.errorCode));
        contentValues.put("e_reason", this.errorReason);
        return contentValues;
    }

    /* renamed from: e_ */
    public String getValue() {
        return this.mTxid.hashString();
    }

    /* renamed from: a */
    public void mo43225a(UInt256 uInt256) {
        this.mTxid = uInt256;
    }

    /* renamed from: a */
    public void mo43224a(int i) {
        this.errorCode = i;
    }

    //mo43227d
    public String getErrorReson() {
        return this.errorReason;
    }

    /* renamed from: a */
    public void mo43226a(String str) {
        this.errorReason = str;
    }
}