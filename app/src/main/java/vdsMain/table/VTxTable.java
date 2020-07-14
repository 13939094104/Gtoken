package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.annotation.NonNull;
import vdsMain.db.WalletDB;
import vdsMain.transaction.SerializedSaplingNoteMap;
import vdsMain.transaction.VCWalletTx;
import vdsMain.transaction.VTransaction;

import java.util.Locale;

//bki
public class VTxTable extends TransactionTable {
    public VTxTable(@NonNull WalletDB walletDB) {
        super(walletDB);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US, "(%s TEXT NOT NULL PRIMARY KEY, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s BLOB, %s INTEGER NO NULL, %s BLOB, %s BLOB, %s INTEGER, %s INTEGER, %s BLOB, %s BLOB, %s BLOB, %s BLOB, %s BLOB, %s BLOB);", new Object[]{"txid", "version", "flag", "locktime", "time", "bno", "block_hash", "tx_ins", "tx_outs", "witness", "exp", "vbalance", "smerk", "sspend", "soutput", "bsig", "sapn", "ext"});
    }

    //mo42548a
    public void updateVTxSapn(VTransaction vTransaction) {
        if (vTransaction.isOutputDescriptionListNotEmpty()) {
            SerializedSaplingNoteMap serializedSaplingNoteMap = new SerializedSaplingNoteMap();
            serializedSaplingNoteMap.addToIndexToSaplingNoteItemMap(vTransaction.getSaplingUtxoValueList(), vTransaction.getMapSaplingNoteDataT());
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put("sapn", serializedSaplingNoteMap.serialToStream());
            } catch (Exception e) {
                e.printStackTrace();
                contentValues.put("sapn", new byte[]{0});
            }
            getDbWritableDataHelper().update(getTableName(), contentValues, "txid=?", new String[]{vTransaction.getTxId().toString()});
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return new VCWalletTx(this.wallet, this);
    }
}