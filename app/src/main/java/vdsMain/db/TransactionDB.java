package vdsMain.db;

import androidx.annotation.NonNull;
import vdsMain.table.BlockTable;
import vdsMain.table.RejectTxTable;
import vdsMain.table.TransactionTable;
import vdsMain.table.TxMarkTable;
import vdsMain.wallet.Wallet;

public abstract class TransactionDB extends WalletDB{
    //f13157a
    protected TransactionTable transactionTable;

    //f13158b
    protected TransactionTable relayedTxTable;

    protected TransactionTable relayedVxdTxTable;

    //f13159c
    protected TransactionTable invalidTxTable;

    //f13160d
    protected BlockTable blockTable;

    //f13161e
    private RejectTxTable rejectTxTable;

    //f13162g
    private TxMarkTable txMarkTable;

    /* access modifiers changed from: protected */
    //mo41134a
    public abstract TransactionTable getTransactionTable();

    /* access modifiers changed from: protected */
    //mo41135b
    public abstract BlockTable getBlockTable();

    public TransactionDB(@NonNull Wallet izVar, @NonNull String str, @NonNull String str2) {
        super(izVar, str, str2, 3);
    }

    public TransactionDB(@NonNull Wallet izVar, @NonNull String str, @NonNull String str2, int i) {
        super(izVar, str, str2, i);
    }

    //mo42539h
    public void InitAndAddtoDbVector() {
        this.transactionTable = getTransactionTable();
        this.blockTable = getBlockTable();
        this.relayedTxTable = getTransactionTable();
        this.relayedVxdTxTable=getTransactionTable();
        this.invalidTxTable = getTransactionTable();
        this.rejectTxTable = getRejectTxTable();
        this.txMarkTable = getTxMarkTable();
        addAbstractTableByName(this.blockTable, "blocks");
        addAbstractTableByName(this.transactionTable, "transactions");
        addAbstractTableByName(this.relayedTxTable, "rtx");
        addAbstractTableByName(this.relayedVxdTxTable,"vxd_rtx");
        addAbstractTableByName(this.invalidTxTable, "InvalidTx");
        addAbstractTableByName(this.rejectTxTable, "rejectTx");
        addAbstractTableByName(this.txMarkTable, "tx_mark");
    }

    //mo42540o
    private TxMarkTable getTxMarkTable() {
        return new TxMarkTable(this);
    }

    //mo42542p
    private RejectTxTable getRejectTxTable() {
        return new RejectTxTable(this);
    }

    //mo44370i
    public TransactionTable getSelfTransactionTable() {
        return this.transactionTable;
    }

    //mo44371j
    public BlockTable getSelfBlockTable() {
        return this.blockTable;
    }

    //mo44372k
    public TransactionTable getSelfRelayedTxTable() {
        return this.relayedTxTable;
    }

    public TransactionTable getSelfRelayedVxdTable() { return this.relayedVxdTxTable;}

    //mo44373l
    public TransactionTable getSelfInvaildTxTable() {
        return this.invalidTxTable;
    }



    //mo44374m
    public RejectTxTable getSelfRejectTxTable() {
        return this.rejectTxTable;
    }

    //mo44375n
    public TxMarkTable getSelfTxMarkTable() {
        return this.txMarkTable;
    }
}
