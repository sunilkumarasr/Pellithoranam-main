package com.stranger_sparks.fcm

import android.app.Application
import android.util.Log
import com.stranger_sparks.api_dragger_flow.di.ApplicationModule
import com.stranger_sparks.utils.SharedPreferenceManager
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName
import com.zegocloud.uikit.prebuilt.call.core.invite.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.event.BackPressEvent
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.IncomingCallButtonListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.OutgoingCallButtonListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider


object ZegoCallManager {
    var isInitialized: Boolean = false

    var currentCallID: String? = null

    private const val appID: Long = 1951552
    private const val appSign: String = "49346d90ae3ed78526cd90f5a47c8df4a9aa25d66b2333e60414db745e0b85fc"

    fun initialize(application: Application, userID: String, userName: String) {
        // val config = ZegoUIKitPrebuiltCallInvitationConfig()

        val config = ZegoUIKitPrebuiltCallInvitationConfig()
        config.provider

        application.applicationContext.wakeUpScreen()

        ZegoUIKitPrebuiltCallService.init(
            application,
            appID,
            appSign,
            userID,
            userName,
            config
        )

        //back press on screen
        ZegoUIKitPrebuiltCallService.events.callEvents.setBackPressEvent(
            object : BackPressEvent {
                override fun onBackPressed(): Boolean {
                    // Return true back press not working on screen
                    return true
                }
            }
        )

        config.provider = object : ZegoUIKitPrebuiltCallConfigProvider {
            override fun requireConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
                val isVideoCall = invitationData.type == ZegoInvitationType.VIDEO_CALL.value
                val isGroupCall = invitationData.invitees.size > 1

                val config = when {
                    isVideoCall && isGroupCall -> ZegoUIKitPrebuiltCallConfig.groupVideoCall()
                    !isVideoCall && isGroupCall -> ZegoUIKitPrebuiltCallConfig.groupVoiceCall()
                    !isVideoCall -> ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall()
                    else -> ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
                }

                config.topMenuBarConfig.isVisible = true
                config.topMenuBarConfig.buttons.add(ZegoMenuBarButtonName.MINIMIZING_BUTTON)

                return config
            }
        }

        ZegoUIKitPrebuiltCallService.events.invitationEvents.setIncomingCallButtonListener(object :
            IncomingCallButtonListener {
            override fun onIncomingCallDeclineButtonPressed() {
                Log.e("stzoho_","10")
                val sharedPreferenceManager = SharedPreferenceManager(application)
                val callUserId = sharedPreferenceManager.getCallUserIdValue("callUserId")
                Log.e("callUserIdstzoho_",callUserId.toString())
                ZegoUIKitPrebuiltCallService.endCall()

                //call api run
                callCancelAPI(callUserId.toString(), "1")

            }
            override fun onIncomingCallAcceptButtonPressed() {
            }
        })

        ZegoUIKitPrebuiltCallService.events.invitationEvents.setOutgoingCallButtonListener(object :
            OutgoingCallButtonListener {
            override fun onOutgoingCallCancelButtonPressed() {
                Log.e("stzoho_","20")
                ZegoUIKitPrebuiltCallService.endCall()
            }
        })

        ZegoUIKitPrebuiltCallService.events.invitationEvents.setInvitationListener(object :
            ZegoInvitationCallListener {
            override fun onIncomingCallReceived(
                callID: String?,
                caller: ZegoCallUser?,
                callType: ZegoCallType?,
                callees: MutableList<ZegoCallUser>?
            ) {
                Log.e("currentCallID_",callID.toString())
                Log.e("stzoho_","32")
                currentCallID = callID
            }
            override fun onIncomingCallCanceled(callID: String?, caller: ZegoCallUser?) {
                Log.e("stzoho_","0")
                cleanUpAndExit()
            }
            override fun onIncomingCallTimeout(callID: String?, caller: ZegoCallUser?) {
                cleanUpAndExit()
            }
            override fun onOutgoingCallAccepted(callID: String, callee: ZegoCallUser) {
                Log.e("stzoho_","1")
            }
            override fun onOutgoingCallRejectedCauseBusy(callID: String?, callee: ZegoCallUser?) {
                Log.e("stzoho_","2")
            }
            override fun onOutgoingCallDeclined(callID: String?, callee: ZegoCallUser?) {
                Log.e("stzoho_","3")
            }
            override fun onOutgoingCallTimeout(
                callID: String?,
                callees: MutableList<ZegoCallUser>?
            ) {
                Log.e("stzoho_","4")
            }
        })

        isInitialized = true

    }

    fun callCancelAPI(userId: String, status: String) {
        Thread {
            try {
                val call = ApplicationModule.getApiService().submitCallCheckStatus(userId, status)
                val response = call.execute() // synchronous call

                if (response.isSuccessful) {
                    Log.d("API", "Call cancelled successfully: ${response.body()}")
                } else {
                    Log.e("API", "API error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API", "API call failed: ${e.message}")
            }
        }.start()
    }


    private fun cleanUpAndExit() {
//        if (context is Activity) {
//            (context as Activity).finishAffinity()
//        } else {
//            val intent = Intent(context, HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            context?.startActivity(intent)
//        }
    }

}