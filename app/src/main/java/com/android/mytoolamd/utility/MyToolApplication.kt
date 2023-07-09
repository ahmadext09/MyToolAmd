package com.android.mytoolamd.utility

import android.app.Application
import androidx.work.WorkManager
import com.onesignal.OneSignal

class MyToolApplication : Application() {
    val ONESIGNAL_APP_ID = "60b8520e-e90c-4f7b-ac2d-33d9baff24b0"


    override fun onCreate() {
        super.onCreate()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)


        OneSignal.promptForPushNotifications()

    }
}