package com.android.mytoolamd

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.*
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfigActivity : AppCompatActivity() {
    lateinit var realtime_textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_config)
        val textView=findViewById<TextView>(R.id.remote_text)
        val fetch_btn=findViewById<Button>(R.id.fetch_remote)
        realtime_textView=findViewById(R.id.realtime_text)
       // val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
//        val remoteConfig = FirebaseRemoteConfig.getInstance()
//
//        val configSettings = FirebaseRemoteConfigSettings.Builder()
//           // .setMinimumFetchIntervalInSeconds(720) // Optional: set minimum fetch interval
//            .build()
//        remoteConfig.setConfigSettingsAsync(configSettings)
//
//// Set default values from XML resource
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
           // minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)



        var textValue = remoteConfig.getString("remote_text")
                    textView.text = textValue
        val textReal= remoteConfig.getString("realtime_text")
                realtime_textView.text=textReal
        fetch_btn.setOnClickListener() {
//            val textValue = remoteConfig.getString("remote_text")
//            textView.text = textValue

            remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val textValue = remoteConfig.getString("remote_text")
                        textView.text = textValue
                    } else {
                        // Handle fetch error
                    }
                }
        git}
            remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate : ConfigUpdate) {
                    Log.d(TAG, "Updated keys: " + configUpdate.updatedKeys);

                    if (configUpdate.updatedKeys.contains("realtime_text")) {
                        remoteConfig.activate().addOnCompleteListener {
                            val realtext= remoteConfig.getString("realtime_text")
                            realtime_textView.text=realtext

                        }
                    }
                }

                override fun onError(error : FirebaseRemoteConfigException) {
                    Log.w(TAG, "Config update error with code: " + error.code, error)
                }
            })


    }

}