package com.swufe.firstapp;

public class RateItem {
    public int id;
    public String curname;
    public String currate;

    public RateItem() {
        super();
        curname = "";
        currate = "";
    }

    public RateItem(String curName, String curRate) {
        super();
        this.curname = curName;
        this.currate = curRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurname() {
        return curname;
    }

    public void setCurname(String curname) {
        this.curname = curname;
    }

    public String getCurrate() {
        return currate;
    }

    public void setCurrate(String currate) {
        this.currate = currate;
    }
}
