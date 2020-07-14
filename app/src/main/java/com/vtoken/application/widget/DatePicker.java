package com.vtoken.application.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.vtoken.application.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatePicker extends LinearLayout implements NumberPickerView.C2882a {
    /* access modifiers changed from: private */

    //f4321a
    public NumberPickerView monthPickerView;
    /* access modifiers changed from: private */

    //f4322b
    public NumberPickerView dayPickerView;
    /* access modifiers changed from: private */

    //f4323c
    public NumberPickerView yearPickerView;
    /* access modifiers changed from: private */

    //f4324d
    public DateChangeEvent dateChangeEvent;
    /* access modifiers changed from: private */

    //f4325e
    public List<String> yearStringList;
    /* access modifiers changed from: private */

    //f4326f
    public List<String> dayStringList;
    /* access modifiers changed from: private */

    //f4327g
    public List<String> monthStringList;

    //C2872a
    public interface DateChangeEvent {
        void onDateChanged(int day, int month, int year);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DatePicker(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    //mo40106a
    public void init(Context context) {
        View inflate = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_date, null, false);
        addView(inflate, new LayoutParams(-1, -2));
        this.monthPickerView = (NumberPickerView) inflate.findViewById(R.id.month_picker);
        this.dayPickerView = (NumberPickerView) inflate.findViewById(R.id.day_picker);
        this.yearPickerView = (NumberPickerView) inflate.findViewById(R.id.year_picker);
        initMonthPickerView(this.monthPickerView);
        initDayPickerView(this.dayPickerView);
        initYearPickerView(this.yearPickerView);
        this.monthPickerView.setOnValueChangedListener(new NumberPickerView.C2883b() {
            /* renamed from: a */
            public void mo40114a(NumberPickerView numberPickerView, String str, String str2) {
                DatePicker datePicker = DatePicker.this;
                int a = datePicker.getDayCount(Integer.valueOf(datePicker.monthPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.yearPickerView.getValue()).intValue());
                DatePicker.this.dayPickerView.setMaxValue(a);
                DatePicker.this.dayPickerView.setMaxIndex(a - 1);
                DatePicker.this.dayPickerView.mo40195a();
                if (DatePicker.this.dateChangeEvent != null) {
                    DatePicker.this.dateChangeEvent.onDateChanged(Integer.valueOf(DatePicker.this.dayPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.monthPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.yearPickerView.getValue()).intValue());
                }
            }
        });
        this.yearPickerView.setOnValueChangedListener(new NumberPickerView.C2883b() {
            /* renamed from: a */
            public void mo40114a(NumberPickerView numberPickerView, String str, String str2) {
                DatePicker datePicker = DatePicker.this;
                int a = datePicker.getDayCount(Integer.valueOf(datePicker.monthPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.yearPickerView.getValue()).intValue());
                DatePicker.this.dayPickerView.setMaxValue(a);
                DatePicker.this.dayPickerView.setMaxIndex(a - 1);
                DatePicker.this.dayPickerView.mo40195a();
                if (DatePicker.this.dateChangeEvent != null) {
                    DatePicker.this.dateChangeEvent.onDateChanged(Integer.valueOf(DatePicker.this.dayPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.monthPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.yearPickerView.getValue()).intValue());
                }
            }
        });
        this.dayPickerView.setOnValueChangedListener(new NumberPickerView.C2883b() {
            /* renamed from: a */
            public void mo40114a(NumberPickerView numberPickerView, String str, String str2) {
                if (DatePicker.this.dateChangeEvent != null) {
                    DatePicker.this.dateChangeEvent.onDateChanged(Integer.valueOf(DatePicker.this.dayPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.monthPickerView.getValue()).intValue(), Integer.valueOf(DatePicker.this.yearPickerView.getValue()).intValue());
                }
            }
        });
    }

    //m4480a
    private void initYearPickerView(NumberPickerView numberPickerView) {
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        this.yearStringList = new ArrayList();
        for (int startYear = curYear - 10; startYear <= curYear; startYear++) {
            List<String> list = this.yearStringList;
            StringBuilder sb = new StringBuilder();
            sb.append(startYear);
            sb.append("");
            list.add(sb.toString());
        }
        numberPickerView.setNumberPickerAdapter((NumberPickerAdapter) new NumberPickerAdapter() {
            /* renamed from: a */
            public String getDefaultString() {
                return "0000";
            }

            /* renamed from: a */
            public String getStringByIndex(int i) {
                return (String) DatePicker.this.yearStringList.get(i);
            }

            /* renamed from: a */
            public int getIndexOfString(String str) {
                return DatePicker.this.yearStringList.indexOf(str);
            }

            /* renamed from: b */
            public int getCollectionSize() {
                return DatePicker.this.yearStringList.size();
            }
        }, true);
        numberPickerView.setUnselectedTextColor(ContextCompat.getColor(getContext(), R.color.wallet_gray));
        numberPickerView.setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.resync_back_selected));
        numberPickerView.setDividerColor(Color.parseColor("#475872"));
        numberPickerView.setOnScrollListener(this);
    }

    //m4482b
    private void initDayPickerView(NumberPickerView numberPickerView) {
        this.dayStringList = new ArrayList();
        for (int i = 1; i <= 31; i++) {
            List<String> list = this.dayStringList;
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("");
            list.add(sb.toString());
        }
        numberPickerView.setNumberPickerAdapter((NumberPickerAdapter) new NumberPickerAdapter() {
            /* renamed from: a */
            public String getDefaultString() {
                return "00";
            }

            /* renamed from: a */
            public String getStringByIndex(int i) {
                if (i >= DatePicker.this.dayStringList.size()) {
                    return "";
                }
                return (String) DatePicker.this.dayStringList.get(i);
            }

            /* renamed from: a */
            public int getIndexOfString(String str) {
                return DatePicker.this.dayStringList.indexOf(str);
            }

            /* renamed from: b */
            public int getCollectionSize() {
                return DatePicker.this.dayStringList.size();
            }
        }, true);
        numberPickerView.setUnselectedTextColor(ContextCompat.getColor(getContext(), R.color.wallet_gray));
        numberPickerView.setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.resync_back_selected));
        numberPickerView.setDividerColor(Color.parseColor("#475872"));
        numberPickerView.setOnScrollListener(this);
    }

    //m4484c
    private void initMonthPickerView(NumberPickerView numberPickerView) {
        this.monthStringList = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            List<String> list = this.monthStringList;
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("");
            list.add(sb.toString());
        }
        numberPickerView.setNumberPickerAdapter((NumberPickerAdapter) new NumberPickerAdapter() {
            /* renamed from: a */
            public String getDefaultString() {
                return "00";
            }

            /* renamed from: a */
            public String getStringByIndex(int i) {
                return (String) DatePicker.this.monthStringList.get(i);
            }

            /* renamed from: a */
            public int getIndexOfString(String str) {
                return DatePicker.this.monthStringList.indexOf(str);
            }

            /* renamed from: b */
            public int getCollectionSize() {
                return DatePicker.this.monthStringList.size();
            }
        }, true);
        numberPickerView.setUnselectedTextColor(ContextCompat.getColor(getContext(), R.color.wallet_gray));
        numberPickerView.setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.resync_back_selected));
        numberPickerView.setDividerColor(Color.parseColor("#475872"));
        numberPickerView.setOnScrollListener(this);
    }

    /* access modifiers changed from: protected */
    //leap year
    public boolean isLeapYear(long j) {
        return (j % 4 == 0 && j % 100 != 0) || j % 400 == 0;
    }

    //mo40103a
    public int getDayCount(int month, int year) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return isLeapYear((long) year) ? 29 : 28;
            default:
                return 30;
        }
    }

    //mo40105a
    public void setDatePickerValue(int day, int month, int year) {
        mo40104a();
        if (month > 0 && month <= 12) {
            this.monthPickerView.setValue(Integer.toString(month));
        }
        if (year >= Integer.parseInt((String) this.yearStringList.get(0))) {
            List<String> list = this.yearStringList;
            if (year <= Integer.parseInt((String) list.get(list.size() - 1))) {
                this.yearPickerView.setValue(Integer.toString(year));
            }
        }
        int dayCount = getDayCount(month, year);
        if (day > 0 && day <= dayCount) {
            this.dayPickerView.setValue(Integer.toString(day));
        }
    }

    /* renamed from: a */
    public void mo40104a() {
        this.dayPickerView.mo40195a();
        this.monthPickerView.mo40195a();
        this.yearPickerView.mo40195a();
    }

    /* renamed from: a */
    public void mo40107a(NumberPickerView numberPickerView, int i) {
        if (i != 0) {
            numberPickerView.mo40199c();
        }
    }

    public int getDay() {
        return Integer.parseInt(this.dayPickerView.getValue());
    }

    public int getMonth() {
        return Integer.parseInt(this.monthPickerView.getValue());
    }

    public int getYear() {
        return Integer.parseInt(this.yearPickerView.getValue());
    }

    public DateChangeEvent getListener() {
        return this.dateChangeEvent;
    }

    public void setListener(DateChangeEvent dateChangeEvent) {
        this.dateChangeEvent = dateChangeEvent;
    }
}
