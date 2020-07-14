package vdsMain.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.vc.libcommon.exception.AddressFormatException;
import generic.exceptions.UnsupportAddressException;
import vdsMain.AddressType;
import vdsMain.CTxDestination;
import vdsMain.EncryptedPrivateKey;
import vdsMain.db.WalletDB;
import vdsMain.model.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AddressTable extends WalletTable {
    public AddressTable(@NonNull WalletDB walletDB) {
        super(walletDB);
    }

    /* access modifiers changed from: protected */
    //mo41100a
    public AbstractTableItem getTableItem(Cursor cursor, int i) throws UnsupportAddressException, AddressFormatException {
        AddressType addressType = Address.getAddressType(cursor.getInt(1));
        Address address = this.wallet.getSelfWalletHelper().getAddress((WalletTable) this, addressType);
        if (address != null) {
            return address;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupport address type ");
        sb.append(addressType.name());
        throw new UnsupportAddressException(sb.toString());
    }

    /* renamed from: a */
    public String getCreateFieldSql(int i) {
        return String.format(Locale.US,
                "(%s TEXT PRIMARY KEY,%s INTEGER,%s INTEGER,%s TEXT, %s INTEGER, %s INTEGER,%s INTEGER, %s INTEGER, %s BLOB, %s BLOB, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT,%s INTEGER,%s INTEGER,%s INTEGER,%s INTEGER, %s TEXT,%s BLOB);",
                new Object[]{"hash", "flag", "account", "lable", "fission_reward", "runaway_reward", "direct_inv_no", "last_block", "pub", "priv", "season_fission_reward", "season_fission_total", "fission_total", "address_group", "address_order_index", "is_direct_sign_vid", "applying_vid", "clue_txid", NotificationCompat.CATEGORY_STATUS, "v_index", "hide","applying_vxd","vxd_txid","full_pub"});
    }

    //mo44332a
    public void updateFlagByCTxDesHash(CTxDestination cTxDestination, int flag) {
        getDbWritableDataHelper().execSQL(String.format(Locale.US, "UPDATE %s SET %s=? WHERE %s=?", new Object[]{getTableName(), "flag", "hash"}), new String[]{Integer.toString(flag), cTxDestination.getHash()});
    }

    //mo44333a
    public void updateLabelByDesHash(CTxDestination cTxDestination, String str) {
        getDbWritableDataHelper().execSQL(String.format(Locale.US, "UPDATE %s SET %s=? WHERE %s=?", new Object[]{getTableName(), "lable", "hash"}), new String[]{str, cTxDestination.getHash()});
    }

    //mo44330a
    public void updateAddressInfo(Address address) {
        SQLiteDatabase database = getDbWritableDataHelper();
        String format = String.format(Locale.US, "UPDATE %s SET %s=?,%s=?,%s=?,%s=?,%s=?,%s=?,%s=? WHERE %s=?", new Object[]{getTableName(), "fission_reward", "runaway_reward", "direct_inv_no", "last_block", "season_fission_reward", "fission_total", "season_fission_total", "hash"});
        StringBuilder sb = new StringBuilder();
        sb.append(address.getFissionReward());
        sb.append("");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(address.getRunawayReward());
        sb2.append("");
        StringBuilder sb3 = new StringBuilder();
        sb3.append(address.getDirectInvNo());
        sb3.append("");
        StringBuilder sb4 = new StringBuilder();
        sb4.append(address.getLastBlock());
        sb4.append("");
        StringBuilder sb5 = new StringBuilder();
        sb5.append(address.getSeasonFissionReward());
        sb5.append("");
        StringBuilder sb6 = new StringBuilder();
        sb6.append(address.getFissionTotal());
        sb6.append("");
        StringBuilder sb7 = new StringBuilder();
        sb7.append(address.getSeasonFissionTotal());
        sb7.append("");
        database.execSQL(format, new String[]{sb.toString(), sb2.toString(), sb3.toString(), sb4.toString(), sb5.toString(), sb6.toString(), sb7.toString(), address.getCTxDestination().getHash()});
    }

    //mo44334b
    public void replace(Address address) {
        replace((AbstractTableItem) address);
    }

    /* renamed from: c */
    public void mo44335c(Address jjVar) {
        mo44331a(jjVar.getCTxDestination());
    }

    /* renamed from: a */
    public synchronized void mo44331a(CTxDestination oVar) {
        if (oVar != null) {
            getDbWritableDataHelper().execSQL(String.format(Locale.US, "DELETE FROM %s WHERE %s='%s';", new Object[]{getTableName(), "hash", oVar.getHash()}));
        }
    }


    //mo44329a
    public synchronized void updateAddressPrivKey(List<Address> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                boolean d = isInTransaction();
                if (!d) {
                    beginTransaction();
                }
                ContentValues contentValues = new ContentValues();
                String format = String.format(Locale.US, "%s=?", new Object[]{"hash"});
                for (Address address : list) {
                    EncryptedPrivateKey privateKey = address.getSelfEncryptedPrivateKey();
                    if (privateKey != null) {
                        if (!privateKey.isBytesEmpty()) {
                            contentValues.put("priv", privateKey.getBytes());
                            getDbWritableDataHelper().update(getTableName(), contentValues, format, new String[]{address.getCTxDestination().getHash()});
                        }
                    }
                }
                if (!d) {
                    endTransaction(true);
                }
            }
        }
    }
}
