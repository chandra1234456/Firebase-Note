package com.example.practice.notesfirebase.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.practice.notesfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Called when a new FCM token is generated
    override fun onNewToken(token : String) {
        super.onNewToken(token)
        Log.d("FCM" , "New token: $token")

        // TODO: Send the token to your server or save it in Firestore under user ID
        sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage : RemoteMessage) {
        // Check if the message contains the custom data
        remoteMessage.data["force_logout"]?.let {
            if (it == "true") {
                // Trigger logout and show notification
                sendLogoutNotification()
                performLogout()
            }
        }
    }

    private fun sendLogoutNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "logout_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId ,
                    "Logout Notifications" ,
                    NotificationManager.IMPORTANCE_HIGH
                                             )
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this , channelId)
                .setSmallIcon(R.drawable.ic_logout) // Use your app's icon
                .setContentTitle("Logged Out")
                .setContentText("You have been logged out from the app.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(1 , builder.build())
    }

    private fun performLogout() {
        // Sign out the user
        FirebaseAuth.getInstance().signOut()

        // Create or get a master key for encryption
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        // Create encrypted shared preferences
        val sharedPreferences = EncryptedSharedPreferences.create(
                "preferences" , // preferences file name
                masterKeyAlias ,
                this , // context
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV ,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                                                 )

        // Clear encrypted preferences
        sharedPreferences.edit().clear().apply()
        // Send broadcast to navigate to the Login Fragment
        val intent = Intent("com.example.app.ACTION_LOGOUT")
        sendBroadcast(intent) // This will notify the Activity to navigate to the Login Fragment
    }


    private fun sendTokenToServer(token : String) {
        val user = FirebaseAuth.getInstance().currentUser
        Log.d("TAG" , "sendTokenToServer: $user")
        user?.let {
            FirebaseFirestore.getInstance().collection("users")
                    .document(it.uid)
                    .update("fcmToken" , token)
        }
    }
}
