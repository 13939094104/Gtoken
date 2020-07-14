package com.vtoken.application.model;

import androidx.databinding.ObservableList;

import java.util.List;

public class GroupNode extends Node {

    /* renamed from: id */
    private int f3397id;
    private String label;
    private ObservableList<Node> vidList;

    public int getNodeType() {
        return 1;
    }

    public GroupNode(String str) {
        this.label = str;
    }

    public GroupNode(String str, ObservableList<Node> observableList) {
        this.label = str;
        this.vidList = observableList;
    }

    public ObservableList<Node> getNodeList() {
        return this.vidList;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public List<Node> getVidList() {
        return this.vidList;
    }

    public void setVidList(ObservableList<Node> observableList) {
        if (observableList != null) {
            for (Node parentNode : observableList) {
                parentNode.setParentNode(this);
            }
        }
        this.vidList = observableList;
    }

    public int getId() {
        return this.f3397id;
    }

    public void setId(int i) {
        this.f3397id = i;
    }
}
