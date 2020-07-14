package vdsMain.model;

import androidx.annotation.NonNull;
import vdsMain.*;
import vdsMain.block.BlockHeader;
import vdsMain.block.ChainSyncStatus;
import vdsMain.message.GetVibMessage;
import vdsMain.observer.SimpleWalletObserver;
import vdsMain.observer.WalletObserver;
import vdsMain.peer.Peer;
import vdsMain.wallet.VWallet;
import vdsMain.wallet.Wallet;

import java.io.IOException;
import java.util.*;

//bpj
public class VibModel extends Model {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public long f12022a = 0;
    /* access modifiers changed from: private */

    //f12023b
    public Map<String, VibInfo> peerKeyToVibInfoMap = new HashMap();

    /* renamed from: c */
    private List<QueneItem> f12024c;

    /* renamed from: c */
    public void mo42700c() {
    }

    /* access modifiers changed from: protected */
    /* renamed from: g */
    public void initAllDataFromDb() {
    }

    public VibModel(@NonNull Wallet izVar) {
        super(izVar);
    }

    //mo42697a
    public void addVibInfoToMap(Peer peer, VibInfo vibInfo) {
        this.peerKeyToVibInfoMap.put(peer.getAddressInfoKeyHex(), vibInfo);
    }

    //mo42695a
    public VibInfo getVibInfo() {
        int i;
        if (this.peerKeyToVibInfoMap.size() == 0) {
            return null;
        }
        ArrayList<VibInfo> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = new ArrayList(this.peerKeyToVibInfoMap.keySet()).iterator();
        while (true) {
            i = 0;
            if (!it.hasNext()) {
                break;
            }
            VibInfo vibInfo = (VibInfo) this.peerKeyToVibInfoMap.get((String) it.next());
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList.size()) {
                    break;
                } else if (vibInfo.equals(arrayList.get(i2))) {
                    int intValue = ((Integer) arrayList2.get(i2)).intValue() + 1;
                    arrayList2.remove(i2);
                    arrayList2.add(i2, Integer.valueOf(intValue));
                    i = 1;
                    break;
                } else {
                    i2++;
                }
            }
            if (i == 0) {
                arrayList.add(vibInfo);
                arrayList2.add(Integer.valueOf(1));
            }
        }
        int i3 = 0;
        while (i < arrayList2.size()) {
            if (((Integer) arrayList2.get(i)).intValue() > ((Integer) arrayList2.get(i3)).intValue()) {
                i3 = i;
            }
            i++;
        }
        for (VibInfo vibInfo : arrayList) {
            if (vibInfo.mo42860a().mo42849a() == 15 && mo42701d() == 15 && vibInfo.mo42860a().equals(mo42694a(15))) {
                return vibInfo;
            }
        }
        if (arrayList2.size() == 0 || ((Integer) arrayList2.get(i3)).intValue() < 4 || ((VibInfo) arrayList.get(i3)).mo42860a().mo42849a() == 15 || mo42701d() == 15) {
            return null;
        }
        return (VibInfo) arrayList.get(i3);
    }

    /* renamed from: a */
    public Season mo42694a(int i) {
        Season bql = new Season();
        bql.mo42850a(i);
        bql.mo42851a(0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new RankItem(1, "1BE2CWybfFW9bQ6WGjyofMYuzvAH2ppKo", 0.052561085425006246d));
        arrayList.add(new RankItem(2, "12Y5jMLRgk2gDX38ZGm8P6PakEL6ZawoBx", 0.05244001180467798d));
        arrayList.add(new RankItem(3, "1uw4kcTtMqdTp9buuR7Dh8N8P34LpDZte", 0.05103253096836195d));
        arrayList.add(new RankItem(4, "1PiN84VCvYzWEFENsbUmRcMEgbWa2REC1A", 0.05083578633532852d));
        arrayList.add(new RankItem(5, "1JgeYU33T8Vs6VXJsPxawEwzoWLxDyC26", 0.05053688583514313d));
        arrayList.add(new RankItem(6, "1XrbcWBdLLAN2UQoy1T7WjoX3be4BYxVi", 0.04992395063223131d));
        arrayList.add(new RankItem(7, "1138S6WiWBqLvqvfJgWh9mALozcxQJojDc", 0.049829361866349856d));
        arrayList.add(new RankItem(8, "14Tte3XUoGzXKpqM2HSsrVCEoK49xhYFSC", 0.04888347420753532d));
        arrayList.add(new RankItem(9, "13hVK1yk9JzGrFLkwAz2jAN1uz5Knn8YRA", 0.04859214080862044d));
        arrayList.add(new RankItem(10, "15AXmg8btPyTtWtzDv37vhXBcJjsxShz5G", 0.045364772116745236d));
        arrayList.add(new RankItem(0, "14NySipPLpRGRsXM83rwBWqmXfivUAhak5", 0.025d));
        arrayList.add(new RankItem(0, "15q6t1KXtzQ3N7RiqeCPZZvAwWzc6YueVL", 0.025d));
        arrayList.add(new RankItem(0, "15tCU44HjTVJHHKS8Yv8cdxVVhP8oyGJ52", 0.025d));
        arrayList.add(new RankItem(0, "15zxZMriDq7XWk58nSB3ZRAXsKecYKiewL", 0.025d));
        arrayList.add(new RankItem(0, "17STmBnibKMr6FbcYLWJE4Bn84xufURFpp", 0.025d));
        arrayList.add(new RankItem(0, "17iVXkmpX2MMPrpXrJFLHHgPyHHKMCWY8m", 0.025d));
        arrayList.add(new RankItem(0, "17vo6xUrAVtLjkS2KEB9qfyADw1PpMsFjX", 0.025d));
        arrayList.add(new RankItem(0, "1855TiuNi19HJ9GsJhfMTVbTPzD6GF5DTg", 0.025d));
        arrayList.add(new RankItem(0, "18EfggSG1xyj2D3dMPdcZQ1Vw3CfvPkE8a", 0.025d));
        arrayList.add(new RankItem(0, "18ibMrGxHD7td6kDbxatRjh4WD1NxXLFvM", 0.025d));
        arrayList.add(new RankItem(0, "1945pqwhDwY9f1LrVaHBUQxFiVAJBwMzdK", 0.025d));
        arrayList.add(new RankItem(0, "1ALPX3rK2HvxCPW72Z2GXb8sSb95E7udmk", 0.025d));
        arrayList.add(new RankItem(0, "1BeDd6zSnk58NjDpK6DLPmTB5PNauAQE4y", 0.025d));
        arrayList.add(new RankItem(0, "1Bek4mfx7bcM4WXyuJLjpYaehxwavQX4Rj", 0.025d));
        arrayList.add(new RankItem(0, "1CULXgPy9UrGt7VNrfi28r6r7Ni5yAV8fF", 0.025d));
        arrayList.add(new RankItem(0, "1DTNMpUarRg6dLVzHN5w6s7sheWsZRUfXT", 0.025d));
        arrayList.add(new RankItem(0, "1DYD9HgSwH5mRbkFtCwCLxKdfpVEM61Pkj", 0.025d));
        arrayList.add(new RankItem(0, "1DsrQh7aUZWaQfQJLkAHkozjXhawr93eQF", 0.025d));
        arrayList.add(new RankItem(0, "1E8wP61P1REkGwW38CLR3YG8ZvNTW5ZNZN", 0.025d));
        arrayList.add(new RankItem(0, "1EFP5hDzWtcnEH4jFijuNBZkjVLjGmkRKG", 0.025d));
        bql.mo42852a((List<RankItem>) arrayList);
        return bql;
    }

    /* renamed from: a */
    public void mo42696a(List<QueneItem> list) {
        this.f12024c = list;
    }

    /* renamed from: a */
    public int mo42693a(String str) {
        List<QueneItem> list = this.f12024c;
        int i = 0;
        if (list == null) {
            return 0;
        }
        int size = list.size() - 1;
        while (size >= 0 && !str.equalsIgnoreCase(((QueneItem) this.f12024c.get(size)).f12079b)) {
            i++;
            size--;
        }
        return i;
    }

    /* renamed from: b */
    public long mo42698b(String str) {
        long j = 0;
        if (this.f12024c == null || getVibInfo() == null) {
            return 0;
        }
        int size = this.f12024c.size() - 1;
        while (size >= 0 && !str.equalsIgnoreCase(((QueneItem) this.f12024c.get(size)).f12079b)) {
            List<Pair<CTxDestination, Long>> list = ((QueneItem) this.f12024c.get(size)).f12081d.f12088a;
            if (list != null) {
                for (Pair gsVar : list) {
                    j += ((Long) gsVar.value).longValue();
                }
            }
            size--;
        }
        return getVibInfo().mo42865c(j);
    }

    /* renamed from: b */
    public void mo42699b() {
        this.wallet.addWalletObserver((WalletObserver) new SimpleWalletObserver() {
            /* renamed from: a */
            public void onBlockNoUpdate(Wallet izVar, ChainSyncStatus ioVar, BlockHeader jtVar, int i, ChainSyncStatus ioVar2) {
                if ((izVar instanceof VWallet) && ioVar == ChainSyncStatus.SYNCHED && VibModel.this.f12022a < VibModel.this.wallet.getCurrentBlockNo()) {
                    VibModel bpj = VibModel.this;
                    bpj.f12022a = bpj.wallet.getCurrentBlockNo();
                    GetVibMessage bls = new GetVibMessage(VibModel.this.wallet);
                    Iterator it = VibModel.this.wallet.getSelfPeerManager().getNormalPeerVector().iterator();
                    while (it.hasNext()) {
                        Peer lhVar = (Peer) it.next();
                        if (!VibModel.this.peerKeyToVibInfoMap.containsKey(lhVar.getAddressInfoKeyHex()) && lhVar.getPeerStatus() == Peer.PeerStatus.Connected) {
                            try {
                                lhVar.addMessageToLinkedList(bls);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    /* renamed from: d */
    public int mo42701d() {
        long R = this.wallet.getCurrentBlockNo();
        VParams bjh = (VParams) this.wallet.getChainParams().mo43961n();
        if (R < ((long) bjh.nBlockCountOf1stSeason)) {
            return 1;
        }
        return (int) (((R - ((long) bjh.nBlockCountOf1stSeason)) / ((long) bjh.nBlockCountOfSeason)) + 2);
    }
}
