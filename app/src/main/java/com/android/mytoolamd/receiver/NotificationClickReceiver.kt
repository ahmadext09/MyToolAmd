package com.android.mytoolamd.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.mytoolamd.activity.MainActivity
import com.android.mytoolamd.utility.AppConstants
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase


class NotificationClickReceiver : BroadcastReceiver() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "NOTIFICATION_CLICKED_ACTION") {
            val notificationId = intent?.getIntExtra("notification_Id",0)
            Log.e("notificationBroadcastReceiver_notification_id", "$notificationId")
            if (notificationId == AppConstants.NOTIFICATION_ID) {
                Log.e("notificationBroadcastReceiver", "notification clicked")
//                val firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
//                val bundle = Bundle()
//                bundle.putString("event_name", "inactive_notification_clicked")
//                firebaseAnalytics.logEvent("notification_event", bundle)

                //  Log.e("nitificationBroadcastReciver","notification clicked")
                firebaseAnalytics = Firebase.analytics
                firebaseAnalytics.logEvent("clicked_notification_event") {

                    param("event_name", "inactive_notification_clicked")
                }
                val mainIntent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context?.startActivity(mainIntent)

            }


        }
        }
    }

//<service
//android:name=".utility.InactivityCheckWorker"
//android:exported="false"
//android:permission="android.permission.BIND_JOB_SERVICE"
//tools:ignore="Instantiatable" />