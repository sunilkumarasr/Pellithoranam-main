package com.stranger_sparks.view.activities.ui.fragments.dating_matches

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stranger_sparks.R

class LikedYouDatingMatchesFragment : Fragment() {

    companion object {
        fun newInstance() = LikedYouDatingMatchesFragment()
    }

    private lateinit var viewModel: LikedYouDatingMatchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liked_you_dating_matches, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LikedYouDatingMatchesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}