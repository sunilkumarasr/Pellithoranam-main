package com.stranger_sparks.view.activities.ui.fragments.congrats_message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stranger_sparks.databinding.FragmentCongratsBinding
import com.stranger_sparks.utils.Constants

class CongratsFragment : Fragment() {



    lateinit var binding: FragmentCongratsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCongratsBinding.inflate(inflater, container, false)
        val  viewModel: CongratsViewModel = ViewModelProvider(this).get(CongratsViewModel::class.java);
        viewModel.binding = binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel!!.observerEvents.observe(viewLifecycleOwner){observerEvents ->
            observerEvents?.let { validateViewModelEvents(observerEvents) }
        }
    }

    private fun validateViewModelEvents(observerEvents: Constants.ObserverEvents) {
        if (observerEvents === Constants.ObserverEvents.OPEN_SIGN_IN_SCREEN) {

            //Navigation.findNavController(binding.root).navigate(R.id.splash_screen_to_login_screen)
        }
    }



}