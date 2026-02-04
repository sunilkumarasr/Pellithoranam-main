package com.stranger_sparks.adapterrs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.stranger_sparks.R
import com.stranger_sparks.view.activities.ui.fragments.dating_matches.AllDatingMatchesFragment

private val TAB_TITLES = arrayOf(
    R.string.all,
    R.string.you_liked,
    R.string.liked_you,
    R.string.views
)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment.
        return AllDatingMatchesFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 4
    }
}