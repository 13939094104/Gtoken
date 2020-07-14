package com.vtoken.application.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;
import com.facebook.stetho.server.http.HttpStatus;
import com.vtoken.application.R;

import java.util.ArrayList;
import java.util.List;

public class NumberPickerView extends View {

    /* renamed from: A */
    private float f4408A;

    /* renamed from: B */
    private boolean f4409B;

    /* renamed from: C */
    private int f4410C;

    /* renamed from: D */
    private int f4411D;

    //f4412E
    private int gapHeight;

    //f4413F
    private int itemHeight;

    //f4414G
    private int paintTextHeight;

    /* renamed from: H */
    private int f4415H;

    /* renamed from: I */
    private C2883b f4416I;

    /* renamed from: J */
    private C2882a f4417J;

    //f4418K
    private NumberPickerAdapter numberPickerAdapter;

    //f4419L
    private boolean fadingEdgeEnabled;

    /* renamed from: M */
    private int f4420M;

    //f4421N
    private int dividerColor;

    //f4422O
    private int dividerHeight;

    /* renamed from: P */
    private SoundPool f4423P;

    /* renamed from: Q */
    private int f4424Q;

    /* renamed from: a */
    private final int f4425a;

    /* renamed from: b */
    private final int f4426b;

    /* renamed from: c */
    private float f4427c;

    /* renamed from: d */
    private int f4428d;

    /* renamed from: e */
    private int f4429e;

    //f4430f
    private int defaultWheelItemCount;

    /* renamed from: g */
    private int f4431g;

    //f4432h
    private int wheelItemCount;

    /* renamed from: i */
    private int f4433i;

    //f4434j
    private int min;

    //f4435k
    private int max;

    /* renamed from: l */
    private int f4436l;

    /* renamed from: m */
    private int f4437m;

    /* renamed from: n */
    private List<Integer> f4438n;

    /* renamed from: o */
    private int f4439o;

    //f4440p
    private boolean wrapSelectorWheel;

    //f4441q
    private Paint paint;

    //f4442r
    private int selectedTextColor;

    //f4443s
    private int textColor;

    /* renamed from: t */
    private VelocityTracker f4444t;

    //f4445u
    private int textSize;

    //f4446v
    private String align;

    /* renamed from: w */
    private OverScroller f4447w;

    /* renamed from: x */
    private int f4448x;

    /* renamed from: y */
    private int f4449y;

    /* renamed from: z */
    private int f4450z;

