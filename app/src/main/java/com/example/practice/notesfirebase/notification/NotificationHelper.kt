package com.example.practice.notesfirebase.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practice.notesfirebase.MainActivity
import com.example.practice.notesfirebase.R
import com.example.practice.notesfirebase.util.Constants.CHANNEL_ID

object NotificationHelper {
 
    fun displayNotification(context: Context , title: String , body: String) {
       
        val intent = Intent(context, MainActivity::class.java)
 
        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                                     )
 
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_none)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
 
        val mNotificationMgr = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                    context ,
                    Manifest.permission.POST_NOTIFICATIONS
                                              ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mNotificationMgr.notify(1, mBuilder.build())
 
    }
 
}
