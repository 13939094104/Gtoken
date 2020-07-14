package vdsMain;

import android.util.Pair;

//bjh
public class VParams extends  ZParams {

    /* renamed from: A */
    public int f11732A;

    /* renamed from: B */
    public int f11733B;

    /* renamed from: C */
    public int f11734C;

    //f11735D
    public long nMinerConfirmationWindow;

    //f11736E
    public short nBlockCountPerDay;

    //f11737F
    public int nBlockCountOfWeek;

    //f11738G
    public int nBlockCountOfSeason;

    //f11739H
    public int nBlockCountOf1stSeason;

    //f11740I
    public short nWeekCount1stSeason;

    //f11741J
    public short nWeekCountOfSeason;

    /* renamed from: K */
    public short f11742K;

    /* renamed from: L */
    public short f11743L;

    /* renamed from: n */
    public boolean f11744n = true;

    /* renamed from: o */
    public boolean f11745o = false;

    /* renamed from: p */
    public int f11746p;

    /* renamed from: q */
    public int f11747q;

    /* renamed from: r */
    public int f11748r;

    /* renamed from: s */
    public int f11749s;

    /* renamed from: t */
    public int f11750t;

    /* renamed from: u */
    public int f11751u;

    /* renamed from: v */
    public int f11752v;

    /* renamed from: w */
    public double f11753w;

    /* renamed from: x */
    public double f11754x;

    /* renamed from: y */
    public double f11755y;

    /* renamed from: z */
    public int f11756z;

    //mo42506a
    public int getCurSeason(int i) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate block no ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        } else if (i >= this.nBlockCountOf1stSeason) {
            return ((i - this.nBlockCountOf1stSeason) / this.nBlockCountOfSeason) + 2;
        } else {
            return 1;
        }
    }

    //mo42507b
    public Pair<Integer, Integer> getSeasonInterval(int season) {
        if (season < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalidate season ");
            sb.append(season);
            throw new IllegalArgumentException(sb.toString());
        } else if (season < 2) {
            return new Pair<>(Integer.valueOf(0), Integer.valueOf(this.nBlockCountOf1stSeason - 1));
        } else {
            int startBlockNo = this.nBlockCountOf1stSeason + ((season - 2) * this.nBlockCountOfSeason);
            return new Pair<>(Integer.valueOf(startBlockNo), Integer.valueOf((startBlockNo + this.nBlockCountOfSeason) - 1));
        }
    }
}
