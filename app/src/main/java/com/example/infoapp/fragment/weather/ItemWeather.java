package com.example.infoapp.fragment.weather;

import android.graphics.drawable.Drawable;

public class ItemWeather {
    private Drawable iconDrawable;
    private String strTempT;
    private String strTempW;
    private String strTempD;
    private String strPrec;
    private String strHumi;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }

    public void setStrTempT(String strTempT) { this.strTempT = strTempT; }

    public void setStrTempW(String strTempW) {
        this.strTempW = strTempW;
    }

    public void setStrTempD(String strTempD) {
        this.strTempD = strTempD;
    }

    public void setStrPrec(String strPrec) {
        this.strPrec = strPrec;
    }

    public void setStrHumi(String strHumi) {
        this.strHumi = strHumi;
    }


    public Drawable getIcon() { return this.iconDrawable; }

    public String getStrTempT() { return this.strTempT; }

    public String getStrTempW() { return this.strTempW; }

    public String getStrTempD() { return this.strTempD; }

    public String getStrPrec() {
        return this.strPrec;
    }

    public String getStrHumi() {
        return this.strHumi;
    }
}
