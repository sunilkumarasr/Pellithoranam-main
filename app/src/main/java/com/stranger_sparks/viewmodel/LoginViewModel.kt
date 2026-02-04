package com.stranger_sparks.viewmodel

import android.app.Application
import com.stranger_sparks.databinding.FragmentLoginBinding
import com.stranger_sparks.databinding.FragmentSplashBinding
import com.stranger_sparks.utils.Constants

class LoginViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentLoginBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}