package com.stranger_sparks.view.activities.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.stranger_sparks.R
import com.stranger_sparks.databinding.FragmentMyAccountBinding
import com.stranger_sparks.databinding.FragmentSettingsBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.view.activities.ui.fragments.my_account.MyAccountViewModel

class SettingsFragment : Fragment() {

   lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val viewModel: SettingsViewModel = ViewModelProvider(this).get(
            SettingsViewModel::class.java
        )
        viewModel.binding = binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel!!.observerEvents.observe(viewLifecycleOwner) { observerEvents ->
            observerEvents?.let { validateViewModelEvents(observerEvents) }
        }
    }

    private fun validateViewModelEvents(observerEvents: Constants.ObserverEvents) {
        if (observerEvents === Constants.ObserverEvents.OPEN_SIGN_IN_SCREEN) {

            Navigation.findNavController(binding.root).navigate(R.id.splash_screen_to_login_screen)
        }
    }
}