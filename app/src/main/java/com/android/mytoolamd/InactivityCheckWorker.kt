package com.android.mytoolamd


    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.content.Context
    import android.os.Build
    import android.util.Log
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import androidx.work.CoroutineWorker
    import androidx.work.WorkerParameters
    import androidx.work.workDataOf
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.withContext
    import java.util.concurrent.TimeUnit

    class InactivityCheckWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {


override suspend fun doWork(): Result {
    // Create and display the notification
    val lastUsageTime = MainActivity.lastUsageTime


    val inactiveDuration = System.currentTimeMillis() - lastUsageTime


    if (inactiveDuration >= INACTIVITY_THRESHOLD) {
        createNotificationChannel()
Log.e("worker alert","entered last usage condition")
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("App Inactive")
            .setContentText("hey , welcome back, its nice to see you again.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notificationBuilder.build())

        }
    }
    return Result.success()
}

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "App Inactive",
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
            private const val NOTIFICATION_ID = 1
        private const val INACTIVITY_THRESHOLD = 15 * 60 * 1000
        }
    }

