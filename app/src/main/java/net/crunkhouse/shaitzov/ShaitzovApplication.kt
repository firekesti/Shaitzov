package net.crunkhouse.shaitzov

import android.app.Application

import timber.log.Timber

/**
 * Application class to set up some things on app startup.
 */

class ShaitzovApplication : Application() {

    lateinit var prefs: LocalPreferences

    override fun onCreate() {
        super.onCreate()
        prefs = LocalPreferences(applicationContext)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
