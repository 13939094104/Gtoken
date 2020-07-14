package com.vtoken.application.model;

import androidx.databinding.ObservableField;
import androidx.annotation.Nullable;
import android.text.TextUtils;

//bcm
public class MyObservableDecimal extends ObservableField<String> {

    /* renamed from: a */
    private boolean f9621a = true;

    public MyObservableDecimal() {
    }

    public MyObservableDecimal(String str, boolean z) {
        super(str);
        this.f9621a = z;
    }

    @Nullable
    /* renamed from: a */
    public String get() {
        return formatDecimal((String) super.get(), this.f9621a);
    }

    //mo41494a
    public String formatSelfDecimal(boolean z) {
        return formatDecimal((String) super.get(), z);
    }

    //mo41493a
    public String formatDecimal(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return z ? "0" : "";
        } else if (!TextUtils.equals(".", str)) {
            return str.replace(",", "");
        } else {
            return z ? "0" : "";
        }
    }

    /* renamed from: a */
    public void set(String str) {
        if (!TextUtils.equals(formatDecimal(str, false), get())) {
            super.set(str);
        }
    }
}
