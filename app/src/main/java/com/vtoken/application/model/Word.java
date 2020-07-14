package com.vtoken.application.model;

import java.io.Serializable;

public class Word implements Serializable {
    private String word;

    public Word(String str) {
        this.word = str;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String str) {
        this.word = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Word) || !((Word) obj).getWord().equals(this.word)) {
            return false;
        }
        return true;
    }
}
