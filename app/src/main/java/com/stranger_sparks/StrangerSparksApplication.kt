package com.stranger_sparks

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.stranger_sparks.api_dragger_flow.di.ApplicationComponent
import com.stranger_sparks.api_dragger_flow.di.DaggerApplicationComponent


class StrangerSparksApplication: Application() {

    lateinit var applicationComponent : ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannels(this);
           // setupBatchChannelInterceptor();
        } else {
            setupLegacyBatchSoundInterceptor();
        }
    }

    private fun setupLegacyBatchSoundInterceptor() {


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotificationChannels(strangerSparksApplication: StrangerSparksApplication) {

        val soundAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
val uri= Uri.parse(
    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName()
        + "/raw/" + "soundName");
        val notificationChannel=NotificationChannel("1","LookingForYou", NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.setSound(uri,soundAttributes)
    }


}