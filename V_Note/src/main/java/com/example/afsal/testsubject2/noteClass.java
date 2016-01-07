package com.example.afsal.testsubject2;


public class noteClass {
    String id;
    String data;

    public String getKey() {
        return id;
    }

    public void setKey(String key) {
        this.id = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}
