package vdsMain.table;

import android.database.Cursor;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.block.CachedSaplingBlock;
import vdsMain.db.WalletDB;
import vdsMain.transaction.SaplingMerkleTree;
import vdsMain.transaction.Transaction;

import java.io.IOException;
import java.util.Collection;

//bkc
public class CachedSaplingBlockTable extends WalletTable {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public AbstractTableItem getTableItem(Cursor cursor, int i) {
        return null;
    }

    public CachedSaplingBlockTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format("(%s TEXT PRIMARY KEY, %s BLOB, %s BLOB)", new Object[]{"block_hash", "tree", "cms"});
    }

    /* renamed from: a */
    public synchronized CachedSaplingBlock mo42529a(UInt256 uInt256) {
        CachedSaplingBlock bjr;
        Cursor query = getDbWritableDataHelper().query(getTableName(), new String[]{"tree", "cms"}, String.format("%s=?", new Object[]{"block_hash"}), new String[]{uInt256.hexString()}, null, null, null);
        bjr = null;
        if (query.moveToNext()) {
            CachedSaplingBlock bjr2 = new CachedSaplingBlock(uInt256, null, null);
            try {
                bjr2.mo42522a(query, 0);
                bjr2.mo42524b(query, 1);
                bjr = bjr2;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        query.close();
        return bjr;
    }

    //mo42530a
    public void checkAndReplaceCachedSaplingBlock(UInt256 uInt256, SaplingMerkleTree saplingMerkleTree, Collection<Transaction> collection) {
        if (collection != null && !collection.isEmpty()) {
            CachedSaplingBlock cachedSaplingBlock = new CachedSaplingBlock(uInt256, saplingMerkleTree, collection);
            if (cachedSaplingBlock.isCachedTxSaplingOutputListNotEmpty()) {
                replaceSynchronized((AbstractTableItem) cachedSaplingBlock);
            }
        }
    }

    /* renamed from: b */
    public void mo42531b(UInt256 uInt256) {
        deleteDataSynchronized(String.format("%s=?", new Object[]{"block_hash"}), new String[]{uInt256.hexString()});
    }
}

