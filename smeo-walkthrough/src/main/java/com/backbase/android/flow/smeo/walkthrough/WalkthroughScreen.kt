package com.backbase.android.flow.smeo.walkthrough

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.screen_walkthrough.*
import org.koin.android.ext.android.inject

class WalkthroughScreen : Fragment(R.layout.screen_walkthrough), ViewPager.OnPageChangeListener {

    private val configuration: WalkthroughConfiguration by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adapter = WalkthroughPagerAdapter(parentFragmentManager, configuration.pages)

        tabIndicator.setupWithViewPager(viewPager, true)
        viewPager.addOnPageChangeListener(this)
        viewPager.adapter = adapter

        onPageChanged(0)

    }

    fun onPageChanged(position: Int) {
        if (position < configuration.pages.size - 1) {
            lblNext.setOnClickListener {
                viewPager.setCurrentItem(position + 1, true)
            }
        } else {
            lblNext.setOnClickListener {

            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        onPageChanged(position)
    }
}