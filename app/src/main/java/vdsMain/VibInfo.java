package vdsMain;

import generic.io.StreamWriter;
import generic.serialized.SeriableData;
import vdsMain.block.VBlockHeader;
import vdsMain.transaction.CAmount;
import vdsMain.wallet.VWallet;

import java.util.ArrayList;
import java.util.List;

//bqn
public class VibInfo extends SeriableData {

    /* renamed from: a */
    long f12089a;

    /* renamed from: b */
    long f12090b;

    /* renamed from: c */
    List<Season> f12091c = new ArrayList();

    /* renamed from: d */
    private VWallet f12092d;

    /* renamed from: a */
    public double mo42857a(int i) {
        return (double) i;
    }

    public void writeSerialData(StreamWriter streamWriter) {
    }

    public VibInfo(VWallet bir) {
        this.f12092d = bir;
    }

    /* renamed from: a */
    public Season mo42860a() {
        if (this.f12091c.size() == 0) {
            return null;
        }
        int size = this.f12091c.size() - 1;
        if (size < 0) {
            size = 0;
        }
        return (Season) this.f12091c.get(size);
    }

    /* renamed from: b */
    public List<Season> mo42863b() {
        return this.f12091c;
    }

    /* renamed from: c */
    public long mo42864c() {
        return this.f12089a;
    }

    /* renamed from: d */
    public long mo42866d() {
        if (this.f12092d.getSelfBlockChainModel().getNewestBlockHeader() == null) {
            return 0;
        }
        return ((VBlockHeader) this.f12092d.getSelfBlockChainModel().getNewestBlockHeader()).mo42501r();
    }

    /* renamed from: e */
    public double mo42868e() {
        return mo42857a(mo42871g());
    }

    /* renamed from: f */
    public long mo42870f() {
        return mo42866d() - mo42859a((long) (mo42871g() - 1));
    }

    /* renamed from: g */
    public int mo42871g() {
        double d = (double) mo42866d();
        int floor = (int) Math.floor(Math.sqrt(d / ((double) CAmount.toSatoshiLong(Double.valueOf(5.0d)))));
        long j = (long) floor;
        if (d < ((double) mo42859a(j))) {
            while (floor >= 0) {
                long a = mo42859a((long) (floor - 1));
                long a2 = mo42859a((long) floor);
                if (((double) a) < d && d <= ((double) a2)) {
                    return floor;
                }
                floor--;
            }
            return -1;
        } else if (d <= ((double) mo42859a(j))) {
            return floor;
        } else {
            while (true) {
                floor++;
                long a3 = mo42859a((long) (floor - 1));
                long a4 = mo42859a((long) floor);
                if (((double) a3) < d && d <= ((double) a4)) {
                    return floor;
                }
            }
        }
    }

    /* renamed from: a */
    public long mo42859a(long j) {
        if (j < 0) {
            return 0;
        }
        return CAmount.toSatoshiLong(Double.valueOf(((double) ((j * (1 + j)) * 10)) / 2.0d));
    }

    /* renamed from: b */
    public long mo42862b(long j) {
        if (j < 0) {
            return 0;
        }
        return CAmount.toSatoshiLong(Double.valueOf(((double) j) * 10.0d));
    }

    /* renamed from: a */
    public long mo42858a(double d) {
        return mo42865c(CAmount.toSatoshiLong(Double.valueOf(d)));
    }

    /* renamed from: c */
    public long mo42865c(long j) {
        double d = (double) j;
        int g = mo42871g();
        double f = (double) mo42870f();
        double e = mo42868e();
        double d2 = d - (f / e);
        if (d2 <= 0.0d || g == 0) {
            return (long) (d * e);
        }
        long a = CAmount.toSatoshiLong(Double.valueOf(10.0d));
        int i = g - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            double d3 = (double) a;
            double d4 = d2 - d3;
            if (d4 <= 0.0d || i == 0) {
                f += d2 * mo42857a(i);
            } else {
                f += d3 * mo42857a(i);
                i--;
                d2 = d4;
            }
        }
        f += d2 * mo42857a(i);
        return (long) f;
    }

    /* renamed from: b */
    public long mo42861b(double d) {
        return mo42867d(CAmount.toSatoshiLong(Double.valueOf(d)));
    }

    /* renamed from: d */
    public long mo42867d(long j) {
        double d = (double) j;
        int g = mo42871g();
        double f = (double) mo42870f();
        double e = mo42868e();
        if (d < f || g == 0) {
            return (long) (d / e);
        }
        double d2 = f / e;
        double d3 = d - f;
        long a = CAmount.toSatoshiLong(Double.valueOf(10.0d));
        int i = g - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            double d4 = (double) a;
            if (d3 - (mo42857a(i) * d4) <= 0.0d || i == 0) {
                d2 += d3 / mo42857a(i);
            } else {
                d2 += d4;
                d3 -= d4 * mo42857a(i);
                i--;
            }
        }
        d2 += d3 / mo42857a(i);
        return (long) d2;
    }

    /* renamed from: h */
    public List<String> mo42872h() {
        ArrayList arrayList = new ArrayList();
        int g = mo42871g();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(mo42870f())));
        arrayList.add(sb.toString());
        for (int i = 1; i < 20; i++) {
            int i2 = g - i;
            if (i2 <= 0) {
                break;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("");
            sb2.append(CAmount.toDecimalSatoshiDouble(Long.valueOf(mo42862b((long) i2))).intValue());
            arrayList.add(sb2.toString());
        }
        return arrayList;
    }

    /* renamed from: i */
    public List<Double> mo42873i() {
        ArrayList arrayList = new ArrayList();
        int g = mo42871g();
        for (int i = 0; i < 20; i++) {
            int i2 = g - i;
            if (i2 <= 0) {
                break;
            }
            arrayList.add(Double.valueOf(mo42857a(i2)));
        }
        return arrayList;
    }

    public void onDecodeSerialData() {
        this.f12089a = readInt64();
        this.f12090b = readInt64();
        this.f12091c = new ArrayList();
        int readInt32 = readInt32();
        for (int i = 0; i < readInt32; i++) {
            Season bql = new Season();
            bql.decodeSerialStream(getTempInput());
            this.f12091c.add(bql);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof VibInfo)) {
            return false;
        }
        VibInfo bqn = (VibInfo) obj;
        if (this.f12091c.size() != bqn.mo42863b().size()) {
            return false;
        }
        for (int i = 0; i < this.f12091c.size(); i++) {
            if (!((Season) this.f12091c.get(i)).equals(bqn.mo42863b().get(i))) {
                return false;
            }
        }
        return true;
    }
}
