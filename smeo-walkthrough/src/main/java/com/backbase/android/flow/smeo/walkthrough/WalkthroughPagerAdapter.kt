package com.backbase.android.flow.smeo.walkthrough

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class WalkthroughPagerAdapter(
    fragmentManager: FragmentManager,
    var lstPages: List<PageContent>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return WalkthroughSingleScreen.newInstance(
            lstPages[position]
        )
    }

    override fun getCount(): Int {
        return lstPages.size
    }
}
