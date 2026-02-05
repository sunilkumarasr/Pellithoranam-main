package com.stranger_sparks.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.stranger_sparks.R
import com.stranger_sparks.databinding.ActivitySplashScreenBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.SplashViewModel
import com.tencent.mmkv.MMKV
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var viewModel: SplashViewModel

    lateinit var observerEvents: Constants.ObserverEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        ConstantUtils.createFullScreen(this)

        // 🔥 Clear cached data only on fresh install
        if (isFirstInstall()) {
            clearAppData()
        }

        observeEvents()
        initFCM()

    }

    // --------------------------------------------------------------------
    //                       FRESH INSTALL CHECK
    // --------------------------------------------------------------------
    private fun isFirstInstall(): Boolean {
        val prefs = getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean("first_install_done", false)

        return if (!isFirstTime) {
            prefs.edit().putBoolean("first_install_done", true).apply()
            true
        } else false
    }

    // --------------------------------------------------------------------
    //                       CLEAR ALL APP DATA
    // --------------------------------------------------------------------
    private fun clearAppData() {
        Log.e("APP_INIT", "Clearing cached data...")

        // Clear SharedPreferences
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit().clear().apply()
        getSharedPreferences("app_cache", Context.MODE_PRIVATE).edit().clear().apply()

        // Clear MMKV
        try {
            MMKV.defaultMMKV()?.clearAll()
            MMKV.mmkvWithID("default")?.clearAll()
            MMKV.mmkvWithID("user")?.clearAll()
        } catch (e: Exception) { e.printStackTrace() }

        // Clear Zego cache
        try {
            // If user is logged in, SDK is initialized
            val isInitialized = ZegoUIKit.getLocalUser() != null

            if (isInitialized) {
                ZegoUIKitPrebuiltCallService.endCall()
                ZegoUIKitPrebuiltCallService.unInit()
                Log.e("ZEGO", "Zego cache cleared successfully")
            } else {
                Log.e("ZEGO", "Zego not initialized, skipping unInit()")
            }

        } catch (e: Exception) {
            Log.e("ZEGO", "Zego cleanup error: ${e.message}")
        }

        Log.e("APP_INIT", "App data cleared successfully on fresh install.")
    }


    // --------------------------------------------------------------------
    //                       OBSERVER EVENTS
    // --------------------------------------------------------------------
    private fun observeEvents() {
        viewModel.observerEvents.observe(this, Observer { event ->
            if (event != null) {
                observerEvents = event
                askNotificationPermission(event)
            }
        })
    }

    // --------------------------------------------------------------------
    //                  NOTIFICATION PERMISSION HANDLING
    // --------------------------------------------------------------------
    private fun askNotificationPermission(event: Constants.ObserverEvents) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                validateViewModelEvents(event)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

        } else {
            validateViewModelEvents(event)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

            if (granted) {
                validateViewModelEvents(observerEvents)
            } else {
                Snackbar.make(
                    binding.containermain,
                    getString(R.string.txt_error_post_notification, getString(R.string.app_name)),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.goto_settings)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                        startActivity(intent)
                    }
                }.show()
            }
        }

    // --------------------------------------------------------------------
    //                  NAVIGATION HANDLING (CLEAN)
    // --------------------------------------------------------------------
    private fun validateViewModelEvents(event: Constants.ObserverEvents) {
        val context = this@SplashScreenActivity

        when (event) {

            Constants.ObserverEvents.OPEN_SIGN_IN_SCREEN -> {
                openNextScreen(context, SignInSignUpActivity::class.java)
            }

            Constants.ObserverEvents.OPEN_BASIC_PROFILE -> {
                openNextScreen(context, BasicProfileActivity::class.java)
            }

            Constants.ObserverEvents.OPEN_HOME_SCREEN -> {
                openNextScreen(context, HomeActivity::class.java)
            }

            else -> {
                // Ignore unhandled events
                openNextScreen(context, SignInSignUpActivity::class.java)
            }
        }
    }

    private fun openNextScreen(context: Context, next: Class<*>) {
        Intent(context, next).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.also {
            startActivity(it)
            finish()
        }
    }

    // --------------------------------------------------------------------
    //                              FCM
    // --------------------------------------------------------------------
    private fun initFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->

            if (!task.isSuccessful) {
                Log.e("FCM", "Token fetch failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.e("FCM", "Device Token: $token")
        }
    }

}