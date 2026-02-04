package com.stranger_sparks.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.stranger_sparks.R
import com.stranger_sparks.databinding.FragmentLoginBinding
import com.stranger_sparks.databinding.FragmentRegistrationBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.LoginViewModel
import com.stranger_sparks.viewmodel.RegistrationViewModel


class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val viewModel: RegistrationViewModel = ViewModelProvider(this).get(
            RegistrationViewModel::class.java
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