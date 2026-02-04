package com.stranger_sparks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.stranger_sparks.utils.Constants


open class BaseViewModel(application: Application?) : AndroidViewModel(application!!) {
    var observerEvents: MutableLiveData<Constants.ObserverEvents> =
        MutableLiveData<Constants.ObserverEvents>()
}
