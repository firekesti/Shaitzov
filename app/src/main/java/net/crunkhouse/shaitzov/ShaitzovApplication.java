package net.crunkhouse.shaitzov;

import android.app.Application;

/**
 * Application class to set up some things on app startup.
 */

public class ShaitzovApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalPreferences.init(getApplicationContext());
    }
}
