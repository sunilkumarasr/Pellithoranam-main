package com.stranger_sparks.view.activities.ui.fragments.wallet

import android.app.Application
import com.stranger_sparks.databinding.FragmentWalletBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class WalletViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentWalletBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
}