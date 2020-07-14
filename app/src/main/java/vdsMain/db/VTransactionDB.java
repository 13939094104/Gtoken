package vdsMain.db;

import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import vdsMain.block.BlockSyncStatus;
import vdsMain.model.VBlockTable;
import vdsMain.table.*;
import vdsMain.wallet.Wallet;

//bkh
public class VTransactionDB extends TransactionDB {

    /* renamed from: e */
//    protected bjz f11827e;

    /* renamed from: g */
//    protected bkb f11828g;

//    /* renamed from: h */
//    protected bka f11829h;

    //f11830i
    protected CachedSaplingBlockTable cachedSaplingBlockTable;

    //910 版本4
    //自己修改到5
    public VTransactionDB(@NonNull Wallet izVar, @NonNull String str, @NonNull String str2) {
        super(izVar, str, str2, 5);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        super.onUpgrade(sQLiteDatabase, i, i2);
        switch (i) {
            case 1:
                sQLiteDatabase.beginTransaction();
                sQLiteDatabase.execSQL("DELETE FROM transactions;");
                BlockTable.m12663a("blocks", sQLiteDatabase, 1, BlockSyncStatus.UNSYNC);
                sQLiteDatabase.setTransactionSuccessful();
                sQLiteDatabase.endTransaction();
                break;
            case 2:
                break;
            default:
                return;
        }
        sQLiteDatabase.execSQL("alter table ad_content  ADD official integer NOT NULL Default 0");
    }

    /* renamed from: h */
    public void InitAndAddtoDbVector() {
        super.InitAndAddtoDbVector();
        this.cachedSaplingBlockTable = new CachedSaplingBlockTable(this);
        this.cachedSaplingBlockTable.setTableName("cachedSapBlock");
        addAbstractTable((AbstractTable) this.cachedSaplingBlockTable);
//        this.f11827e = mo42540o();
//        mo43595a(this.f11827e, "ad_content");
//        this.f11828g = mo42542p();
//        mo43595a(this.f11828g, "ad_tx");
//        this.f11829h = mo42543q();
//        mo43595a(this.f11829h, "ad_section");
    }

    /* renamed from: o */
//    public bjz mo42540o() {
//        return new bjz(this);
//    }
//
//    /* renamed from: p */
//    public bkb mo42542p() {
//        return new bkb(this);
//    }
//
//    /* renamed from: q */
//    public bka mo42543q() {
//        return new bka(this);
//    }
//
//    /* renamed from: s */
//    public bjz mo42544s() {
//        return this.f11827e;
//    }
//
//    /* renamed from: t */
//    public bkb mo42545t() {
//        return this.f11828g;
//    }
//
//    /* renamed from: u */
//    public bka mo42546u() {
//        return this.f11829h;
//    }

    /* renamed from: a */
    public TransactionTable getTransactionTable() {
        return new VTxTable(this);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public BlockTable getBlockTable() {
        return new VBlockTable(this);
    }

    //mo42547v
    public CachedSaplingBlockTable getCachedSaplingBlockTable() {
        return this.cachedSaplingBlockTable;
    }
}

