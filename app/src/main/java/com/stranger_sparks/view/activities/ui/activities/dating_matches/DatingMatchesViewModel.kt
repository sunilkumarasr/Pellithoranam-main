package com.stranger_sparks.view.activities.ui.activities.dating_matches

import android.app.Application
import com.stranger_sparks.databinding.ActivityDatingMatchesBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class DatingMatchesViewModel(application: Application?) : BaseViewModel(application) {
    var binding: ActivityDatingMatchesBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
    fun closeHelp(){
        observerEvents.setValue(Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN)
    }
}