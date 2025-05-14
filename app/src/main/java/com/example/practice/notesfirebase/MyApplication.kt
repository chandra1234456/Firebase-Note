package com.example.practice.notesfirebase

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MyApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // Initialize Firebase Analytics
        // Enable Crashlytics collection for your app
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

        // Optionally, log custom keys for better reporting
        FirebaseCrashlytics.getInstance().setCustomKey("AppStartTime" , System.currentTimeMillis())

        // Optionally log any additional user information or settings
        FirebaseCrashlytics.getInstance()
                .setUserId("user_id_here")  // You can set dynamic user ID, etc.
        val channel = NotificationChannel(
                "reminder_channel",
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
                                         )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)


    }
}