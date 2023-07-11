package com.android.mytoolamd.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Button
import androidx.work.*
import com.android.mytoolamd.R
import com.android.mytoolamd.utility.AppConstants
import com.android.mytoolamd.utility.InactivityCheckWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    private val workManager = WorkManager.getInstance(this)
    lateinit var sharedPreferences: SharedPreferences

    companion object {


        //   private const val INACTIVE_NOTIFICATION__REPETE_INTERVAL:Long = 15
        var lastUsageTime: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        lastUsageTime = System.currentTimeMillis()
        sharedPreferences.edit().putLong("lastUsageTime", lastUsageTime).apply()
        Log.e(
            "sharedPrefrence lastUsageTime updated value in OnCreate",
            "${sharedPreferences.getLong("lastUsageTime", 0)}"
        )

        val btnUrl = findViewById<Button>(R.id.share_url_btn)
        val remote_config_btn = findViewById<Button>(R.id.remote_config_btn)
        val bottom_nav_btn = findViewById<Button>(R.id.bottom_nav_btn)



        btnUrl.setOnClickListener() {
            val intent = Intent(this, ShareImageUrlActivity::class.java)
            startActivity(intent)
        }

        remote_config_btn.setOnClickListener() {
            val intent = Intent(this, RemoteConfigActivity::class.java)
            startActivity(intent)
        }


        bottom_nav_btn.setOnClickListener() {
            val intent = Intent(this, BottomNavActivity::class.java)
            startActivity(intent)
        }



        Log.e("on alert", "entered on create")

    }

    override fun onResume() {
        super.onResume()
        // Cancel any existing work requests
        Log.e("on alert", "entered on resume")
        workManager.cancelAllWorkByTag(AppConstants.WORK_TAG)

    }

    override fun onPause() {
        super.onPause()

        Log.e("onpause alert", "entered on pause")

        Log.e(
            "sharedPrefrence lastUsageTime updated value in OnPause",
            "${sharedPreferences.getLong("lastUsageTime", 0)}"
        )
        Log.e("lastusagetime value", "$lastUsageTime")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationWorkRequest =
            PeriodicWorkRequestBuilder<InactivityCheckWorker>(
                AppConstants.INACTIVE_NOTIFICATION__REPEAT_INTERVAL, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

        // Enqueue unique work with a unique name to prevent duplicate work requests
        workManager.enqueueUniquePeriodicWork(
            AppConstants.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            notificationWorkRequest
        )
        Log.e("onpause alert", "work manager assigned")
    }

}
