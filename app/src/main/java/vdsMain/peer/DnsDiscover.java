package vdsMain.peer;

import vdsMain.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class DnsDiscover {

    /* renamed from: a */
    private String[] f12752a = null;

    /* renamed from: a */
    public synchronized void mo43651a(String[] strArr) {
        this.f12752a = strArr;
    }

    /* renamed from: a */
    public InetAddress[] mo43652a(long j) {
        InetAddress[] inetAddressArr;
        String[] strArr = this.f12752a;
        if (strArr != null) {
            ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(strArr.length);
            ArrayList arrayList = new ArrayList();
            try {
                ArrayList arrayList2 = new ArrayList();
                for (final String str : strArr) {
                    arrayList2.add(new Callable<InetAddress[]>() {
                        /* renamed from: a */
                        public InetAddress[] call() throws UnknownHostException {
                            return InetAddress.getAllByName(str);
                        }
                    });
                }
                List invokeAll = newFixedThreadPool.invokeAll(arrayList2, j, TimeUnit.SECONDS);
                for (int i = 0; i < invokeAll.size(); i++) {
                    Future future = (Future) invokeAll.get(i);
                    if (future.isCancelled()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("discover seed timed out: ");
                        sb.append(strArr[i]);
                        Log.logObjectWarning((Object) this, sb.toString());
                    } else {
                        try {
                            for (InetAddress inetAddress : (InetAddress[]) future.get()) {
                                if (inetAddress.getAddress().length <= 4) {
                                    arrayList.add(inetAddress);
                                }
                            }
                        } catch (ExecutionException e) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failed to look up DNS seeds from ");
                            sb2.append(strArr[i]);
                            Log.LogObjError((Object) this, sb2.toString(), (Throwable) e);
                        }
                    }
                }
                Collections.shuffle(arrayList);
                newFixedThreadPool.shutdownNow();
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                newFixedThreadPool.shutdown();
                throw th;
            }
            newFixedThreadPool.shutdown();
            return (InetAddress[]) arrayList.toArray(new InetAddress[arrayList.size()]);
        }
        throw new NullPointerException("discover dns for seed failed, please use setSeedServers first.");
    }
}
