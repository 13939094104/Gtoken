package vdsMain.model;

import android.database.Cursor;
import androidx.annotation.NonNull;
import vdsMain.block.VBlockHeader;
import vdsMain.db.WalletDB;
import vdsMain.table.AbstractTableItem;
import vdsMain.table.BlockTable;

import java.util.Locale;

//bkf
//910 bko
public class VBlockTable extends BlockTable {
    public VBlockTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    /* renamed from: b */
    public void createTable(int i) {
        super.createTable(i);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return new VBlockHeader(this.wallet, this);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US, "(%s TEXT PRIMARY KEY, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s BLOB, %s INTEGER, %s INTEGER)", new Object[]{"hash", "no", "prev", "stat", "merkle", "sapling", "statroot", "utxoroot", "vibpool", "nonce", "chain", "bits", "time", "sol", "ver", "anon_stat"});
    }
}