package vdsMain.wallet;

import androidx.annotation.NonNull;

import bitcoin.CKey;
import bitcoin.UInt256;
import com.vc.libcommon.exception.AddressFormatException;
import vdsMain.*;
import vdsMain.block.BlockHeader;
import vdsMain.block.BlockInfo;
import vdsMain.block.VBlockHeader;
import vdsMain.block.VBlockInfo;
import vdsMain.message.BloomFilter;
import vdsMain.message.MessageFactory;
import vdsMain.message.VMessageFactory;
import vdsMain.model.Address;
import vdsMain.model.BitCoinAddress;
import vdsMain.model.BitCoinVAddress;
import vdsMain.model.VAnonymousAddress;
import vdsMain.peer.BloomFilterInterface;
import vdsMain.peer.BloomUpdate;
import vdsMain.peer.Peer;
import vdsMain.peer.VPeer;
import vdsMain.table.WalletTable;
import vdsMain.transaction.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//bis
public class VWalletHelper extends WalletHelper {

    public VWalletHelper(@NonNull Wallet izVar) {
        super(izVar);
    }

    //mo42403a
    public Transaction getNewTransaction() {
        new VTransaction(this.wallet).setFlag(1);
        return new VTransaction(this.wallet);
    }

    public Transaction mo42404a(CMutableTransaction cMutableTransaction) {
        return new VTransaction(cMutableTransaction);
    }

    public Transaction mo42405a(Transaction transaction) {
        return new VTransaction(transaction);
    }

