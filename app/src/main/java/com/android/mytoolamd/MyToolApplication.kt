package com.android.mytoolamd

import android.app.Application
import androidx.work.WorkManager
import com.onesignal.OneSignal

class MyToolApplication:Application() {
    val ONESIGNAL_APP_ID = "60b8520e-e90c-4f7b-ac2d-33d9baff24b0"


    private val channelId = "i.apps.notifications"

    private val workManager = WorkManager.getInstance(this)

    companion object {
        private const val WORK_TAG = "notification_work"
        private const val WORK_NAME = "notification_work"
        var lastUsageTime: Long = 0
    }

    override fun onCreate() {
        super.onCreate()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications()

    }
}