package vdsMain.tool;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.vtoken.vdsecology.vcash.VCashCore;
import com.vtoken.application.ApplicationLoader;

import vdsMain.BLOCK_CHAIN_TYPE;
import vdsMain.peer.Peer;

import java.util.ArrayList;
import java.util.Iterator;

//bbq
public class PeerUtils {
    //m6971a
    //915 m7206a
    public static void checkPeersStatus(Peer lhVar, Peer.PeerStatus aVar) {
        boolean hasPeer=false;
        VCashCore vCashCore = ApplicationLoader.getVcashCore();
        ApplicationLoader applicationLoader = ApplicationLoader.getSingleApplicationContext();
        ArrayList normalVcashPeerList = new ArrayList(vCashCore.getNormalPeerList(BLOCK_CHAIN_TYPE.VCASH));
        if (!normalVcashPeerList.isEmpty()) {
            Iterator iterator = normalVcashPeerList.iterator();
            while (true) {
                if (iterator.hasNext()) {
                    if (((Peer) iterator.next()).getPeerStatus() == Peer.PeerStatus.Connected) {
                        hasPeer = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            hasPeer = false;
        }
        applicationLoader.setVcashNetworkIsNormal(hasPeer);
        LocalBroadcastManager.getInstance(ApplicationLoader.getSingleApplicationContext()).sendBroadcast(new Intent("com.peer.PeerBroadcastReceiver.Disconnect"));
    }
}
