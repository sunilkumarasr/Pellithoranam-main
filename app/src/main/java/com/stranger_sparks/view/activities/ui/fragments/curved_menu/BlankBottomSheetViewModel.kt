package com.stranger_sparks.view.activities.ui.fragments.curved_menu

import android.app.Application
import com.stranger_sparks.databinding.FragmentBlankBottomSheetBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel

class BlankBottomSheetViewModel(application: Application?) : BaseViewModel(application) {
    var binding: FragmentBlankBottomSheetBinding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.CLOSE_WINDOW)
    }
}