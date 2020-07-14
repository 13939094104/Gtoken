package vdsMain.tool;

import com.vtoken.vdsecology.vcash.VCashCore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.vtoken.application.model.AddressPack;
import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.ComplexBitcoinAddress;
import vdsMain.model.Address;


//bay
public class AddressPackUtil {
    public static ArrayList<AddressPack> m6840a(VCashCore vCashCore, Address address) {
        if (address == null) {
            return null;
        }
        ComplexBitcoinAddress complexBitcoinAddress = vCashCore.mo43828c(address);
        if (complexBitcoinAddress == null) {
            return null;
        }
        HashSet hashSet = complexBitcoinAddress.mo42494d();
        ArrayList<AddressPack> arrayList = new ArrayList<>();
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Address address1 = (Address) it.next();
            AddressPack addressPack = new AddressPack();
            addressPack.setVdsAddress(address1.getAddressString(BLOCK_CHAIN_TYPE.VCASH));
            addressPack.setBtcAddress(address1.getAddressString(BLOCK_CHAIN_TYPE.BITCOIN));
            if (addressPack.getVdsAddress().equals(address.getAddressString(BLOCK_CHAIN_TYPE.VCASH))) {
                addressPack.setChecked(true);
            } else {
                addressPack.setChecked(false);
            }
            arrayList.add(addressPack);
        }
        return arrayList;
    }
}