    /* renamed from: v.dimensional.widget.NumberPickerView$1 */
    static /* synthetic */ class C28811 {

        /* renamed from: a */
        static final /* synthetic */ int[] f4451a = new int[Paint.Align.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            f4451a[Paint.Align.LEFT.ordinal()] = 1;
            f4451a[Paint.Align.CENTER.ordinal()] = 2;
            try {
                f4451a[Paint.Align.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: v.dimensional.widget.NumberPickerView$a */
    public interface C2882a {
        /* renamed from: a */
        void mo40107a(NumberPickerView numberPickerView, int i);
    }

    /* renamed from: v.dimensional.widget.NumberPickerView$b */
    public interface C2883b {
        /* renamed from: a */
        void mo40114a(NumberPickerView numberPickerView, String str, String str2);
    }

    public boolean isSoundEffectsEnabled() {
        return false;
    }

    public NumberPickerView(Context context) {
        this(context, null);
    }

    public NumberPickerView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumberPickerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f4425a = ViewCompat.MEASURED_STATE_MASK;
        this.f4426b = 1;
        this.f4427c = 0.9f;
        this.f4428d = 300;
        this.f4429e = 4;
        this.defaultWheelItemCount = 3;
        this.f4431g = 80;
        this.f4439o = 0;
        this.paint = new Paint();
        this.f4408A = 0.0f;
        this.f4409B = false;
        this.f4410C = 0;
        this.f4411D = Integer.MIN_VALUE;
        this.gapHeight = 0;
        this.itemHeight = 0;
        this.paintTextHeight = 0;
        this.f4415H = 0;
        this.fadingEdgeEnabled = true;
        this.f4420M = 0;
        m4543a(context, attributeSet, i);
    }

    /* renamed from: a */
    private void m4543a(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NumberPickerView, i, 0);
        this.wheelItemCount = obtainStyledAttributes.getInt(R.styleable.NumberPickerView_wheelItemCount, this.defaultWheelItemCount) + 2;
        this.f4436l = (this.wheelItemCount - 1) / 2;
        this.f4433i = this.wheelItemCount - 2;
        this.f4437m = (this.f4433i - 1) / 2;
        this.f4438n = new ArrayList(this.wheelItemCount);
        this.min = obtainStyledAttributes.getInt(R.styleable.NumberPickerView_min, 0);
        this.max = obtainStyledAttributes.getInt(R.styleable.NumberPickerView_max, 99);
        this.wrapSelectorWheel = obtainStyledAttributes.getBoolean(R.styleable.NumberPickerView_wrapSelectorWheel, false);
        this.f4447w = new OverScroller(context, new DecelerateInterpolator(2.5f));
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f4448x = viewConfiguration.getScaledTouchSlop();
        this.f4449y = viewConfiguration.getScaledMaximumFlingVelocity() / this.f4429e;
        this.f4450z = viewConfiguration.getScaledMinimumFlingVelocity();
        this.selectedTextColor = obtainStyledAttributes.getColor(R.styleable.NumberPickerView_selectedTextColor, ContextCompat.getColor(context, R.color.red));
        this.textColor = obtainStyledAttributes.getColor(R.styleable.NumberPickerView_textColor, ContextCompat.getColor(context, R.color.white));
        this.textSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NumberPickerView_textSize, this.f4431g);
        this.dividerColor = obtainStyledAttributes.getColor(R.styleable.NumberPickerView_dividerColor, ViewCompat.MEASURED_STATE_MASK);
        this.dividerHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.NumberPickerView_dividerHeight, 1);
        switch (obtainStyledAttributes.getInt(R.styleable.NumberPickerView_align, 1)) {
            case 0:
                this.align = "LEFT";
                break;
            case 1:
                this.align = "CENTER";
                break;
            case 2:
                this.align = "RIGHT";
                break;
            default:
                this.align = "CENTER";
                break;
        }
        this.fadingEdgeEnabled = obtainStyledAttributes.getBoolean(R.styleable.NumberPickerView_fadingEdgeEnabled, true);
        if (this.paint == null) {
            this.paint = new Paint();
        }
        this.paint.setAntiAlias(true);
        this.paint.setTextSize((float) this.textSize);
        this.paint.setTextAlign(Paint.Align.valueOf(this.align));
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        obtainStyledAttributes.recycle();
        m4555g();
        setSoundEffectsEnabled(true);
        m4563n();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            m4552e();
            m4554f();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(-2, -2);
        }
        setMeasuredDimension(m4540a(getSuggestedMinimumWidth(), layoutParams.width, i) + getPaddingLeft() + getPaddingRight(), m4540a(getSuggestedMinimumHeight(), layoutParams.height, i2) + getPaddingTop() + getPaddingBottom());
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumWidth() {
        int suggestedMinimumHeight = super.getSuggestedMinimumHeight();
        return this.f4433i > 0 ? Math.max(suggestedMinimumHeight, m4549d()) : suggestedMinimumHeight;
    }

    /* access modifiers changed from: protected */
    public int getSuggestedMinimumHeight() {
        int suggestedMinimumWidth = super.getSuggestedMinimumWidth();
        if (this.f4433i <= 0) {
            return suggestedMinimumWidth;
        }
        Paint.FontMetricsInt fontMetricsInt = this.paint.getFontMetricsInt();
        return Math.max(suggestedMinimumWidth, (fontMetricsInt.descent - fontMetricsInt.ascent) * this.f4433i);
    }

