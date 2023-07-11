package com.android.mytoolamd.utility



    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.Context
    import android.content.Intent
    import android.os.Build
    import android.util.Log

    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import androidx.work.CoroutineWorker
    import androidx.work.WorkerParameters
    import com.android.mytoolamd.receiver.NotificationClickReceiver
    import com.android.mytoolamd.R
    import com.google.firebase.analytics.FirebaseAnalytics
    import com.google.firebase.analytics.ktx.analytics
    import com.google.firebase.analytics.ktx.logEvent
    import com.google.firebase.ktx.Firebase

class InactivityCheckWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    //  var lastUsageTime = MainActivity.lastUsageTime
    var sharedPreferences =
        applicationContext.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
    var lastUsageTime = sharedPreferences.getLong("lastUsageTime", 0)


    override suspend fun doWork(): Result {
        // Create and display the notification
        //var lastUsageTime = MainActivity.lastUsageTime
        Log.e(
            "sharedPrefrence lastUsageTime updated value in workmanager",
            "${sharedPreferences.getLong("lastUsageTime", 0)}"
        )

        val inactiveDuration = System.currentTimeMillis() - lastUsageTime
        Log.e("inactive duration", "$inactiveDuration")
        Log.e("lastUsageTime", "$lastUsageTime")
        Log.e("inactive threshold", "${AppConstants.INACTIVITY_THRESHOLD}")

        if (inactiveDuration >= AppConstants.INACTIVITY_THRESHOLD) {
            Log.e("worker alert", "entered last usage condition")
            createNotificationChannel()

            val clickIntent =
                Intent(applicationContext, NotificationClickReceiver::class.java).apply {
                    action = "NOTIFICATION_CLICKED_ACTION"

                }
            clickIntent.putExtra("notification_Id", AppConstants.NOTIFICATION_ID)
            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(AppConstants.INACTIVE_NOTIFICATION_TITLE)
                .setContentText(AppConstants.INACTIVE_NOTIFICATION_PROMPT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(AppConstants.INACTIVE_NOTIFICATION_PROMPT))
                .setContentIntent(pendingIntent)

                .setAutoCancel(true)
            notificationBuilder.setFullScreenIntent(pendingIntent, true)

            // Log a message when the user clicks the notification

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(AppConstants.NOTIFICATION_ID, notificationBuilder.build())

            }
            //   lastUsageTime=System.currentTimeMillis()
            // sharedPreferences.edit().putLong("lastUsageTime", lastUsageTime).apply()
            //  Log.e("sharedPrefrence lastUsageTime updated value in notificationbuilder","${sharedPreferences.getLong("lastUsageTime", 0)}")
            Log.e("NotificationWorkManager", "notification Received")
            logEvent("inactive_notification_received")
        }
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                AppConstants.INACTIVE_NOTIFICATION_TITLE,
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "app_inactive_channel"
    // private const val INACTIVITY_THRESHOLD = 45 * 60 * 1000
        //  private const val INACTIVE_NOTIFICATION_TITLE ="App Inactive"
    }

    private fun logEvent(eventName: String) {
        // Log the event using Firebase Analytics
//            val firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
//            val bundle = Bundle()
//            bundle.putString("event_name", eventName)
//            firebaseAnalytics.logEvent("received_notification_event", bundle)
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent("received_notification_event") {

            param("event_name", eventName)
        }
    }
}