    //mo42406a
    public List<Address> getAddressListFromPrivateKey(CPrivateKeyInterface cPrivateKeyInterface, String str) {
        PrivateKeyType pivateKeyType = cPrivateKeyInterface.getPivateKeyType();
        ArrayList arrayList = new ArrayList();
        if (pivateKeyType == PrivateKeyType.SAPLING_EXTENED_SPEND_KEY) {
            arrayList.add(new VAnonymousAddress(this.wallet.getPersonalDB().getSelfAddressTable(), (SaplingExtendedSpendingKey) cPrivateKeyInterface, str));
            return arrayList;
        }
        arrayList.add(new BitCoinAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cPrivateKeyInterface.getPubKey().getByteArr(),((CKey)cPrivateKeyInterface).getUnCompressedPubKey().getByteArr() ,cPrivateKeyInterface.getCopyBytes(), str));
        if (cPrivateKeyInterface.IsCompressed()) {
            arrayList.add(new WitnessKeyHashAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cPrivateKeyInterface.getPubKey().getByteArr(), cPrivateKeyInterface.getCopyBytes(), str));
            arrayList.add(new WitnessKeyScriptAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cPrivateKeyInterface.getPubKey().getByteArr(), cPrivateKeyInterface.getCopyBytes(), str));
        }
        return arrayList;
    }

    public Address mo42409a(CTxDestination cTxDestination) {
        CTxDestinationType a = cTxDestination.getCTxDestinationType();
        if (a == CTxDestinationType.KEYID) {
            return new BitCoinAddress((WalletTable) this.wallet.getPersonalDB().getSelfAddressTable(), cTxDestination);
        }
        if (a == CTxDestinationType.SCRIPTID) {
            return new BitcoinMultiSigAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cTxDestination);
        }
        if (a == CTxDestinationType.WITNESS_V0_KEY_HASH) {
            return new WitnessKeyHashAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cTxDestination);
        }
        if (a == CTxDestinationType.WITNESS_V0_SCRIPT_HASH) {
            return new WitnessScriptHashAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cTxDestination);
        }
        if (a == CTxDestinationType.ANONYMOUST_KEY) {
            return new VAnonymousAddress(this.wallet.getPersonalDB().getSelfAddressTable(), cTxDestination);
        }
        return null;
    }

    public Address getAddress(WalletTable kjVar, AddressType... jlVarArr) throws AddressFormatException {
        if (jlVarArr.length == 0) {
            return new BitCoinAddress(kjVar);
        }
        switch (jlVarArr[0]) {
            case GENERAL:
                return new BitCoinAddress(kjVar);
            case IDENTITY:
                return new BitCoinVAddress(kjVar);
            case MULTISIG:
                return new BitcoinMultiSigAddress(kjVar);
            case ANONYMOUS:
                return new VAnonymousAddress(kjVar);
            case WITNESS_V0_KEY_HASH:
                return new WitnessKeyHashAddress(kjVar);
            case WITNESS_V0_KEY_HASH_SCRIPT:
                return new WitnessKeyScriptAddress(kjVar);
            case WITNESS_V0_SCRIPT_HASH:
                return new WitnessScriptHashAddress(kjVar);
            case WITNESS_V0_SCRIPT_HASH_SCRIPT:
                return new WitnessScriptHashScriptAddress(kjVar);
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Can not create address type ");
                sb.append(jlVarArr[0]);
                sb.append(" for wallet ");
                sb.append(this.wallet.getBlockChainType());
                throw new AddressFormatException(sb.toString());
        }
    }

    public Class<?> mo42420f() {
        return VTxOut.class;
    }

    //mo42414b
    public Transaction getNewVCWalletTx(Transaction transaction) {
        return new VCWalletTx(transaction);
    }

    public OfflineTransaction mo42419e() {
        return new VOfflineTransaction(this.wallet);
    }



    public MessageFactory getMessageFactory() {
        return new VMessageFactory(this.wallet);
    }

    //mo42413a
    public Peer getNewPeer(Wallet izVar) {
        return new VPeer(izVar);
    }



    public TxOut getNewTxOut() {
        return new VTxOut(this.wallet);
    }



    public BlockHeader getNewBlockHeader() {
        return new VBlockHeader(this.wallet);
    }

    //mo42416b
    public BlockInfo getNewBlockInfo() {
        return new VBlockInfo(new VBlockHeader(this.wallet));
    }

    //mo42411a
    public BlockInfo getBlockInfoFromBlockHeader(BlockHeader blockHeader) {
        return new VBlockInfo(new VBlockHeader(blockHeader));
    }

    public BlockHeader mo42415b(int i, long j, @NonNull UInt256 uInt256, @NonNull UInt256 uInt2562, @NonNull UInt256 uInt2563, long j2) {
        VBlockHeader bje = new VBlockHeader(this.wallet, i, j, uInt256, uInt2562, uInt2563, j2);
        return bje;
    }

    public BlockInfo mo42410a(int i, long j, @NonNull UInt256 uInt256, @NonNull UInt256 uInt2562, @NonNull UInt256 uInt2563, long j2) {
        return new VBlockInfo(mo42415b(i, j, uInt256, uInt2562, uInt2563, j2));
    }

    //mo42412a
    public BloomFilterInterface getNewBloomFilter(int nElements, double nFPRate, BloomUpdate bloomUpdate) {
        return new BloomFilter(this.wallet, nElements, nFPRate, new Random().nextLong(), bloomUpdate);
    }

    public COutPoint getNewCOutPoint() {
        return new VCOutPoint();
    }


    public Address mo42408a(CPrivateKeyInterface privateKeyInterface, CharSequence charSequence) {
        PrivateKeyType h = privateKeyInterface.getPivateKeyType();
        if (h == PrivateKeyType.BITCOIN) {
            return new BitCoinAddress(this.wallet.getPersonalDB().getSelfAddressTable(), privateKeyInterface.getPubKey().getByteArr(), privateKeyInterface.getCopyBytes(), charSequence);
        }
        if (h == PrivateKeyType.WITNESS_V0_KEY_HASH && privateKeyInterface.IsCompressed()) {
            return new WitnessKeyHashAddress(this.wallet.getPersonalDB().getSelfAddressTable(), privateKeyInterface.getPubKey().getByteArr(), privateKeyInterface.getCopyBytes(), charSequence);
        }
        if (h == PrivateKeyType.WITNESS_V0_KEY_HASH_SCRIPT && privateKeyInterface.IsCompressed()) {
            return new WitnessKeyScriptAddress(this.wallet.getPersonalDB().getSelfAddressTable(), privateKeyInterface.getPubKey().getByteArr(), privateKeyInterface.getCopyBytes(), charSequence);
        }
        if (privateKeyInterface.getPivateKeyType() == PrivateKeyType.SAPLING_EXTENED_SPEND_KEY) {
            return new VAnonymousAddress(this.wallet.getPersonalDB().getSelfAddressTable(), (SaplingExtendedSpendingKey) privateKeyInterface, charSequence);
        }
        return null;
    }
}
