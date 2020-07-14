package com.vtoken.application.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import java.io.Serializable;

public class TopItem extends BaseObservable implements Serializable {
    public ObservableField<String> directChildren = new ObservableField<>();
    public ObservableField<String> num = new ObservableField<>();
    public ObservableField<String> percent = new ObservableField<>();
    public ObservableField<String> ranking = new ObservableField<>();
    public ObservableField<String> weight = new ObservableField<>();
}