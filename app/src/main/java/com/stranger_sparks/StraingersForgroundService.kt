package com.stranger_sparks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.VERSION
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat


class StraingersForgroundService : Service() {

    private
    val NOTIFICATION_ID: Int = 1234567800
    private
    val CHANNEL_ID: String = "audio_channel_id"

    override fun onCreate() {
        super.onCreate()
        val notification: Notification = getDefaultNotification()

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (ex: Exception) {
            Log.e("TAG", "", ex)
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private fun getDefaultNotification(): Notification {
        val appInfo = this.applicationContext.applicationInfo
        val name = this.applicationContext.packageManager.getApplicationLabel(appInfo).toString()
        var icon = appInfo.icon

        try {
            val iconBitMap = BitmapFactory.decodeResource(this.applicationContext.resources, icon)
            if (iconBitMap == null || iconBitMap.byteCount == 0) {
                Log.w("TAG", "Couldn't load icon from icon of applicationInfo, use android default")
                icon = R.drawable.ic_call
            }
        } catch (ex: java.lang.Exception) {
            Log.w("TAG", "Couldn't load icon from icon of applicationInfo, use android default")
            icon = R.mipmap.ic_launcher
        }

        if (Build.VERSION.SDK_INT >= 26) {
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(mChannel)
        }

        val activityPendingIntent: PendingIntent
        val intent = Intent()
        //  intent.setClass(this, MainActivity::class.java)
        activityPendingIntent = if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        /*val builder = Notification.Builder(this)

            .setContentText("Calling ...")
            .setOngoing(true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setSmallIcon(icon)
            .setTicker(name)
            .setWhen(System.currentTimeMillis())
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId(CHANNEL_ID)
        }*/


        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle("Stranger Sparks")

            .setAutoCancel(true)
            .setOngoing(true)

            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= 26) {
            notificationBuilder.setChannelId(CHANNEL_ID)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        return notificationBuilder.build()
    }
}