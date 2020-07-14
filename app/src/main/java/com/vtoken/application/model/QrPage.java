package com.vtoken.application.model;

import java.io.Serializable;
import java.util.Objects;

public class QrPage implements Serializable {
    private String info;
    private Integer postion;
    private Integer size;

    public Integer getPostion() {
        return this.postion;
    }

    public void setPostion(Integer num) {
        this.postion = num;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer num) {
        this.size = num;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String str) {
        this.info = str;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QrPage qrPage = (QrPage) obj;
        if (!Objects.equals(this.postion, qrPage.postion) || !Objects.equals(this.info, qrPage.info)) {
            z = false;
        }
        return z;
    }
}