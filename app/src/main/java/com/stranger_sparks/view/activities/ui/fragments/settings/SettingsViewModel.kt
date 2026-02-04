package com.stranger_sparks.view.activities.ui.fragments.settings

import android.app.Application
import com.stranger_sparks.databinding.FragmentSettingsBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class SettingsViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentSettingsBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}