    /* renamed from: d */
    private int m4549d() {
        this.paint.setTextSize(((float) this.textSize) * 1.3f);
        NumberPickerAdapter bhj = this.numberPickerAdapter;
        if (bhj == null) {
            int measureText = (int) this.paint.measureText(Integer.toString(this.min));
            int measureText2 = (int) this.paint.measureText(Integer.toString(this.max));
            this.paint.setTextSize(((float) this.textSize) * 1.0f);
            return measureText > measureText2 ? measureText : measureText2;
        } else if (!bhj.getDefaultString().isEmpty()) {
            int measureText3 = (int) this.paint.measureText(this.numberPickerAdapter.getDefaultString());
            this.paint.setTextSize(((float) this.textSize) * 1.0f);
            return measureText3;
        } else {
            int measureText4 = (int) this.paint.measureText("0000");
            this.paint.setTextSize(((float) this.textSize) * 1.0f);
            return measureText4;
        }
    }

    /* renamed from: a */
    private int m4540a(int i, int i2, int i3) {
        int size = MeasureSpec.getSize(i3);
        int mode = MeasureSpec.getMode(MeasureSpec.getMode(i3));
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode != 1073741824) {
                    return 0;
                }
            } else if (i2 == -2 || i2 == -1) {
                return i;
            } else {
                return i2;
            }
        } else if (i2 == -2) {
            return Math.min(i, size);
        } else {
            if (i2 != -1) {
                return Math.min(i2, size);
            }
        }
        return size;
    }

    /* renamed from: e */
    private void m4552e() {
        this.itemHeight = getItemHeight();
        this.paintTextHeight = getPaintTextHeight();
        this.gapHeight = getGapHeight();
        int i = this.itemHeight;
        this.f4411D = ((this.f4437m * i) + ((this.paintTextHeight + i) / 2)) - (i * this.f4436l);
        this.f4410C = this.f4411D;
    }

    /* renamed from: f */
    private void m4554f() {
        setVerticalFadingEdgeEnabled(this.fadingEdgeEnabled);
        if (this.fadingEdgeEnabled) {
            setFadingEdgeLength(((getBottom() - getTop()) - this.textSize) / 2);
        }
    }

    /* renamed from: g */
    private void m4555g() {
        List<Integer> list = this.f4438n;
        if (list == null) {
            this.f4438n = new ArrayList<>();
        } else {
            list.clear();
        }
        for (int i = 0; i < this.wheelItemCount; i++) {
            int i2 = i - this.f4436l;
            if (this.wrapSelectorWheel) {
                i2 = m4551e(i2);
            }
            this.f4438n.add(Integer.valueOf(i2));
        }
    }

    public float getBottomFadingEdgeStrength() {
        return this.f4427c;
    }

    public float getTopFadingEdgeStrength() {
        return this.f4427c;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        m4544a(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        m4545a(motionEvent);
        return true;
    }

    /* renamed from: a */
    private void m4545a(MotionEvent motionEvent) {
        if (this.f4444t == null) {
            this.f4444t = VelocityTracker.obtain();
        }
        this.f4444t.addMovement(motionEvent);
        switch (motionEvent.getActionMasked()) {
            case 0:
                OverScroller overScroller = this.f4447w;
                if (overScroller != null && !overScroller.isFinished()) {
                    this.f4447w.forceFinished(true);
                }
                this.f4408A = motionEvent.getY();
                return;
            case 1:
                if (this.f4409B) {
                    this.f4409B = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    this.f4444t.computeCurrentVelocity(1000, (float) this.f4449y);
                    int yVelocity = (int) this.f4444t.getYVelocity();
                    if (Math.abs(yVelocity) > this.f4450z) {
                        this.f4415H = 0;
                        this.f4447w.fling(getScrollX(), getScrollY(), 0, yVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, (int) (((double) getItemHeight()) * 0.7d));
                        m4560k();
                        m4548c(2);
                    }
                    m4558i();
                } else {
                    m4547b((int) motionEvent.getY());
                }
                invalidate();
                return;
            case 2:
                float y = motionEvent.getY() - this.f4408A;
                if (!this.f4409B && Math.abs(y) > ((float) this.f4448x)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if (y > 0.0f) {
                        y -= (float) this.f4448x;
                    } else {
                        y += (float) this.f4448x;
                    }
                    m4548c(1);
                    this.f4409B = true;
                }
                if (this.f4409B) {
                    scrollBy(0, (int) y);
                    invalidate();
                    this.f4408A = motionEvent.getY();
                    OverScroller overScroller2 = this.f4447w;
                    if (overScroller2 != null && !overScroller2.isFinished()) {
                        this.f4447w.forceFinished(true);
                        return;
                    }
                    return;
                }
                return;
            case 3:
                if (this.f4409B) {
                    this.f4409B = false;
                }
                m4558i();
                return;
            default:
                return;
        }
    }

    /* renamed from: b */
    private void m4547b(int i) {
        m4550d((i / this.itemHeight) - this.f4437m);
    }

    public void scrollBy(int i, int i2) {
        if (i2 != 0) {
            int i3 = this.gapHeight;
            List<Integer> list = this.f4438n;
            if (list != null && !list.isEmpty()) {
                if (!this.wrapSelectorWheel && i2 > 0 && ((Integer) this.f4438n.get(this.f4436l)).intValue() <= this.min) {
                    int i4 = this.f4410C;
                    int i5 = i4 + i2;
                    int i6 = this.f4411D;
                    int i7 = i3 / 2;
                    if (i5 - i6 < i7) {
                        this.f4410C = i4 + i2;
                    } else {
                        this.f4410C = i6 + i7;
                        if (!this.f4447w.isFinished() && !this.f4409B) {
                            this.f4447w.abortAnimation();
                        }
                    }
                } else if (this.wrapSelectorWheel || i2 >= 0 || ((Integer) this.f4438n.get(this.f4436l)).intValue() < this.max) {
                    this.f4410C += i2;
                    while (true) {
                        int i8 = this.f4410C;
                        if (i8 - this.f4411D >= (-i3)) {
                            break;
                        }
                        this.f4410C = i8 + this.itemHeight;
                        m4561l();
                        if (!this.wrapSelectorWheel && ((Integer) this.f4438n.get(this.f4436l)).intValue() >= this.max) {
                            this.f4410C = this.f4411D;
                        }
                    }
                    while (true) {
                        int i9 = this.f4410C;
                        if (i9 - this.f4411D > i3) {
                            this.f4410C = i9 - this.itemHeight;
                            m4562m();
                            if (!this.wrapSelectorWheel && ((Integer) this.f4438n.get(this.f4436l)).intValue() <= this.min) {
                                this.f4410C = this.f4411D;
                            }
                        } else {
                            m4542a(((Integer) this.f4438n.get(this.f4436l)).intValue(), true);
                            return;
                        }
                    }
                } else {
                    int i10 = this.f4410C;
                    int i11 = i10 + i2;
                    int i12 = this.f4411D;
                    int i13 = i3 / 2;
                    if (i11 - i12 > (-i13)) {
                        this.f4410C = i10 + i2;
                    } else {
                        this.f4410C = i12 - i13;
                        if (!this.f4447w.isFinished() && !this.f4409B) {
                            this.f4447w.abortAnimation();
                        }
                    }
                }
            }
        }
    }

    public void computeScroll() {
        OverScroller overScroller = this.f4447w;
        if (overScroller != null && overScroller.computeScrollOffset()) {
            int currX = this.f4447w.getCurrX();
            int currY = this.f4447w.getCurrY();
            if (this.f4415H == 0) {
                this.f4415H = this.f4447w.getStartY();
            }
            scrollBy(currX, currY - this.f4415H);
            this.f4415H = currY;
            invalidate();
        } else if (!this.f4409B) {
            m4557h();
        }
    }

    /* renamed from: h */
    private void m4557h() {
        this.f4415H = 0;
        int i = this.f4411D - this.f4410C;
        int abs = Math.abs(i);
        int i2 = this.itemHeight;
        int i3 = abs > i2 / 2 ? i > 0 ? i + (-i2) : i + i2 : i;
        if (i3 != 0) {
            OverScroller overScroller = this.f4447w;
            if (overScroller != null) {
                overScroller.startScroll(getScrollX(), getScrollY(), 0, i3, 800);
                m4560k();
            }
        }
        m4548c(0);
    }

    /* renamed from: i */
    private void m4558i() {
        this.f4444t.recycle();
        this.f4444t = null;
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }

    /* renamed from: c */
    private void m4548c(int i) {
        if (this.f4420M != i) {
            this.f4420M = i;
            C2882a aVar = this.f4417J;
            if (aVar != null) {
                aVar.mo40107a(this, i);
            }
        }
    }

    private int getItemHeight() {
        return getHeight() / (this.wheelItemCount - 2);
    }

    private int getGapHeight() {
        return getItemHeight() - getPaintTextHeight();
    }

    //m4559j
    private int getPaintTextHeight() {
        Paint.FontMetricsInt fontMetricsInt = this.paint.getFontMetricsInt();
        return Math.abs(fontMetricsInt.bottom + fontMetricsInt.top);
    }

    /* renamed from: k */
    private void m4560k() {
        if (Build.VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }

    /* renamed from: a */
    private void m4544a(Canvas canvas) {
        float f;
        List<Integer> list = this.f4438n;
        if (list != null && !list.isEmpty()) {
            int itemHeight = getItemHeight();
            switch (C28811.f4451a[this.paint.getTextAlign().ordinal()]) {
                case 1:
                    f = (float) getPaddingLeft();
                    break;
                case 2:
                    f = (float) ((getRight() - getLeft()) / 2);
                    break;
                case 3:
                    f = (float) ((getRight() - getLeft()) - getPaddingRight());
                    break;
                default:
                    f = (float) ((getRight() - getLeft()) / 2);
                    break;
            }
            float f2 = (float) this.f4410C;
            int i = this.f4437m;
            int max = Math.max(i, (this.f4433i - i) - 1);
            for (int i2 = 0; i2 < this.f4438n.size(); i2++) {
                float abs = Math.abs(f2 - ((float) (this.f4411D + (this.f4436l * itemHeight))));
                float f3 = 1.0f;
                if (max != 0) {
                    float f4 = (float) (itemHeight * max);
                    f3 = 1.0f + (((f4 - abs) * 0.4f) / f4);
                }
                if (abs < ((float) (this.itemHeight / 2))) {
                    this.paint.setColor(this.selectedTextColor);
                } else {
                    this.paint.setColor(this.textColor);
                }
                canvas.save();
                canvas.scale(f3, f3, f, f2);
                canvas.drawText(mo40194a(((Integer) this.f4438n.get(i2)).intValue()), f, f2, this.paint);
                canvas.restore();
                f2 += (float) itemHeight;
            }
            int i3 = (this.f4433i / 2) * this.itemHeight;
            this.paint.setColor(this.dividerColor);
            float f5 = (float) i3;
            canvas.drawLine(0.0f, f5, (float) getRight(), f5, this.paint);
            float f6 = (float) (i3 + this.itemHeight);
            canvas.drawLine(0.0f, f6, (float) getRight(), f6, this.paint);
        }
    }

    /* renamed from: b */
    private int m4546b(String str) {
        NumberPickerAdapter bhj = this.numberPickerAdapter;
        if (bhj != null) {
            return bhj.getIndexOfString(str);
        }
        try {
            return m4553f(Integer.valueOf(str).intValue());
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    /* renamed from: l */
    private void m4561l() {
        int i = 0;
        while (i < this.f4438n.size() - 1) {
            List<Integer> list = this.f4438n;
            int i2 = i + 1;
            list.set(i, list.get(i2));
            i = i2;
        }
        List<Integer> list2 = this.f4438n;
        int intValue = ((Integer) list2.get(list2.size() - 2)).intValue() + 1;
        if (this.wrapSelectorWheel && intValue > this.max) {
            intValue = this.min;
        }
        List<Integer> list3 = this.f4438n;
        list3.set(list3.size() - 1, Integer.valueOf(intValue));
    }

    /* renamed from: m */
    private void m4562m() {
        for (int size = this.f4438n.size() - 1; size > 0; size--) {
            List<Integer> list = this.f4438n;
            list.set(size, list.get(size - 1));
        }
        int intValue = ((Integer) this.f4438n.get(1)).intValue() - 1;
        if (this.wrapSelectorWheel && intValue < this.min) {
            intValue = this.max;
        }
        this.f4438n.set(0, Integer.valueOf(intValue));
    }

    /* renamed from: d */
    private void m4550d(int i) {
        this.f4415H = 0;
        this.f4447w.startScroll(0, 0, 0, (-this.itemHeight) * i, this.f4428d);
        invalidate();
    }

    /* renamed from: a */
    private void m4542a(int i, boolean z) {
        int i2 = this.f4439o;
        this.f4439o = i;
        if (z && i2 != i) {
            m4541a(i2, i);
        }
    }

    /* renamed from: e */
    private int m4551e(int i) {
        int max = this.max;
        if (i > max) {
            int min = this.min;
            return (min + ((i - max) % ((max - min) + 1))) - 1;
        }
        int i4 = this.min;
        return i < i4 ? (max - ((i4 - i) % ((max - i4) + 1))) + 1 : i;
    }

    /* renamed from: a */
    private void m4541a(int i, int i2) {
        C2883b bVar = this.f4416I;
        if (bVar != null) {
            bVar.mo40114a(this, mo40194a(i), mo40194a(i2));
        }
    }

    /* renamed from: f */
    private int m4553f(int i) {
        if (this.wrapSelectorWheel) {
            return m4551e(i);
        }
        int i2 = this.max;
        if (i > i2) {
            return i2;
        }
        int i3 = this.min;
        return i < i3 ? i3 : i;
    }

    /* renamed from: g */
    private void m4556g(int i) {
        this.f4439o = i;
        this.f4438n.clear();
        for (int i2 = 0; i2 < this.wheelItemCount; i2++) {
            int i3 = this.f4439o + (i2 - this.f4436l);
            if (this.wrapSelectorWheel) {
                i3 = m4551e(i3);
            }
            this.f4438n.add(Integer.valueOf(i3));
        }
    }

    public void setOnValueChangedListener(C2883b bVar) {
        this.f4416I = bVar;
    }

    public void setOnScrollListener(C2882a aVar) {
        this.f4417J = aVar;
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public void mo40197a(String str) {
        m4556g(m4546b(str));
    }

    public void setUnselectedTextColor(int i) {
        this.textColor = i;
    }

    //mo40196a
    public void setNumberPickerAdapter(NumberPickerAdapter numberPickerAdapter, boolean setMaxAndMin) {
        this.numberPickerAdapter = numberPickerAdapter;
        if (this.numberPickerAdapter == null) {
            invalidate();
            return;
        }
        if (numberPickerAdapter.getCollectionSize() != -1 && setMaxAndMin) {
            this.max = numberPickerAdapter.getCollectionSize() - 1;
            this.min = 0;
        }
        mo40195a();
    }

    /* access modifiers changed from: 0000 */
    public boolean getWrapSelectorWheel() {
        return this.wrapSelectorWheel;
    }

    /* access modifiers changed from: 0000 */
    public void setWrapSelectorWheel(boolean z) {
        this.wrapSelectorWheel = z;
        requestLayout();
    }

    /* access modifiers changed from: 0000 */
    public void setWheelItemCount(int i) {
        this.wheelItemCount = i + 2;
        int i2 = this.wheelItemCount;
        this.f4436l = (i2 - 1) / 2;
        this.f4433i = i2 - 2;
        this.f4437m = (this.f4433i - 1) / 2;
        this.f4438n = new ArrayList(i2);
        mo40195a();
        requestLayout();
    }

    public void setSelectedTextColor(int i) {
        this.selectedTextColor = i;
        requestLayout();
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public String mo40194a(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("getValue: real pos= ");
        sb.append(i);
        Log.e("WheelPicker", sb.toString());
        if (!this.wrapSelectorWheel) {
            return (i > this.max || i < this.min) ? "" : Integer.toString(i);
        }
        NumberPickerAdapter bhj = this.numberPickerAdapter;
        if (bhj != null) {
            return bhj.getStringByIndex(i);
        }
        return Integer.toString(m4551e(i));
    }

    public String getValue() {
        return mo40194a(this.f4439o);
    }

    /* access modifiers changed from: 0000 */
    public void setValue(String str) {
        mo40197a(str);
    }

    /* access modifiers changed from: 0000 */
    public String getMaxValue() {
        NumberPickerAdapter bhj = this.numberPickerAdapter;
        if (bhj != null) {
            return bhj.getStringByIndex(this.max);
        }
        return Integer.toString(this.max);
    }

    /* access modifiers changed from: 0000 */
    public void setMaxValue(int i) {
        this.max = i;
    }

    /* access modifiers changed from: 0000 */
    public String getMinValue() {
        NumberPickerAdapter numberPickerAdapter = this.numberPickerAdapter;
        if (numberPickerAdapter != null) {
            return numberPickerAdapter.getStringByIndex(this.min);
        }
        return Integer.toString(this.min);
    }

    /* access modifiers changed from: 0000 */
    public void setMinValue(int i) {
        this.min = i;
    }

    /* access modifiers changed from: 0000 */
    /* renamed from: a */
    public void mo40195a() {
        m4555g();
        m4552e();
        requestLayout();
        this.f4439o = 0;
    }

    public float getTOP_AND_BOTTOM_FADING_EDGE_STRENGTH() {
        return this.f4427c;
    }

    public void setTOP_AND_BOTTOM_FADING_EDGE_STRENGTH(float f) {
        this.f4427c = f;
    }

    public int getMinIndex() {
        return this.min;
    }

    public void setMinIndex(int i) {
        this.min = i;
    }

    public int getMaxIndex() {
        return this.max;
    }

    public void setMaxIndex(int i) {
        this.max = i;
    }

    public int getDividerColor() {
        return this.dividerColor;
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
    }

    public int getDividerHeight() {
        return this.dividerHeight;
    }

    public void setDividerHeight(int i) {
        this.dividerHeight = i;
    }

    /* renamed from: b */
    public void mo40198b() {
        if (this.f4417J != null) {
            this.f4417J = null;
        }
        if (this.f4416I != null) {
            this.f4416I = null;
        }
    }

    /* renamed from: n */
    private void m4563n() {
        this.f4423P = new SoundPool.Builder().build();
        this.f4424Q = this.f4423P.load(getContext(), R.raw.picker, 1);
    }

    /* renamed from: c */
    public void mo40199c() {
        if (this.f4423P == null) {
            m4563n();
        }
        this.f4423P.play(this.f4424Q, 0.5f, 0.5f, HttpStatus.HTTP_INTERNAL_SERVER_ERROR, 0, 1.0f);
    }
}
