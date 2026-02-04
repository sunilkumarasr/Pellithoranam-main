package com.stranger_sparks.view.activities.ui.fragments.my_account

import android.app.Application
import com.stranger_sparks.databinding.FragmentMyAccountBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class MyAccountViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentMyAccountBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}