package vdsMain.table;

import android.content.ContentValues;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.db.WalletDB;
import vdsMain.transaction.Transaction;

import java.util.Locale;

public abstract class TransactionTable extends WalletTable{

    public TransactionTable(@NonNull WalletDB walletDB) {
        super(walletDB);
    }

    //mo44377a
    public void updateBlockHash(UInt256 uInt256, UInt256 updateHash) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("block_hash", updateHash.hashString());
        getDbWritableDataHelper().update(getTableName(), contentValues, String.format(Locale.US, "%s=?", new Object[]{"txid"}), new String[]{uInt256.hashString()});
    }

    //mo44376a
    public void replaceUnConfirmTransaction(UInt256 txid) {
        Transaction transaction = this.wallet.getSelfWalletHelper().getNewVCWalletTx(null);
        transaction.setTable(this);
        transaction.setSelfCWallet(this.wallet.getSelfCWallet());
        transaction.setTxid(txid);
        transaction.setVersion(-1);
        replace((AbstractTableItem) transaction);
    }
}
