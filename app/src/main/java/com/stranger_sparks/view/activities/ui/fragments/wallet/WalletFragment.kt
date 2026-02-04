package com.stranger_sparks.view.activities.ui.fragments.wallet

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.stranger_sparks.R
import com.stranger_sparks.databinding.FragmentWalletBinding
import com.stranger_sparks.utils.Constants

class WalletFragment : Fragment() {

    lateinit var binding: FragmentWalletBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        val viewModel: WalletViewModel = ViewModelProvider(this).get(
            WalletViewModel::class.java
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