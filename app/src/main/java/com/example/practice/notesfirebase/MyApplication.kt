package com.example.practice.notesfirebase

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MyApplication : Application() {
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

    }
}