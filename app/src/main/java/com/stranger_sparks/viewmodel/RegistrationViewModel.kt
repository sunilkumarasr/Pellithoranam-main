package com.stranger_sparks.viewmodel

import android.app.Application
import com.stranger_sparks.databinding.FragmentLoginBinding
import com.stranger_sparks.databinding.FragmentRegistrationBinding
import com.stranger_sparks.databinding.FragmentSplashBinding
import com.stranger_sparks.utils.Constants

class RegistrationViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentRegistrationBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}