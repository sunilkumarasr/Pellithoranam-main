package com.stranger_sparks.view.activities.ui.fragments.help

import android.app.Application
import com.stranger_sparks.databinding.FragmentHelpBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class HelpViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentHelpBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}