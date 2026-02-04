package com.stranger_sparks.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stranger_sparks.R
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.ui.activities.notifications.Notifications
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingServiceOLD : FirebaseMessagingService() {

    var title: String = ""
    var body: String = ""
    var type: String = ""
    var email: String = ""
    var user_id: String = ""
    var profile_id: String = ""

    companion object {
        private var title: String = ""
        var token: String? = null
        var body: String? = ""
        var type: String? = ""
        var receiver_id: String? = ""
        var id: String? = null
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("onremoteMessage: ", remoteMessage.toString())
        Log.e("onMessageReceived: ", remoteMessage.data.toString())

        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            val message = data["notification_body"] ?: return

            try {
                // Try parsing the "body" as JSON
                val obj = JSONObject(message)

                // Check if it contains "notification" object
                if (obj.has("notification")) {
                    val notification = obj.getJSONObject("notification")
                    title = notification.optString("notification_title", title)
                    body = notification.optString("notification_body", body)
                    type = notification.optString("type", type)
                    email = notification.optString("email", email)
                    user_id = notification.optString("user_id", user_id)
                    profile_id = notification.optString("profile_id", profile_id)
                } else {
                    // If no "notification" object, try direct keys in root
                    title = obj.optString("notification_title", title)
                    body = obj.optString("notification_body", body)
                    type = obj.optString("type", type)
                    email = obj.optString("email", email)
                    user_id = obj.optString("user_id", user_id)
                    profile_id = obj.optString("profile_id", profile_id)
                }
            } catch (e: JSONException) {
                // If JSON parsing fails, fallback to keys directly from data map
                title = data["notification_title"] ?: data["title"] ?: title
                body = data["notification_body"] ?: message
                type = data["type"] ?: ""
                email = data["email"] ?: ""
                user_id = data["user_id"] ?: ""
                profile_id = data["profile_id"] ?: ""
            }

            var callHandled = false

            if (!callHandled && !type.isNullOrEmpty() && (type == "Audio" || type == "Video")) {
                callHandled = true
                showPopup(title, email)
            }

            if (!callHandled) {
                createNotification(body)
            }

        }

    }

    private fun showPopup(title: String, email: String) {
        // Wake up screen and keep it on
        applicationContext.wakeUpScreenAndKeepOn()

        // Initialize ZegoCallManager with current user info
        val sharedPreferenceManager = SharedPreferenceManager(this)
        sharedPreferenceManager.saveCallUserIdValue("callUserId", user_id)
        sharedPreferenceManager.saveCallUserIdValue("profile_id", profile_id)
        sharedPreferenceManager.saveCallUserIdValue("title", title)
        val userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
        val userName = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.name.toString()
        //ZegoCallManager.initialize(application, profile_id, userName)
        Log.e("userID_",profile_id)
        Log.e("userName_",title)

//        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
//        callInvitationConfig.outgoingCallBackground = ColorDrawable(Color.BLUE)
//        callInvitationConfig.incomingCallBackground = ColorDrawable(Color.GREEN)
//        ZegoUIKitPrebuiltCallService.init(
//            getApplication(),
//            1951552,
//            "49346d90ae3ed78526cd90f5a47c8df4a9aa25d66b2333e60414db745e0b85fc",
//            userID,
//            userName,
//            callInvitationConfig
//        )

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig().apply {
            translationText.incomingCallPageDeclineButton = "Decline"
            translationText.incomingCallPageAcceptButton = "Accept"
        }

        ZegoUIKitPrebuiltCallService.init(
            application,
            1951552,
            "49346d90ae3ed78526cd90f5a47c8df4a9aa25d66b2333e60414db745e0b85fc",
            userID,
            userName,
            callInvitationConfig
        )

//        // Start the service to initialize Zego
//        val serviceIntent = Intent(applicationContext, CallService::class.java)
//        ContextCompat.startForegroundService(applicationContext, serviceIntent)

    }

    // Extension function to wake screen and keep it on for 10 minutes
    private fun Context.wakeUpScreenAndKeepOn(): PowerManager.WakeLock {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "Pellithoranam:IncomingCallWakeLock"
        )
        wakeLock.acquire(30 * 1000L) // 30 seconds

        return wakeLock
    }

    private fun createNotification(body: String) {

        Log.e("asdfghj_","123")

        val intent = Intent(this, Notifications::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val channelId = "1"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel(channelId)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle("Pellithoranam")
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(pendingIntent, true)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(channelId: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            "Pellithoranam",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = body
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Companion.token = token
        Log.e("FCM Token", token)
    }
}