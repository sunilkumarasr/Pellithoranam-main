package com.stranger_sparks.view.activities.ui.activities.chat

import android.app.Application
import com.stranger_sparks.databinding.ActivityChatBinding
import com.stranger_sparks.databinding.ActivityHelpBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class ChatViewModel(application: Application?) : BaseViewModel(application) {
    var binding: ActivityChatBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
    fun closeHelp(){
        observerEvents.setValue(Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN)
    }
}