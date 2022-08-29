package com.example.infoapp.var;

public final class StatusSpace {
    private static StatusSpace mInstance;

    public static synchronized StatusSpace getInstance() {
        if (mInstance == null) {
            mInstance = new StatusSpace();
        }
        return mInstance;
    }
}
