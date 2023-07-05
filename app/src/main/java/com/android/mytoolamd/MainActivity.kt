package com.android.mytoolamd


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.String.format
import java.net.HttpCookie.parse
import java.util.concurrent.TimeUnit


import java.util.logging.Level.parse
import kotlin.math.round

class MainActivity : AppCompatActivity() {

//
//     val ONESIGNAL_APP_ID = "60b8520e-e90c-4f7b-ac2d-33d9baff24b0"
//

     private val channelId = "i.apps.notifications"

    private val workManager = WorkManager.getInstance(this)

    companion object {
        private const val WORK_TAG = "notification_work"
        private const val WORK_NAME = "notification_work"
        var lastUsageTime: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btnUrl = findViewById<Button>(R.id.share_url_btn)
        val remote_config_btn=findViewById<Button>(R.id.remote_config_btn)
         btnUrl.setOnClickListener(){
             val intent=Intent(this,ShareImageUrlActivity::class.java)
             startActivity(intent)
         }

        remote_config_btn.setOnClickListener(){
            val intent=Intent(this,RemoteConfigActivity::class.java)
            startActivity(intent)
        }


//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
//
//        // OneSignal Initialization
//        OneSignal.initWithContext(this)
//        OneSignal.setAppId(ONESIGNAL_APP_ID)
//
//        // promptForPushNotifications will show the native Android notification permission prompt.
//        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
//        OneSignal.promptForPushNotifications()
//
//
//
        Log.e("on alert","entered on create")






    }




    override fun onResume() {
        super.onResume()
        // Cancel any existing work requests
        Log.e("on alert","entered on resume")
        workManager.cancelAllWorkByTag(WORK_TAG)

    }


    override fun onPause() {
        super.onPause()

   Log.e("onpause alert","entered on pause")
        lastUsageTime = System.currentTimeMillis()
        Log.e("lastusagetime value","$lastUsageTime")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<InactivityCheckWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        // Enqueue unique work with a unique name to prevent duplicate work requests
        workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, notificationWorkRequest)
        Log.e("onpause alert","work manager assigned")
    }

}
