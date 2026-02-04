package com.stranger_sparks.view.activities.ui.fragments.congrats_message

import android.app.Application
import com.stranger_sparks.databinding.FragmentCongratsBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class CongratsViewModel (application: Application?) : BaseViewModel(application) {
    var binding: FragmentCongratsBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}