package com.vtoken.application.model;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Node {
    public static final int CHILD = 2;
    public static final int EMPTY = 3;
    public static final int GROUP = 1;
    private int index;
    public ObservableBoolean isExpand = new ObservableBoolean(false);
    public Node parentNode;

    @Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: v.dimensional.model.Node$NodeType */
    public @interface NodeType {
    }

    public abstract ObservableList<Node> getNodeList();

    public abstract int getNodeType();

    public Node getParentNode() {
        return this.parentNode;
    }

    public void setParentNode(Node node) {
        this.parentNode = node;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }
}
