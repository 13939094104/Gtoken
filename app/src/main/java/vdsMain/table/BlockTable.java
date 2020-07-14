package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import bitcoin.UInt256;
import vdsMain.Log;
import vdsMain.StringToolkit;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.BlockSyncStatus;
import vdsMain.db.WalletDB;

import java.util.*;

public abstract class BlockTable extends WalletTable {
    public BlockTable(@NonNull WalletDB kiVar) {
        super(kiVar);
    }

    //mo44343a
    //910 mo44420a
    public void updateBlockInfoStatus(BlockInfo blockInfo) {
        updateBlockStatus(blockInfo.getBlockHeader());
    }

    //mo44342a
    public void updateBlockStatus(BlockHeader blockHeader) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("stat", Integer.valueOf(blockHeader.getBlockSyncStatus().getValue()));
        getDbWritableDataHelper().update(getTableName(), contentValues, String.format(Locale.US, "%s=?", new Object[]{"hash"}), new String[]{blockHeader.getBlockHash().toString()});
    }

    //mo44339a
    public BlockHeader getBlockHeaderFromDbByHash(UInt256 uInt256) {
        Cursor cursor = rawQuery(String.format(Locale.US, "SELECT * FROM %s WHERE %s=?", new Object[]{getTableName(), "hash"}), new String[]{uInt256.hashString()});
        BlockHeader blockHeader = getBlockHeaderFromDbCursor(cursor);
        cursor.close();
        return blockHeader;
    }

    //mo44336a
    public long getTimeStampByBlockNo(long blockNo) {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT %s FROM %s WHERE %s=?", new Object[]{"time", getTableName(), "no"}), new String[]{Long.toString(blockNo)});
        long time = c.moveToNext() ? c.getLong(0) : 0;
        c.close();
        return time;
    }

    //mo44344b
    public BlockHeader getBlockHeaderFromDbByNo(long no) {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT * FROM %s WHERE %s=?", new Object[]{getTableName(), "no"}), new String[]{Long.toString(no)});
        BlockHeader c2 = getBlockHeaderFromDbCursor(c);
        c.close();
        return c2;
    }

    //mo44347c
    public BlockHeader getFirstBlockHeaderAfterTime(long time) {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT * FROM %s WHERE %s>=? LIMIT 1", new Object[]{getTableName(), "time"}), new String[]{Long.toString(time)});
        BlockHeader blockHeader = getBlockHeaderFromDbCursor(c);
        c.close();
        return blockHeader;
    }

    //mo44350d
    public UInt256 selectBlockHashByNo(long j) {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT %s FROM %s WHERE %s=?", new Object[]{"hash", getTableName(), "no"}), new String[]{Long.toString(j)});
        UInt256 fromHash = c.moveToNext() ? UInt256.fromHash(c.getString(0)) : null;
        c.close();
        return fromHash;
    }

    //mo44346b
    public boolean getSynchedBlockByHash(UInt256 uInt256) {
        Cursor c = rawQueryNoArgs(String.format(Locale.US, "SELECT %s FROM %s WHERE %s='%s' AND %s=%d", new Object[]{"hash", getTableName(), "hash", Integer.valueOf(uInt256.hashCode()), "stat", Integer.valueOf(BlockSyncStatus.SYNCHED.getValue())}));
        boolean moveToNext = c.moveToNext();
        c.close();
        return moveToNext;
    }

    //mo44349c
    public boolean isBlockHashInDbTable(UInt256 uInt256) {
        Cursor c = rawQuery(String.format(Locale.US, "SELECT %s FROM %s WHERE %s=?", new Object[]{"no", getTableName(), "hash"}), new String[]{uInt256.hashString()});
        boolean moveToNext = c.moveToNext();
        c.close();
        return moveToNext;
    }

    //mo44354i
    public BlockHeader getBlockHeaderByNo() {
        Cursor cursor = rawQuery(String.format(Locale.US, "SELECT * FROM %s ORDER BY %s DESC LIMIT 1", new Object[]{getTableName(), "no"}), new String[0]);
        BlockHeader blockHeader = null;
        if (cursor.moveToNext()) {
            try {
                blockHeader = this.wallet.getSelfWalletHelper().getNewBlockHeader();
                blockHeader.initAllTableItemVariable(cursor, getVersion(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return blockHeader;
    }

    //mo44351d
    public List<BlockHeader> selectSynchedBlockByLimit(int limit) {
        List<BlockHeader> e = mo44353e(String.format(Locale.US, "SELECT * FROM %s WHERE %s=%d ORDER BY %s DESC LIMIT %d", new Object[]{getTableName(), "stat", Integer.valueOf(BlockSyncStatus.SYNCHED.getValue()), "no", Integer.valueOf(limit)}));
        Collections.reverse(e);
        return e;
    }

    /* renamed from: e */
    public List<BlockHeader> mo44352e(int i) {
        return mo44353e(String.format(Locale.US, "SELECT * FROM %s WHERE %s!=%d LIMIT %d", new Object[]{getTableName(), "stat", Integer.valueOf(BlockSyncStatus.SYNCHED.getValue()), Integer.valueOf(i)}));
    }

    /* renamed from: e */
    public List<BlockHeader> mo44353e(String str) {
        Cursor c = rawQuery(str, null);
        Vector vector = new Vector(c.getCount());
        try {
            BlockHeader jtVar = (BlockHeader) getCompleteTableItem(c);
            while (jtVar != null) {
                vector.add(jtVar);
                jtVar = (BlockHeader) getCompleteTableItem(c);
            }
            c.close();
            return vector;
        } catch (Exception e) {
            e.printStackTrace();
            Log.LogObjError((Object) this, StringToolkit.m11523a((Throwable) e));
            throw e;
        } catch (Throwable th) {
            c.close();
            throw th;
        }
    }

    //mo44340a
    public void updateBlockSyncStatusByNo(int i, BlockSyncStatus blockSyncStatus) {
        execSql(String.format(Locale.US, "UPDATE %s SET %s=%d WHERE %s<=%d", new Object[]{getTableName(), "stat", Integer.valueOf(blockSyncStatus.getValue()), "no", Integer.valueOf(i)}));
    }

    /* renamed from: a */
    public void mo44341a(long j, BlockSyncStatus ikVar) {
        execSql(String.format(Locale.US, "UPDATE %s SET %s=%d WHERE %s>=%d", new Object[]{getTableName(), "stat", Integer.valueOf(ikVar.getValue()), "no", Long.valueOf(j)}));
    }

    //mo44345b
    public void deleteBlocksSinceStartBlockNo(long startBlockNo, BlockSyncStatus ikVar) {
        execSql(String.format(Locale.US, "DELETE FROM %s WHERE %s>=%d", new Object[]{getTableName(), "no", Long.valueOf(startBlockNo)}));
    }

    /* renamed from: a */
    public static void m12663a(String str, SQLiteDatabase sQLiteDatabase, long j, BlockSyncStatus ikVar) {
        sQLiteDatabase.execSQL(String.format(Locale.US, "UPDATE %s SET %s=%d WHERE %s>=%d", new Object[]{str, "stat", Integer.valueOf(ikVar.getValue()), "no", Long.valueOf(j)}));
    }

    /* access modifiers changed from: protected */
    //mo44348c
    public BlockHeader getBlockHeaderFromDbCursor(Cursor cursor) {
        BlockHeader blockHeader = null;
        try {
            if (cursor.moveToNext()) {
                BlockHeader tmpBlockHeader = this.wallet.getSelfWalletHelper().getNewBlockHeader();
                tmpBlockHeader.setTable(this);
                tmpBlockHeader.initAllTableItemVariable(cursor, getVersion(), 0);
                blockHeader = tmpBlockHeader;
            }
            return blockHeader;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //mo44337a
    public List<BlockHeader> getBlockHeaderByMinNoAndLimit(int minNo, int limit) {
        Cursor rawQuery = getDbWritableDataHelper().rawQuery(String.format(Locale.US, "SELECT * FROM %s WHERE %s >= %d ORDER BY %s ASC LIMIT %d", new Object[]{getTableName(), "no", Integer.valueOf(minNo), "no", Integer.valueOf(limit)}), null);
        Vector vector = new Vector(limit);
        while (rawQuery.moveToNext()) {
            try {
                BlockHeader newBlockHeader = this.wallet.getSelfWalletHelper().getNewBlockHeader();
                newBlockHeader.initAllTableItemVariable(rawQuery, getVersion(), 0);
                vector.add(newBlockHeader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rawQuery.close();
        return vector;
    }

    //mo44338a
    public List<byte[]> getBlockHashListFromHeightList(List<Integer> list) throws Throwable {
        Cursor cursor;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(",");
            }
        }
        ArrayList<byte[]> arrayList = new ArrayList<>();
        try {
            cursor = getDbWritableDataHelper().rawQuery(String.format(Locale.US, "SELECT %s FROM %s WHERE %s IN ( %s ) ORDER BY %s DESC", new Object[]{"hash", getTableName(), "no", sb.toString(), "no"}), null);
            while (cursor.moveToNext()) {
                arrayList.add(new UInt256(cursor.getString(0)).data());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }



    //910 mo44433k
    public UInt256 getFirstUnsyncBlockHash() {
        UInt256 uInt256;
        try {
            Cursor c = rawQuery(String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d ORDER BY %s ASC LIMIT 1", new Object[]{"hash", getTableName(), "anon_stat", Integer.valueOf(BlockSyncStatus.UNSYNC.getValue()), "no"}), new String[0]);
            uInt256 = null;
            while (c.moveToNext()) {
                uInt256 = new UInt256(c.getString(0));
            }
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            uInt256 = null;
            e.printStackTrace();
            return uInt256;
        }
        return uInt256;
    }
}
