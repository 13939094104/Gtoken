package vdsMain.transaction;

import androidx.annotation.NonNull;
import org.spongycastle.asn1.cmc.BodyPartID;
import vdsMain.wallet.Wallet;
import vdsMain.model.Address;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConvertedTxIns extends ArrayList<TxIn> {

    //f12956a
    public long sumVinsValue = 0;

    //f12957b
    public String mineAddressString;

    //f12958c
    public TxOut txOut;

    //f12959d
    public Wallet wallet;

    public ConvertedTxIns(@NonNull Wallet wallet) {
        this.wallet = wallet;
    }

    //mo43995a
    public void addTxOutList(List<TxOut> list) {
        if (list != null) {
            String str = this.mineAddressString;
            long a = (str == null || str == null) ? -1 : this.txOut.getSatoshi();
            for (TxOut txOut : list) {
                TxIn txIn = new TxIn(txOut);
                txIn.setSequence(BodyPartID.bodyIdMax);
                add(txIn);
                Address address = this.wallet.getAddressFromUsingAddressMap((CharSequence) txOut.getAddress());
                if (address != null) {
                    long value = txOut.getSatoshi();
                    if (a == -1 || value < a) {
                        this.mineAddressString = address.getAddressString(this.wallet.getBlockChainType());
                        this.txOut = txOut;
                        a = value;
                    }
                    this.sumVinsValue += value;
                }
            }
        }
    }

    public void addContractTxOutList(List<TxOut> list) {
        if (list != null) {
            String str = this.mineAddressString;
            long a = (str == null || str == null) ? -1 : this.txOut.getSatoshi();
            for (TxOut txOut : list) {
                TxIn txIn = new TxIn(txOut);
                txIn.setSequence(BodyPartID.bodyIdMax-1);
                add(txIn);
                Address address = this.wallet.getAddressFromUsingAddressMap((CharSequence) txOut.getAddress());
                if (address != null) {
                    long value = txOut.getSatoshi();
                    if (a == -1 || value < a) {
                        this.mineAddressString = address.getAddressString(this.wallet.getBlockChainType());
                        this.txOut = txOut;
                        a = value;
                    }
                    this.sumVinsValue += value;
                }
            }
        }
    }

    public void clear() {
        super.clear();
        this.sumVinsValue = 0;
        this.mineAddressString = null;
        this.txOut = null;
    }

    public boolean addAll(Collection<? extends TxIn> collection) {
        long j = -1;
        for (TxIn dlVar : collection) {
            String str = this.mineAddressString;
            if (!(str == null || str == null)) {
                j = this.txOut.getSatoshi();
            }
            Address b = this.wallet.getAddressByCTxDestinationFromArrayMap(dlVar.getCTxDestination());
            if (b != null) {
                long a = dlVar.getSatoshi();
                if (j == -1 || a < j) {
                    this.mineAddressString = b.getAddressString(this.wallet.getBlockChainType());
                    j = a;
                }
                this.sumVinsValue += a;
            }
        }
        return super.addAll(collection);
    }
}
