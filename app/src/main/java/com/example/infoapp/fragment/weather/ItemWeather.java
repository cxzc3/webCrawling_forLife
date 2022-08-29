package com.example.infoapp.fragment.weather;

import android.graphics.drawable.Drawable;

public class ItemWeather {
    private Drawable iconDrawable;
    private String strTemp;
    private String strPrec;
    private String strHumi;

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }

    public void setStrTemp(String strTemp) {
        this.strTemp = strTemp;
    }

    public void setStrPrec(String strPrec) {
        this.strPrec = strPrec;
    }

    public void setStrHumi(String strHumi) {
        this.strHumi = strHumi;
    }

    public Drawable getIcon() {
        return this.iconDrawable;
    }

    public String getStrTemp() {
        return this.strTemp;
    }

    public String getStrPrec() {
        return this.strPrec;
    }

    public String getStrHumi() {
        return this.strHumi;
    }
}
