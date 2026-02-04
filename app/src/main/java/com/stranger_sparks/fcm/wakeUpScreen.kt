package com.stranger_sparks.fcm

import android.content.Context
import android.os.PowerManager

fun Context.wakeUpScreen() {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    val wakeLock = powerManager.newWakeLock(
        PowerManager.FULL_WAKE_LOCK or
                PowerManager.ACQUIRE_CAUSES_WAKEUP or
                PowerManager.ON_AFTER_RELEASE,
        "stranger_sparks:IncomingCallWakeLock"
    )
    wakeLock.acquire(20000L) // Wake screen for 5 seconds
}
