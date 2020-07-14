package vdsMain.wallet;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import com.vc.libcommon.exception.AddressFormatException;
import vdsMain.AddressType;
import vdsMain.CPrivateKeyInterface;
import vdsMain.CTxDestination;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.message.MessageFactory;
import vdsMain.peer.BloomFilterInterface;
import vdsMain.peer.BloomUpdate;
import vdsMain.peer.Peer;
import vdsMain.table.WalletTable;
import vdsMain.model.Address;
import vdsMain.transaction.*;

import java.util.List;

public  abstract class WalletHelper {

    //f13031a
    protected Wallet wallet = null;

    //f13032b
    protected MessageFactory messageFactory = null;

    public WalletHelper(@NonNull Wallet izVar) {
        this.wallet = izVar;
        this.messageFactory = getMessageFactory();
    }

    //mo42403a
    public abstract Transaction getNewTransaction();

    public abstract Transaction mo42404a(CMutableTransaction dfVar);

    public abstract Transaction mo42405a(Transaction transaction);

    public abstract List<Address> getAddressListFromPrivateKey(CPrivateKeyInterface kmVar, String str);

    //mo42407a
    public abstract Address getAddress(WalletTable kjVar, AddressType... jlVarArr) throws AddressFormatException;

    public abstract Address mo42408a(CPrivateKeyInterface kmVar, CharSequence charSequence);

    public abstract Address mo42409a(CTxDestination oVar);

    public abstract BlockInfo mo42410a(int i, long j, @NonNull UInt256 uInt256, @NonNull UInt256 uInt2562, @NonNull UInt256 uInt2563, long j2);

    //mo42411a
    public abstract BlockInfo getBlockInfoFromBlockHeader(BlockHeader jtVar);

    //910 mo42458a
    //mo42412a
    public abstract BloomFilterInterface getNewBloomFilter(int nElements, double nFPRate, BloomUpdate bloomUpdate);

    //mo42413a
    public abstract Peer getNewPeer(Wallet izVar);

    public abstract Transaction getNewVCWalletTx(Transaction transaction);

    //mo42416b
    public abstract BlockInfo getNewBlockInfo();

    //mo42417c
    public abstract BlockHeader getNewBlockHeader();

    //mo42418d
    public abstract MessageFactory getMessageFactory();

    public abstract OfflineTransaction mo42419e();

    public abstract Class<?> mo42420f();

    //mo44132g
    public MessageFactory getSelfMessageFactory() {
        return this.messageFactory;
    }

    public Transaction mo44131a(TransactionInteface transactionInteface) {
        if (transactionInteface instanceof Transaction) {
            return mo42405a((Transaction) transactionInteface);
        }
        if (transactionInteface instanceof CMutableTransaction) {
            return mo42404a((CMutableTransaction) transactionInteface);
        }
        return null;
    }

    //mo42421h
    public TxOut getNewTxOut() {
        return new TxOut(this.wallet);
    }

    //mo42422i
    public COutPoint getNewCOutPoint() {
        return new COutPoint();
    }

    //mo42402a
    public COutPoint getNewCOutPointByTxidAndIndex(UInt256 uInt256, int i) {
        return new COutPoint(uInt256, i);
    }


}