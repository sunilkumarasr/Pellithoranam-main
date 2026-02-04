package com.stranger_sparks.view.activities.ui.fragments.dating_matches

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stranger_sparks.R

class ViewsDatingMatchesFragment : Fragment() {

    companion object {
        fun newInstance() = ViewsDatingMatchesFragment()
    }

    private lateinit var viewModel: ViewsDatingMatchesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_views_dating_matches, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewsDatingMatchesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}