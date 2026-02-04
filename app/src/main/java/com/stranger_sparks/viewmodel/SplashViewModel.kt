package com.stranger_sparks.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.startActivity
import com.stranger_sparks.databinding.FragmentSplashBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.BasicProfileActivity


class SplashViewModel(application: Application?) : BaseViewModel(application) {
    init {
        splashTimer()
    }

    var binding: FragmentSplashBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }

    fun splashTimer() {
        Handler(Looper.getMainLooper()).postDelayed(
            { this.checkLocalDbAndGetRemoteSportsData() },
            1000
        )
    }
    private fun checkLocalDbAndGetRemoteSportsData() {
        validateScreenOpening()
    }

    fun validateScreenOpening() {
        val sharedPreferenceManager =  SharedPreferenceManager(getApplication())
        if(sharedPreferenceManager.getSavedLoginResponseUser()!=null){
            if(sharedPreferenceManager.getSavedLoginResponseUser()?.data?.profile_completed == "0"){
                observerEvents.postValue(Constants.ObserverEvents.OPEN_BASIC_PROFILE)
            }else{
                observerEvents.postValue(Constants.ObserverEvents.OPEN_HOME_SCREEN)
            }
        }else{
            observerEvents.postValue(Constants.ObserverEvents.OPEN_SIGN_IN_SCREEN)
        }
    }
}

