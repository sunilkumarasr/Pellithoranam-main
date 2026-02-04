package com.stranger_sparks.view.activities.ui.activities.settings

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.databinding.ActivitySettingsBinding
import com.stranger_sparks.fcm.ZegoCallManager
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.ui.activities.call_List.CallHistoryListActivity
import com.stranger_sparks.view.activities.ui.activities.account.AccountActivity
import com.stranger_sparks.view.activities.ui.activities.account.like_liked.LikeLikedActivity
import com.stranger_sparks.view.activities.ui.activities.help.HelpActivity
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.ManageSubscriptionNew
import com.stranger_sparks.view.activities.ui.activities.notifications.Notifications
import com.stranger_sparks.view.activities.ui.activities.wallet.WalletActivity
import com.stranger_sparks.view.activities.ui.activities.wallet.transections.WalletTransactionsActivity
import com.stranger_sparks.view.activities.ui.activities.webview.WebViewUrlLoad
import javax.inject.Inject
import com.tencent.mmkv.MMKV
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService



class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var viewModel: SettingsViewModel
    lateinit var userID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(
            this,
            ContextCompat.getColor(this, R.color.black),
            false
        )

        /*val viewModel: SettingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java);*/
        viewModel.binding = binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        /*binding.viewModel!!.observerEvents.observe(this) { observerEvents ->
            observerEvents?.let { validateViewModelEvents(observerEvents) }
        }*/

        binding.tvAboutUs.setOnClickListener { validateViewModelEvents(Constants.ObserverEvents.ABOUT_US) }
        binding.tvPrivacy.setOnClickListener { validateViewModelEvents(Constants.ObserverEvents.PRIVACY_POLICY) }
        binding.tvTermsConstions.setOnClickListener { validateViewModelEvents(Constants.ObserverEvents.TERMS_AND_CONDITIONS) }
        binding.tvFaq.setOnClickListener { validateViewModelEvents(Constants.ObserverEvents.FAQ) }
        binding.ivSignOut.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
            builder.setTitle("Alert !")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    viewModel.logoutLiveData(userID)
                    /*if(SharedPreferenceManager(this).clearAllData()){
                        Intent(applicationContext, SignInSignUpActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }*/
                } as DialogInterface.OnClickListener)
            builder.setNegativeButton(
                "No",
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    // If user click no then dialog box is canceled.
                    dialog.cancel()
                } as DialogInterface.OnClickListener)
            val alertDialog = builder.create()
            alertDialog.show()
        }
        viewModel.logoutLiveData.observe(this) {
            if (it?.status == true) {
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
                if (SharedPreferenceManager(this).clearAllData()) {

                    Intent(
                        applicationContext,
                        SignInSignUpActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).also {
                        setResult(200)
                        startActivity(it)
                        finish()

                    }
                }
            } else {
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
            }
        }
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.tvWallet.setOnClickListener {
            Intent(applicationContext, WalletActivity::class.java).also {
                startActivity(it)

            }
        }
        binding.tvSignOut.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
            builder.setTitle("Alert !")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog, _ ->
                viewModel.logoutLiveData(userID)
                //local
                performLogout()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()

            // Set button text colors after dialog is shown
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this, R.color.blue_text_color))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(this, R.color.blue_text_color))
        }
        binding.tvNotifications.setOnClickListener {
            Intent(applicationContext, Notifications::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)

            }
        }
        binding.tvHelp.setOnClickListener {
            Intent(applicationContext, HelpActivity::class.java).also {
                //it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)

            }
        }
        binding.tvAccount.setOnClickListener {
            Intent(applicationContext, AccountActivity::class.java).also {
                startActivity(it)

            }
        }
        binding.tvCallHistory.setOnClickListener {
            Intent(applicationContext, CallHistoryListActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.tvPayment.setOnClickListener {
            Intent(applicationContext, WalletTransactionsActivity::class.java).also {
                startActivity(it)

            }
        }

        binding.tvManageSubscription.setOnClickListener {
//            Intent(applicationContext, ManageSubscription::class.java).also {
//                startActivity(it)
//            }
            Intent(applicationContext, ManageSubscriptionNew::class.java).also {
                startActivity(it)
            }
        }
        binding.tvLikeLiked.setOnClickListener {
            Intent(applicationContext, LikeLikedActivity::class.java).also {
                startActivity(it)

            }
        }

        //incoming call received
        incomingCallReceived()
    }

    private fun performLogout() {

        // Clear SharedPreferences
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit { clear() }
        getSharedPreferences("app_cache", Context.MODE_PRIVATE).edit { clear() }

        // Clear MMKV data
        try {
            MMKV.defaultMMKV().clearAll()
            MMKV.mmkvWithID("default").clearAll()
            MMKV.mmkvWithID("user").clearAll()
        } catch (e: Exception) { e.printStackTrace() }

        // Clear Zego cache
        try {
            val isInit = ZegoUIKit.getLocalUser() != null

            if (isInit) {
                ZegoUIKitPrebuiltCallService.endCall()
                ZegoUIKitPrebuiltCallService.unInit()
            }
        } catch (e: Exception) { e.printStackTrace() }

        // Redirect to Sign-In
        val intent = Intent(this, SignInSignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun incomingCallReceived() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val userName = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.name.orEmpty()
        ZegoCallManager.initialize(application, userID, userName)
    }

    private fun validateViewModelEvents(observerEvents: Constants.ObserverEvents) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN) {

            finish()
        } else if (observerEvents === Constants.ObserverEvents.ABOUT_US) {
            Intent(applicationContext, WebViewUrlLoad::class.java).also {
                it.putExtra("url_type", Constants.ObserverEvents.ABOUT_US.toString())
                startActivity(it)

            }

        } else if (observerEvents === Constants.ObserverEvents.PRIVACY_POLICY) {
            Intent(applicationContext, WebViewUrlLoad::class.java).also {
                it.putExtra("url_type", Constants.ObserverEvents.PRIVACY_POLICY.toString())
                startActivity(it)

            }

        } else if (observerEvents === Constants.ObserverEvents.TERMS_AND_CONDITIONS) {

            Intent(applicationContext, WebViewUrlLoad::class.java).also {
                it.putExtra("url_type", Constants.ObserverEvents.TERMS_AND_CONDITIONS.toString())
                startActivity(it)

            }
        } else if (observerEvents === Constants.ObserverEvents.FAQ) {

            Intent(applicationContext, WebViewUrlLoad::class.java).also {
                it.putExtra("url_type", Constants.ObserverEvents.FAQ.toString())
                startActivity(it)

            }
        } else if (observerEvents === Constants.ObserverEvents.HELP) {


        }
    }



}