package com.plexoc.helpma.Utils;

import android.app.Application;
import android.content.ContextWrapper;

public class App extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
    
}
