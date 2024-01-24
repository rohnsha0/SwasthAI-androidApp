package com.rohnsha.medbuddyai.domain.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.rohnsha.medbuddyai.MainActivity
import com.rohnsha.medbuddyai.R

class dbUpdateService(
    private val context: Context
) {
    private val notificationManager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val activityIntent= Intent(context, MainActivity::class.java)
    private val pendingIntent= PendingIntent.getActivity(
        context,
        1,
        activityIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    fun showNotification(title: String, message: String){
        val notification= NotificationCompat.Builder(context, dbUPDATEchannelName)
            .setSmallIcon(R.drawable.notific_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(1, notification)
    }

    companion object{
        const val dbUPDATEchannelName= "dbUpdateChannel"
    }

}