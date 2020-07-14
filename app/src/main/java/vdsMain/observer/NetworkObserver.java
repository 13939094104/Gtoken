package vdsMain.observer;

import vdsMain.peer.Peer;
import vdsMain.peer.PeerManager;

public interface NetworkObserver {
    //mo39553a
    void onConnectStatusChange(Peer peer, Peer.PeerStatus peerStatus);

    //mo39554a
    void onPeerManagerStatusChange(PeerManager.PeerManagerStatus peerManagerStatus);

    //mo39555a
    void onMainPeerChange(PeerManager peerManager, Peer peer);
}