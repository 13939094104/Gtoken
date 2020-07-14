package vdsMain.config;

import androidx.annotation.NonNull;
import bitcoin.UInt256;
import net.bither.bitherj.utils.Utils;
import vdsMain.wallet.ChainParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import io.realm.SyncCredentials.IdentityProvider;

public class WalletConfigure {

    /* renamed from: a */
    //f13030a
    //915 f13696a
    public ConfigFile configFile;

    //mo44130a
    //915 mo44497a
    public synchronized void initConfigFile(@NonNull ChainParams chainParams, @NonNull String str) {
        this.configFile = new ConfigFile();
        this.configFile.excuteConfigFile(new File(str));
        ConfigFile file = new ConfigFile(str);
        if (file.getBooleanValue("regtest", false)) {
            chainParams.initRegtestParams();
        } else if (file.getBooleanValue("testnet", false)) {
            chainParams.initTestParams();
        } else {
            chainParams.initMainParams();
        }
        chainParams.protocalVersion=(file.getIntergerValue("protocol_version", chainParams.protocalVersion));
        chainParams.peerPort=(file.getIntergerValue("peer_port", chainParams.peerPort));
        chainParams.getClass();
        chainParams.peerCount=(file.getIntergerValue("peer_count", 5));
        chainParams.getClass();
        chainParams.setPeerTimeout(file.getLongValue("peer_timeout", 60000));
        chainParams.getClass();
        chainParams.setPingTime(file.getLongValue("ping_time", 30000));
        chainParams.setTestNetAddress(file.getStringValue("test_net_addr", (String) null));
        chainParams.setImPort(file.getIntergerValue("im_port", 55555));
        String genesisBlock = file.getStringValue("genesis_block", (String) null);
        if (!(genesisBlock == null || genesisBlock.length() == 0)) {
            chainParams.hash=new UInt256(Utils.getReverseStringBytes(genesisBlock), true);
        }
        String genesisMerkelRoot = file.getStringValue("genesis_merkel_root", (String) null);
        if (!(genesisMerkelRoot == null || genesisMerkelRoot.length() == 0)) {
            chainParams.setGenesisMerkelRootHash(new UInt256(Utils.getReverseStringBytes(genesisMerkelRoot), true));
        }
        chainParams.setPassword(file.getStringValue(IdentityProvider.USERNAME_PASSWORD, (String) null));
        HashMap<String, String[]> b = file.splitAndAddAddressToMap("test_address");
        if (b != null && !b.isEmpty()) {
            chainParams.setTestAddressList(new ArrayList());
            for (String[] add : b.values()) {
                chainParams.getTestAddressList().add(add);
            }
        }
    }
}
