package vdsMain.db;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.vtoken.application.ApplicationLoader;

import vdsMain.model.Settings;
import vdsMain.table.*;
import vdsMain.tool.SharedPreferencesUtil;
import vdsMain.wallet.Wallet;

public abstract class PersonalDB extends WalletDB {

    protected Settings settings;

    //f13150d
    protected VidGroupTable vidGroupTable;

    //f13148b
    protected AddressTable addressTable;

    //f13149c
    protected HDAccountTable hdAccountTable;

    //f13151e
    protected OriginNodeTable originNodeTable;

    //f13153h
    private SubAddressTable subAddressTable;

    //mo41118b
    public abstract HDAccountTable getHDAccountTable();

    //mo41117a
    public abstract AddressTable getAddressTable();

    //mo44360j
    public AddressTable getSelfAddressTable() {
        return this.addressTable;
    }

    //mo42790h
    public Settings getNewSetting() {
        return new Settings(this);
    }

    //mo42791i
    //915 mo43099i
    public Settings getSetting() {
        return this.settings;
    }

    //mo44363n
    public HDAccountTable getSelfHDAccountTable() {
        return this.hdAccountTable;
    }

    public PersonalDB(@NonNull Wallet wallet, @NonNull String prefix, @NonNull String name, int version) {
        super(wallet, prefix, name, version);
    }

    public PersonalDB(@NonNull Wallet wallet, @NonNull String str, @NonNull String str2) {
        super(wallet, str, str2, 4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int oldVersion, int newVersion) {
        super.onUpgrade(sQLiteDatabase, oldVersion, newVersion);
        switch (newVersion){
            case 7:
                if(oldVersion>=7) break;
                sQLiteDatabase.execSQL("alter table address add column full_pub BLOB");
                SharedPreferencesUtil.getSharedPreferencesUtil().putBooleanValue("needFixFullPub"+ApplicationLoader.vCashCore.getAppWalletPath(),true, ApplicationLoader.applicationContext);
        }
    }

    //mo44366q
    public SubAddressTable getSubAddressTable() {
        return this.subAddressTable;
    }

    //m12692u
    private SubAddressTable getNewSubAddressTable() {
        return new SubAddressTable(this);
    }

    //mo42538t
    private OriginNodeTable getNewOriginNodeTable() {
        return new OriginNodeTable(this);
    }

    //m12693v
    private VidGroupTable getNewVidGroupTable() {
        return new VidGroupTable(this);
    }

    //mo44361k
    public VidGroupTable getSelfVidGroupTable() {
        return this.vidGroupTable;
    }


    //mo42536m
    public void InitAndAddtoDbVector() {
        if (this.settings == null) {
            this.settings = getNewSetting();
        }
        addAbstractTableByName(this.settings, "settings");
        this.hdAccountTable = getHDAccountTable();
        addAbstractTableByName(this.hdAccountTable, "hd_account");
        this.addressTable = getAddressTable();
        addAbstractTableByName(this.addressTable, "address");
        //TODO 其他用户数据表
        this.vidGroupTable = getNewVidGroupTable();
        addAbstractTableByName(this.vidGroupTable, "vid_group");
        this.originNodeTable = getNewOriginNodeTable();
        addAbstractTableByName(this.originNodeTable, "origin_node");
//        this.f13152g = m12694w();
//        addAbstractTableByName(this.f13152g, "ad_tag");
        this.subAddressTable = getNewSubAddressTable();
        addAbstractTableByName(this.subAddressTable, "sub_address");
//        this.f13154i = mo42537s();
//        addAbstractTableByName(this.f13154i, "address_bak_record");
    }


    //mo44364o
    public OriginNodeTable getOriginNodeTable() {
        return this.originNodeTable;
    }
}
