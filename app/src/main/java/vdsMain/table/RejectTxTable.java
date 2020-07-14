package vdsMain.table;

import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.db.WalletDB;
import vdsMain.model.RejectTx;

import java.util.Locale;

public class RejectTxTable extends WalletTable {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    public RejectTxTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US, "(%s TEXT NOT NULL PRIMARY KEY,%s INTEGER, %s TEXT);", new Object[]{"txid", "e_code", "e_reason"});
    }

    //mo44367a
    public RejectTx getRejectTx(UInt256 txid) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * from ");
        sb.append(getTableName());
        sb.append(" WHERE ");
        sb.append("txid");
        sb.append("=? ");
        Cursor c = rawQuery(sb.toString(), new String[]{txid.toString()});
        if (!c.moveToNext()) {
            return null;
        }
        int i = c.getInt(1);
        String string = c.getString(2);
        RejectTx dgVar = new RejectTx();
        dgVar.mo43225a(txid);
        dgVar.mo43224a(i);
        dgVar.mo43226a(string);
        closeCursor(c);
        return dgVar;
    }
}
